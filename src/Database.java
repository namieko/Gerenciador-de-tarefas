import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:gerenciador_tarefas.db";

    /**
     * Estabelece conexão com o banco de dados SQLite.
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conn;
    }
    
    /**
     * Cria todas as tabelas necessárias do sistema.
     * Este método é chamado na inicialização da aplicação.
     */
    public static void createTables() {
        criarTabelaUsuarios();
        criarTabelaProjetos();
        criarTabelaMembros();
        criarTabelaTarefas();
        criarTabelaComentarios();
        criarTabelaRegistrosTempo();
        criarTabelaAmizades();
        criarTabelaConvitesProjeto();
        
        System.out.println("\n=================================");
        System.out.println("Todas as tabelas foram criadas/verificadas com sucesso!");
        System.out.println("=================================\n");
    }

    /**
     * Cria a tabela de usuários.
     */
    private static void criarTabelaUsuarios() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "email TEXT NOT NULL UNIQUE,"
                + "senha TEXT NOT NULL"
                + ");";

        executarSQL(sql, "usuarios");
    }

    /**
     * Cria a tabela de projetos.
     */
    private static void criarTabelaProjetos() {
        String sql = "CREATE TABLE IF NOT EXISTS projetos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "id_dono INTEGER NOT NULL,"
                + "FOREIGN KEY (id_dono) REFERENCES usuarios (id)"
                + ");";

        executarSQL(sql, "projetos");
    }

    /**
     * Cria a tabela de membros.
     */
    private static void criarTabelaMembros() {
        String sql = "CREATE TABLE IF NOT EXISTS membros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL"
                + ");";

        executarSQL(sql, "membros");
    }

    /**
     * Cria a tabela de tarefas.
     */
    private static void criarTabelaTarefas() {
        String sql = "CREATE TABLE IF NOT EXISTS tarefas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "titulo TEXT NOT NULL,"
                + "descricao TEXT,"
                + "prazo DATE NOT NULL,"
                + "status TEXT NOT NULL,"
                + "membro_id INTEGER,"
                + "projeto_id INTEGER NOT NULL,"
                + "FOREIGN KEY (membro_id) REFERENCES membros (id),"
                + "FOREIGN KEY (projeto_id) REFERENCES projetos (id) ON DELETE CASCADE"
                + ");";

        executarSQL(sql, "tarefas");
    }

    /**
     * Cria a tabela de comentários.
     * Caso de Uso 10: Adicionar comentário em uma tarefa
     */
    private static void criarTabelaComentarios() {
        String sql = "CREATE TABLE IF NOT EXISTS comentarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tarefa_id INTEGER NOT NULL,"
                + "texto TEXT NOT NULL,"
                + "autor_id INTEGER NOT NULL,"
                + "data_hora DATE NOT NULL,"
                + "FOREIGN KEY (tarefa_id) REFERENCES tarefas (id) ON DELETE CASCADE,"
                + "FOREIGN KEY (autor_id) REFERENCES usuarios (id)"
                + ");";

        executarSQL(sql, "comentarios");
    }

    /**
     * Cria a tabela de registros de tempo.
     * RF010: O sistema deve permitir que o usuário registre o tempo que gastou para realizar uma tarefa
     */
    private static void criarTabelaRegistrosTempo() {
        String sql = "CREATE TABLE IF NOT EXISTS registros_tempo ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "horas_gastas REAL NOT NULL,"
                + "data_registro DATE NOT NULL,"
                + "tarefa_id INTEGER NOT NULL,"
                + "usuario_id INTEGER NOT NULL,"
                + "FOREIGN KEY (tarefa_id) REFERENCES tarefas (id) ON DELETE CASCADE,"
                + "FOREIGN KEY (usuario_id) REFERENCES usuarios (id)"
                + ");";

        executarSQL(sql, "registros_tempo");
    }

    /**
     * Método auxiliar para executar SQL e exibir mensagem de sucesso.
     */
    private static void executarSQL(String sql, String nomeTabela) {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✓ Tabela '" + nomeTabela + "' criada/verificada");
        } catch (SQLException e) {
            System.out.println("✗ Erro ao criar tabela '" + nomeTabela + "': " + e.getMessage());
        }
    }
}