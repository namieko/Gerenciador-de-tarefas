import java.util.List;

public class MembroController {
    private final MembroDAO membroDAO;
    
    public MembroController() {
        this.membroDAO = new MembroDAO();
    }

    public MembroController(MembroDAO membroDAO) {
        this.membroDAO = membroDAO;
    }

    /**
     * Adiciona um novo membro com validações.
     * @return true se o membro foi adicionado com sucesso, false caso contrário
     */
    public boolean adicionarNovoMembro(String nome) {
        // Validação
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome do membro não pode ser vazio!");
            return false;
        }

        Membro membro = new Membro();
        membro.setNome(nome.trim());
        
        membroDAO.adicionarMembro(membro);
        return true;
    }

    /**
     * Lista todos os membros cadastrados.
     */
    public List<Membro> listarMembros() {
        return membroDAO.listarTodos();
    }

    /**
     * Busca um membro por ID.
     */
    public Membro buscarMembroPorId(int id) {
        if (id <= 0) {
            System.out.println("ID inválido!");
            return null;
        }

        List<Membro> membros = membroDAO.listarTodos();
        for (Membro m : membros) {
            if (m.getId() == id) {
                return m;
            }
        }
        
        System.out.println("Membro não encontrado!");
        return null;
    }

    /**
     * Atualiza o nome de um membro existente.
     * @return true se atualizado com sucesso, false caso contrário
     */
    public boolean atualizarMembro(int id, String novoNome) {
        // Validações
        if (id <= 0) {
            System.out.println("ID inválido!");
            return false;
        }

        if (novoNome == null || novoNome.trim().isEmpty()) {
            System.out.println("Novo nome não pode ser vazio!");
            return false;
        }

        // Verifica se o membro existe
        Membro membro = buscarMembroPorId(id);
        if (membro == null) {
            return false;
        }

        membro.setNome(novoNome.trim());
        membroDAO.atualizarMembro(membro);
        return true;
    }

    /**
     * Deleta um membro por ID.
     * @return true se deletado com sucesso, false caso contrário
     */
    public boolean deletarMembro(int id) {
        if (id <= 0) {
            System.out.println("ID inválido!");
            return false;
        }

        // Verifica se o membro existe
        Membro membro = buscarMembroPorId(id);
        if (membro == null) {
            return false;
        }

        membroDAO.deletarMembroPorId(id);
        return true;
    }

    /**
     * Valida se um nome de membro é válido.
     */
    public boolean validarNome(String nome) {
        return nome != null && !nome.trim().isEmpty() && nome.length() >= 2;
    }

    /**
     * Busca membros por nome (busca parcial).
     */
    public List<Membro> buscarMembrosPorNome(String nomeParcial) {
        if (nomeParcial == null || nomeParcial.trim().isEmpty()) {
            return listarMembros();
        }

        List<Membro> todosMembros = membroDAO.listarTodos();
        String busca = nomeParcial.trim().toLowerCase();
        
        return todosMembros.stream()
            .filter(m -> m.getNome().toLowerCase().contains(busca))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Retorna a contagem total de membros.
     */
    public int getContagemTotalMembros() {
        return membroDAO.listarTodos().size();
    }
}