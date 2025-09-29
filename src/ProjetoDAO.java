import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.ResultSet;

public class ProjetoDAO {

    /**
     * Adiciona um novo projeto na tabela 'projetos' do banco de dados.
     * Caso de Uso 6: Criar projetos
     */
    public void adicionarProjeto(Projeto projeto) {
        String sql = "INSERT INTO projetos(nome, id_dono) VALUES(?,?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, projeto.getNome());
            pstmt.setInt(2, projeto.getIdDono());

            pstmt.executeUpdate();

            System.out.println("Projeto '" + projeto.getNome() + "' foi adicionado ao banco de dados.");

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar projeto: " + e.getMessage());
        }
    }

    /**
     * Busca e retorna uma lista com todos os projetos cadastrados no banco de dados.
     */
    public List<Projeto> listarTodos() {
        String sql = "SELECT * FROM projetos";
        List<Projeto> projetos = new ArrayList<>();

        try (Connection conn = Database.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Projeto projeto = new Projeto();
                projeto.setId(rs.getInt("id"));
                projeto.setNome(rs.getString("nome"));
                projeto.setDono(rs.getInt("id_dono"));
                projetos.add(projeto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar os projetos: " + e.getMessage());
        }
        
        return projetos;
    }

    /**
     * Lista todos os projetos de um usuário específico.
     */
    public List<Projeto> listarTodosPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM projetos WHERE id_dono = ?";
        List<Projeto> projetos = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Projeto projeto = new Projeto();
                projeto.setId(rs.getInt("id"));
                projeto.setNome(rs.getString("nome"));
                projeto.setDono(rs.getInt("id_dono"));
                projetos.add(projeto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar projetos do usuário: " + e.getMessage());
        }
        
        return projetos;
    }

    /**
     * Deleta um projeto do banco de dados com base no seu ID.
     * Caso de Uso 5: Deletar projetos
     */
    public void deletarProjetoPorId(int id) {
        String sql = "DELETE FROM projetos WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Projeto com ID " + id + " foi deletado com sucesso.");
            } else {
                System.out.println("Nenhum projeto encontrado com o ID " + id + ". Nenhuma ação foi tomada.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao deletar projeto: " + e.getMessage());
        }
    }

    /**
     * Atualiza o nome de um projeto existente no banco de dados.
     * Caso de Uso 7: Editar projetos
     */
    public void atualizarProjeto(Projeto projeto) {
        String sql = "UPDATE projetos SET nome = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, projeto.getNome());
            pstmt.setInt(2, projeto.getId());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Projeto com ID " + projeto.getId() + " foi atualizado com sucesso.");
            } else {
                System.out.println("Nenhum projeto encontrado com o ID " + projeto.getId() + ". Nenhuma alteração foi feita.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar projeto: " + e.getMessage());
        }
    }

    /**
     * Busca um projeto específico por ID.
     */
    public Projeto buscarPorId(int id) {
        String sql = "SELECT * FROM projetos WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Projeto projeto = new Projeto();
                projeto.setId(rs.getInt("id"));
                projeto.setNome(rs.getString("nome"));
                projeto.setDono(rs.getInt("id_dono"));
                return projeto;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar projeto: " + e.getMessage());
        }
        
        return null;
    }
}