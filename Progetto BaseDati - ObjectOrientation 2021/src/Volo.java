 import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Volo {
	
	private CompagniaAerea compagnia;
	private Date orarioDecollo;
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
	public Date getOrarioDecollo() {
		return orarioDecollo;
	}
	public void setOrarioDecollo(Date orarioDecollo) {
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
	public Slot getSlot() {
		return slot;
	}
	public void setSlot(Slot slot) {
		this.slot = slot;
	}
	
	public void printFlightInfo() {
		
		System.out.println("Compagnia: " + compagnia.getNome() + " Data: " + orarioDecollo + " gate: " + gate.getNumeroGate() + "\nSlot: " + slot.getTempoStimatoInferiore() + " - " + slot.getTempoStimatoSuperiore());
		for(Coda c : gate.getListaCode()) {
			System.out.print(c.getTipo() + " ");
		}
		
	}

}