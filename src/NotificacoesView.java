import javax.swing.*;
import java.awt.*;
import java.util.List;

class NotificacoesView extends JPanel {
    private final GerenciadorJanelas gerenciador;
    private final int idUsuario;
    private final AmizadeController amizadeController;
    private final ConviteProjetoController conviteProjetoController;
    private final JPanel painelNotificacoes;

    public NotificacoesView(GerenciadorJanelas gerenciador, int idUsuario) {
        this.gerenciador = gerenciador;
        this.idUsuario = idUsuario;
        this.amizadeController = new AmizadeController();
        this.conviteProjetoController = new ConviteProjetoController();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        // Painel superior
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(255, 152, 0));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton botaoVoltar = new JButton("← Voltar");
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setBackground(new Color(245, 124, 0));
        botaoVoltar.setFocusPainted(false);
        botaoVoltar.addActionListener(e -> gerenciador.mostrarTela("projetos"));

        JLabel labelTitulo = new JLabel("Notificações");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        painelSuperior.add(botaoVoltar, BorderLayout.WEST);
        painelSuperior.add(labelTitulo, BorderLayout.CENTER);

        add(painelSuperior, BorderLayout.NORTH);

        // Painel central
        painelNotificacoes = new JPanel();
        painelNotificacoes.setLayout(new BoxLayout(painelNotificacoes, BoxLayout.Y_AXIS));
        painelNotificacoes.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(painelNotificacoes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        carregarNotificacoes();
    }

    private void carregarNotificacoes() {
        painelNotificacoes.removeAll();

        // Carrega solicitações de amizade
        List<Amizade> solicitacoesAmizade = amizadeController.listarSolicitacoesPendentes(idUsuario);
        
        // Carrega convites de projeto
        List<ConviteProjeto> convitesProjeto = conviteProjetoController.listarConvitesPendentes(idUsuario);

        boolean temNotificacoes = false;

        if (solicitacoesAmizade != null && !solicitacoesAmizade.isEmpty()) {
            JLabel labelSecao = new JLabel("Solicitações de Amizade");
            labelSecao.setFont(new Font("Arial", Font.BOLD, 16));
            labelSecao.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelNotificacoes.add(labelSecao);
            painelNotificacoes.add(Box.createVerticalStrut(10));

            for (Amizade amizade : solicitacoesAmizade) {
                painelNotificacoes.add(criarCardSolicitacaoAmizade(amizade));
                painelNotificacoes.add(Box.createVerticalStrut(10));
            }
            temNotificacoes = true;
        }

        if (convitesProjeto != null && !convitesProjeto.isEmpty()) {
            if (temNotificacoes) {
                painelNotificacoes.add(Box.createVerticalStrut(20));
            }

            JLabel labelSecao = new JLabel("Convites para Projetos");
            labelSecao.setFont(new Font("Arial", Font.BOLD, 16));
            labelSecao.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelNotificacoes.add(labelSecao);
            painelNotificacoes.add(Box.createVerticalStrut(10));

            for (ConviteProjeto convite : convitesProjeto) {
                painelNotificacoes.add(criarCardConviteProjeto(convite));
                painelNotificacoes.add(Box.createVerticalStrut(10));
            }
            temNotificacoes = true;
        }

        if (!temNotificacoes) {
            JLabel labelVazio = new JLabel("Nenhuma notificação pendente");
            labelVazio.setFont(new Font("Arial", Font.ITALIC, 14));
            labelVazio.setForeground(Color.GRAY);
            labelVazio.setAlignmentX(Component.CENTER_ALIGNMENT);
            painelNotificacoes.add(Box.createVerticalStrut(50));
            painelNotificacoes.add(labelVazio);
        }

        painelNotificacoes.revalidate();
        painelNotificacoes.repaint();
    }

    private JPanel criarCardSolicitacaoAmizade(Amizade amizade) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(76, 175, 80)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        Usuario remetente = amizadeController.buscarUsuarioPorId(amizade.getUsuarioId());
        String textoNotificacao = remetente != null 
            ? remetente.getNome() + " quer ser seu amigo"
            : "Solicitação de amizade";

        JLabel labelTexto = new JLabel(textoNotificacao);
        labelTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        card.add(labelTexto, BorderLayout.WEST);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        painelBotoes.setBackground(Color.WHITE);

        JButton botaoAceitar = new JButton("Aceitar");
        botaoAceitar.setBackground(new Color(76, 175, 80));
        botaoAceitar.setForeground(Color.WHITE);
        botaoAceitar.setFocusPainted(false);
        botaoAceitar.addActionListener(e -> {
            amizadeController.aceitarSolicitacao(amizade.getId());
            carregarNotificacoes();
            JOptionPane.showMessageDialog(this, "Amizade aceita!");
        });

        JButton botaoRecusar = new JButton("Recusar");
        botaoRecusar.setBackground(new Color(244, 67, 54));
        botaoRecusar.setForeground(Color.WHITE);
        botaoRecusar.setFocusPainted(false);
        botaoRecusar.addActionListener(e -> {
            amizadeController.recusarSolicitacao(amizade.getId());
            carregarNotificacoes();
            JOptionPane.showMessageDialog(this, "Solicitação recusada");
        });

        painelBotoes.add(botaoAceitar);
        painelBotoes.add(botaoRecusar);
        card.add(painelBotoes, BorderLayout.EAST);

        return card;
    }

    private JPanel criarCardConviteProjeto(ConviteProjeto convite) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(156, 39, 176)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        Projeto projeto = conviteProjetoController.buscarProjeto(convite.getProjetoId());
        Usuario remetente = conviteProjetoController.buscarUsuario(convite.getRemetenteId());

        String textoNotificacao = remetente != null && projeto != null
            ? remetente.getNome() + " convidou você para o projeto: " + projeto.getNome()
            : "Convite para projeto";

        JLabel labelTexto = new JLabel(textoNotificacao);
        labelTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        card.add(labelTexto, BorderLayout.WEST);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        painelBotoes.setBackground(Color.WHITE);

        JButton botaoAceitar = new JButton("Aceitar");
        botaoAceitar.setBackground(new Color(76, 175, 80));
        botaoAceitar.setForeground(Color.WHITE);
        botaoAceitar.setFocusPainted(false);
        botaoAceitar.addActionListener(e -> {
            conviteProjetoController.aceitarConvite(convite.getId());
            carregarNotificacoes();
            JOptionPane.showMessageDialog(this, "Convite aceito! Você agora é colaborador do projeto.");
        });

        JButton botaoRecusar = new JButton("Recusar");
        botaoRecusar.setBackground(new Color(244, 67, 54));
        botaoRecusar.setForeground(Color.WHITE);
        botaoRecusar.setFocusPainted(false);
        botaoRecusar.addActionListener(e -> {
            conviteProjetoController.recusarConvite(convite.getId());
            carregarNotificacoes();
            JOptionPane.showMessageDialog(this, "Convite recusado");
        });

        painelBotoes.add(botaoAceitar);
        painelBotoes.add(botaoRecusar);
        card.add(painelBotoes, BorderLayout.EAST);

        return card;
    }
}