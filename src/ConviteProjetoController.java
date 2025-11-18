import java.util.List;

public class ConviteProjetoController {
    private final ConviteProjetoDAO conviteProjetoDAO;
    private final ProjetoDAO projetoDAO;
    private final UsuarioDAO usuarioDAO;

    public ConviteProjetoController() {
        this.conviteProjetoDAO = new ConviteProjetoDAO();
        this.projetoDAO = new ProjetoDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Envia um convite de projeto para um amigo.
     */
    public boolean enviarConvite(int projetoId, int remetenteId, int destinatarioId) {
        // Validações
        if (projetoId <= 0 || remetenteId <= 0 || destinatarioId <= 0) {
            System.out.println("IDs inválidos!");
            return false;
        }

        // Verifica se o projeto existe
        Projeto projeto = projetoDAO.buscarPorId(projetoId);
        if (projeto == null) {
            System.out.println("Projeto não encontrado!");
            return false;
        }

        // Verifica se o remetente é o dono do projeto
        if (projeto.getIdDono() != remetenteId) {
            System.out.println("Apenas o dono do projeto pode enviar convites!");
            return false;
        }

        // Não pode convidar a si mesmo
        if (remetenteId == destinatarioId) {
            System.out.println("Você não pode convidar a si mesmo!");
            return false;
        }

        // Verifica se já existe convite pendente
        if (conviteProjetoDAO.jaExisteConvitePendente(projetoId, destinatarioId)) {
            System.out.println("Já existe um convite pendente para este usuário!");
            return false;
        }

        conviteProjetoDAO.enviarConvite(projetoId, remetenteId, destinatarioId);
        return true;
    }

    /**
     * Aceita um convite de projeto.
     */
    public boolean aceitarConvite(int conviteId) {
        if (conviteId <= 0) {
            System.out.println("ID de convite inválido!");
            return false;
        }

        ConviteProjeto convite = conviteProjetoDAO.buscarPorId(conviteId);
        if (convite == null) {
            System.out.println("Convite não encontrado!");
            return false;
        }

        if (convite.getStatus() != StatusConvite.PENDENTE) {
            System.out.println("Este convite já foi respondido!");
            return false;
        }

        conviteProjetoDAO.aceitarConvite(conviteId);
        return true;
    }

    /**
     * Recusa um convite de projeto.
     */
    public boolean recusarConvite(int conviteId) {
        if (conviteId <= 0) {
            System.out.println("ID de convite inválido!");
            return false;
        }

        ConviteProjeto convite = conviteProjetoDAO.buscarPorId(conviteId);
        if (convite == null) {
            System.out.println("Convite não encontrado!");
            return false;
        }

        if (convite.getStatus() != StatusConvite.PENDENTE) {
            System.out.println("Este convite já foi respondido!");
            return false;
        }

        conviteProjetoDAO.recusarConvite(conviteId);
        return true;
    }

    /**
     * Lista convites pendentes de um usuário.
     */
    public List<ConviteProjeto> listarConvitesPendentes(int usuarioId) {
        if (usuarioId <= 0) {
            System.out.println("ID de usuário inválido!");
            return null;
        }
        return conviteProjetoDAO.listarConvitesPendentes(usuarioId);
    }

    /**
     * Lista todos os projetos em que o usuário é colaborador.
     */
    public List<Projeto> listarProjetosColaborador(int usuarioId) {
        if (usuarioId <= 0) {
            System.out.println("ID de usuário inválido!");
            return null;
        }
        return conviteProjetoDAO.listarProjetosColaborador(usuarioId);
    }

    /**
     * Busca informações de um projeto.
     */
    public Projeto buscarProjeto(int projetoId) {
        return projetoDAO.buscarPorId(projetoId);
    }

    /**
     * Busca informações de um usuário.
     */
    public Usuario buscarUsuario(int usuarioId) {
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
     * Verifica se um usuário tem acesso a um projeto (é dono ou colaborador).
     */
    public boolean usuarioTemAcessoAoProjeto(int usuarioId, int projetoId) {
        Projeto projeto = projetoDAO.buscarPorId(projetoId);
        if (projeto == null) {
            return false;
        }

        // É o dono?
        if (projeto.getIdDono() == usuarioId) {
            return true;
        }

        // É colaborador?
        List<Projeto> projetosColaborador = conviteProjetoDAO.listarProjetosColaborador(usuarioId);
        if (projetosColaborador != null) {
            for (Projeto p : projetosColaborador) {
                if (p.getId() == projetoId) {
                    return true;
                }
            }
        }

        return false;
    }
}