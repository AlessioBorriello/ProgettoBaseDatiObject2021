
public class Coda {

	private queueType tipo;
	private int personeInCoda = 0;
	
	public queueType getTipo() {
		return tipo;
	}
	public void setTipo(queueType tipo) {
		this.tipo = tipo;
	}
	public int getPersoneInCoda() {
		return personeInCoda;
	}
	public void setPersoneInCoda(int personeInCoda) {
		this.personeInCoda = personeInCoda;
	}
	
}

enum queueType{
	
	famiglie,
	diversamenteAbili,
	priority,
	businessClass,
	standardClass,
	economyClass
	
}