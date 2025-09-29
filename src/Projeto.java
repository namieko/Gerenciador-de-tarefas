import java.util.ArrayList;

public class Projeto extends Membro {
    private int idDono;
    private ArrayList<Integer> usuarios;
    private ArrayList<Integer> tarefas;

    public Projeto() {
        this.usuarios = new ArrayList<>();
        this.tarefas = new ArrayList<>();
    }

    // Getters e Setters corrigidos
    public ArrayList<Integer> getTarefas() {
        return tarefas;
    }

    public ArrayList<Integer> getUsuarios() {
        return usuarios;
    }

    public int getIdDono() {
        return idDono;
    }

    public void setDono(int id) {
        this.idDono = id;
    }

    public void adicionarUsuario(int id) {
        if (!usuarios.contains(id)) {
            usuarios.add(id);
        }
    }

    public void adicionarTarefa(int id) {
        if (!tarefas.contains(id)) {
            tarefas.add(id);
        }
    }

    public void removerUsuario(int id) {
        usuarios.remove(Integer.valueOf(id));
    }

    public void removerTarefa(int id) {
        tarefas.remove(Integer.valueOf(id));
    }
}