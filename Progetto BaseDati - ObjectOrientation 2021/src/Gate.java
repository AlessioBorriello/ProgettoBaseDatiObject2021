import java.util.ArrayList;

public class Gate {
	
	private int numeroGate;
	private ArrayList<Coda> listaCode = new ArrayList<Coda>();
	
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
