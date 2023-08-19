package bo.edu.ucb.sis213;

import java.sql.*;

import javax.swing.JOptionPane;


public class App {

    private static int intentos = 3;
    private static int usuarioId;
    private static double saldo;
    private static int pinActual;

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3307;
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


    public static Connection getConnection() throws SQLException {
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
        try {
            // Asegúrate de tener el driver de MySQL agregado en tu proyecto
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found.", e);
        }

        return DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
    }

    public boolean loginAttempt(String username, int pinIngresado){
           
        //Log-In logic
        if (intentos > 0) {
            if (validarPIN(connection, username, pinIngresado)) {
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
        } else {
            JOptionPane.showMessageDialog(null,"PIN incorrecto. Ha excedido el número de intentos.");
            System.exit(0);
            return false;
        }
    }



    public boolean validarPIN(Connection connection, String username, int pin) {
        String query_verify = "SELECT pin FROM usuarios WHERE alias = ?";
        String query_get = "SELECT id, saldo FROM usuarios WHERE alias = ?";
        boolean f = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query_verify);
            preparedStatement.setString(1, username);
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
            preparedStatement2.setString(1, username);
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

    public void setUsuario(String username){
        String query = "SELECT id FROM usuarios WHERE alias = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                usuarioId = resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getSaldo(){
        return saldo;
    }


    public void showSaldo(){
        JOptionPane.showMessageDialog(null,"Su saldo actual es: $" + saldo);
    }

    
    public void realizarDeposito() {
        double cantidad = Double.parseDouble(JOptionPane.showInputDialog("Ingrese la cantidad a depositar: $"));

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null,"Cantidad no válida.");
        } else {
            saldo += cantidad;
            updateSaldo(cantidad, "DEPOSITO");
            JOptionPane.showMessageDialog(null,"Depósito realizado con éxito. Su nuevo saldo es: $" + saldo);
        }
    }

    public void realizarRetiro() {
        double cantidad = Double.parseDouble(JOptionPane.showInputDialog("Ingrese la cantidad a retirar: $"));

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

    public void cambiarPIN() {
        int pinIngresado = Integer.parseInt(JOptionPane.showInputDialog("Ingrese su PIN actual: "));

        if (pinIngresado == pinActual) {
            int nuevoPin = Integer.parseInt(JOptionPane.showInputDialog("Ingrese su nuevo PIN: "));
            
            int confirmacionPin = Integer.parseInt(JOptionPane.showInputDialog("Confirme su nuevo PIN: "));

            if (nuevoPin == confirmacionPin) {
                pinActual = nuevoPin;
                updatePIN(nuevoPin);
                JOptionPane.showMessageDialog(null,"PIN actualizado con éxito.");
            } else {
                JOptionPane.showMessageDialog(null,"Los PINs no coinciden.");
            }
        } else {
            JOptionPane.showMessageDialog(null,"PIN incorrecto.");
        }
    }

    private void updateSaldo(double cantidad, String operacion){
        String query_insert = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?,?,?)";
        String query_update = "UPDATE usuarios SET saldo = ? WHERE id = ?;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query_insert);
            preparedStatement.setInt(1, usuarioId);
            preparedStatement.setString(2, operacion);
            preparedStatement.setDouble(3, cantidad);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (operacion.equals("DEPOSITO")){ //Depósito
            try{
                PreparedStatement preparedStatement2 = connection.prepareStatement(query_update);
                preparedStatement2.setDouble(1, saldo+cantidad);
                preparedStatement2.setInt(2, usuarioId);
                preparedStatement2.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else { //Retiro
            try{
                PreparedStatement preparedStatement2 = connection.prepareStatement(query_update);
                preparedStatement2.setDouble(1, saldo-cantidad);
                preparedStatement2.setInt(2, usuarioId);
                preparedStatement2.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    private void updatePIN(int nuevoPin){
        String query_update = "UPDATE usuarios SET pin = ? WHERE id = ?:";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query_update);
            preparedStatement.setInt(1, nuevoPin);
            preparedStatement.setInt(2, usuarioId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
