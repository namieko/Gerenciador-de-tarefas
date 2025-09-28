import java.util.List;

public class UsuarioController extends ControllerAbstract{
    private static UsuarioDAO usuarioDAO;
    private static MembroDAO membroDAO;
    private String email;
    private String senha;

    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }


      public void adicionarNovoMembro() {
        System.out.print("Digite o nome do novo membro: ");
        String nome = scanner.nextLine();
        Membro membro = new Membro();
        membro.setNome(nome);
        membroDAO.adicionarMembro(membro);
    }

    public void listarMembros() {
        List<Membro> membros = membroDAO.listarTodos();
        System.out.println("\n--- LISTA DE MEMBROS ---");
        if (membros.isEmpty()) { System.out.println("Nenhum membro cadastrado."); } 
        else { for (Membro m : membros) { System.out.println("ID: " + m.getId() + " | Nome: " + m.getNome()); } }
        System.out.println("------------------------");
    }

    public void editarMembro() {
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

    public void deletarMembro() {
        System.out.print("Digite o ID do membro que deseja deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        membroDAO.deletarMembroPorId(id);
    }
}