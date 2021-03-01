
public class Coda {

	private String tipo; //Queue type
	private int personeInCoda = 0; //People in this queue
	
	//Setters and getters
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) throws NonExistentQueueTypeException{
		if(!tipo.equals("Famiglia") && !tipo.equals("Priority") && !tipo.equals("Diversamente abili") && !tipo.equals("Business Class") && !tipo.equals("Standard Class") && !tipo.equals("Economy Class")) {
			this.tipo = "Famiglia"; //Set queue type to 'Famiglia' by default
			throw new NonExistentQueueTypeException("Questo tipo di coda non esiste: " + tipo);
		}else {
			this.tipo = tipo;
		}
	}
	public int getPersoneInCoda() {
		return personeInCoda;
	}
	public void setPersoneInCoda(int personeInCoda) {
		this.personeInCoda = personeInCoda;
	}
	
}