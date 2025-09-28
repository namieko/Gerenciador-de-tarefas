

public class Main {
    private static ProjetoController projetoController = new ProjetoController();
    private static TarefaController tarefaController = new TarefaController();
    private static UsuarioController usuarioController = new UsuarioController();
    private static TarefaDAO tarefaDAO = new TarefaDAO();
    public static void main(String[] args) {
        Database.createTables();
        GerenciadorJanelas janela = new GerenciadorJanelas();

    }
}
