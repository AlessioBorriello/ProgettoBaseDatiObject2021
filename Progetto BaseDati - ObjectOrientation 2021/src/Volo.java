 import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Volo {
	
	private CompagniaAerea compagnia; //Company linked to the flight
	private Date orarioDecollo; //Take off time
	private Gate gate; //Gate linked to the flight
	private Slot slot; //Slot linked to the flight
	private int numeroPrenotazioni = 0; //Number of reservations
	private boolean partito = false; //If the flight has taken off
	private boolean cancellato = false; //If the flight has been cancelled
	private boolean inRitardo = false; //If the flight has taken off late
	private String ID = null; //Flight if
	private String destinazione; //Flight destination
	
	/**
	 * Print the flight's info
	 */
	public void printFlightInfo() {
		
		//Print it's info
		System.out.println("ID: " + ID + " Compagnia: " + compagnia.getNome() + " Data: " + orarioDecollo + " gate: " + gate.getNumeroGate() + "\nSlot: " + slot.getInizioTempoStimato() + " - " + slot.getFineTempoStimato());
		//Print queue info for each queue in the linked gate
		for(Coda c : gate.getListaCode()) {
			System.out.print(c.getTipo() + "-");
		}
		
	}

	/**
	 * Check if a flight has taken off later (if it has at all) then it's estimated slot time
	 * @return If a flight has taken off later then it's estimated slot time
	 */
	public boolean checkIfFlightTookOffLate() {
		
		//If the flight has not taken off yet
		if(partito == false) {
			return false;
		}
		
		//Get slot times
		Date fineTempoStimato = slot.getFineTempoStimato();
		Date inizioTempoEffettivo = slot.getInizioTempoEffettivo();
		Date fineTempoEffettivo = slot.getFineTempoEffettivo();
		
		//If the effective times are null (should not be possible if the above if is ignored)
		if(inizioTempoEffettivo == null || fineTempoEffettivo == null) {
			return false;
		}
		
		return fineTempoStimato.before(fineTempoEffettivo); //Return if the Higher end of the estimated slot time is before the Higher end of the effective slot time
		
	}

	//Setters and getters
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

	public String getDestinazione() {
		return destinazione;
	}

	public void setDestinazione(String destinazione) {
		this.destinazione = destinazione;
	}
	
}