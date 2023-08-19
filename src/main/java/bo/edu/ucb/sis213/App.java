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
            connection = getConnection(); // Conectarse a la base de datos al instanciar la clase
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }
    }


    public static Connection getConnection() throws SQLException {
        //Connector genérico para la BDD
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

    //  FUNCIONES DE LA APP

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



    public void setUsuario(String username){
        //Obtener el ID del usuario
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
        //Mostrar el saldo actual en pantalla
        JOptionPane.showMessageDialog(null,"Su saldo actual es: $" + saldo);
    }

    
    public void realizarDeposito() {
        //Depósito por ventana
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
        //Retiro por ventana
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
        //Cambiar PIN por ventana
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


    //FUNCIONES CON LA BDD

    public boolean validarPIN(Connection connection, String username, int pin) {
        //Validar PIN desde la BDD
        String query_verify = "SELECT pin FROM usuarios WHERE alias = ?";
        String query_get = "SELECT id, saldo FROM usuarios WHERE alias = ?";
        boolean f = false;

        //Validar PIN con el usuario
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query_verify);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) { //Si existe el usuario
                if (resultSet.getInt("pin") == pin) f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (!f) return false;

        //Recuperar el ID del usuario y su saldo
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

    private void updateSaldo(double cantidad, String operacion){
        String query_insert = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?,?,?)";
        String query_update = "UPDATE usuarios SET saldo = ? WHERE id = ?;";

        //Actualizar el historial en la BDD
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query_insert);
            preparedStatement.setInt(1, usuarioId);
            preparedStatement.setString(2, operacion);
            preparedStatement.setDouble(3, cantidad);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Actualizar el saldo en la BDD
        if (operacion.equals("DEPOSITO")){ //Depósito
            try{
                PreparedStatement preparedStatement2 = connection.prepareStatement(query_update);
                preparedStatement2.setDouble(1, saldo);
                preparedStatement2.setInt(2, usuarioId);
                preparedStatement2.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else { //Retiro
            try{
                PreparedStatement preparedStatement2 = connection.prepareStatement(query_update);
                preparedStatement2.setDouble(1, saldo);
                preparedStatement2.setInt(2, usuarioId);
                preparedStatement2.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    private void updatePIN(int nuevoPin){
        String query_update = "UPDATE usuarios SET pin = ? WHERE id = ?;";
        //Actualizar el PIN en la BDD
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
