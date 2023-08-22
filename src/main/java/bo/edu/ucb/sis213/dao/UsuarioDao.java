package bo.edu.ucb.sis213.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDao {

    //  CONEXIÓN A LA BDD
    private static Connection connection = null;

    public UsuarioDao() {
        try {
            connection = ConnectionMySQL.getConnection(); // Conectarse a la base de datos al instanciar la clase
        } catch (Exception ex) {
            System.err.println("No se puede conectar a Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    //FUNCIONES CON LA BDD

    public int getUsuarioId(String username){
        int result = -1;
        //Obtener el ID del usuario
        String query = "SELECT id FROM usuarios WHERE alias = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public int getUsuarioPin(String username){
        int result = -1;
        //Obtener el PIN del usuario
        String query = "SELECT pin FROM usuarios WHERE alias = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt("pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public double getUsuarioSaldo(String username){
        Double result = -1.0;
        //Obtener el Saldo del usuario
        String query = "SELECT saldo FROM usuarios WHERE alias = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getDouble("saldo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getUsuarioNombre(String username){
        String result = "";
        //Obtener el Nombre del usuario
        String query = "SELECT nombre FROM usuarios WHERE alias = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("nombre");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void actualizarSaldo(int usuarioId, double saldo){
        String query_update = "UPDATE usuarios SET saldo = ? WHERE id = ?;";
        
        try{
                PreparedStatement preparedStatement2 = connection.prepareStatement(query_update);
                preparedStatement2.setDouble(1, saldo);
                preparedStatement2.setInt(2, usuarioId);
                preparedStatement2.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    
    public void updatePIN(int usuarioId, int nuevoPin){
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
