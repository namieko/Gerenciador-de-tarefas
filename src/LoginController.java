class LoginController {
    private final UsuarioDAO usuarioDAO;

    // O Controller depende de um DAO para buscar os dados.
    public LoginController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Valida as credenciais do usuário.
     * @param userFromView Objeto Usuario vindo da tela de login.
     * @return true se o login for válido, false caso contrário.
     */
    public boolean login(Usuario userFromView) {
        // 1. Pede ao DAO para buscar o usuário pelo e-mail.
        Usuario userFromDB = usuarioDAO.buscarPorEmail(userFromView.getEmail());

        // 2. Aplica a regra de negócio: verifica se o usuário existe e se a senha confere.
        if (userFromDB != null) {
            return userFromView.getSenha().equals(userFromDB.getSenha());
        }
        
        // Retorna falso se o usuário não foi encontrado.
        return false;
    }
}
