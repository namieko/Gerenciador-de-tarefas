import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:gerenciador_tarefas.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conn;
    }
    
    public static void createTables() {
        // SQL para criar a tabela de usuários
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " email TEXT NOT NULL UNIQUE,"
                + " senha TEXT NOT NULL"
                + ");";

        // SQL para criar a tabela de projetos
        String sqlProjetos = "CREATE TABLE IF NOT EXISTS projetos ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " id_dono INTEGER NOT NULL,"
                + " FOREIGN KEY (id_dono) REFERENCES usuarios (id)"
                + ");";

        // SQL para criar a tabela de membros
        String sqlMembros = "CREATE TABLE IF NOT EXISTS membros ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL"
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
                + " FOREIGN KEY (projeto_id) REFERENCES projetos (id) ON DELETE CASCADE"
                + ");";

        // SQL para criar a tabela de comentários (Caso de Uso 10)
        String sqlComentarios = "CREATE TABLE IF NOT EXISTS comentarios ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " tarefa_id INTEGER NOT NULL,"
                + " texto TEXT NOT NULL,"
                + " autor_id INTEGER NOT NULL,"
                + " data_hora DATE NOT NULL,"
                + " FOREIGN KEY (tarefa_id) REFERENCES tarefas (id) ON DELETE CASCADE,"
                + " FOREIGN KEY (autor_id) REFERENCES usuarios (id)"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlProjetos);
            stmt.execute(sqlMembros);
            stmt.execute(sqlTarefas);
            stmt.execute(sqlComentarios);
            System.out.println("Tabelas criadas/verificadas com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar as tabelas: " + e.getMessage());
        }
    }
}