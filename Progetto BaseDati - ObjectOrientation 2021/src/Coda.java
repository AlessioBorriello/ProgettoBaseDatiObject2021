
public class Coda {

	private String tipo;
	private int personeInCoda = 0;
	
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

enum queueType{
	
	famiglie,
	diversamenteAbili,
	priority,
	businessClass,
	standardClass,
	economyClass
	
}