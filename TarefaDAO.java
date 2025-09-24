import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date; // Importante: usar java.sql.Date
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TarefaDAO {

    /**
     * Adiciona uma nova tarefa no banco de dados.
     */
    public void adicionarTarefa(Tarefa tarefa) {
        String sql = "INSERT INTO tarefas(titulo, descricao, prazo, status, projeto_id, membro_id) VALUES(?,?,?,?,?,?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tarefa.getNome());
            pstmt.setString(2, tarefa.getDescricao());
            // Convertemos LocalDate do Java para o tipo Date do SQL
            pstmt.setDate(3, Date.valueOf(tarefa.getPrazo())); 
            // Convertemos o Enum para String para salvar no banco
            pstmt.setString(4, tarefa.getStatus().name()); 
            pstmt.setInt(5, tarefa.getProjetoId());
            pstmt.setInt(6, tarefa.getMembroId());

            pstmt.executeUpdate();
            System.out.println("Tarefa '" + tarefa.getNome() + "' adicionada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar tarefa: " + e.getMessage());
        }
    }

    /**
     * Retorna uma lista com todas as tarefas cadastradas.
     */
    public List<Tarefa> listarTodas() {
        String sql = "SELECT * FROM tarefas";
        List<Tarefa> tarefas = new ArrayList<>();

        try (Connection conn = Database.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(rs.getInt("id"));
                tarefa.setNome(rs.getString("titulo"));
                tarefa.setDescricao(rs.getString("descricao"));
                // Convertemos o Date do SQL de volta para LocalDate do Java
                tarefa.setPrazo(rs.getDate("prazo").toLocalDate());
                // Convertemos a String do banco de volta para o Enum
                tarefa.setStatus(StatusTarefa.valueOf(rs.getString("status"))); 
                tarefa.setProjetoId(rs.getInt("projeto_id"));
                tarefa.setMembroId(rs.getInt("membro_id"));
                
                tarefas.add(tarefa);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar tarefas: " + e.getMessage());
        }
        return tarefas;
    }

    /**
     * Atualiza apenas o status de uma tarefa específica.
     * @param id O ID da tarefa a ser atualizada.
     * @param novoStatus O novo status para a tarefa.
     */
    public void atualizarStatusTarefa(int id, StatusTarefa novoStatus) {
        String sql = "UPDATE tarefas SET status = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // O primeiro '?' é o novo status, que salvamos como texto.
            pstmt.setString(1, novoStatus.name());
            // O segundo '?' é o ID da tarefa.
            pstmt.setInt(2, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Status da tarefa ID " + id + " atualizado para " + novoStatus + ".");
            } else {
                System.out.println("Nenhuma tarefa encontrada com o ID " + id + ".");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar status da tarefa: " + e.getMessage());
        }
    }

    /**
     * Deleta uma tarefa do banco de dados com base no seu ID.
     */
    public void deletarTarefaPorId(int id) {
        String sql = "DELETE FROM tarefas WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Tarefa com ID " + id + " foi deletada com sucesso.");
            } else {
                System.out.println("Nenhuma tarefa encontrada com o ID " + id + ".");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao deletar tarefa: " + e.getMessage());
        }
    }

    /**
     * Busca no banco de dados todas as tarefas que correspondem a um status específico.
     * @param status O status pelo qual as tarefas serão filtradas.
     * @return Uma lista de tarefas que correspondem ao filtro.
     */
    public List<Tarefa> filtrarPorStatus(StatusTarefa status) {
        List<Tarefa> tarefasFiltradas = new ArrayList<>();
        // A mágica está aqui: adicionamos a cláusula WHERE para filtrar
        String sql = "SELECT * FROM tarefas WHERE status = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Definimos o valor do '?' no SQL para o status que queremos buscar
            pstmt.setString(1, status.name());
            
            ResultSet rs = pstmt.executeQuery();

            // O resto do código é idêntico ao método listarTodos,
            // mas ele opera apenas sobre o resultado já filtrado pelo banco.
            while (rs.next()) {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(rs.getInt("id"));
                tarefa.setNome(rs.getString("titulo"));
                tarefa.setDescricao(rs.getString("descricao"));
                tarefa.setPrazo(rs.getDate("prazo").toLocalDate());
                tarefa.setStatus(StatusTarefa.valueOf(rs.getString("status")));
                tarefa.setProjetoId(rs.getInt("projeto_id"));
                tarefa.setMembroId(rs.getInt("membro_id"));
                tarefasFiltradas.add(tarefa);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao filtrar tarefas por status: " + e.getMessage());
        }
        
        return tarefasFiltradas;
    }
    
    /**
     * Conta o número total de tarefas no banco de dados.
     * @return O número total de tarefas.
     */
    public int getContagemTotal() {
        // A função COUNT(*) do SQL conta todas as linhas de uma tabela.
        // Usamos "AS total" para dar um nome à coluna do resultado.
        String sql = "SELECT COUNT(*) AS total FROM tarefas";
        int total = 0;

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // O resultado de COUNT(*) é sempre uma única linha com uma única coluna.
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar o total de tarefas: " + e.getMessage());
        }
        return total;
    }

    /**
     * Conta o número de tarefas para cada status e retorna um Mapa.
     * @return Um Mapa onde a chave é o StatusTarefa e o valor é a contagem.
     */
    public Map<StatusTarefa, Integer> getContagemPorStatus() {
        // Map é uma estrutura de dados que armazena pares de chave-valor (como um dicionário).
        Map<StatusTarefa, Integer> contagem = new HashMap<>();

        // Este SQL conta as tarefas e as AGRUPA por status.
        String sql = "SELECT status, COUNT(*) AS cont FROM tarefas GROUP BY status";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // O resultado terá várias linhas, uma para cada status que tem pelo menos uma tarefa.
            while (rs.next()) {
                // Pegamos o status (String) e o convertemos para o nosso Enum.
                StatusTarefa status = StatusTarefa.valueOf(rs.getString("status"));
                // Pegamos a contagem para aquele status.
                int count = rs.getInt("cont");
                // Adicionamos a chave (status) e o valor (contagem) ao nosso Mapa.
                contagem.put(status, count);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar tarefas por status: " + e.getMessage());
        }
        return contagem;
    }
}