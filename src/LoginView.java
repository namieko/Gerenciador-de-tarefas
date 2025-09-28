import javax.swing.*;
import java.awt.*;
class LoginView extends JPanel {
    private final GerenciadorJanelas gerenciador;

    public LoginView(GerenciadorJanelas gerenciador) {
        this.gerenciador = gerenciador;

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
        painelBotoes.setBackground(getBackground()); // Mantém a cor de fundo

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(painelBotoes, gbc);


        // --- Ações dos botões ---
        botaoEntrar.addActionListener(e -> {
            String email = campoEmail.getText();
            String senha = new String(campoSenha.getPassword());
            JOptionPane.showMessageDialog(this, "Email: " + email + "\nSenha: " + senha, "Tentativa de Login", JOptionPane.INFORMATION_MESSAGE);
        });

        // Pede ao gerenciador para mostrar a tela de registro
        botaoRegistrar.addActionListener(e -> gerenciador.mostrarTela("registro"));
    }
}