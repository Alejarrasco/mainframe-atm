package bo.edu.ucb.sis213.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class HistoricoDao {
    
    //  CONEXIÓN A LA BDD
    private static Connection connection = null;

    public HistoricoDao() {
        try {
            connection = ConnectionMySQL.getConnection(); // Conectarse a la base de datos al instanciar la clase
        } catch (Exception ex) {
            System.err.println("No se puede conectar a Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void actualizarHistorico(int usuarioId, BigDecimal cantidad, String operacion){
        String query_insert = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?,?,?)";

        //Actualizar el historial en la BDD
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query_insert);
            preparedStatement.setInt(1, usuarioId);
            preparedStatement.setString(2, operacion);
            preparedStatement.setBigDecimal(3, cantidad);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
