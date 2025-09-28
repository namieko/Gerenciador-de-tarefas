import java.time.LocalDate;

public class Tarefa extends Membro{
    private String descricao;
    private LocalDate prazo;
    private StatusTarefa status;
    private int membroId; // "Link" para o ID de um Membro
    private int projetoId; // "Link" para o ID de um Projeto

    // Getters e Setters para todos os atributos...
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }
    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; }
    public int getMembroId() { return membroId; }
    public void setMembroId(int membroId) { this.membroId = membroId; }
    public int getProjetoId() { return projetoId; }
    public void setProjetoId(int projetoId) { this.projetoId = projetoId; }
}