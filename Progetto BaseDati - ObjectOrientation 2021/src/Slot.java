import java.util.Date;

public class Slot {
	
	private Date inizioTempoStimato = null; //Lower end of the estimated time slot
	private Date fineTempoStimato = null; //Higher end of the estimated time slot
	private Date inizioTempoEffettivo = null; //Lower end of the effective time slot
	private Date fineTempoEffettivo = null; //Higher end of the effective time slot
	
	//Setter and getters
	public Date getInizioTempoStimato() {
		return inizioTempoStimato;
	}
	public void setInizioTempoStimato(Date inizioTempoStimato) {
		this.inizioTempoStimato = inizioTempoStimato;
	}
	public Date getFineTempoStimato() {
		return fineTempoStimato;
	}
	public void setFineTempoStimato(Date fineTempoStimato) {
		this.fineTempoStimato = fineTempoStimato;
	}
	public Date getInizioTempoEffettivo() {
		return inizioTempoEffettivo;
	}
	public void setInizioTempoEffettivo(Date inizioTempoEffettivo) {
		this.inizioTempoEffettivo = inizioTempoEffettivo;
	}
	public Date getFineTempoEffettivo() {
		return fineTempoEffettivo;
	}
	public void setFineTempoEffettivo(Date fineTempoEffettivo) {
		this.fineTempoEffettivo = fineTempoEffettivo;
	}
	
}
