import java.util.List;

public class UsuarioController extends ControllerAbstract{
    private static UsuarioDAO usuarioDAO;
    private static MembroDAO membroDAO;

      public static void adicionarNovoMembro() {
        System.out.print("Digite o nome do novo membro: ");
        String nome = scanner.nextLine();
        Membro membro = new Membro();
        membro.setNome(nome);
        membroDAO.adicionarMembro(membro);
    }

    public static void listarMembros() {
        List<Membro> membros = membroDAO.listarTodos();
        System.out.println("\n--- LISTA DE MEMBROS ---");
        if (membros.isEmpty()) { System.out.println("Nenhum membro cadastrado."); } 
        else { for (Membro m : membros) { System.out.println("ID: " + m.getId() + " | Nome: " + m.getNome()); } }
        System.out.println("------------------------");
    }

    public static void editarMembro() {
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

    public static void deletarMembro() {
        System.out.print("Digite o ID do membro que deseja deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        membroDAO.deletarMembroPorId(id);
    }
}