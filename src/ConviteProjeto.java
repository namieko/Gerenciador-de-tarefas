import java.time.LocalDate;

public class ConviteProjeto {
    private int id;
    private int projetoId;
    private int remetenteId;
    private int destinatarioId;
    private StatusConvite status;
    private LocalDate dataConvite;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjetoId() {
        return projetoId;
    }

    public void setProjetoId(int projetoId) {
        this.projetoId = projetoId;
    }

    public int getRemetenteId() {
        return remetenteId;
    }

    public void setRemetenteId(int remetenteId) {
        this.remetenteId = remetenteId;
    }

    public int getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(int destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public StatusConvite getStatus() {
        return status;
    }

    public void setStatus(StatusConvite status) {
        this.status = status;
    }

    public LocalDate getDataConvite() {
        return dataConvite;
    }

    public void setDataConvite(LocalDate dataConvite) {
        this.dataConvite = dataConvite;
    }
}