import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

class TarefasView extends JPanel {
    private final GerenciadorJanelas gerenciador;
    private final Projeto projeto;
    private final TarefaDAO tarefaDAO;
    private final MembroDAO membroDAO;
    private final ComentarioDAO comentarioDAO;
    
    private JPanel painelAfazer;
    private JPanel painelEmAndamento;
    private JPanel painelConcluida;
    
    private Timer timerAtualizacao;

    public TarefasView(GerenciadorJanelas gerenciador, Projeto projeto) {
        this.gerenciador = gerenciador;
        this.projeto = projeto;
        this.tarefaDAO = new TarefaDAO();
        this.membroDAO = new MembroDAO();
        this.comentarioDAO = new ComentarioDAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        // Painel superior
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(63, 81, 181));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton botaoVoltar = new JButton("← Voltar");
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setBackground(new Color(48, 63, 159));
        botaoVoltar.setFocusPainted(false);
        botaoVoltar.addActionListener(e -> gerenciador.mostrarTela("projetos"));

        JLabel labelTitulo = new JLabel(projeto.getNome());
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton botaoNovaTarefa = new JButton("+ Nova Tarefa");
        botaoNovaTarefa.setBackground(new Color(76, 175, 80));
        botaoNovaTarefa.setForeground(Color.WHITE);
        botaoNovaTarefa.setFocusPainted(false);
        botaoNovaTarefa.addActionListener(e -> criarNovaTarefa());

        painelSuperior.add(botaoVoltar, BorderLayout.WEST);
        painelSuperior.add(labelTitulo, BorderLayout.CENTER);
        painelSuperior.add(botaoNovaTarefa, BorderLayout.EAST);

        add(painelSuperior, BorderLayout.NORTH);

        // Painel central com três colunas (Kanban)
        JPanel painelCentral = new JPanel(new GridLayout(1, 3, 10, 0));
        painelCentral.setBackground(new Color(245, 245, 245));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Coluna A Fazer
        painelAfazer = criarColunaStatus("A FAZER", new Color(158, 158, 158));
        JScrollPane scrollAfazer = new JScrollPane(painelAfazer);
        scrollAfazer.setBorder(BorderFactory.createTitledBorder("A Fazer"));

        // Coluna Em Andamento
        painelEmAndamento = criarColunaStatus("EM ANDAMENTO", new Color(255, 152, 0));
        JScrollPane scrollEmAndamento = new JScrollPane(painelEmAndamento);
        scrollEmAndamento.setBorder(BorderFactory.createTitledBorder("Em Andamento"));

        // Coluna Concluída
        painelConcluida = criarColunaStatus("CONCLUÍDA", new Color(76, 175, 80));
        JScrollPane scrollConcluida = new JScrollPane(painelConcluida);
        scrollConcluida.setBorder(BorderFactory.createTitledBorder("Concluída"));

        painelCentral.add(scrollAfazer);
        painelCentral.add(scrollEmAndamento);
        painelCentral.add(scrollConcluida);

        add(painelCentral, BorderLayout.CENTER);

        // Carrega as tarefas
        carregarTarefas();
        
        // Inicia atualização automática (Caso de Uso 8)
        iniciarAtualizacaoAutomatica();
    }

    private JPanel criarColunaStatus(String status, Color cor) {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Color.WHITE);
        return painel;
    }

    private void carregarTarefas() {
        painelAfazer.removeAll();
        painelEmAndamento.removeAll();
        painelConcluida.removeAll();

        List<Tarefa> tarefas = tarefaDAO.listarPorProjeto(projeto.getId());

        for (Tarefa tarefa : tarefas) {
            JPanel cardTarefa = criarCardTarefa(tarefa);
            
            switch (tarefa.getStatus()) {
                case A_FAZER:
                    painelAfazer.add(cardTarefa);
                    painelAfazer.add(Box.createVerticalStrut(10));
                    break;
                case EM_ANDAMENTO:
                    painelEmAndamento.add(cardTarefa);
                    painelEmAndamento.add(Box.createVerticalStrut(10));
                    break;
                case CONCLUIDA:
                    painelConcluida.add(cardTarefa);
                    painelConcluida.add(Box.createVerticalStrut(10));
                    break;
            }
        }

        painelAfazer.revalidate();
        painelAfazer.repaint();
        painelEmAndamento.revalidate();
        painelEmAndamento.repaint();
        painelConcluida.revalidate();
        painelConcluida.repaint();
    }

    private JPanel criarCardTarefa(Tarefa tarefa) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Título
        JLabel labelTitulo = new JLabel(tarefa.getNome());
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        labelTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(labelTitulo);

        card.add(Box.createVerticalStrut(5));

        // Descrição
        JTextArea areaDescricao = new JTextArea(tarefa.getDescricao());
        areaDescricao.setEditable(false);
        areaDescricao.setLineWrap(true);
        areaDescricao.setWrapStyleWord(true);
        areaDescricao.setFont(new Font("Arial", Font.PLAIN, 12));
        areaDescricao.setBackground(Color.WHITE);
        areaDescricao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        areaDescricao.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(areaDescricao);

        card.add(Box.createVerticalStrut(5));

        // Prazo
        JLabel labelPrazo = new JLabel("Prazo: " + tarefa.getPrazo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        labelPrazo.setFont(new Font("Arial", Font.ITALIC, 11));
        labelPrazo.setForeground(Color.GRAY);
        labelPrazo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(labelPrazo);

        card.add(Box.createVerticalStrut(10));

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        painelBotoes.setBackground(Color.WHITE);
        painelBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton botaoEditar = new JButton("Editar");
        botaoEditar.setFont(new Font("Arial", Font.PLAIN, 10));
        botaoEditar.addActionListener(e -> editarTarefa(tarefa));

        JButton botaoMover = new JButton("→");
        botaoMover.setFont(new Font("Arial", Font.BOLD, 10));
        botaoMover.addActionListener(e -> moverTarefa(tarefa));

        JButton botaoComentar = new JButton("💬");
        botaoComentar.setFont(new Font("Arial", Font.PLAIN, 10));
        botaoComentar.addActionListener(e -> adicionarComentario(tarefa));

        JButton botaoDeletar = new JButton("🗑");
        botaoDeletar.setFont(new Font("Arial", Font.PLAIN, 10));
        botaoDeletar.setForeground(Color.RED);
        botaoDeletar.addActionListener(e -> deletarTarefa(tarefa));

        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoMover);
        painelBotoes.add(botaoComentar);
        painelBotoes.add(botaoDeletar);

        card.add(painelBotoes);

        return card;
    }

    private void criarNovaTarefa() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nova Tarefa", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        JTextField campoTitulo = new JTextField(20);
        dialog.add(campoTitulo, gbc);

        // Descrição
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        JTextArea campoDescricao = new JTextArea(3, 20);
        campoDescricao.setLineWrap(true);
        dialog.add(new JScrollPane(campoDescricao), gbc);

        // Prazo
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Prazo (dd/mm/aaaa):"), gbc);
        gbc.gridx = 1;
        JTextField campoPrazo = new JTextField(10);
        dialog.add(campoPrazo, gbc);

        // Membro
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Responsável:"), gbc);
        gbc.gridx = 1;
        List<Membro> membros = membroDAO.listarTodos();
        JComboBox<String> comboMembros = new JComboBox<>();
        comboMembros.addItem("Sem responsável");
        for (Membro m : membros) {
            comboMembros.addItem(m.getId() + " - " + m.getNome());
        }
        dialog.add(comboMembros, gbc);

        // Botões
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCancelar = new JButton("Cancelar");

        botaoSalvar.addActionListener(e -> {
            try {
                String titulo = campoTitulo.getText().trim();
                String descricao = campoDescricao.getText().trim();
                String prazoStr = campoPrazo.getText().trim();

                if (titulo.isEmpty() || prazoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Título e prazo são obrigatórios!");
                    return;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate prazo = LocalDate.parse(prazoStr, formatter);

                Tarefa tarefa = new Tarefa();
                tarefa.setNome(titulo);
                tarefa.setDescricao(descricao);
                tarefa.setPrazo(prazo);
                tarefa.setStatus(StatusTarefa.A_FAZER);
                tarefa.setProjetoId(projeto.getId());

                int selectedIndex = comboMembros.getSelectedIndex();
                if (selectedIndex > 0) {
                    String item = (String) comboMembros.getSelectedItem();
                    int membroId = Integer.parseInt(item.split(" - ")[0]);
                    tarefa.setMembroId(membroId);
                }

                tarefaDAO.adicionarTarefa(tarefa);
                carregarTarefas();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao criar tarefa: " + ex.getMessage());
            }
        });

        botaoCancelar.addActionListener(e -> dialog.dispose());

        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCancelar);
        dialog.add(painelBotoes, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editarTarefa(Tarefa tarefa) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Tarefa", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos preenchidos com dados atuais
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        JTextField campoTitulo = new JTextField(tarefa.getNome(), 20);
        dialog.add(campoTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        JTextArea campoDescricao = new JTextArea(tarefa.getDescricao(), 3, 20);
        campoDescricao.setLineWrap(true);
        dialog.add(new JScrollPane(campoDescricao), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Prazo:"), gbc);
        gbc.gridx = 1;
        JTextField campoPrazo = new JTextField(tarefa.getPrazo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        dialog.add(campoPrazo, gbc);

        // Botões
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCancelar = new JButton("Cancelar");

        botaoSalvar.addActionListener(e -> {
            try {
                tarefa.setNome(campoTitulo.getText().trim());
                tarefa.setDescricao(campoDescricao.getText().trim());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                tarefa.setPrazo(LocalDate.parse(campoPrazo.getText().trim(), formatter));

                tarefaDAO.atualizarTarefa(tarefa);
                carregarTarefas();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao editar tarefa: " + ex.getMessage());
            }
        });

        botaoCancelar.addActionListener(e -> dialog.dispose());

        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCancelar);
        dialog.add(painelBotoes, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void moverTarefa(Tarefa tarefa) {
        StatusTarefa novoStatus = null;
        
        switch (tarefa.getStatus()) {
            case A_FAZER:
                novoStatus = StatusTarefa.EM_ANDAMENTO;
                break;
            case EM_ANDAMENTO:
                novoStatus = StatusTarefa.CONCLUIDA;
                break;
            case CONCLUIDA:
                JOptionPane.showMessageDialog(this, "Tarefa já está concluída!");
                return;
        }

        tarefaDAO.atualizarStatusTarefa(tarefa.getId(), novoStatus);
        carregarTarefas();
    }

    private void deletarTarefa(Tarefa tarefa) {
        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja deletar esta tarefa?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            tarefaDAO.deletarTarefaPorId(tarefa.getId());
            carregarTarefas();
        }
    }

    private void adicionarComentario(Tarefa tarefa) {
        String comentarioTexto = JOptionPane.showInputDialog(this, 
            "Digite seu comentário:", 
            "Adicionar Comentário", 
            JOptionPane.PLAIN_MESSAGE);

        if (comentarioTexto != null && !comentarioTexto.trim().isEmpty()) {
            Comentario comentario = new Comentario();
            comentario.setTarefaId(tarefa.getId());
            comentario.setTexto(comentarioTexto.trim());
            comentario.setAutor(gerenciador.getIdUsuarioLogado());
            comentario.setDataHora(LocalDate.now());

            comentarioDAO.adicionarComentario(comentario);
            
            // Mostra os comentários
            mostrarComentarios(tarefa);
        }
    }

    private void mostrarComentarios(Tarefa tarefa) {
        List<Comentario> comentarios = comentarioDAO.listarPorTarefa(tarefa.getId());
        
        StringBuilder sb = new StringBuilder();
        sb.append("Comentários da tarefa: ").append(tarefa.getNome()).append("\n\n");
        
        if (comentarios.isEmpty()) {
            sb.append("Nenhum comentário ainda.");
        } else {
            for (Comentario c : comentarios) {
                sb.append(c.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                  .append(" - Usuário ")
                  .append(c.getAutor())
                  .append(":\n")
                  .append(c.getTexto())
                  .append("\n\n");
            }
        }

        JTextArea areaComentarios = new JTextArea(sb.toString());
        areaComentarios.setEditable(false);
        areaComentarios.setLineWrap(true);
        areaComentarios.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(areaComentarios);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Comentários", JOptionPane.PLAIN_MESSAGE);
    }

    // Caso de Uso 8: Atualização em tempo real
    private void iniciarAtualizacaoAutomatica() {
        // Atualiza a cada 5 segundos
        timerAtualizacao = new Timer(5000, e -> carregarTarefas());
        timerAtualizacao.start();
    }

    // Para o timer quando a tela não está mais visível
    public void pararAtualizacao() {
        if (timerAtualizacao != null) {
            timerAtualizacao.stop();
        }
    }
}