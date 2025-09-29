import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDAO {

    /**
     * Adiciona um novo comentário no banco de dados.
     * Caso de Uso 10: Adicionar comentário em uma tarefa
     */
    public void adicionarComentario(Comentario comentario) {
        String sql = "INSERT INTO comentarios(tarefa_id, texto, autor_id, data_hora) VALUES(?,?,?,?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, comentario.getTarefaId());
            pstmt.setString(2, comentario.getTexto());
            pstmt.setInt(3, comentario.getAutor());
            pstmt.setDate(4, Date.valueOf(comentario.getDataHora()));

            pstmt.executeUpdate();
            System.out.println("Comentário adicionado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar comentário: " + e.getMessage());
        }
    }

    /**
     * Lista todos os comentários de uma tarefa específica.
     */
    public List<Comentario> listarPorTarefa(int tarefaId) {
        String sql = "SELECT * FROM comentarios WHERE tarefa_id = ? ORDER BY data_hora DESC";
        List<Comentario> comentarios = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tarefaId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Comentario comentario = new Comentario();
                comentario.setId(rs.getInt("id"));
                comentario.setTarefaId(rs.getInt("tarefa_id"));
                comentario.setTexto(rs.getString("texto"));
                comentario.setAutor(rs.getInt("autor_id"));
                comentario.setDataHora(rs.getDate("data_hora").toLocalDate());
                comentarios.add(comentario);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar comentários: " + e.getMessage());
        }
        return comentarios;
    }

    /**
     * Deleta um comentário do banco de dados.
     */
    public void deletarComentario(int id) {
        String sql = "DELETE FROM comentarios WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Comentário deletado com sucesso.");
            } else {
                System.out.println("Nenhum comentário encontrado com o ID " + id + ".");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao deletar comentário: " + e.getMessage());
        }
    }

    /**
     * Conta o número total de comentários em uma tarefa.
     */
    public int contarComentariosPorTarefa(int tarefaId) {
        String sql = "SELECT COUNT(*) AS total FROM comentarios WHERE tarefa_id = ?";
        int total = 0;

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tarefaId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar comentários: " + e.getMessage());
        }
        return total;
    }
}