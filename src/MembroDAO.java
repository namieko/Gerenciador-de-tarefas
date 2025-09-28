import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MembroDAO {

    /**
     * Adiciona um novo membro na tabela 'membros'.
     */
    public void adicionarMembro(Membro membro) {
        String sql = "INSERT INTO membros(nome) VALUES(?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, membro.getNome());
            pstmt.executeUpdate();
            System.out.println("Membro '" + membro.getNome() + "' adicionado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar membro: " + e.getMessage());
        }
    }

    /**
     * Retorna uma lista com todos os membros cadastrados.
     */
    public List<Membro> listarTodos() {
        String sql = "SELECT * FROM membros";
        List<Membro> membros = new ArrayList<>();

        try (Connection conn = Database.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Membro membro = new Membro();
                membro.setId(rs.getInt("id"));
                membro.setNome(rs.getString("nome"));
                membros.add(membro);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar membros: " + e.getMessage());
        }
        return membros;
    }
    
    /**
     * Atualiza o nome de um membro existente no banco de dados.
     */
    public void atualizarMembro(Membro membro) {
        String sql = "UPDATE membros SET nome = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, membro.getNome());
            pstmt.setInt(2, membro.getId());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Membro com ID " + membro.getId() + " foi atualizado com sucesso.");
            } else {
                System.out.println("Nenhum membro encontrado com o ID " + membro.getId() + ".");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar membro: " + e.getMessage());
        }
    }

    /**
     * Deleta um membro do banco de dados com base no seu ID.
     */
    public void deletarMembroPorId(int id) {
        String sql = "DELETE FROM membros WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Membro com ID " + id + " foi deletado com sucesso.");
            } else {
                System.out.println("Nenhum membro encontrado com o ID " + id + ".");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao deletar membro: " + e.getMessage());
        }
    }

}