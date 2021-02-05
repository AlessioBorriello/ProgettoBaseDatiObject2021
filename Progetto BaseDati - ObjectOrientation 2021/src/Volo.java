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
	private boolean cancellato = false;
	private boolean inRitardo = false;
	private String ID = null;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
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
	public boolean isCancellato() {
		return cancellato;
	}
	public void setCancellato(boolean cancellato) {
		this.cancellato = cancellato;
	}
	public boolean isInRitardo() {
		return inRitardo;
	}
	public void setInRitardo(boolean inRitardo) {
		this.inRitardo = inRitardo;
	}
	
	public void printFlightInfo() {
		
		System.out.println("ID: " + ID + " Compagnia: " + compagnia.getNome() + " Data: " + orarioDecollo + " gate: " + gate.getNumeroGate() + "\nSlot: " + slot.getInizioTempoStimato() + " - " + slot.getFineTempoStimato());
		for(Coda c : gate.getListaCode()) {
			System.out.print(c.getTipo() + " ");
		}
		
	}

	public boolean checkIfFlightTookOffLate() {
		
		//If the flight has not taken off yet
		if(partito == false) {
			return false;
		}
		
		//Get slot times
		//Date inizioTempoStimato = slot.getInizioTempoStimato();
		Date fineTempoStimato = slot.getFineTempoStimato();
		Date inizioTempoEffettivo = slot.getInizioTempoEffettivo();
		Date fineTempoEffettivo = slot.getFineTempoEffettivo();
		
		//If the effective times are null (should not be possible if the above if is ignored)
		if(inizioTempoEffettivo == null || fineTempoEffettivo == null) {
			return false;
		}
		
		return fineTempoStimato.before(fineTempoEffettivo);
		
	}

	
}