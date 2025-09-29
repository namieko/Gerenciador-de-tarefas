import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Inicializa o banco de dados e cria as tabelas
        Database.createTables();
        
        // Inicia a interface gráfica na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            new GerenciadorJanelas();
        });
    }
}