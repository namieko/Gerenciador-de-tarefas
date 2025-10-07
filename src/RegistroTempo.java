import java.time.LocalDate;

public class RegistroTempo {
    private int id;
    private double horasGastas;
    private LocalDate dataRegistro;
    private int tarefaId;
    private int usuarioId;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getHorasGastas() {
        return horasGastas;
    }

    public void setHorasGastas(double horasGastas) {
        this.horasGastas = horasGastas;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public int getTarefaId() {
        return tarefaId;
    }

    public void setTarefaId(int tarefaId) {
        this.tarefaId = tarefaId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}