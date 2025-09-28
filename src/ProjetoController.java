import java.util.Scanner;
import java.util.List;
public class ProjetoController extends ControllerAbstract {
    //Atributos
    private static ProjetoDAO projetoDAO;
    
    ProjetoController(){
        scanner = new Scanner(System.in);
        projetoDAO = new ProjetoDAO();
    }

    public void adicionarNovoProjeto() {
        System.out.print("Digite o nome do novo projeto: ");
        String nome = scanner.nextLine();
        Projeto projeto = new Projeto();
        projeto.setNome(nome);
        projetoDAO.adicionarProjeto(projeto);
    }

    public void listarProjetos() {
        List<Projeto> projetos = projetoDAO.listarTodos();
        System.out.println("\n--- LISTA DE PROJETOS ---");
        if (projetos.isEmpty()) { System.out.println("Nenhum projeto cadastrado."); } 
        else { for (Projeto p : projetos) { System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome()); } }
        System.out.println("-------------------------");
    }
    
    public void deletarProjeto() {
        System.out.print("Digite o ID do projeto que deseja deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        projetoDAO.deletarProjetoPorId(id);
    }

    public void editarProjeto() {
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
}   