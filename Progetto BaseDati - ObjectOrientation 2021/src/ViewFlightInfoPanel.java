import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.text.DateFormatter;

import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewFlightInfoPanel extends JPanel {

	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	
	//Flight instance to show its info
	private Volo volo;
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm"); //Date format
	//Labels, declared here so that they can be accessed by the methods
	private JLabel tempoEffettivoLabel;
	private JLabel flightStatusLabel;
	//Buttons, declared here so that they can be accessed by the methods
	private JButton buttonFlightTakenOff;
	private JButton buttonCancelFlight;
	private JButton buttonEditFlight;

	public ViewFlightInfoPanel(Rectangle bounds, MainFrame mf, MainController c, Volo v) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		volo = v;
		
		//setBounds(bounds);
		setBounds(72, 2, 1124, 666);
		setLayout(null);
		
		JLabel idLabel = new JLabel("ID: " + v.getID());
		idLabel.setName("idLabel");
		idLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		idLabel.setBounds(255, 70, 350, 22);
		add(idLabel);
		
		JLabel companyLogoLabel = new JLabel("");
		companyLogoLabel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		companyLogoLabel.setName("companyLogoLabel");
		companyLogoLabel.setBounds(45, 45, 200, 200);
		add(companyLogoLabel);
		
		JLabel nomeCompagniaLabel = new JLabel("Compagnia: " + volo.getCompagnia().getNome());
		nomeCompagniaLabel.setName("nomeCompagniaLabel");
		nomeCompagniaLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		nomeCompagniaLabel.setBounds(255, 103, 350, 22);
		add(nomeCompagniaLabel);
		
		JLabel gateNumberLabel = new JLabel("Numero gate: " + volo.getGate().getNumeroGate());
		gateNumberLabel.setName("gateNumberLabel");
		gateNumberLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		gateNumberLabel.setBounds(255, 136, 350, 22);
		add(gateNumberLabel);
		
		JLabel numeroPrenotazioniLabel = new JLabel("Numero prenotazioni: " + volo.getNumeroPrenotazioni());
		numeroPrenotazioniLabel.setName("numeroPrenotazioniLabel");
		numeroPrenotazioniLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		numeroPrenotazioniLabel.setBounds(255, 169, 350, 22);
		add(numeroPrenotazioniLabel);
		
		JLabel orarioLabel = new JLabel("Partenza: " + dateTimeFormat.format(volo.getOrarioDecollo()));
		orarioLabel.setName("orarioLabel");
		orarioLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		orarioLabel.setBounds(255, 202, 350, 22);
		add(orarioLabel);
		
		JPanel slotPanel = new JPanel();
		slotPanel.setName("slotPanel");
		slotPanel.setBounds(45, 256, 560, 120);
		add(slotPanel);
		slotPanel.setLayout(null);
		
		JLabel slotLabel = new JLabel("Slot: ");
		slotLabel.setName("slotLabel");
		slotLabel.setHorizontalAlignment(SwingConstants.CENTER);
		slotLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		slotLabel.setBounds(0, 0, 560, 26);
		slotPanel.add(slotLabel);
		
		JLabel tempoStimatoLabel = new JLabel("Tempo stimato: " + dateTimeFormat.format(volo.getSlot().getInizioTempoStimato()) + " - - " + dateTimeFormat.format(volo.getSlot().getFineTempoStimato()));
		tempoStimatoLabel.setName("tempoStimatoLabel");
		tempoStimatoLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		tempoStimatoLabel.setBounds(0, 37, 560, 26);
		slotPanel.add(tempoStimatoLabel);
		
		//Check if the effective time has been inserted, otherwise set the string to "Non definito"
		String inizioTempoEffettivo = (volo.getSlot().getInizioTempoEffettivo() == null)? "Non definito" : dateTimeFormat.format(volo.getSlot().getInizioTempoEffettivo());
		String fineTempoEffettivo = (volo.getSlot().getFineTempoEffettivo() == null)? "Non definito" : dateTimeFormat.format(volo.getSlot().getFineTempoEffettivo());
		tempoEffettivoLabel = new JLabel("Tempo effettivo: " + inizioTempoEffettivo + " - - " + fineTempoEffettivo);
		tempoEffettivoLabel.setName("tempoEffettivoLabel");
		tempoEffettivoLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		tempoEffettivoLabel.setBounds(0, 74, 560, 26);
		slotPanel.add(tempoEffettivoLabel);
		
		JPanel codePanel = new JPanel();
		codePanel.setName("codePanel");
		codePanel.setBounds(45, 387, 560, 268);
		add(codePanel);
		codePanel.setLayout(new GridLayout(7, 0, 0, 0)); //Set rows to the amount of queue types
		
		flightStatusLabel = new JLabel();
		flightStatusLabel.setName("flightStatusLabel");
		flightStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		flightStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		flightStatusLabel.setBounds(634, 45, 453, 64);
		//Check flight status and change the text
		if(volo.isPartito()) {
			if(volo.checkIfFlightTookOffLate()) {
				flightStatusLabel.setText("Questo volo e' partito in ritardo");
			}else {
				flightStatusLabel.setText("Questo volo e' partito in orario");
			}
		}else {
			if(volo.isCancellato()) {
				flightStatusLabel.setText("Questo volo e' stato cancellato");
			}else {
				flightStatusLabel.setText("Questo volo non e' ancora partito");
			}
		}
		add(flightStatusLabel);
		
		buttonFlightTakenOff = new JButton("Fai partire questo volo");
		buttonFlightTakenOff.setName("buttonFlightTakenOff");
		buttonFlightTakenOff.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			
				setFlightAsTakenOff();
			
			}
		});
		buttonFlightTakenOff.setFont(new Font("Tahoma", Font.BOLD, 14));
		buttonFlightTakenOff.setBounds(765, 139, 191, 64);
		if(!volo.isPartito() && !volo.isCancellato()) {
			add(buttonFlightTakenOff);
		}
		
		buttonCancelFlight = new JButton("Cancella questo volo");
		buttonCancelFlight.setName("buttonCancelFlight");
		buttonCancelFlight.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			
				setFlightAsCancelled();
			
			}
		});
		buttonCancelFlight.setFont(new Font("Tahoma", Font.BOLD, 14));
		buttonCancelFlight.setBounds(765, 214, 191, 64);
		if(!volo.isPartito() && !volo.isCancellato()) {
			add(buttonCancelFlight);
		}
		
		buttonEditFlight = new JButton("Modifica volo");
		buttonEditFlight.setName("buttonEditFlight");
		buttonEditFlight.setFont(new Font("Tahoma", Font.BOLD, 14));
		buttonEditFlight.setBounds(765, 289, 191, 64);
		if(!volo.isPartito() && !volo.isCancellato()) {
			add(buttonEditFlight);
		}
		
		ArrayList<Coda> listaCode = volo.getGate().getListaCode();
		for(Coda coda : listaCode) {
			JLabel lbl = new JLabel();
			lbl.setText("Tipo: " + coda.getTipo() + ", persone in coda: " + coda.getPersoneInCoda());
			codePanel.add(lbl);
		}
		
	}
	
	public void setFlightAsTakenOff() {
		
		SetEffectiveTimeFrame frame = new SetEffectiveTimeFrame(mainFrame, volo.getSlot().getInizioTempoStimato()); //Create a popup panel
		frame.setVisible(true); //Make it visible
		Date data = frame.getDate(); //Get the date taken from the frame
		
		if(data != null) {
			if(mainFrame.createConfirmationFrame("Sei sicuro di voler impostare questo volo come 'partito'?")) {
				//Create new slot and set values
				Slot s = (new SlotDAO().getSlotByID(volo.getID()));
				s.setInizioTempoEffettivo(data); //Set lower range
				
				//Get slot's higher range
				Calendar c = Calendar.getInstance(); //Create a calendar instance
				c.setTime(data);
				c.add(Calendar.MINUTE, 15); //Add 15 minutes to the lower range of the slot
				Date fineTempoEffettivo = new Date();
				fineTempoEffettivo = c.getTime();
				
				s.setFineTempoEffettivo(fineTempoEffettivo); //Set higher range
				volo.setSlot(s); //Update flight's slot
				
				//Update slot in database
				SlotDAO daoSlot = new SlotDAO();
				if(!daoSlot.updateTempoEffettivo(mainFrame, s, volo.getID())) {
					mainFrame.createNotificationFrame("Impossibile aggiornare lo slot!");
					return;
				}
				
				//Update date label text
				SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm");
				tempoEffettivoLabel.setText("Tempo effettivo: " + dateTimeFormat.format(s.getInizioTempoEffettivo()) + " - - " + dateTimeFormat.format(s.getFineTempoEffettivo()));
				
				//Remove buttons
				remove(buttonFlightTakenOff);
				remove(buttonCancelFlight);
				remove(buttonEditFlight);
				
				//Update flight in database
				new VoloDAO().setFlightAsTakenOff(mainFrame, volo);
				volo.setPartito(true);
				
				//Update status label text
				if(!volo.checkIfFlightTookOffLate()) {
					flightStatusLabel.setText("Questo volo e' partito in orario");
				}else {
					flightStatusLabel.setText("Questo volo e' partito in ritardo");
				}
				
				//Repaint and revalidate panel
				repaint();
				revalidate();
				mainFrame.createNotificationFrame("Volo impostato come 'partito'!");
				
			}
		}
		
	}

	public void setFlightAsCancelled() {
		
		if(mainFrame.createConfirmationFrame("Sei sicuro di voler impostare questo volo come 'cancellato'?")) {
			
			//Remove buttons
			remove(buttonFlightTakenOff);
			remove(buttonCancelFlight);
			remove(buttonEditFlight);
			
			//Update flight in database
			new VoloDAO().setFlightAsCancelled(mainFrame, volo);
			volo.setCancellato(true);
			
			flightStatusLabel.setText("Questo volo e' stato cancellato");
			
			//Repaint and revalidate panel
			repaint();
			revalidate();
			mainFrame.createNotificationFrame("Volo impostato come 'cancellato'!");
			
		}
		
	}

}
