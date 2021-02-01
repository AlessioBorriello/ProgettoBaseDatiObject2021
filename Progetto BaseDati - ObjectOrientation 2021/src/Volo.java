 import java.sql.Time;

public class Volo {
	
	private CompagniaAerea compagnia;
	private Time orarioDecollo;
	private Gate gate;
	private Slot slot;
	private int numeroPrenotazioni = 0;
	private boolean partito = false;
	private int ID = 0;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public CompagniaAerea getCompagnia() {
		return compagnia;
	}
	public void setCompagnia(CompagniaAerea compagnia) {
		this.compagnia = compagnia;
	}
	public Time getOrarioDecollo() {
		return orarioDecollo;
	}
	public void setOrarioDecollo(Time orarioDecollo) {
		this.orarioDecollo = orarioDecollo;
	}
	public Gate getGate() {
		return gate;
	}
	public void setGate(Gate gate) {
		this.gate = gate;
	}
	public int getNumeroPrenotazioni() {
		return numeroPrenotazioni;
	}
	public void setNumeroPrenotazioni(int numeroPrenotazioni) {
		this.numeroPrenotazioni = numeroPrenotazioni;
	}
	public boolean isPartito() {
		return partito;
	}
	public void setPartito(boolean partito) {
		this.partito = partito;
	}
	
}