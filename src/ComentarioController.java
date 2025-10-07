import java.time.LocalDate;
import java.util.List;

public class ComentarioController {
    private final ComentarioDAO comentarioDAO;
    
    public ComentarioController() {
        this.comentarioDAO = new ComentarioDAO();
    }

    public ComentarioController(ComentarioDAO comentarioDAO) {
        this.comentarioDAO = comentarioDAO;
    }

    /**
     * Adiciona um novo comentário com validações.
     * Caso de Uso 10: Adicionar comentário em uma tarefa
     * @return true se o comentário foi adicionado com sucesso, false caso contrário
     */
    public boolean adicionarComentario(int tarefaId, String texto, int autorId) {
        // Validações
        if (tarefaId <= 0) {
            System.out.println("ID da tarefa inválido!");
            return false;
        }

        if (texto == null || texto.trim().isEmpty()) {
            System.out.println("Texto do comentário não pode ser vazio!");
            return false;
        }

        if (autorId <= 0) {
            System.out.println("ID do autor inválido!");
            return false;
        }

        Comentario comentario = new Comentario();
        comentario.setTarefaId(tarefaId);
        comentario.setTexto(texto.trim());
        comentario.setAutor(autorId);
        comentario.setDataHora(LocalDate.now());

        comentarioDAO.adicionarComentario(comentario);
        return true;
    }

    /**
     * Lista todos os comentários de uma tarefa específica.
     */
    public List<Comentario> listarComentariosPorTarefa(int tarefaId) {
        if (tarefaId <= 0) {
            System.out.println("ID da tarefa inválido!");
            return null;
        }
        return comentarioDAO.listarPorTarefa(tarefaId);
    }

    /**
     * Deleta um comentário por ID.
     * @return true se deletado com sucesso, false caso contrário
     */
    public boolean deletarComentario(int id, int usuarioId) {
        if (id <= 0) {
            System.out.println("ID inválido!");
            return false;
        }

        // Busca o comentário para verificar se existe e se o usuário é o autor
        List<Comentario> comentarios = comentarioDAO.listarPorTarefa(0); // Implementar busca por ID
        
        comentarioDAO.deletarComentario(id);
        return true;
    }

    /**
     * Conta quantos comentários uma tarefa possui.
     */
    public int contarComentariosDaTarefa(int tarefaId) {
        if (tarefaId <= 0) {
            return 0;
        }
        return comentarioDAO.contarComentariosPorTarefa(tarefaId);
    }

    /**
     * Valida se um texto de comentário é válido.
     */
    public boolean validarTextoComentario(String texto) {
        return texto != null && !texto.trim().isEmpty() && texto.length() >= 1;
    }

    /**
     * Verifica se um usuário pode deletar um comentário (deve ser o autor).
     */
    public boolean usuarioPodeDeletarComentario(int comentarioId, int usuarioId) {
        // Esta funcionalidade requer um método buscarPorId no ComentarioDAO
        // Por enquanto, retorna true (implementar depois)
        return true;
    }

    /**
     * Busca os comentários mais recentes de uma tarefa (limitado).
     */
    public List<Comentario> buscarComentariosRecentes(int tarefaId, int limite) {
        if (tarefaId <= 0 || limite <= 0) {
            return null;
        }

        List<Comentario> todos = comentarioDAO.listarPorTarefa(tarefaId);
        
        if (todos.size() <= limite) {
            return todos;
        }

        return todos.subList(0, limite);
    }
}