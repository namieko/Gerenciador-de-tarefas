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
     * @param projeto O objeto Projeto que contém os dados a serem salvos.
     */
    public void adicionarProjeto(Projeto projeto) {
        // Comando SQL para inserir um novo registro. 
        // O '?' é um marcador de posição que vamos preencher de forma segura.
        String sql = "INSERT INTO projetos(nome) VALUES(?)";

        // O 'try-with-resources' garante que a conexão com o banco de dados
        // seja fechada automaticamente no final, mesmo que ocorra um erro.
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Aqui nós substituímos o primeiro '?' no SQL pelo nome do projeto.
            // Usar PreparedStatement protege contra ataques de SQL Injection.
            pstmt.setString(1, projeto.getNome());

            // Esta linha executa o comando SQL no banco de dados.
            pstmt.executeUpdate();

            System.out.println("Projeto '" + projeto.getNome() + "' foi adicionado ao banco de dados.");

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar projeto: " + e.getMessage());
        }
    }
    /**
     * Busca e retorna uma lista com todos os projetos cadastrados no banco de dados.
     * @return Uma lista de objetos Projeto.
     */
    public List<Projeto> listarTodos() {
        // O comando SQL para selecionar todos os registros da tabela projetos.
        String sql = "SELECT * FROM projetos";
        
        // Criamos uma lista vazia onde colocaremos os projetos encontrados.
        List<Projeto> projetos = new ArrayList<>();

        try (Connection conn = Database.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){ // ResultSet é como uma tabela virtual com os resultados.
            
            // O loop 'while (rs.next())' percorre cada linha que o banco de dados retornou.
            while (rs.next()) {
                // Para cada linha, criamos um novo objeto Projeto.
                Projeto projeto = new Projeto();
                
                // Pegamos os valores das colunas 'id' e 'nome' da linha atual.
                projeto.setId(rs.getInt("id"));
                projeto.setNome(rs.getString("nome"));
                
                // Adicionamos o projeto recém-criado à nossa lista.
                projetos.add(projeto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar os projetos: " + e.getMessage());
        }
        
        // Retornamos a lista (pode estar vazia se não houver projetos).
        return projetos;
    }

    /**
     * Deleta um projeto do banco de dados com base no seu ID.
     * @param id O ID do projeto a ser deletado.
     */
    public void deletarProjetoPorId(int id) {
        // SQL para deletar um registro ONDE o id corresponder ao parâmetro.
        // A cláusula WHERE é ESSENCIAL aqui. Sem ela, todos os registros seriam apagados!
        String sql = "DELETE FROM projetos WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define o valor do primeiro '?' no SQL para o id que recebemos.
            pstmt.setInt(1, id);

            // O método executeUpdate() retorna o número de linhas que foram afetadas.
            // Para um delete por ID, esperamos que seja 1 (se o ID existir).
            int linhasAfetadas = pstmt.executeUpdate();

            // Verificamos se alguma linha foi realmente deletada para dar um feedback melhor.
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
     * @param projeto O objeto Projeto contendo o ID do projeto a ser atualizado e o novo nome.
     */
    public void atualizarProjeto(Projeto projeto) {
        // SQL para ATUALIZAR a coluna nome ONDE o id for o especificado.
        String sql = "UPDATE projetos SET nome = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // O primeiro '?' é o novo nome (String).
            pstmt.setString(1, projeto.getNome());
            // O segundo '?' é o id para a cláusula WHERE (int).
            pstmt.setInt(2, projeto.getId());

            // Executa a atualização e pega o número de linhas afetadas.
            int linhasAfetadas = pstmt.executeUpdate();

            // Dá um feedback para o usuário.
            if (linhasAfetadas > 0) {
                System.out.println("Projeto com ID " + projeto.getId() + " foi atualizado com sucesso.");
            } else {
                System.out.println("Nenhum projeto encontrado com o ID " + projeto.getId() + ". Nenhuma alteração foi feita.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar projeto: " + e.getMessage());
        }
    }
}