import javax.swing.*;
import java.awt.*;

class LoginView extends JPanel {
    private final GerenciadorJanelas gerenciador;
    private final LoginController loginController;

    public LoginView(GerenciadorJanelas gerenciador) {
        this.gerenciador = gerenciador;
        this.loginController = new LoginController(new UsuarioDAO());

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Título
        JLabel labelTitulo = new JLabel("Bem-vindo ao TaskHub");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelTitulo, gbc);

        // Email
        JLabel labelEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(labelEmail, gbc);

        JTextField campoEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(campoEmail, gbc);

        // Senha
        JLabel labelSenha = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(labelSenha, gbc);

        JPasswordField campoSenha = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(campoSenha, gbc);
        
        // Painel para os botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton botaoEntrar = new JButton("Entrar");
        JButton botaoRegistrar = new JButton("Registrar-se");
        painelBotoes.add(botaoEntrar);
        painelBotoes.add(botaoRegistrar);
        painelBotoes.setBackground(getBackground());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(painelBotoes, gbc);

        // --- Ações dos botões ---
        botaoEntrar.addActionListener(e -> {
            String email = campoEmail.getText();
            String senha = new String(campoSenha.getPassword());

            // Validação básica
            if (email.trim().isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, preencha todos os campos!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cria objeto usuário com os dados do formulário
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setSenha(senha);

            // Tenta fazer login
            if (loginController.login(usuario)) {
                // Busca o usuário completo do banco
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                Usuario usuarioLogado = usuarioDAO.buscarPorEmail(email);
                
                JOptionPane.showMessageDialog(this, 
                    "Login realizado com sucesso!\nBem-vindo, " + usuarioLogado.getNome() + "!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Redireciona para a tela de projetos
                gerenciador.fazerLogin(usuarioLogado.getId());
                
                // Limpa os campos
                campoEmail.setText("");
                campoSenha.setText("");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Email ou senha incorretos!", 
                    "Erro de Login", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Pede ao gerenciador para mostrar a tela de registro
        botaoRegistrar.addActionListener(e -> gerenciador.mostrarTela("registro"));
    }
}