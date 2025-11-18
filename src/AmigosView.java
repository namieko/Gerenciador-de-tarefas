import javax.swing.*;
import java.awt.*;
import java.util.List;

class AmigosView extends JPanel {
    private final GerenciadorJanelas gerenciador;
    private final int idUsuario;
    private final AmizadeController amizadeController;
    private final JPanel painelAmigos;

    public AmigosView(GerenciadorJanelas gerenciador, int idUsuario) {
        this.gerenciador = gerenciador;
        this.idUsuario = idUsuario;
        this.amizadeController = new AmizadeController();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        // Painel superior
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(76, 175, 80));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton botaoVoltar = new JButton("← Voltar");
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setBackground(new Color(56, 142, 60));
        botaoVoltar.setFocusPainted(false);
        botaoVoltar.addActionListener(e -> gerenciador.mostrarTela("projetos"));

        JLabel labelTitulo = new JLabel("Meus Amigos");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton botaoAdicionar = new JButton("+ Adicionar Amigo");
        botaoAdicionar.setBackground(new Color(33, 150, 243));
        botaoAdicionar.setForeground(Color.WHITE);
        botaoAdicionar.setFocusPainted(false);
        botaoAdicionar.addActionListener(e -> adicionarAmigo());

        painelSuperior.add(botaoVoltar, BorderLayout.WEST);
        painelSuperior.add(labelTitulo, BorderLayout.CENTER);
        painelSuperior.add(botaoAdicionar, BorderLayout.EAST);

        add(painelSuperior, BorderLayout.NORTH);

        // Painel central com lista de amigos
        painelAmigos = new JPanel();
        painelAmigos.setLayout(new BoxLayout(painelAmigos, BoxLayout.Y_AXIS));
        painelAmigos.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(painelAmigos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        carregarAmigos();
    }

    private void carregarAmigos() {
        painelAmigos.removeAll();
        List<Usuario> amigos = amizadeController.listarAmigos(idUsuario);

        if (amigos == null || amigos.isEmpty()) {
            JLabel labelVazio = new JLabel("Você ainda não tem amigos. Adicione alguém!");
            labelVazio.setFont(new Font("Arial", Font.ITALIC, 14));
            labelVazio.setForeground(Color.GRAY);
            labelVazio.setAlignmentX(Component.CENTER_ALIGNMENT);
            painelAmigos.add(Box.createVerticalStrut(50));
            painelAmigos.add(labelVazio);
        } else {
            for (Usuario amigo : amigos) {
                painelAmigos.add(criarCardAmigo(amigo));
                painelAmigos.add(Box.createVerticalStrut(10));
            }
        }

        painelAmigos.revalidate();
        painelAmigos.repaint();
    }

    private JPanel criarCardAmigo(Usuario amigo) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new BoxLayout(painelInfo, BoxLayout.Y_AXIS));
        painelInfo.setBackground(Color.WHITE);

        JLabel labelNome = new JLabel(amigo.getNome());
        labelNome.setFont(new Font("Arial", Font.BOLD, 14));
        painelInfo.add(labelNome);

        JLabel labelEmail = new JLabel(amigo.getEmail());
        labelEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        labelEmail.setForeground(Color.GRAY);
        painelInfo.add(labelEmail);

        card.add(painelInfo, BorderLayout.WEST);

        return card;
    }

    private void adicionarAmigo() {
        String email = JOptionPane.showInputDialog(this,
            "Digite o email do amigo que deseja adicionar:",
            "Adicionar Amigo",
            JOptionPane.PLAIN_MESSAGE);

        if (email != null && !email.trim().isEmpty()) {
            boolean sucesso = amizadeController.enviarSolicitacao(idUsuario, email);

            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                    "Solicitação de amizade enviada!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Não foi possível enviar a solicitação.\nVerifique o email informado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}