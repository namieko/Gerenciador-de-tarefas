import javax.swing.*;
import java.awt.*;

class GerenciadorJanelas {
    private final JFrame frame;
    public final JPanel painelPrincipal; // Tornado público para acesso de ProjetosView
    private final CardLayout cardLayout;
    private int idUsuarioLogado = -1; // Armazena o ID do usuário logado

    public GerenciadorJanelas() {
        frame = new JFrame("TaskHub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);

        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        // Cria as telas iniciais
        LoginView telaLogin = new LoginView(this);
        RegistroView telaRegistro = new RegistroView(this);

        // Adiciona as telas ao painel principal
        painelPrincipal.add("login", telaLogin);
        painelPrincipal.add("registro", telaRegistro);

        frame.add(painelPrincipal);
        cardLayout.show(painelPrincipal, "login");

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Altera a tela visível no painel principal.
     */
    public void mostrarTela(String nomeTela) {
        cardLayout.show(painelPrincipal, nomeTela);
    }

    /**
     * Define o ID do usuário logado e cria a tela de projetos.
     */
    public void fazerLogin(int idUsuario) {
        this.idUsuarioLogado = idUsuario;
        
        // Remove tela de projetos antiga se existir
        Component[] components = painelPrincipal.getComponents();
        for (Component comp : components) {
            if (comp instanceof ProjetosView) {
                painelPrincipal.remove(comp);
            }
        }
        
        // Cria e adiciona a tela de projetos
        ProjetosView telaProjetos = new ProjetosView(this, idUsuario);
        painelPrincipal.add("projetos", telaProjetos);
        
        // Exibe a tela de projetos
        mostrarTela("projetos");
    }

    /**
     * Faz logout e volta para a tela de login.
     */
    public void fazerLogout() {
        // Remove telas de projetos e tarefas
        Component[] components = painelPrincipal.getComponents();
        for (Component comp : components) {
            if (comp instanceof ProjetosView || comp instanceof TarefasView) {
                // Para timer de atualização se for TarefasView
                if (comp instanceof TarefasView) {
                    ((TarefasView) comp).pararAtualizacao();
                }
                painelPrincipal.remove(comp);
            }
        }
        
        this.idUsuarioLogado = -1;
        mostrarTela("login");
    }

    public int getIdUsuarioLogado() {
        return idUsuarioLogado;
    }
}