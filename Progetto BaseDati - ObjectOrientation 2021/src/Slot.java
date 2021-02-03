import java.sql.Time;
import java.util.Date;

public class Slot {
	
	private Date inizioTempoStimato = null;
	private Date fineTempoStimato = null;
	private Date inizioTempoEffettivo = null;
	private Date fineTempoEffettivo = null;
	
	
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
