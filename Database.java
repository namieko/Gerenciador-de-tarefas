import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    // Define o nome do arquivo do banco de dados que será criado na pasta do projeto
    private static final String URL = "jdbc:sqlite:gerenciador_tarefas.db";

    /**
     * Tenta se conectar ao banco de dados.
     * @return um objeto de Conexão.
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            // A "mágica" do driver JDBC acontece aqui
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conn;
    }

    /**
     * Cria as tabelas do banco de dados se elas ainda não existirem.
     */
    public static void createTables() {
        // SQL para criar a tabela de projetos
        String sqlProjetos = "CREATE TABLE IF NOT EXISTS projetos ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL"
                + ");";

        // SQL para criar a tabela de membros
        String sqlMembros = "CREATE TABLE IF NOT EXISTS membros ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL "
                + ");";

        // SQL para criar a tabela de tarefas
        String sqlTarefas = "CREATE TABLE IF NOT EXISTS tarefas ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " titulo TEXT NOT NULL,"
                + " descricao TEXT,"
                + " prazo DATE NOT NULL,"
                + " status TEXT NOT NULL,"
                + " membro_id INTEGER,"
                + " projeto_id INTEGER NOT NULL,"
                + " FOREIGN KEY (membro_id) REFERENCES membros (id),"
                + " FOREIGN KEY (projeto_id) REFERENCES projetos (id)"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // Executa os três comandos SQL para criar as tabelas
            stmt.execute(sqlProjetos);
            stmt.execute(sqlMembros);
            stmt.execute(sqlTarefas);
        } catch (SQLException e) {
            System.out.println("Erro ao criar as tabelas: " + e.getMessage());
        }
        
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " email TEXT NOT NULL UNIQUE," // E-mail será o nosso login e não pode repetir
                + " senha TEXT NOT NULL"
                + ");";
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // ... (stmt.execute para as outras tabelas)
            stmt.execute(sqlUsuarios); // Adicione esta linha
        } catch (SQLException e) {
            System.out.println("Erro ao criar as tabelas: " + e.getMessage());
        }
    }
}