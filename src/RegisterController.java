public class RegisterController {
    private final UsuarioDAO usuarioDAO;

    public RegisterController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Registra um novo usuário no sistema.
     * @param nome Nome do usuário
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return true se o registro foi bem-sucedido, false caso contrário
     */
    public boolean registrar(String nome, String email, String senha) {
        // Validações básicas
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome não pode ser vazio!");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email não pode ser vazio!");
            return false;
        }

        if (!email.contains("@")) {
            System.out.println("Email inválido!");
            return false;
        }

        if (senha == null || senha.length() < 4) {
            System.out.println("Senha deve ter no mínimo 4 caracteres!");
            return false;
        }

        // Verifica se o email já está cadastrado
        Usuario usuarioExistente = usuarioDAO.buscarPorEmail(email);
        if (usuarioExistente != null) {
            System.out.println("Este email já está cadastrado!");
            return false;
        }

        // Cria o novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome.trim());
        novoUsuario.setEmail(email.trim());
        novoUsuario.setSenha(senha);

        // Adiciona no banco de dados
        usuarioDAO.adicionarUsuario(novoUsuario);
        return true;
    }

    /**
     * Valida se um email é válido.
     */
    public boolean validarEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Valida se uma senha é forte o suficiente.
     */
    public boolean validarSenha(String senha) {
        return senha != null && senha.length() >= 4;
    }
}