
public class Coda {

	private String tipo; //Queue type
	private int personeInCoda = 0; //People in this queue
	
	//Setters and getters
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getPersoneInCoda() {
		return personeInCoda;
	}
	public void setPersoneInCoda(int personeInCoda) {
		this.personeInCoda = personeInCoda;
	}
	
}

//Enum containing the possible types of queues
enum queueType{
	
	famiglie,
	diversamenteAbili,
	priority,
	businessClass,
	standardClass,
	economyClass
	
}