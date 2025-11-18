import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConviteProjetoDAO {

    /**
     * Envia um convite para um usuário participar de um projeto.
     */
    public void enviarConvite(int projetoId, int remetenteId, int destinatarioId) {
        String sql = "INSERT INTO convites_projeto(projeto_id, remetente_id, destinatario_id, status, data_convite) VALUES(?,?,?,?,?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, projetoId);
            pstmt.setInt(2, remetenteId);
            pstmt.setInt(3, destinatarioId);
            pstmt.setString(4, StatusConvite.PENDENTE.name());
            pstmt.setDate(5, Date.valueOf(LocalDate.now()));

            pstmt.executeUpdate();
            System.out.println("Convite de projeto enviado!");

        } catch (SQLException e) {
            System.out.println("Erro ao enviar convite: " + e.getMessage());
        }
    }

    /**
     * Aceita um convite de projeto e adiciona o usuário como colaborador.
     */
    public void aceitarConvite(int conviteId) {
        String sql = "UPDATE convites_projeto SET status = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, StatusConvite.ACEITO.name());
            pstmt.setInt(2, conviteId);
            pstmt.executeUpdate();

            System.out.println("Convite aceito!");

        } catch (SQLException e) {
            System.out.println("Erro ao aceitar convite: " + e.getMessage());
        }
    }

    /**
     * Recusa um convite de projeto.
     */
    public void recusarConvite(int conviteId) {
        String sql = "UPDATE convites_projeto SET status = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, StatusConvite.RECUSADO.name());
            pstmt.setInt(2, conviteId);
            pstmt.executeUpdate();

            System.out.println("Convite recusado!");

        } catch (SQLException e) {
            System.out.println("Erro ao recusar convite: " + e.getMessage());
        }
    }

    /**
     * Lista convites pendentes para um usuário.
     */
    public List<ConviteProjeto> listarConvitesPendentes(int usuarioId) {
        String sql = "SELECT * FROM convites_projeto WHERE destinatario_id = ? AND status = ?";
        List<ConviteProjeto> convites = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, StatusConvite.PENDENTE.name());
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ConviteProjeto convite = new ConviteProjeto();
                convite.setId(rs.getInt("id"));
                convite.setProjetoId(rs.getInt("projeto_id"));
                convite.setRemetenteId(rs.getInt("remetente_id"));
                convite.setDestinatarioId(rs.getInt("destinatario_id"));
                convite.setStatus(StatusConvite.valueOf(rs.getString("status")));
                convite.setDataConvite(rs.getDate("data_convite").toLocalDate());
                convites.add(convite);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar convites: " + e.getMessage());
        }
        
        return convites;
    }

    /**
     * Busca um convite específico por ID.
     */
    public ConviteProjeto buscarPorId(int id) {
        String sql = "SELECT * FROM convites_projeto WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                ConviteProjeto convite = new ConviteProjeto();
                convite.setId(rs.getInt("id"));
                convite.setProjetoId(rs.getInt("projeto_id"));
                convite.setRemetenteId(rs.getInt("remetente_id"));
                convite.setDestinatarioId(rs.getInt("destinatario_id"));
                convite.setStatus(StatusConvite.valueOf(rs.getString("status")));
                convite.setDataConvite(rs.getDate("data_convite").toLocalDate());
                return convite;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar convite: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Verifica se um usuário já tem um convite pendente para um projeto.
     */
    public boolean jaExisteConvitePendente(int projetoId, int destinatarioId) {
        String sql = "SELECT COUNT(*) as total FROM convites_projeto " +
                     "WHERE projeto_id = ? AND destinatario_id = ? AND status = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, projetoId);
            pstmt.setInt(2, destinatarioId);
            pstmt.setString(3, StatusConvite.PENDENTE.name());
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar convite: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Lista todos os projetos em que um usuário é colaborador (convites aceitos).
     */
    public List<Projeto> listarProjetosColaborador(int usuarioId) {
        String sql = "SELECT p.* FROM projetos p " +
                     "INNER JOIN convites_projeto c ON p.id = c.projeto_id " +
                     "WHERE c.destinatario_id = ? AND c.status = ?";
        List<Projeto> projetos = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, StatusConvite.ACEITO.name());
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Projeto projeto = new Projeto();
                projeto.setId(rs.getInt("id"));
                projeto.setNome(rs.getString("nome"));
                projeto.setDono(rs.getInt("id_dono"));
                projetos.add(projeto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar projetos colaborador: " + e.getMessage());
        }
        
        return projetos;
    }
}