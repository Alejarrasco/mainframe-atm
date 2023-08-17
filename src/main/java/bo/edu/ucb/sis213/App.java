package bo.edu.ucb.sis213;

import java.sql.PreparedStatement;

import javax.swing.JOptionPane;


public class App {

    private static int intentos = 3;
    private static int usuarioId;
    private static double saldo;
    private static int pinActual;

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3306;
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String DATABASE = "atm";

    private static Connection connection = null;

    
    public App() {
        try {
            connection = getConnection(); // Reemplaza esto con tu conexión real
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public double getSaldo(){
        return saldo;
    }

    public boolean loginAttempt(String username, int PIN){
           
        //Log-In logic
        if (intentos > 0) {
            if (validarPIN(connection, pinIngresado)) {
                pinActual = pinIngresado;
                return true;
            } else {
                intentos--;
                if (intentos > 0) {
                    JOptionPane.showMessageDialog(null,"PIN incorrecto. Le quedan " + intentos + " intentos.");
                } else {
                    JOptionPane.showMessageDialog(null,"PIN incorrecto. Ha excedido el número de intentos.");
                    System.exit(0);
                }
                return false;
            }
        }
    }



    public static boolean validarPIN(Connection connection, String username, int pin) {
        String query_verify = "SELECT pin FROM usuarios WHERE alias = ?";
        String query_get = "SELECT id, saldo FROM usuarios WHERE pin = ?";
        boolean f = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query_verify);
            preparedStatement.setInt(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getInt("pin") == pin) f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (!f) return false;


        try{
            PreparedStatement preparedStatement2 = connection.prepareStatement(query_get);
            preparedStatement2.setInt(1, username);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            if (resultSet2.next()) {
                usuarioId = resultSet2.getInt("id");
                saldo = resultSet2.getDouble("saldo");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void showSaldo(){
        JOptionPane.showMessageDialog(null,"Su saldo actual es: $" + saldo);
    }

    
    public static void realizarDeposito() {
        double cantidad = (double)JOptionPane.showInputDialog("Ingrese la cantidad a depositar: $");

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null,"Cantidad no válida.");
        } else {
            saldo += cantidad;
            updateSaldo(cantidad, "DEPOSITO");
            JOptionPane.showMessageDialog(null,"Depósito realizado con éxito. Su nuevo saldo es: $" + saldo);
        }
    }

    public static void realizarRetiro() {
        double cantidad = (double)JOptionPane.showInputDialog("Ingrese la cantidad a retirar: $");

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null,"Cantidad no válida.");
        } else if (cantidad > saldo) {
            JOptionPane.showMessageDialog(null,"Saldo insuficiente.");
        } else {
            saldo -= cantidad;
            updateSaldo(cantidad, "RETIRO");
            JOptionPane.showMessageDialog(null,"Retiro realizado con éxito. Su nuevo saldo es: $" + saldo);
        }
    }

    public static void cambiarPIN() {
        int pinIngresado = (int)JOptionPane.showInputDialog("Ingrese su PIN actual: ");

        if (pinIngresado == pinActual) {
            int nuevoPin = (int)JOptionPane.showInputDialog("Ingrese su nuevo PIN: ");
            
            int confirmacionPin = (int)JOptionPane.showInputDialog("Confirme su nuevo PIN: ");

            if (nuevoPin == confirmacionPin) {
                pinActual = nuevoPin;
                updatePIN(nuevoPin);
                JOptionPane.showMessageDialog(null,"PIN actualizado con éxito.");
            } else {
                JOptionPane.showMessageDialog(null,"Los PINs no coinciden.", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,"PIN incorrecto.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateSaldo(double cantidad, String operacion){
        String query_insert = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?,?,?)";
        String query_update = "UPDATE usuario SET saldo = ? WHERE id = ?;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query_insert);
            preparedStatement.setInt(1, usuarioId);
            preparedStatement.setString(2, operacion);
            preparedStatement.setDouble(3, cantidad);
            preparedStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (operacion.equals("DEPOSITO")){ //Depósito
            try{
                PreparedStatement preparedStatement2 = connection.prepareStatement(query_insert);
                preparedStatement2.setDouble(1, saldo+cantidad);
                preparedStatement2.setInt(2, usuarioId);
                preparedStatement2.executeQuery();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else { //Retiro
            try{
                PreparedStatement preparedStatement2 = connection.prepareStatement(query_insert);
                preparedStatement2.setDouble(1, saldo-cantidad);
                preparedStatement2.setInt(2, usuarioId);
                preparedStatement2.executeQuery();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
}
