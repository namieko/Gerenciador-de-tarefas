import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("    TaskHub - Sistema de Gestão");
        System.out.println("=================================\n");
        
        // Inicializa o banco de dados e cria todas as tabelas
        System.out.println("Inicializando banco de dados...\n");
        Database.createTables();
        
        // Inicia a interface gráfica na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            new GerenciadorJanelas();
        });
    }
}