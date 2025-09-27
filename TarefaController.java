import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeParseException; 
import java.util.InputMismatchException;      
public class TarefaController extends ControllerAbstract {

    private static TarefaDAO tarefaDAO = new TarefaDAO();

    public static void criarNovaTarefa() {
        System.out.println("\n--- CRIAR NOVA TAREFA ---");

        try {
            System.out.print("Digite o ID do projeto para esta tarefa: ");
            int projetoId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Digite o ID do membro para atribuir esta tarefa: ");
            int membroId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Digite o título da tarefa: ");
            String titulo = scanner.nextLine();
            System.out.print("Digite a descrição da tarefa: ");
            String descricao = scanner.nextLine();
            System.out.print("Digite o prazo (formato AAAA-MM-DD): ");
            String prazoStr = scanner.nextLine();
            LocalDate prazo = LocalDate.parse(prazoStr);
            Tarefa novaTarefa = new Tarefa();
            novaTarefa.setNome(titulo);
            novaTarefa.setDescricao(descricao);
            novaTarefa.setPrazo(prazo);
            novaTarefa.setProjetoId(projetoId);
            novaTarefa.setMembroId(membroId);
            novaTarefa.setStatus(StatusTarefa.A_FAZER);
            tarefaDAO.adicionarTarefa(novaTarefa);
            System.out.println("Tarefa criada com sucesso!");

        } catch (InputMismatchException e) {
            System.out.println("Erro: O ID deve ser um número. Operação cancelada.");
            scanner.nextLine(); // Limpa o buffer do scanner
        } catch (DateTimeParseException e) {
            System.out.println("Erro: Formato de data inválido. Use AAAA-MM-DD. Operação cancelada.");
        }
    }

    public static void listarTarefas() {
        List<Tarefa> tarefas = tarefaDAO.listarTodas();
        System.out.println("\n--- LISTA DE TAREFAS ---");
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
        } else {
            for (Tarefa t : tarefas) {
                System.out.println("ID: " + t.getId() + " | Título: " + t.getNome() +
                        " | P.ID: " + t.getProjetoId() + " | M.ID: " + t.getMembroId() +
                        " | Prazo: " + t.getPrazo() + " | Status: " + t.getStatus());
            }
        }
        System.out.println("--------------------------");
    }

    public static void atualizarStatusTarefa() {
        System.out.println("\n--- ATUALIZAR STATUS DA TAREFA ---");
        listarTarefas();
        
        try {
            System.out.print("Digite o ID da tarefa que deseja atualizar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Escolha o novo status: 1:A_FAZER | 2:EM_ANDAMENTO | 3:CONCLUIDA");
            System.out.print("Opção de status: ");
            int statusOpcao = scanner.nextInt();
            scanner.nextLine();
            StatusTarefa novoStatus = null;
            switch (statusOpcao) {
                case 1: novoStatus = StatusTarefa.A_FAZER; break;
                case 2: novoStatus = StatusTarefa.EM_ANDAMENTO; break;
                case 3: novoStatus = StatusTarefa.CONCLUIDA; break;
                default: System.out.println("Opção de status inválida."); break;
            }
            if (novoStatus != null) {
                tarefaDAO.atualizarStatusTarefa(id, novoStatus);
                System.out.println("Status da tarefa atualizado com sucesso!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Erro: O ID e a opção de status devem ser números. Operação cancelada.");
            scanner.nextLine(); // Limpa o buffer do scanner
        }
    }

    public static void deletarTarefa() {
        listarTarefas();
        System.out.print("Digite o ID da tarefa que deseja deletar: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            tarefaDAO.deletarTarefaPorId(id);
            System.out.println("Tarefa deletada com sucesso!");
        } catch (InputMismatchException e) {
            System.out.println("Erro: O ID deve ser um número. Operação cancelada.");
            scanner.nextLine();
        }
    }
}