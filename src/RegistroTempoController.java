import java.time.LocalDate;
import java.util.List;

public class RegistroTempoController {
    private final RegistroTempoDAO registroTempoDAO;
    
    public RegistroTempoController() {
        this.registroTempoDAO = new RegistroTempoDAO();
    }

    public RegistroTempoController(RegistroTempoDAO registroTempoDAO) {
        this.registroTempoDAO = registroTempoDAO;
    }

    /**
     * Registra tempo gasto em uma tarefa.
     * RF010: O sistema deve permitir que o usuário registre o tempo que gastou para realizar uma tarefa
     */
    public boolean registrarTempo(int tarefaId, int usuarioId, double horasGastas, LocalDate dataRegistro) {
        // Validações
        if (tarefaId <= 0) {
            System.out.println("ID da tarefa inválido!");
            return false;
        }

        if (usuarioId <= 0) {
            System.out.println("ID do usuário inválido!");
            return false;
        }

        if (horasGastas <= 0) {
            System.out.println("Horas gastas deve ser maior que zero!");
            return false;
        }

        if (horasGastas > 24) {
            System.out.println("Horas gastas não pode ser maior que 24 horas!");
            return false;
        }

        if (dataRegistro == null) {
            System.out.println("Data de registro é obrigatória!");
            return false;
        }

        if (dataRegistro.isAfter(LocalDate.now())) {
            System.out.println("Data de registro não pode ser no futuro!");
            return false;
        }

        RegistroTempo registro = new RegistroTempo();
        registro.setTarefaId(tarefaId);
        registro.setUsuarioId(usuarioId);
        registro.setHorasGastas(horasGastas);
        registro.setDataRegistro(dataRegistro);

        registroTempoDAO.adicionarRegistroTempo(registro);
        return true;
    }

    /**
     * Lista todos os registros de tempo de uma tarefa.
     */
    public List<RegistroTempo> listarRegistrosPorTarefa(int tarefaId) {
        if (tarefaId <= 0) {
            System.out.println("ID da tarefa inválido!");
            return null;
        }
        return registroTempoDAO.listarPorTarefa(tarefaId);
    }

    /**
     * Lista todos os registros de tempo de um usuário.
     */
    public List<RegistroTempo> listarRegistrosPorUsuario(int usuarioId) {
        if (usuarioId <= 0) {
            System.out.println("ID do usuário inválido!");
            return null;
        }
        return registroTempoDAO.listarPorUsuario(usuarioId);
    }

    /**
     * Calcula o total de horas gastas em uma tarefa.
     */
    public double calcularTotalHorasTarefa(int tarefaId) {
        if (tarefaId <= 0) {
            return 0.0;
        }
        return registroTempoDAO.calcularTotalHorasTarefa(tarefaId);
    }

    /**
     * Deleta um registro de tempo.
     */
    public boolean deletarRegistroTempo(int id, int usuarioId) {
        if (id <= 0) {
            System.out.println("ID inválido!");
            return false;
        }

        // Aqui você poderia adicionar validação para verificar se o usuário
        // é o dono do registro antes de deletar
        
        registroTempoDAO.deletarRegistroTempo(id);
        return true;
    }

    /**
     * Atualiza um registro de tempo existente.
     */
    public boolean atualizarRegistroTempo(int id, double novasHoras, LocalDate novaData) {
        if (id <= 0) {
            System.out.println("ID inválido!");
            return false;
        }

        if (novasHoras <= 0 || novasHoras > 24) {
            System.out.println("Horas inválidas!");
            return false;
        }

        if (novaData == null || novaData.isAfter(LocalDate.now())) {
            System.out.println("Data inválida!");
            return false;
        }

        RegistroTempo registro = new RegistroTempo();
        registro.setId(id);
        registro.setHorasGastas(novasHoras);
        registro.setDataRegistro(novaData);

        registroTempoDAO.atualizarRegistroTempo(registro);
        return true;
    }

    /**
     * Formata horas para exibição (ex: 1.5 -> "1h 30min").
     */
    public String formatarHoras(double horas) {
        int horasInteiras = (int) horas;
        int minutos = (int) ((horas - horasInteiras) * 60);
        
        if (minutos == 0) {
            return horasInteiras + "h";
        }
        return horasInteiras + "h " + minutos + "min";
    }

    /**
     * Valida se as horas informadas são válidas.
     */
    public boolean validarHoras(double horas) {
        return horas > 0 && horas <= 24;
    }
}