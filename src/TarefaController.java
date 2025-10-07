import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TarefaController {
    private final TarefaDAO tarefaDAO;
    
    public TarefaController() {
        this.tarefaDAO = new TarefaDAO();
    }

    public TarefaController(TarefaDAO tarefaDAO) {
        this.tarefaDAO = tarefaDAO;
    }

    /**
     * Adiciona uma nova tarefa com validações.
     * Caso de Uso 3: Criar tarefas
     */
    public boolean adicionarNovaTarefa(String titulo, String descricao, LocalDate prazo, 
                                       int projetoId, int membroId) {
        // Validações
        if (titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Título da tarefa não pode ser vazio!");
            return false;
        }

        if (prazo == null) {
            System.out.println("Prazo é obrigatório!");
            return false;
        }

        if (projetoId <= 0) {
            System.out.println("ID do projeto inválido!");
            return false;
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setNome(titulo.trim());
        tarefa.setDescricao(descricao != null ? descricao.trim() : "");
        tarefa.setPrazo(prazo);
        tarefa.setStatus(StatusTarefa.A_FAZER);
        tarefa.setProjetoId(projetoId);
        
        if (membroId > 0) {
            tarefa.setMembroId(membroId);
        }

        tarefaDAO.adicionarTarefa(tarefa);
        return true;
    }

    /**
     * Lista todas as tarefas cadastradas.
     */
    public List<Tarefa> listarTarefas() {
        return tarefaDAO.listarTodas();
    }

    /**
     * Lista tarefas de um projeto específico.
     */
    public List<Tarefa> listarTarefasPorProjeto(int projetoId) {
        if (projetoId <= 0) {
            System.out.println("ID do projeto inválido!");
            return null;
        }
        return tarefaDAO.listarPorProjeto(projetoId);
    }

    /**
     * Deleta uma tarefa por ID.
     * Caso de Uso 2: Deletar tarefas
     */
    public boolean deletarTarefa(int id) {
        if (id <= 0) {
            System.out.println("ID inválido!");
            return false;
        }

        Tarefa tarefa = tarefaDAO.buscarPorId(id);
        if (tarefa == null) {
            System.out.println("Tarefa não encontrada!");
            return false;
        }

        tarefaDAO.deletarTarefaPorId(id);
        return true;
    }

    /**
     * Edita uma tarefa existente.
     * Caso de Uso 4: Editar tarefas
     */
    public boolean editarTarefa(int id, String novoTitulo, String novaDescricao, LocalDate novoPrazo) {
        // Validações
        if (id <= 0) {
            System.out.println("ID inválido!");
            return false;
        }

        Tarefa tarefa = tarefaDAO.buscarPorId(id);
        if (tarefa == null) {
            System.out.println("Tarefa não encontrada!");
            return false;
        }

        if (novoTitulo == null || novoTitulo.trim().isEmpty()) {
            System.out.println("Título não pode ser vazio!");
            return false;
        }

        if (novoPrazo == null) {
            System.out.println("Prazo é obrigatório!");
            return false;
        }

        tarefa.setNome(novoTitulo.trim());
        tarefa.setDescricao(novaDescricao != null ? novaDescricao.trim() : "");
        tarefa.setPrazo(novoPrazo);

        tarefaDAO.atualizarTarefa(tarefa);
        return true;
    }

    /**
     * Atualiza o status de uma tarefa.
     * Caso de Uso 8: Andamento de tarefas em tempo real
     */
    public boolean atualizarStatusTarefa(int id, StatusTarefa novoStatus) {
        if (id <= 0) {
            System.out.println("ID inválido!");
            return false;
        }

        if (novoStatus == null) {
            System.out.println("Status não pode ser nulo!");
            return false;
        }

        Tarefa tarefa = tarefaDAO.buscarPorId(id);
        if (tarefa == null) {
            System.out.println("Tarefa não encontrada!");
            return false;
        }

        tarefaDAO.atualizarStatusTarefa(id, novoStatus);
        return true;
    }

    /**
     * Move a tarefa para o próximo status.
     */
    public boolean moverTarefaParaProximoStatus(int id) {
        Tarefa tarefa = tarefaDAO.buscarPorId(id);
        if (tarefa == null) {
            System.out.println("Tarefa não encontrada!");
            return false;
        }

        StatusTarefa novoStatus;
        switch (tarefa.getStatus()) {
            case A_FAZER:
                novoStatus = StatusTarefa.EM_ANDAMENTO;
                break;
            case EM_ANDAMENTO:
                novoStatus = StatusTarefa.CONCLUIDA;
                break;
            case CONCLUIDA:
                System.out.println("Tarefa já está concluída!");
                return false;
            default:
                return false;
        }

        tarefaDAO.atualizarStatusTarefa(id, novoStatus);
        return true;
    }

    /**
     * Filtra tarefas por status.
     * Caso de Uso 9: Filtrar tarefas pelo status de andamento
     */
    public List<Tarefa> filtrarTarefasPorStatus(StatusTarefa status) {
        if (status == null) {
            System.out.println("Status não pode ser nulo!");
            return null;
        }
        return tarefaDAO.filtrarPorStatus(status);
    }

    /**
     * Filtra tarefas por status dentro de um projeto específico.
     */
    public List<Tarefa> filtrarTarefasPorStatusEProjeto(StatusTarefa status, int projetoId) {
        if (status == null) {
            System.out.println("Status não pode ser nulo!");
            return null;
        }
        if (projetoId <= 0) {
            System.out.println("ID do projeto inválido!");
            return null;
        }
        return tarefaDAO.filtrarPorStatusEProjeto(status, projetoId);
    }

    /**
     * Busca uma tarefa por ID.
     */
    public Tarefa buscarTarefaPorId(int id) {
        if (id <= 0) {
            System.out.println("ID inválido!");
            return null;
        }
        return tarefaDAO.buscarPorId(id);
    }

    /**
     * Retorna estatísticas de tarefas.
     */
    public int getContagemTotalTarefas() {
        return tarefaDAO.getContagemTotal();
    }

    /**
     * Retorna contagem de tarefas por status.
     */
    public Map<StatusTarefa, Integer> getContagemPorStatus() {
        return tarefaDAO.getContagemPorStatus();
    }

    /**
     * Atribui um membro a uma tarefa.
     */
    public boolean atribuirMembroATarefa(int tarefaId, int membroId) {
        if (tarefaId <= 0 || membroId <= 0) {
            System.out.println("IDs inválidos!");
            return false;
        }

        Tarefa tarefa = tarefaDAO.buscarPorId(tarefaId);
        if (tarefa == null) {
            System.out.println("Tarefa não encontrada!");
            return false;
        }

        tarefa.setMembroId(membroId);
        tarefaDAO.atualizarTarefa(tarefa);
        return true;
    }

    /**
     * Verifica se uma tarefa está atrasada.
     */
    public boolean tarefaEstaAtrasada(int tarefaId) {
        Tarefa tarefa = tarefaDAO.buscarPorId(tarefaId);
        if (tarefa == null) {
            return false;
        }

        return tarefa.getStatus() != StatusTarefa.CONCLUIDA 
               && tarefa.getPrazo().isBefore(LocalDate.now());
    }
}