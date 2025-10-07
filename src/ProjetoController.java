import java.util.List;

public class ProjetoController {
    private final ProjetoDAO projetoDAO;
    
    public ProjetoController() {
        this.projetoDAO = new ProjetoDAO();
    }

    public ProjetoController(ProjetoDAO projetoDAO) {
        this.projetoDAO = projetoDAO;
    }

    /**
     * Adiciona um novo projeto com validações.
     * @return true se o projeto foi adicionado com sucesso, false caso contrário
     */
    public boolean adicionarNovoProjeto(String nome, int idDono) {
        // Validações
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome do projeto não pode ser vazio!");
            return false;
        }

        if (idDono <= 0) {
            System.out.println("ID do dono inválido!");
            return false;
        }

        Projeto projeto = new Projeto();
        projeto.setNome(nome.trim());
        projeto.setDono(idDono);
        
        projetoDAO.adicionarProjeto(projeto);
        return true;
    }

    /**
     * Lista todos os projetos cadastrados.
     */
    public List<Projeto> listarProjetos() {
        return projetoDAO.listarTodos();
    }

    /**
     * Lista projetos de um usuário específico.
     */
    public List<Projeto> listarProjetosPorUsuario(int idUsuario) {
        if (idUsuario <= 0) {
            System.out.println("ID de usuário inválido!");
            return null;
        }
        return projetoDAO.listarTodosPorUsuario(idUsuario);
    }
    
    /**
     * Deleta um projeto por ID.
     * @return true se deletado com sucesso, false caso contrário
     */
    public boolean deletarProjeto(int id) {
        if (id <= 0) {
            System.out.println("ID inválido!");
            return false;
        }

        // Verifica se o projeto existe
        Projeto projeto = projetoDAO.buscarPorId(id);
        if (projeto == null) {
            System.out.println("Projeto não encontrado!");
            return false;
        }

        projetoDAO.deletarProjetoPorId(id);
        return true;
    }

    /**
     * Edita o nome de um projeto existente.
     * @return true se editado com sucesso, false caso contrário
     */
    public boolean editarProjeto(int id, String novoNome) {
        // Validações
        if (id <= 0) {
            System.out.println("ID inválido!");
            return false;
        }

        if (novoNome == null || novoNome.trim().isEmpty()) {
            System.out.println("Novo nome não pode ser vazio!");
            return false;
        }

        // Verifica se o projeto existe
        Projeto projeto = projetoDAO.buscarPorId(id);
        if (projeto == null) {
            System.out.println("Projeto não encontrado!");
            return false;
        }

        projeto.setNome(novoNome.trim());
        projetoDAO.atualizarProjeto(projeto);
        return true;
    }

    /**
     * Busca um projeto por ID.
     */
    public Projeto buscarProjetoPorId(int id) {
        if (id <= 0) {
            System.out.println("ID inválido!");
            return null;
        }
        return projetoDAO.buscarPorId(id);
    }

    /**
     * Verifica se um usuário é dono de um projeto.
     */
    public boolean usuarioEhDonoDoProjeto(int idProjeto, int idUsuario) {
        Projeto projeto = projetoDAO.buscarPorId(idProjeto);
        if (projeto == null) {
            return false;
        }
        return projeto.getIdDono() == idUsuario;
    }
}