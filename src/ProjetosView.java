import javax.swing.*;
import java.awt.*;
import java.util.List;

class ProjetosView extends JPanel {
    private final GerenciadorJanelas gerenciador;
    private final int idUsuario;
    private final ProjetoController projetoController;
    private final ConviteProjetoController conviteProjetoController;
    private final JPanel painelProjetos;

    public ProjetosView(GerenciadorJanelas gerenciador, int idUsuario) {
        this.gerenciador = gerenciador;
        this.idUsuario = idUsuario;
        this.projetoController = new ProjetoController();
        this.conviteProjetoController = new ConviteProjetoController();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        // Painel superior com título e botão de logout
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(63, 81, 181));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel labelTitulo = new JLabel("Meus Projetos");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setForeground(Color.WHITE);
        painelSuperior.add(labelTitulo, BorderLayout.WEST);

        // Painel de botões à direita
        JPanel painelBotoesSuperiores = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoesSuperiores.setBackground(new Color(63, 81, 181));

        JButton botaoAmigos = new JButton("👥 Amigos");
        botaoAmigos.setBackground(new Color(76, 175, 80));
        botaoAmigos.setForeground(Color.WHITE);
        botaoAmigos.addActionListener(e -> abrirTelaAmigos());

        JButton botaoNotificacoes = new JButton("🔔 Notificações");
        botaoNotificacoes.setBackground(new Color(255, 152, 0));
        botaoNotificacoes.setForeground(Color.WHITE);
        botaoNotificacoes.addActionListener(e -> abrirNotificacoes());

        JButton botaoLogout = new JButton("Logout");
        botaoLogout.addActionListener(e -> gerenciador.fazerLogout());

        painelBotoesSuperiores.add(botaoAmigos);
        painelBotoesSuperiores.add(botaoNotificacoes);
        painelBotoesSuperiores.add(botaoLogout);

        painelSuperior.add(painelBotoesSuperiores, BorderLayout.EAST);

        add(painelSuperior, BorderLayout.NORTH);

        // Painel central com scroll para lista de projetos
        painelProjetos = new JPanel();
        painelProjetos.setLayout(new BoxLayout(painelProjetos, BoxLayout.Y_AXIS));
        painelProjetos.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(painelProjetos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Painel inferior com botão de adicionar projeto
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelInferior.setBackground(new Color(245, 245, 245));
        
        JButton botaoNovoProjeto = new JButton("+ Novo Projeto");
        botaoNovoProjeto.setFont(new Font("Arial", Font.BOLD, 14));
        botaoNovoProjeto.setBackground(new Color(76, 175, 80));
        botaoNovoProjeto.setForeground(Color.WHITE);
        botaoNovoProjeto.setFocusPainted(false);
        botaoNovoProjeto.addActionListener(e -> criarNovoProjeto());
        painelInferior.add(botaoNovoProjeto);

        add(painelInferior, BorderLayout.SOUTH);

        // Carrega os projetos
        carregarProjetos();
    }

    private void carregarProjetos() {
        painelProjetos.removeAll();
        
        // Carrega projetos próprios
        List<Projeto> projetosProprios = projetoController.listarProjetosPorUsuario(idUsuario);
        
        // Carrega projetos onde é colaborador
        List<Projeto> projetosColaborador = conviteProjetoController.listarProjetosColaborador(idUsuario);

        boolean temProjetos = false;

        if (projetosProprios != null && !projetosProprios.isEmpty()) {
            JLabel labelMeusProjetos = new JLabel("Meus Projetos");
            labelMeusProjetos.setFont(new Font("Arial", Font.BOLD, 16));
            labelMeusProjetos.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelProjetos.add(labelMeusProjetos);
            painelProjetos.add(Box.createVerticalStrut(10));

            for (Projeto projeto : projetosProprios) {
                painelProjetos.add(criarCardProjeto(projeto, true));
                painelProjetos.add(Box.createVerticalStrut(10));
            }
            temProjetos = true;
        }

        if (projetosColaborador != null && !projetosColaborador.isEmpty()) {
            if (temProjetos) {
                painelProjetos.add(Box.createVerticalStrut(20));
            }

            JLabel labelColaborador = new JLabel("Projetos Colaborando");
            labelColaborador.setFont(new Font("Arial", Font.BOLD, 16));
            labelColaborador.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelProjetos.add(labelColaborador);
            painelProjetos.add(Box.createVerticalStrut(10));

            for (Projeto projeto : projetosColaborador) {
                painelProjetos.add(criarCardProjeto(projeto, false));
                painelProjetos.add(Box.createVerticalStrut(10));
            }
            temProjetos = true;
        }

        if (!temProjetos) {
            JLabel labelVazio = new JLabel("Nenhum projeto cadastrado. Crie seu primeiro projeto!");
            labelVazio.setFont(new Font("Arial", Font.ITALIC, 14));
            labelVazio.setForeground(Color.GRAY);
            labelVazio.setAlignmentX(Component.CENTER_ALIGNMENT);
            painelProjetos.add(Box.createVerticalStrut(50));
            painelProjetos.add(labelVazio);
        }

        painelProjetos.revalidate();
        painelProjetos.repaint();
    }

    private JPanel criarCardProjeto(Projeto projeto, boolean ehDono) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Nome do projeto com badge se for colaborador
        JPanel painelNome = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        painelNome.setBackground(Color.WHITE);
        
        JLabel labelNome = new JLabel(projeto.getNome());
        labelNome.setFont(new Font("Arial", Font.BOLD, 16));
        painelNome.add(labelNome);

        if (!ehDono) {
            JLabel labelColaborador = new JLabel(" [Colaborador]");
            labelColaborador.setFont(new Font("Arial", Font.ITALIC, 12));
            labelColaborador.setForeground(new Color(255, 152, 0));
            painelNome.add(labelColaborador);
        }

        card.add(painelNome, BorderLayout.WEST);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        painelBotoes.setBackground(Color.WHITE);

        JButton botaoAbrir = new JButton("Abrir");
        botaoAbrir.setBackground(new Color(33, 150, 243));
        botaoAbrir.setForeground(Color.WHITE);
        botaoAbrir.setFocusPainted(false);
        botaoAbrir.addActionListener(e -> abrirProjeto(projeto));
        painelBotoes.add(botaoAbrir);

        // Apenas o dono pode editar, deletar e convidar
        if (ehDono) {
            JButton botaoConvidar = new JButton("Convidar");
            botaoConvidar.setBackground(new Color(156, 39, 176));
            botaoConvidar.setForeground(Color.WHITE);
            botaoConvidar.setFocusPainted(false);
            botaoConvidar.addActionListener(e -> convidarParaProjeto(projeto));
            painelBotoes.add(botaoConvidar);

            JButton botaoEditar = new JButton("Editar");
            botaoEditar.addActionListener(e -> editarProjeto(projeto));
            painelBotoes.add(botaoEditar);

            JButton botaoDeletar = new JButton("Deletar");
            botaoDeletar.setBackground(new Color(244, 67, 54));
            botaoDeletar.setForeground(Color.WHITE);
            botaoDeletar.setFocusPainted(false);
            botaoDeletar.addActionListener(e -> deletarProjeto(projeto));
            painelBotoes.add(botaoDeletar);
        }

        card.add(painelBotoes, BorderLayout.EAST);

        return card;
    }

    private void criarNovoProjeto() {
        String nome = JOptionPane.showInputDialog(this, "Nome do projeto:", "Novo Projeto", JOptionPane.PLAIN_MESSAGE);
        
        if (nome != null && !nome.trim().isEmpty()) {
            // Usa o Controller em vez do DAO
            boolean sucesso = projetoController.adicionarNovoProjeto(nome, idUsuario);
            
            if (sucesso) {
                carregarProjetos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao criar projeto!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarProjeto(Projeto projeto) {
        String novoNome = JOptionPane.showInputDialog(this, "Novo nome do projeto:", projeto.getNome());
        
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            // Usa o Controller em vez do DAO
            boolean sucesso = projetoController.editarProjeto(projeto.getId(), novoNome);
            
            if (sucesso) {
                carregarProjetos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao editar projeto!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deletarProjeto(Projeto projeto) {
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja deletar o projeto '" + projeto.getNome() + "'?",
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            // Usa o Controller em vez do DAO
            boolean sucesso = projetoController.deletarProjeto(projeto.getId());
            
            if (sucesso) {
                carregarProjetos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao deletar projeto!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirProjeto(Projeto projeto) {
        // Cria e adiciona a tela de tarefas
        TarefasView telaTarefas = new TarefasView(gerenciador, projeto);
        
        // Remove a tela antiga se existir
        Component[] components = gerenciador.painelPrincipal.getComponents();
        for (Component comp : components) {
            if (comp instanceof TarefasView) {
                gerenciador.painelPrincipal.remove(comp);
            }
        }
        
        gerenciador.painelPrincipal.add("tarefas", telaTarefas);
        gerenciador.mostrarTela("tarefas");
    }

    private void convidarParaProjeto(Projeto projeto) {
        AmizadeController amizadeController = new AmizadeController();
        List<Usuario> amigos = amizadeController.listarAmigos(idUsuario);

        if (amigos == null || amigos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Você não tem amigos para convidar.\nAdicione amigos primeiro!",
                "Sem Amigos",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] nomesAmigos = new String[amigos.size()];
        for (int i = 0; i < amigos.size(); i++) {
            nomesAmigos[i] = amigos.get(i).getNome() + " (" + amigos.get(i).getEmail() + ")";
        }

        String selecionado = (String) JOptionPane.showInputDialog(this,
            "Selecione um amigo para convidar:",
            "Convidar para " + projeto.getNome(),
            JOptionPane.QUESTION_MESSAGE,
            null,
            nomesAmigos,
            nomesAmigos[0]);

        if (selecionado != null) {
            int indice = java.util.Arrays.asList(nomesAmigos).indexOf(selecionado);
            Usuario amigoSelecionado = amigos.get(indice);

            boolean sucesso = conviteProjetoController.enviarConvite(
                projeto.getId(),
                idUsuario,
                amigoSelecionado.getId()
            );

            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                    "Convite enviado para " + amigoSelecionado.getNome() + "!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao enviar convite!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirTelaAmigos() {
        AmigosView telaAmigos = new AmigosView(gerenciador, idUsuario);
        
        Component[] components = gerenciador.painelPrincipal.getComponents();
        for (Component comp : components) {
            if (comp instanceof AmigosView) {
                gerenciador.painelPrincipal.remove(comp);
            }
        }
        
        gerenciador.painelPrincipal.add("amigos", telaAmigos);
        gerenciador.mostrarTela("amigos");
    }

    private void abrirNotificacoes() {
        NotificacoesView telaNotificacoes = new NotificacoesView(gerenciador, idUsuario);
        
        Component[] components = gerenciador.painelPrincipal.getComponents();
        for (Component comp : components) {
            if (comp instanceof NotificacoesView) {
                gerenciador.painelPrincipal.remove(comp);
            }
        }
        
        gerenciador.painelPrincipal.add("notificacoes", telaNotificacoes);
        gerenciador.mostrarTela("notificacoes");
    }
}