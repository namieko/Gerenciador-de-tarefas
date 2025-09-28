import javax.swing.*;
import java.awt.*;

class RegistroView extends JPanel {
    private final GerenciadorJanelas gerenciador;

    public RegistroView(GerenciadorJanelas gerenciador) {
        this.gerenciador = gerenciador;

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Título
        JLabel labelTitulo = new JLabel("Crie sua Conta");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelTitulo, gbc);

        // Nome
        JLabel labelNome = new JLabel("Nome:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(labelNome, gbc);

        JTextField campoNome = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(campoNome, gbc);

        // Email
        JLabel labelEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(labelEmail, gbc);

        JTextField campoEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(campoEmail, gbc);

        // Senha
        JLabel labelSenha = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(labelSenha, gbc);

        JPasswordField campoSenha = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(campoSenha, gbc);

        // Painel para os botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton botaoRegistrar = new JButton("Registrar");
        JButton botaoVoltar = new JButton("Voltar para Login");
        painelBotoes.add(botaoRegistrar);
        painelBotoes.add(botaoVoltar);
        painelBotoes.setBackground(getBackground());

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(painelBotoes, gbc);
        
        // --- Ações dos botões ---
        botaoRegistrar.addActionListener(e -> {
            String nome = campoNome.getText();
            String email = campoEmail.getText();
            String senha = new String(campoSenha.getPassword());
            JOptionPane.showMessageDialog(this, "Tentativa de Registro:\nNome: " + nome + "\nEmail: " + email, "Registro", JOptionPane.INFORMATION_MESSAGE);
        });

        // Pede ao gerenciador para voltar para a tela de login
        botaoVoltar.addActionListener(e -> gerenciador.mostrarTela("login"));
    }
}
