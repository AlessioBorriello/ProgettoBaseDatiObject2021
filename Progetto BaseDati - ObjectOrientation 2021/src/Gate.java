import java.util.ArrayList;

public class Gate {
	
	private int numeroGate; //Gate's number
	private ArrayList<Coda> listaCode = new ArrayList<Coda>(); //List of queues in the gate
	
	//Setters and getters
	public int getNumeroGate() {
		return numeroGate;
	}
	public void setNumeroGate(int numeroGate) {
		this.numeroGate = numeroGate;
	}
	public ArrayList<Coda> getListaCode() {
		return listaCode;
	}
	public void setListaCode(ArrayList<Coda> listaCode) {
		this.listaCode = listaCode;
	}
	
}
