import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    // Movemos os DAOs e o Scanner para se tornarem variáveis estáticas da classe,
    // assim todos os nossos novos métodos podem acessá-los.
    private static ProjetoDAO projetoDAO = new ProjetoDAO();
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
                case 1: adicionarNovoProjeto(); break;
                case 2: listarProjetos(); break;
                case 3: deletarProjeto(); break;
                case 4: editarProjeto(); break;
                case 5: adicionarNovoMembro(); break;
                case 6: listarMembros(); break;
                case 7: editarMembro(); break;
                case 8: deletarMembro(); break;
                case 9: criarNovaTarefa(); break;
                case 10: listarTarefas(); break;
                case 11: atualizarStatusTarefa(); break;
                case 12: deletarTarefa(); break;
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

    // --- MÉTODOS PARA PROJETOS ---
    private static void adicionarNovoProjeto() {
        System.out.print("Digite o nome do novo projeto: ");
        String nome = scanner.nextLine();
        Projeto projeto = new Projeto();
        projeto.setNome(nome);
        projetoDAO.adicionarProjeto(projeto);
    }

    private static void listarProjetos() {
        List<Projeto> projetos = projetoDAO.listarTodos();
        System.out.println("\n--- LISTA DE PROJETOS ---");
        if (projetos.isEmpty()) { System.out.println("Nenhum projeto cadastrado."); } 
        else { for (Projeto p : projetos) { System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome()); } }
        System.out.println("-------------------------");
    }
    
    private static void deletarProjeto() {
        System.out.print("Digite o ID do projeto que deseja deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        projetoDAO.deletarProjetoPorId(id);
    }

    private static void editarProjeto() {
        System.out.print("Digite o ID do projeto que deseja editar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Digite o NOVO nome para o projeto: ");
        String novoNome = scanner.nextLine();
        Projeto projeto = new Projeto();
        projeto.setId(id);
        projeto.setNome(novoNome);
        projetoDAO.atualizarProjeto(projeto);
    }

    // --- MÉTODOS PARA MEMBROS ---
    private static void adicionarNovoMembro() {
        System.out.print("Digite o nome do novo membro: ");
        String nome = scanner.nextLine();
        Membro membro = new Membro();
        membro.setNome(nome);
        membroDAO.adicionarMembro(membro);
    }

    private static void listarMembros() {
        List<Membro> membros = membroDAO.listarTodos();
        System.out.println("\n--- LISTA DE MEMBROS ---");
        if (membros.isEmpty()) { System.out.println("Nenhum membro cadastrado."); } 
        else { for (Membro m : membros) { System.out.println("ID: " + m.getId() + " | Nome: " + m.getNome()); } }
        System.out.println("------------------------");
    }

    private static void editarMembro() {
        System.out.print("Digite o ID do membro que deseja editar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Digite o NOVO nome para o membro: ");
        String novoNome = scanner.nextLine();
        Membro membro = new Membro();
        membro.setId(id);
        membro.setNome(novoNome);
        membroDAO.atualizarMembro(membro);
    }

    private static void deletarMembro() {
        System.out.print("Digite o ID do membro que deseja deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        membroDAO.deletarMembroPorId(id);
    }

    // --- MÉTODOS PARA TAREFAS ---
    private static void criarNovaTarefa() {
        System.out.println("\n--- CRIAR NOVA TAREFA ---");
        listarProjetos();
        System.out.print("Digite o ID do projeto para esta tarefa: ");
        int projetoId = scanner.nextInt();
        scanner.nextLine();

        listarMembros();
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
    }

    private static void listarTarefas() {
        List<Tarefa> tarefas = tarefaDAO.listarTodas();
        System.out.println("\n--- LISTA DE TAREFAS ---");
        if (tarefas.isEmpty()) { System.out.println("Nenhuma tarefa cadastrada."); } 
        else {
            for (Tarefa t : tarefas) {
                System.out.println("ID: " + t.getId() + " | Título: " + t.getNome() + 
                                   " | P.ID: " + t.getProjetoId() + " | M.ID: " + t.getMembroId() + 
                                   " | Prazo: " + t.getPrazo() + " | Status: " + t.getStatus());
            }
        }
        System.out.println("--------------------------");
    }

    private static void atualizarStatusTarefa() {
        System.out.println("\n--- ATUALIZAR STATUS DA TAREFA ---");
        listarTarefas();
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
        }
    }

    private static void deletarTarefa() {
        System.out.print("Digite o ID da tarefa que deseja deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        tarefaDAO.deletarTarefaPorId(id);
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
