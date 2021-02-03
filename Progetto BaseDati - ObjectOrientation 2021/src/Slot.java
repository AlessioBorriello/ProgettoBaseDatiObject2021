import java.sql.Time;
import java.util.Date;

public class Slot {
	
	private Date tempoStimatoInferiore;
	private Date tempoStimatoSuperiore;
	private Date tempoEffettivoInferiore;
	private Date tempoEffettivoSuperiore;
	
	
	public Date getTempoStimatoInferiore() {
		return tempoStimatoInferiore;
	}
	public void setTempoStimatoInferiore(Date tempoStimatoInferiore) {
		this.tempoStimatoInferiore = tempoStimatoInferiore;
	}
	public Date getTempoStimatoSuperiore() {
		return tempoStimatoSuperiore;
	}
	public void setTempoStimatoSuperiore(Date tempoStimatoSuperiore) {
		this.tempoStimatoSuperiore = tempoStimatoSuperiore;
	}
	
}
