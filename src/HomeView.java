import javax.swing.*;
import java.awt.*;

class HomeView extends JPanel {
    private final GerenciadorJanelas gerenciador;

    public HomeView(GerenciadorJanelas gerenciador) {
        this.gerenciador = gerenciador;
        setLayout(new BorderLayout());

        JLabel labelBoasVindas = new JLabel("Você está logado!", SwingConstants.CENTER);
        labelBoasVindas.setFont(new Font("Arial", Font.BOLD, 32));
        add(labelBoasVindas, BorderLayout.CENTER);

        JButton botaoLogout = new JButton("Logout");
        add(botaoLogout, BorderLayout.SOUTH);

        botaoLogout.addActionListener(e -> {
            // Lógica de logout (limpar sessão, etc.) viria aqui
            gerenciador.mostrarTela("login");
        });
    }
}