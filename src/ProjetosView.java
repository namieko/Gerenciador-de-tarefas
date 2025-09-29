import javax.swing.*;
import java.awt.*;
import java.util.List;

class ProjetosView extends JPanel {
    private final GerenciadorJanelas gerenciador;
    private final int idUsuario;
    private final ProjetoDAO projetoDAO;
    private final JPanel painelProjetos;

    public ProjetosView(GerenciadorJanelas gerenciador, int idUsuario) {
        this.gerenciador = gerenciador;
        this.idUsuario = idUsuario;
        this.projetoDAO = new ProjetoDAO();

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

        JButton botaoLogout = new JButton("Logout");
        botaoLogout.addActionListener(e -> gerenciador.fazerLogout());
        painelSuperior.add(botaoLogout, BorderLayout.EAST);

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
        List<Projeto> projetos = projetoDAO.listarTodosPorUsuario(idUsuario);

        if (projetos.isEmpty()) {
            JLabel labelVazio = new JLabel("Nenhum projeto cadastrado. Crie seu primeiro projeto!");
            labelVazio.setFont(new Font("Arial", Font.ITALIC, 14));
            labelVazio.setForeground(Color.GRAY);
            labelVazio.setAlignmentX(Component.CENTER_ALIGNMENT);
            painelProjetos.add(Box.createVerticalStrut(50));
            painelProjetos.add(labelVazio);
        } else {
            for (Projeto projeto : projetos) {
                painelProjetos.add(criarCardProjeto(projeto));
                painelProjetos.add(Box.createVerticalStrut(10));
            }
        }

        painelProjetos.revalidate();
        painelProjetos.repaint();
    }

    private JPanel criarCardProjeto(Projeto projeto) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Nome do projeto
        JLabel labelNome = new JLabel(projeto.getNome());
        labelNome.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(labelNome, BorderLayout.WEST);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        painelBotoes.setBackground(Color.WHITE);

        JButton botaoAbrir = new JButton("Abrir");
        botaoAbrir.setBackground(new Color(33, 150, 243));
        botaoAbrir.setForeground(Color.WHITE);
        botaoAbrir.setFocusPainted(false);
        botaoAbrir.addActionListener(e -> abrirProjeto(projeto));

        JButton botaoEditar = new JButton("Editar");
        botaoEditar.addActionListener(e -> editarProjeto(projeto));

        JButton botaoDeletar = new JButton("Deletar");
        botaoDeletar.setBackground(new Color(244, 67, 54));
        botaoDeletar.setForeground(Color.WHITE);
        botaoDeletar.setFocusPainted(false);
        botaoDeletar.addActionListener(e -> deletarProjeto(projeto));

        painelBotoes.add(botaoAbrir);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoDeletar);

        card.add(painelBotoes, BorderLayout.EAST);

        return card;
    }

    private void criarNovoProjeto() {
        String nome = JOptionPane.showInputDialog(this, "Nome do projeto:", "Novo Projeto", JOptionPane.PLAIN_MESSAGE);
        
        if (nome != null && !nome.trim().isEmpty()) {
            Projeto projeto = new Projeto();
            projeto.setNome(nome.trim());
            projeto.setDono(idUsuario);
            projetoDAO.adicionarProjeto(projeto);
            carregarProjetos();
        }
    }

    private void editarProjeto(Projeto projeto) {
        String novoNome = JOptionPane.showInputDialog(this, "Novo nome do projeto:", projeto.getNome());
        
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            projeto.setNome(novoNome.trim());
            projetoDAO.atualizarProjeto(projeto);
            carregarProjetos();
        }
    }

    private void deletarProjeto(Projeto projeto) {
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja deletar o projeto '" + projeto.getNome() + "'?",
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            projetoDAO.deletarProjetoPorId(projeto.getId());
            carregarProjetos();
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
}