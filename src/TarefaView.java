import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

class TarefasView extends JPanel {
    private final GerenciadorJanelas gerenciador;
    private final Projeto projeto;
    private final TarefaController tarefaController;
    private final MembroController membroController;
    private final ComentarioController comentarioController;
    private final RegistroTempoController registroTempoController;
    
    private JPanel painelAfazer;
    private JPanel painelEmAndamento;
    private JPanel painelConcluida;
    
    private Timer timerAtualizacao;

    public TarefasView(GerenciadorJanelas gerenciador, Projeto projeto) {
        this.gerenciador = gerenciador;
        this.projeto = projeto;
        this.tarefaController = new TarefaController();
        this.membroController = new MembroController();
        this.comentarioController = new ComentarioController();
        this.registroTempoController = new RegistroTempoController();

        inicializarInterface();
        carregarTarefas();
        iniciarAtualizacaoAutomatica();
    }

    private void inicializarInterface() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        add(criarPainelSuperior(), BorderLayout.NORTH);
        add(criarPainelCentral(), BorderLayout.CENTER);
    }

    private JPanel criarPainelSuperior() {
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

        return painelSuperior;
    }

    private JPanel criarPainelCentral() {
        JPanel painelCentral = new JPanel(new GridLayout(1, 3, 10, 0));
        painelCentral.setBackground(new Color(245, 245, 245));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelAfazer = criarColunaStatus();
        painelEmAndamento = criarColunaStatus();
        painelConcluida = criarColunaStatus();

        painelCentral.add(criarScrollPane(painelAfazer, "A Fazer"));
        painelCentral.add(criarScrollPane(painelEmAndamento, "Em Andamento"));
        painelCentral.add(criarScrollPane(painelConcluida, "Concluída"));

        return painelCentral;
    }

    private JPanel criarColunaStatus() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Color.WHITE);
        return painel;
    }

    private JScrollPane criarScrollPane(JPanel painel, String titulo) {
        JScrollPane scroll = new JScrollPane(painel);
        scroll.setBorder(BorderFactory.createTitledBorder(titulo));
        return scroll;
    }

    private void carregarTarefas() {
        limparColunas();

        List<Tarefa> tarefas = tarefaController.listarTarefasPorProjeto(projeto.getId());

        if (tarefas != null) {
            for (Tarefa tarefa : tarefas) {
                adicionarTarefaNaColuna(tarefa);
            }
        }

        atualizarColunas();
    }

    private void limparColunas() {
        painelAfazer.removeAll();
        painelEmAndamento.removeAll();
        painelConcluida.removeAll();
    }

    private void adicionarTarefaNaColuna(Tarefa tarefa) {
        JPanel card = criarCardTarefa(tarefa);
        
        switch (tarefa.getStatus()) {
            case A_FAZER:
                painelAfazer.add(card);
                painelAfazer.add(Box.createVerticalStrut(10));
                break;
            case EM_ANDAMENTO:
                painelEmAndamento.add(card);
                painelEmAndamento.add(Box.createVerticalStrut(10));
                break;
            case CONCLUIDA:
                painelConcluida.add(card);
                painelConcluida.add(Box.createVerticalStrut(10));
                break;
        }
    }

    private void atualizarColunas() {
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

        card.add(criarTituloCard(tarefa));
        card.add(Box.createVerticalStrut(5));
        card.add(criarDescricaoCard(tarefa));
        card.add(Box.createVerticalStrut(5));
        card.add(criarPrazoCard(tarefa));
        card.add(Box.createVerticalStrut(10));
        card.add(criarBotoesCard(tarefa));

        return card;
    }

    private JLabel criarTituloCard(Tarefa tarefa) {
        JLabel label = new JLabel(tarefa.getNome());
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextArea criarDescricaoCard(Tarefa tarefa) {
        JTextArea area = new JTextArea(tarefa.getDescricao());
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Arial", Font.PLAIN, 12));
        area.setBackground(Color.WHITE);
        area.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        return area;
    }

    private JLabel criarPrazoCard(Tarefa tarefa) {
        JLabel label = new JLabel("Prazo: " + tarefa.getPrazo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        label.setFont(new Font("Arial", Font.ITALIC, 11));
        label.setForeground(Color.GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel criarBotoesCard(Tarefa tarefa) {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        painel.setBackground(Color.WHITE);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(criarBotao("Editar", null, null, e -> editarTarefa(tarefa)));
        painel.add(criarBotao("Mover →", null, null, e -> moverTarefa(tarefa)));
        painel.add(criarBotao("Tempo", new Color(156, 39, 176), Color.WHITE, e -> gerenciarTempo(tarefa)));
        painel.add(criarBotao("Comentários", new Color(33, 150, 243), Color.WHITE, e -> mostrarComentarios(tarefa)));
        painel.add(criarBotao("Deletar", new Color(244, 67, 54), Color.WHITE, e -> deletarTarefa(tarefa)));

        return painel;
    }

    private JButton criarBotao(String texto, Color bg, Color fg, java.awt.event.ActionListener action) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.PLAIN, 10));
        if (bg != null) botao.setBackground(bg);
        if (fg != null) botao.setForeground(fg);
        botao.addActionListener(action);
        return botao;
    }

    // ==================== OPERAÇÕES DE TAREFA ====================

    private void criarNovaTarefa() {
        DialogoCriarTarefa dialogo = new DialogoCriarTarefa();
        dialogo.exibir();
    }

    private void editarTarefa(Tarefa tarefa) {
        DialogoEditarTarefa dialogo = new DialogoEditarTarefa(tarefa);
        dialogo.exibir();
    }

    private void moverTarefa(Tarefa tarefa) {
        boolean sucesso = tarefaController.moverTarefaParaProximoStatus(tarefa.getId());
        if (sucesso) {
            carregarTarefas();
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível mover a tarefa!");
        }
    }

    private void deletarTarefa(Tarefa tarefa) {
        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja deletar esta tarefa?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            boolean sucesso = tarefaController.deletarTarefa(tarefa.getId());
            if (sucesso) {
                carregarTarefas();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao deletar tarefa!");
            }
        }
    }

    private void gerenciarTempo(Tarefa tarefa) {
        DialogoGerenciarTempo dialogo = new DialogoGerenciarTempo(tarefa);
        dialogo.exibir();
    }

    private void mostrarComentarios(Tarefa tarefa) {
        DialogoComentarios dialogo = new DialogoComentarios(tarefa);
        dialogo.exibir();
    }

    // ==================== ATUALIZAÇÃO AUTOMÁTICA ====================

    private void iniciarAtualizacaoAutomatica() {
        timerAtualizacao = new Timer(5000, e -> carregarTarefas());
        timerAtualizacao.start();
    }

    public void pararAtualizacao() {
        if (timerAtualizacao != null) {
            timerAtualizacao.stop();
        }
    }

    // ==================== CLASSES INTERNAS PARA DIÁLOGOS ====================

    private class DialogoCriarTarefa {
        private JDialog dialog;
        private JTextField campoTitulo;
        private JTextArea campoDescricao;
        private JTextField campoPrazo;
        private JComboBox<String> comboMembros;

        public void exibir() {
            dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(TarefasView.this), "Nova Tarefa", true);
            dialog.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            adicionarCampos(gbc);
            adicionarBotoes(gbc);

            dialog.pack();
            dialog.setLocationRelativeTo(TarefasView.this);
            dialog.setVisible(true);
        }

        private void adicionarCampos(GridBagConstraints gbc) {
            // Título
            gbc.gridx = 0; gbc.gridy = 0;
            dialog.add(new JLabel("Título:"), gbc);
            gbc.gridx = 1;
            campoTitulo = new JTextField(20);
            dialog.add(campoTitulo, gbc);

            // Descrição
            gbc.gridx = 0; gbc.gridy = 1;
            dialog.add(new JLabel("Descrição:"), gbc);
            gbc.gridx = 1;
            campoDescricao = new JTextArea(3, 20);
            campoDescricao.setLineWrap(true);
            dialog.add(new JScrollPane(campoDescricao), gbc);

            // Prazo
            gbc.gridx = 0; gbc.gridy = 2;
            dialog.add(new JLabel("Prazo (dd/mm/aaaa):"), gbc);
            gbc.gridx = 1;
            campoPrazo = new JTextField(10);
            dialog.add(campoPrazo, gbc);

            // Membro
            gbc.gridx = 0; gbc.gridy = 3;
            dialog.add(new JLabel("Responsável:"), gbc);
            gbc.gridx = 1;
            comboMembros = new JComboBox<>();
            comboMembros.addItem("Sem responsável");
            List<Membro> membros = membroController.listarMembros();
            if (membros != null) {
                for (Membro m : membros) {
                    comboMembros.addItem(m.getId() + " - " + m.getNome());
                }
            }
            dialog.add(comboMembros, gbc);
        }

        private void adicionarBotoes(GridBagConstraints gbc) {
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            JPanel painelBotoes = new JPanel(new FlowLayout());
            
            JButton botaoSalvar = new JButton("Salvar");
            botaoSalvar.addActionListener(e -> salvar());
            
            JButton botaoCancelar = new JButton("Cancelar");
            botaoCancelar.addActionListener(e -> dialog.dispose());

            painelBotoes.add(botaoSalvar);
            painelBotoes.add(botaoCancelar);
            dialog.add(painelBotoes, gbc);
        }

        private void salvar() {
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

                int membroId = 0;
                int selectedIndex = comboMembros.getSelectedIndex();
                if (selectedIndex > 0) {
                    String item = (String) comboMembros.getSelectedItem();
                    membroId = Integer.parseInt(item.split(" - ")[0]);
                }

                boolean sucesso = tarefaController.adicionarNovaTarefa(
                    titulo, descricao, prazo, projeto.getId(), membroId
                );

                if (sucesso) {
                    carregarTarefas();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao criar tarefa!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage());
            }
        }
    }

    private class DialogoEditarTarefa {
        private JDialog dialog;
        private Tarefa tarefa;
        private JTextField campoTitulo;
        private JTextArea campoDescricao;
        private JTextField campoPrazo;

        public DialogoEditarTarefa(Tarefa tarefa) {
            this.tarefa = tarefa;
        }

        public void exibir() {
            dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(TarefasView.this), "Editar Tarefa", true);
            dialog.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            adicionarCampos(gbc);
            adicionarBotoes(gbc);

            dialog.pack();
            dialog.setLocationRelativeTo(TarefasView.this);
            dialog.setVisible(true);
        }

        private void adicionarCampos(GridBagConstraints gbc) {
            gbc.gridx = 0; gbc.gridy = 0;
            dialog.add(new JLabel("Título:"), gbc);
            gbc.gridx = 1;
            campoTitulo = new JTextField(tarefa.getNome(), 20);
            dialog.add(campoTitulo, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            dialog.add(new JLabel("Descrição:"), gbc);
            gbc.gridx = 1;
            campoDescricao = new JTextArea(tarefa.getDescricao(), 3, 20);
            campoDescricao.setLineWrap(true);
            dialog.add(new JScrollPane(campoDescricao), gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            dialog.add(new JLabel("Prazo:"), gbc);
            gbc.gridx = 1;
            campoPrazo = new JTextField(tarefa.getPrazo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
            dialog.add(campoPrazo, gbc);
        }

        private void adicionarBotoes(GridBagConstraints gbc) {
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            JPanel painelBotoes = new JPanel(new FlowLayout());
            
            JButton botaoSalvar = new JButton("Salvar");
            botaoSalvar.addActionListener(e -> salvar());
            
            JButton botaoCancelar = new JButton("Cancelar");
            botaoCancelar.addActionListener(e -> dialog.dispose());

            painelBotoes.add(botaoSalvar);
            painelBotoes.add(botaoCancelar);
            dialog.add(painelBotoes, gbc);
        }

        private void salvar() {
            try {
                String novoTitulo = campoTitulo.getText().trim();
                String novaDescricao = campoDescricao.getText().trim();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate novoPrazo = LocalDate.parse(campoPrazo.getText().trim(), formatter);

                boolean sucesso = tarefaController.editarTarefa(
                    tarefa.getId(), novoTitulo, novaDescricao, novoPrazo
                );

                if (sucesso) {
                    carregarTarefas();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao editar tarefa!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage());
            }
        }
    }

    private class DialogoGerenciarTempo {
        private JDialog dialog;
        private Tarefa tarefa;
        private JTextArea areaRegistros;
        private JLabel labelTotal;
        private JTextField campoHoras;
        private JTextField campoMinutos;
        private JTextField campoData;

        public DialogoGerenciarTempo(Tarefa tarefa) {
            this.tarefa = tarefa;
        }

        public void exibir() {
            dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(TarefasView.this), 
                "Gerenciar Tempo - " + tarefa.getNome(), true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(600, 500);

            dialog.add(criarPainelTotal(), BorderLayout.NORTH);
            dialog.add(criarPainelHistorico(), BorderLayout.CENTER);
            dialog.add(criarPainelRegistro(), BorderLayout.SOUTH);

            dialog.setLocationRelativeTo(TarefasView.this);
            dialog.setVisible(true);
        }

        private JPanel criarPainelTotal() {
            JPanel painel = new JPanel(new BorderLayout());
            painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            painel.setBackground(new Color(156, 39, 176));

            double total = registroTempoController.calcularTotalHorasTarefa(tarefa.getId());
            labelTotal = new JLabel("Total: " + registroTempoController.formatarHoras(total));
            labelTotal.setFont(new Font("Arial", Font.BOLD, 20));
            labelTotal.setForeground(Color.WHITE);
            labelTotal.setHorizontalAlignment(SwingConstants.CENTER);
            painel.add(labelTotal, BorderLayout.CENTER);

            return painel;
        }

        private JScrollPane criarPainelHistorico() {
            areaRegistros = new JTextArea();
            areaRegistros.setEditable(false);
            areaRegistros.setLineWrap(true);
            areaRegistros.setWrapStyleWord(true);
            areaRegistros.setFont(new Font("Arial", Font.PLAIN, 12));

            carregarRegistros();

            JScrollPane scroll = new JScrollPane(areaRegistros);
            scroll.setBorder(BorderFactory.createTitledBorder("Histórico de Registros"));
            return scroll;
        }

        private void carregarRegistros() {
            List<RegistroTempo> registros = registroTempoController.listarRegistrosPorTarefa(tarefa.getId());
            StringBuilder sb = new StringBuilder();

            if (registros == null || registros.isEmpty()) {
                sb.append("Nenhum registro de tempo ainda.\n");
            } else {
                for (RegistroTempo r : registros) {
                    sb.append("═══════════════════════════\n");
                    sb.append("Data: ").append(r.getDataRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
                    sb.append("Tempo: ").append(registroTempoController.formatarHoras(r.getHorasGastas())).append("\n");
                    sb.append("Usuário ID: ").append(r.getUsuarioId()).append("\n\n");
                }
            }

            areaRegistros.setText(sb.toString());
            areaRegistros.setCaretPosition(0);
        }

        private JPanel criarPainelRegistro() {
            JPanel painel = new JPanel(new GridBagLayout());
            painel.setBorder(BorderFactory.createTitledBorder("Registrar Novo Tempo"));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Horas
            gbc.gridx = 0; gbc.gridy = 0;
            painel.add(new JLabel("Horas:"), gbc);
            gbc.gridx = 1;
            campoHoras = new JTextField(5);
            painel.add(campoHoras, gbc);

            // Minutos
            gbc.gridx = 2;
            painel.add(new JLabel("Minutos:"), gbc);
            gbc.gridx = 3;
            campoMinutos = new JTextField(5);
            painel.add(campoMinutos, gbc);

            // Data
            gbc.gridx = 0; gbc.gridy = 1;
            painel.add(new JLabel("Data (dd/mm/aaaa):"), gbc);
            gbc.gridx = 1; gbc.gridwidth = 3;
            campoData = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            painel.add(campoData, gbc);

            // Botão
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
            JButton botaoRegistrar = new JButton("Registrar Tempo");
            botaoRegistrar.setBackground(new Color(76, 175, 80));
            botaoRegistrar.setForeground(Color.WHITE);
            botaoRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
            botaoRegistrar.addActionListener(e -> registrarTempo());
            painel.add(botaoRegistrar, gbc);

            return painel;
        }

        private void registrarTempo() {
            try {
                String horasStr = campoHoras.getText().trim();
                String minutosStr = campoMinutos.getText().trim();
                String dataStr = campoData.getText().trim();

                if (horasStr.isEmpty() && minutosStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Informe pelo menos horas ou minutos!");
                    return;
                }

                int horas = horasStr.isEmpty() ? 0 : Integer.parseInt(horasStr);
                int minutos = minutosStr.isEmpty() ? 0 : Integer.parseInt(minutosStr);
                double horasDecimal = horas + (minutos / 60.0);

                if (horasDecimal <= 0) {
                    JOptionPane.showMessageDialog(dialog, "O tempo deve ser maior que zero!");
                    return;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate data = LocalDate.parse(dataStr, formatter);

                boolean sucesso = registroTempoController.registrarTempo(
                    tarefa.getId(), 
                    gerenciador.getIdUsuarioLogado(), 
                    horasDecimal, 
                    data
                );

                if (sucesso) {
                    carregarRegistros();
                    double novoTotal = registroTempoController.calcularTotalHorasTarefa(tarefa.getId());
                    labelTotal.setText("Total: " + registroTempoController.formatarHoras(novoTotal));

                    campoHoras.setText("");
                    campoMinutos.setText("");
                    campoData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                    JOptionPane.showMessageDialog(dialog, "Tempo registrado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao registrar tempo!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Horas e minutos devem ser números válidos!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage());
            }
        }
    }

    private class DialogoComentarios {
        private JDialog dialog;
        private Tarefa tarefa;
        private JTextArea areaComentarios;
        private JTextArea campoNovoComentario;

        public DialogoComentarios(Tarefa tarefa) {
            this.tarefa = tarefa;
        }

        public void exibir() {
            dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(TarefasView.this), 
                "Comentários - " + tarefa.getNome(), true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(500, 400);

            dialog.add(criarPainelComentarios(), BorderLayout.CENTER);
            dialog.add(criarPainelNovoComentario(), BorderLayout.SOUTH);

            dialog.setLocationRelativeTo(TarefasView.this);
            dialog.setVisible(true);
        }

        private JScrollPane criarPainelComentarios() {
            areaComentarios = new JTextArea();
            areaComentarios.setEditable(false);
            areaComentarios.setLineWrap(true);
            areaComentarios.setWrapStyleWord(true);
            areaComentarios.setFont(new Font("Arial", Font.PLAIN, 12));

            carregarComentarios();

            JScrollPane scroll = new JScrollPane(areaComentarios);
            scroll.setBorder(BorderFactory.createTitledBorder("Comentários Existentes"));
            return scroll;
        }

        private void carregarComentarios() {
            List<Comentario> comentarios = comentarioController.listarComentariosPorTarefa(tarefa.getId());
            StringBuilder sb = new StringBuilder();

            if (comentarios == null || comentarios.isEmpty()) {
                sb.append("Nenhum comentário ainda.\n");
            } else {
                for (Comentario c : comentarios) {
                    sb.append("─────────────────────────\n");
                    sb.append("Data: ").append(c.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
                    sb.append("Usuário ID: ").append(c.getAutor()).append("\n");
                    sb.append("\n").append(c.getTexto()).append("\n\n");
                }
            }

            areaComentarios.setText(sb.toString());
            areaComentarios.setCaretPosition(0);
        }

        private JPanel criarPainelNovoComentario() {
            JPanel painel = new JPanel(new BorderLayout(5, 5));
            painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            campoNovoComentario = new JTextArea(3, 30);
            campoNovoComentario.setLineWrap(true);
            campoNovoComentario.setWrapStyleWord(true);
            campoNovoComentario.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            JScrollPane scrollNovo = new JScrollPane(campoNovoComentario);

            JButton botaoAdicionar = new JButton("Adicionar Comentário");
            botaoAdicionar.setBackground(new Color(76, 175, 80));
            botaoAdicionar.setForeground(Color.WHITE);
            botaoAdicionar.addActionListener(e -> adicionarComentario());

            painel.add(new JLabel("Novo Comentário:"), BorderLayout.NORTH);
            painel.add(scrollNovo, BorderLayout.CENTER);
            painel.add(botaoAdicionar, BorderLayout.SOUTH);

            return painel;
        }

        private void adicionarComentario() {
            String texto = campoNovoComentario.getText().trim();
            if (texto.isEmpty()) {
                return;
            }

            boolean sucesso = comentarioController.adicionarComentario(
                tarefa.getId(), 
                texto, 
                gerenciador.getIdUsuarioLogado()
            );

            if (sucesso) {
                carregarComentarios();
                campoNovoComentario.setText("");
                JOptionPane.showMessageDialog(dialog, "Comentário adicionado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao adicionar comentário!");
            }
        }
    }
}