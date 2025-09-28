import java.util.ArrayList;
public class Projeto extends Membro{
    private int idDono;
    private ArrayList<Integer> usuarios;
    private ArrayList<Integer> tarefas;

    public ArrayList<Integer> getTarefas(){
        return usuarios;
    }
    public ArrayList<Integer> getUsuarios(){
        return usuarios;
    }
    public ArrayList<Integer> getDono(){
        return usuarios;
    }
    public void setDono(int id){
        idDono = id;
    }
    public void adicionarUsuario(int id){
        usuarios.add(id);
    }
    public void adicionarTarefa(int id){
        tarefas.add(id);
    }

}