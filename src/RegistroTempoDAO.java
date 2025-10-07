import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class RegistroTempoDAO {

    /**
     * Adiciona um novo registro de tempo no banco de dados.
     * RF010: Registrar tempo gasto em uma tarefa
     */
    public void adicionarRegistroTempo(RegistroTempo registro) {
        String sql = "INSERT INTO registros_tempo(horas_gastas, data_registro, tarefa_id, usuario_id) VALUES(?,?,?,?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, registro.getHorasGastas());
            pstmt.setDate(2, Date.valueOf(registro.getDataRegistro()));
            pstmt.setInt(3, registro.getTarefaId());
            pstmt.setInt(4, registro.getUsuarioId());

            pstmt.executeUpdate();
            System.out.println("Registro de tempo adicionado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar registro de tempo: " + e.getMessage());
        }
    }

    /**
     * Lista todos os registros de tempo de uma tarefa específica.
     */
    public List<RegistroTempo> listarPorTarefa(int tarefaId) {
        String sql = "SELECT * FROM registros_tempo WHERE tarefa_id = ? ORDER BY data_registro DESC";
        List<RegistroTempo> registros = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tarefaId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                RegistroTempo registro = new RegistroTempo();
                registro.setId(rs.getInt("id"));
                registro.setHorasGastas(rs.getDouble("horas_gastas"));
                registro.setDataRegistro(rs.getDate("data_registro").toLocalDate());
                registro.setTarefaId(rs.getInt("tarefa_id"));
                registro.setUsuarioId(rs.getInt("usuario_id"));
                registros.add(registro);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar registros de tempo: " + e.getMessage());
        }
        return registros;
    }

    /**
     * Lista todos os registros de tempo de um usuário específico.
     */
    public List<RegistroTempo> listarPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM registros_tempo WHERE usuario_id = ? ORDER BY data_registro DESC";
        List<RegistroTempo> registros = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                RegistroTempo registro = new RegistroTempo();
                registro.setId(rs.getInt("id"));
                registro.setHorasGastas(rs.getDouble("horas_gastas"));
                registro.setDataRegistro(rs.getDate("data_registro").toLocalDate());
                registro.setTarefaId(rs.getInt("tarefa_id"));
                registro.setUsuarioId(rs.getInt("usuario_id"));
                registros.add(registro);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar registros de tempo: " + e.getMessage());
        }
        return registros;
    }

    /**
     * Calcula o total de horas gastas em uma tarefa.
     */
    public double calcularTotalHorasTarefa(int tarefaId) {
        String sql = "SELECT SUM(horas_gastas) AS total FROM registros_tempo WHERE tarefa_id = ?";
        double total = 0.0;

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tarefaId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular total de horas: " + e.getMessage());
        }
        return total;
    }

    /**
     * Calcula o total de horas gastas por um usuário em um período.
     */
    public double calcularTotalHorasUsuarioPeriodo(int usuarioId, Date dataInicio, Date dataFim) {
        String sql = "SELECT SUM(horas_gastas) AS total FROM registros_tempo " +
                     "WHERE usuario_id = ? AND data_registro BETWEEN ? AND ?";
        double total = 0.0;

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            pstmt.setDate(2, dataInicio);
            pstmt.setDate(3, dataFim);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular total de horas: " + e.getMessage());
        }
        return total;
    }

    /**
     * Deleta um registro de tempo por ID.
     */
    public void deletarRegistroTempo(int id) {
        String sql = "DELETE FROM registros_tempo WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Registro de tempo deletado com sucesso.");
            } else {
                System.out.println("Nenhum registro encontrado com o ID " + id + ".");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao deletar registro de tempo: " + e.getMessage());
        }
    }

    /**
     * Atualiza um registro de tempo existente.
     */
    public void atualizarRegistroTempo(RegistroTempo registro) {
        String sql = "UPDATE registros_tempo SET horas_gastas = ?, data_registro = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, registro.getHorasGastas());
            pstmt.setDate(2, Date.valueOf(registro.getDataRegistro()));
            pstmt.setInt(3, registro.getId());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Registro de tempo atualizado com sucesso.");
            } else {
                System.out.println("Nenhum registro encontrado com o ID " + registro.getId() + ".");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar registro de tempo: " + e.getMessage());
        }
    }
}