import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TarefaDAO {

    /**
     * Adiciona uma nova tarefa no banco de dados.
     * Caso de Uso 3: Criar tarefas
     */
    public void adicionarTarefa(Tarefa tarefa) {
        String sql = "INSERT INTO tarefas(titulo, descricao, prazo, status, projeto_id, membro_id) VALUES(?,?,?,?,?,?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tarefa.getNome());
            pstmt.setString(2, tarefa.getDescricao());
            pstmt.setDate(3, Date.valueOf(tarefa.getPrazo())); 
            pstmt.setString(4, tarefa.getStatus().name()); 
            pstmt.setInt(5, tarefa.getProjetoId());
            
            if (tarefa.getMembroId() > 0) {
                pstmt.setInt(6, tarefa.getMembroId());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

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
                Tarefa tarefa = criarTarefaDeResultSet(rs);
                tarefas.add(tarefa);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar tarefas: " + e.getMessage());
        }
        return tarefas;
    }

    /**
     * Lista todas as tarefas de um projeto específico.
     */
    public List<Tarefa> listarPorProjeto(int projetoId) {
        String sql = "SELECT * FROM tarefas WHERE projeto_id = ?";
        List<Tarefa> tarefas = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, projetoId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Tarefa tarefa = criarTarefaDeResultSet(rs);
                tarefas.add(tarefa);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar tarefas do projeto: " + e.getMessage());
        }
        return tarefas;
    }

    /**
     * Atualiza apenas o status de uma tarefa específica.
     * Caso de Uso 8: Andamento de tarefas em tempo real
     */
    public void atualizarStatusTarefa(int id, StatusTarefa novoStatus) {
        String sql = "UPDATE tarefas SET status = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, novoStatus.name());
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
     * Atualiza todos os dados de uma tarefa.
     * Caso de Uso 4: Editar tarefas
     */
    public void atualizarTarefa(Tarefa tarefa) {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ?, prazo = ?, status = ?, membro_id = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tarefa.getNome());
            pstmt.setString(2, tarefa.getDescricao());
            pstmt.setDate(3, Date.valueOf(tarefa.getPrazo()));
            pstmt.setString(4, tarefa.getStatus().name());
            
            if (tarefa.getMembroId() > 0) {
                pstmt.setInt(5, tarefa.getMembroId());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            pstmt.setInt(6, tarefa.getId());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Tarefa ID " + tarefa.getId() + " atualizada com sucesso.");
            } else {
                System.out.println("Nenhuma tarefa encontrada com o ID " + tarefa.getId() + ".");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar tarefa: " + e.getMessage());
        }
    }

    /**
     * Deleta uma tarefa do banco de dados com base no seu ID.
     * Caso de Uso 2: Deletar tarefas
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
     * Caso de Uso 9: Filtrar tarefas pelo status de andamento
     */
    public List<Tarefa> filtrarPorStatus(StatusTarefa status) {
        List<Tarefa> tarefasFiltradas = new ArrayList<>();
        String sql = "SELECT * FROM tarefas WHERE status = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Tarefa tarefa = criarTarefaDeResultSet(rs);
                tarefasFiltradas.add(tarefa);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao filtrar tarefas por status: " + e.getMessage());
        }
        
        return tarefasFiltradas;
    }

    /**
     * Filtra tarefas por status dentro de um projeto específico.
     */
    public List<Tarefa> filtrarPorStatusEProjeto(StatusTarefa status, int projetoId) {
        List<Tarefa> tarefasFiltradas = new ArrayList<>();
        String sql = "SELECT * FROM tarefas WHERE status = ? AND projeto_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setInt(2, projetoId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Tarefa tarefa = criarTarefaDeResultSet(rs);
                tarefasFiltradas.add(tarefa);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao filtrar tarefas: " + e.getMessage());
        }
        
        return tarefasFiltradas;
    }
    
    /**
     * Conta o número total de tarefas no banco de dados.
     */
    public int getContagemTotal() {
        String sql = "SELECT COUNT(*) AS total FROM tarefas";
        int total = 0;

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
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
     */
    public Map<StatusTarefa, Integer> getContagemPorStatus() {
        Map<StatusTarefa, Integer> contagem = new HashMap<>();
        String sql = "SELECT status, COUNT(*) AS cont FROM tarefas GROUP BY status";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                StatusTarefa status = StatusTarefa.valueOf(rs.getString("status"));
                int count = rs.getInt("cont");
                contagem.put(status, count);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar tarefas por status: " + e.getMessage());
        }
        return contagem;
    }

    /**
     * Busca uma tarefa específica por ID.
     */
    public Tarefa buscarPorId(int id) {
        String sql = "SELECT * FROM tarefas WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return criarTarefaDeResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar tarefa: " + e.getMessage());
        }
        return null;
    }

    /**
     * Método auxiliar para criar objeto Tarefa a partir de ResultSet.
     */
    private Tarefa criarTarefaDeResultSet(ResultSet rs) throws SQLException {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(rs.getInt("id"));
        tarefa.setNome(rs.getString("titulo"));
        tarefa.setDescricao(rs.getString("descricao"));
        tarefa.setPrazo(rs.getDate("prazo").toLocalDate());
        tarefa.setStatus(StatusTarefa.valueOf(rs.getString("status")));
        tarefa.setProjetoId(rs.getInt("projeto_id"));
        
        int membroId = rs.getInt("membro_id");
        if (!rs.wasNull()) {
            tarefa.setMembroId(membroId);
        }
        
        return tarefa;
    }
}