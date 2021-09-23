import java.util.ArrayList;

public class Gate {
	
	private int numeroGate; //Gate's number
	private int numeroVoli; //Gate's number of flights
	private ArrayList<Coda> listaCode = new ArrayList<Coda>(); //List of queues in the gate
	
	//Setters and getters
	public int getNumeroGate() {
		return numeroGate;
	}
	public void setNumeroGate(int numeroGate) throws NonExistentGateException{
		if(numeroGate < 1 || numeroGate > MainController.gateAirportNumber) {
			this.numeroGate = 1; //Set gate to 1 by default
			throw new NonExistentGateException("Il gate espresso non esiste");
		}else {
			this.numeroGate = numeroGate;
		}
	}
	public ArrayList<Coda> getListaCode() {
		return listaCode;
	}
	public void setListaCode(ArrayList<Coda> listaCode) {
		this.listaCode = listaCode;
	}
	public int getNumeroVoli() {
		return numeroVoli;
	}
	public void setNumeroVoli(int numeroVoli) {
		this.numeroVoli = numeroVoli;
	}
	
}