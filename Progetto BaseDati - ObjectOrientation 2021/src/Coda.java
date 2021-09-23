
public class Coda {

	private String tipo; //Queue type
	private int gateNumber; //This queue's gate
	private int lunghezzaMax = 0; //Max number of people in the queue
	
	//Setters and getters
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) throws NonExistentQueueTypeException{
		if(!tipo.equals("Famiglia") && !tipo.equals("Priority") && !tipo.equals("Diversamente Abili") && !tipo.equals("Business Class") && !tipo.equals("Standard Class") && !tipo.equals("Economy Class")) {
			this.tipo = "Famiglia"; //Set queue type to 'Famiglia' by default
			throw new NonExistentQueueTypeException("Questo tipo di coda non esiste: " + tipo);
		}else {
			this.tipo = tipo;
		}
	}
	public int getGateNumber() {
		return gateNumber;
	}
	public void setGateNumber(int gateNumber) {
		this.gateNumber = gateNumber;
	}
	public int getLunghezzaMax() {
		return lunghezzaMax;
	}
	public void setLunghezzaMax(int lunghezzaMax) {
		this.lunghezzaMax = lunghezzaMax;
	}
	
}