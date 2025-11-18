import java.util.List;

public class AmizadeController {
    private final AmizadeDAO amizadeDAO;
    private final UsuarioDAO usuarioDAO;

    public AmizadeController() {
        this.amizadeDAO = new AmizadeDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Envia uma solicitação de amizade para outro usuário.
     */
    public boolean enviarSolicitacao(int usuarioId, String emailAmigo) {
        // Validações
        if (usuarioId <= 0) {
            System.out.println("ID de usuário inválido!");
            return false;
        }

        if (emailAmigo == null || emailAmigo.trim().isEmpty()) {
            System.out.println("Email do amigo não pode ser vazio!");
            return false;
        }

        // Busca o usuário pelo email
        Usuario amigo = usuarioDAO.buscarPorEmail(emailAmigo.trim());
        if (amigo == null) {
            System.out.println("Usuário não encontrado!");
            return false;
        }

        // Não pode adicionar a si mesmo
        if (amigo.getId() == usuarioId) {
            System.out.println("Você não pode adicionar a si mesmo como amigo!");
            return false;
        }

        // Verifica se já existe amizade
        if (amizadeDAO.jaExisteAmizade(usuarioId, amigo.getId())) {
            System.out.println("Já existe uma solicitação ou amizade com este usuário!");
            return false;
        }

        amizadeDAO.enviarSolicitacao(usuarioId, amigo.getId());
        return true;
    }

    /**
     * Aceita uma solicitação de amizade.
     */
    public boolean aceitarSolicitacao(int amizadeId) {
        if (amizadeId <= 0) {
            System.out.println("ID de amizade inválido!");
            return false;
        }

        amizadeDAO.aceitarSolicitacao(amizadeId);
        return true;
    }

    /**
     * Recusa uma solicitação de amizade.
     */
    public boolean recusarSolicitacao(int amizadeId) {
        if (amizadeId <= 0) {
            System.out.println("ID de amizade inválido!");
            return false;
        }

        amizadeDAO.recusarSolicitacao(amizadeId);
        return true;
    }

    /**
     * Lista todos os amigos de um usuário.
     */
    public List<Usuario> listarAmigos(int usuarioId) {
        if (usuarioId <= 0) {
            System.out.println("ID de usuário inválido!");
            return null;
        }
        return amizadeDAO.listarAmigos(usuarioId);
    }

    /**
     * Lista solicitações de amizade pendentes.
     */
    public List<Amizade> listarSolicitacoesPendentes(int usuarioId) {
        if (usuarioId <= 0) {
            System.out.println("ID de usuário inválido!");
            return null;
        }
        return amizadeDAO.listarSolicitacoesPendentes(usuarioId);
    }

    /**
     * Busca informações de um usuário que enviou solicitação.
     */
    public Usuario buscarUsuarioPorId(int usuarioId) {
        List<Usuario> todosUsuarios = usuarioDAO.listarTodos();
        if (todosUsuarios != null) {
            for (Usuario u : todosUsuarios) {
                if (u.getId() == usuarioId) {
                    return u;
                }
            }
        }
        return null;
    }

    /**
     * Remove uma amizade.
     */
    public boolean removerAmizade(int amizadeId) {
        if (amizadeId <= 0) {
            System.out.println("ID de amizade inválido!");
            return false;
        }
        amizadeDAO.removerAmizade(amizadeId);
        return true;
    }
}