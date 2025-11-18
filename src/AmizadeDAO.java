import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AmizadeDAO {

    /**
     * Envia uma solicitação de amizade.
     */
    public void enviarSolicitacao(int usuarioId, int amigoId) {
        String sql = "INSERT INTO amizades(usuario_id, amigo_id, status, data_solicitacao) VALUES(?,?,?,?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, amigoId);
            pstmt.setString(3, StatusAmizade.PENDENTE.name());
            pstmt.setDate(4, Date.valueOf(LocalDate.now()));

            pstmt.executeUpdate();
            System.out.println("Solicitação de amizade enviada!");

        } catch (SQLException e) {
            System.out.println("Erro ao enviar solicitação: " + e.getMessage());
        }
    }

    /**
     * Aceita uma solicitação de amizade.
     */
    public void aceitarSolicitacao(int amizadeId) {
        String sql = "UPDATE amizades SET status = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, StatusAmizade.ACEITO.name());
            pstmt.setInt(2, amizadeId);
            pstmt.executeUpdate();

            System.out.println("Amizade aceita!");

        } catch (SQLException e) {
            System.out.println("Erro ao aceitar amizade: " + e.getMessage());
        }
    }

    /**
     * Recusa uma solicitação de amizade.
     */
    public void recusarSolicitacao(int amizadeId) {
        String sql = "UPDATE amizades SET status = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, StatusAmizade.RECUSADO.name());
            pstmt.setInt(2, amizadeId);
            pstmt.executeUpdate();

            System.out.println("Amizade recusada!");

        } catch (SQLException e) {
            System.out.println("Erro ao recusar amizade: " + e.getMessage());
        }
    }

    /**
     * Lista todos os amigos aceitos de um usuário.
     */
    public List<Usuario> listarAmigos(int usuarioId) {
        String sql = "SELECT u.* FROM usuarios u " +
                     "INNER JOIN amizades a ON (u.id = a.amigo_id OR u.id = a.usuario_id) " +
                     "WHERE (a.usuario_id = ? OR a.amigo_id = ?) AND a.status = ? AND u.id != ?";
        List<Usuario> amigos = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, usuarioId);
            pstmt.setString(3, StatusAmizade.ACEITO.name());
            pstmt.setInt(4, usuarioId);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                amigos.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar amigos: " + e.getMessage());
        }
        
        return amigos;
    }

    /**
     * Lista solicitações de amizade pendentes recebidas por um usuário.
     */
    public List<Amizade> listarSolicitacoesPendentes(int usuarioId) {
        String sql = "SELECT * FROM amizades WHERE amigo_id = ? AND status = ?";
        List<Amizade> solicitacoes = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, StatusAmizade.PENDENTE.name());
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Amizade amizade = new Amizade();
                amizade.setId(rs.getInt("id"));
                amizade.setUsuarioId(rs.getInt("usuario_id"));
                amizade.setAmigoId(rs.getInt("amigo_id"));
                amizade.setStatus(StatusAmizade.valueOf(rs.getString("status")));
                amizade.setDataSolicitacao(rs.getDate("data_solicitacao").toLocalDate());
                solicitacoes.add(amizade);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar solicitações: " + e.getMessage());
        }
        
        return solicitacoes;
    }

    /**
     * Verifica se já existe uma amizade (em qualquer status) entre dois usuários.
     */
    public boolean jaExisteAmizade(int usuarioId, int amigoId) {
        String sql = "SELECT COUNT(*) as total FROM amizades " +
                     "WHERE (usuario_id = ? AND amigo_id = ?) OR (usuario_id = ? AND amigo_id = ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, amigoId);
            pstmt.setInt(3, amigoId);
            pstmt.setInt(4, usuarioId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar amizade: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Remove uma amizade.
     */
    public void removerAmizade(int amizadeId) {
        String sql = "DELETE FROM amizades WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, amizadeId);
            pstmt.executeUpdate();
            System.out.println("Amizade removida!");

        } catch (SQLException e) {
            System.out.println("Erro ao remover amizade: " + e.getMessage());
        }
    }
}