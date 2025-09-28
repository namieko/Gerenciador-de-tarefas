import javax.swing.*;
import java.awt.*;
class GerenciadorJanelas {
    private final JFrame frame;
    private final JPanel painelPrincipal;
    private final CardLayout cardLayout;

    public GerenciadorJanelas() {
        frame = new JFrame("TaskHub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);

        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        // Cria as telas, passando uma referência deste gerenciador para elas.
        // Isso permite que as telas peçam para o gerenciador trocar a tela visível.
        LoginView telaLogin = new LoginView(this);
        RegistroView telaRegistro = new RegistroView(this);

        // Adiciona as telas ao painel principal com um nome único
        painelPrincipal.add("login", telaLogin);
        painelPrincipal.add("registro", telaRegistro);

        frame.add(painelPrincipal);

        // Exibe a primeira tela
        cardLayout.show(painelPrincipal, "login");

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Altera a tela visível no painel principal.
     * @param nomeTela O nome da tela a ser exibida (ex: "login", "registro").
     */
    public void mostrarTela(String nomeTela) {
        cardLayout.show(painelPrincipal, nomeTela);
    }
}