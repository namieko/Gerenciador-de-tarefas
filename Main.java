import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    // Movemos os DAOs e o Scanner para se tornarem variáveis estáticas da classe,
    // assim todos os nossos novos métodos podem acessá-los.
    private static ProjetoController ProjetoController = new ProjetoController();
    private static TarefaController tarefaController = new TarefaController();
    private static UsuarioController usuarioController = new UsuarioController();
    private static MembroDAO membroDAO = new MembroDAO();
    private static TarefaDAO tarefaDAO = new TarefaDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Database.createTables();

        while (true) {
            exibirMenu();
            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1: ProjetoController.adicionarNovoProjeto(); break;
                case 2: ProjetoController.listarProjetos(); break;
                case 3: ProjetoController.deletarProjeto(); break;
                case 4: ProjetoController.editarProjeto(); break;
                case 5: usuarioController.adicionarNovoMembro(); break;
                case 6: usuarioController.listarMembros(); break;
                case 7: usuarioController.editarMembro(); break;
                case 8: usuarioController.deletarMembro(); break;
                case 9: tarefaController.criarNovaTarefa(); break;
                case 10: tarefaController.listarTarefas(); break;
                case 11: tarefaController.atualizarStatusTarefa(); break;
                case 12: tarefaController.deletarTarefa(); break;
                case 13: filtrarTarefas(); break;
                case 14: exibirPainelDeProgresso(); break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida! Por favor, escolha uma opção do menu.");
                    break;
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n===== GERENCIADOR DE TAREFAS =====");
        System.out.println("--- Projetos ---");
        System.out.println("[1] Adicionar | [2] Listar | [3] Deletar | [4] Editar");
        System.out.println("--- Membros da Equipe ---");
        System.out.println("[5] Adicionar | [6] Listar | [7] Editar  | [8] Deletar");
        System.out.println("--- Tarefas ---");
        System.out.println("[9] Criar | [10] Listar | [11] Atualizar Status | [12] Deletar");
        System.out.println("--- Relatórios e Filtros ---");
        System.out.println("[13] Filtrar Tarefas");    
        System.out.println("[14] Ver Painel de Progresso");    
        System.out.println("---------------------------");
        System.out.println("[0] Sair do Programa");
        System.out.print("Escolha uma opção: ");
    }

    // --- MÉTODOS DE FILTRO ---
    private static void filtrarTarefas() {
        System.out.println("\n--- FILTRAR TAREFAS ---");
        System.out.println("Escolha o critério de filtro:");
        System.out.println("[1] Por Status");
        System.out.println("[2] Por Prazo (em breve)");
        System.out.print("Opção: ");
        int filtroOpcao = scanner.nextInt();
        scanner.nextLine();

        switch (filtroOpcao) {
            case 1:
                filtrarTarefasPorStatus();
                break;
            // O case para o filtro de prazo virá aqui no futuro
            default:
                System.out.println("Opção de filtro inválida.");
                break;
        }
    }

    private static void filtrarTarefasPorStatus() {
        System.out.println("\nEscolha o status para filtrar:");
        System.out.println("1: A_FAZER | 2: EM_ANDAMENTO | 3: CONCLUIDA");
        System.out.print("Opção de status: ");
        int statusOpcao = scanner.nextInt();
        scanner.nextLine();

        StatusTarefa statusFiltro = null;
        switch (statusOpcao) {
            case 1: statusFiltro = StatusTarefa.A_FAZER; break;
            case 2: statusFiltro = StatusTarefa.EM_ANDAMENTO; break;
            case 3: statusFiltro = StatusTarefa.CONCLUIDA; break;
            default: System.out.println("Opção de status inválida."); break;
        }

        if (statusFiltro != null) {
            List<Tarefa> tarefasFiltradas = tarefaDAO.filtrarPorStatus(statusFiltro);
            System.out.println("\n--- TAREFAS COM STATUS '" + statusFiltro + "' ---");
            if (tarefasFiltradas.isEmpty()) {
                System.out.println("Nenhuma tarefa encontrada com este status.");
            } else {
                for (Tarefa t : tarefasFiltradas) {
                     System.out.println("ID: " + t.getId() + " | Título: " + t.getNome() + 
                                       " | P.ID: " + t.getProjetoId() + " | M.ID: " + t.getMembroId() + 
                                       " | Prazo: " + t.getPrazo() + " | Status: " + t.getStatus());
                }
            }
            System.out.println("---------------------------------------");
        }
    }
    
    private static void exibirPainelDeProgresso() {
        System.out.println("\n--- PAINEL DE PROGRESSO ---");

        // Buscamos o total de tarefas
        int total = tarefaDAO.getContagemTotal();
        System.out.println("Total de Tarefas: " + total);

        // Se houver tarefas, buscamos a contagem por status
        if (total > 0) {
            Map<StatusTarefa, Integer> contagemPorStatus = tarefaDAO.getContagemPorStatus();

            // Usamos getOrDefault para evitar erros caso um status não tenha nenhuma tarefa.
            // Se a chave não for encontrada no Mapa, ele retorna o valor padrão (0).
            int aFazer = contagemPorStatus.getOrDefault(StatusTarefa.A_FAZER, 0);
            int emAndamento = contagemPorStatus.getOrDefault(StatusTarefa.EM_ANDAMENTO, 0);
            int concluidas = contagemPorStatus.getOrDefault(StatusTarefa.CONCLUIDA, 0);

            System.out.println("- A Fazer:        " + aFazer);
            System.out.println("- Em Andamento:   " + emAndamento);
            System.out.println("- Concluídas:     " + concluidas);
        }
        
        System.out.println("-------------------------");
    }
}
