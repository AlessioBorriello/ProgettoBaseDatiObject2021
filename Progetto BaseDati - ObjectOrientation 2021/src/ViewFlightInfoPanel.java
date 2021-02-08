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

	/**
	 * Panel that shows the info of a given flight in detail
	 * @param bounds Bounds of the contentPanel that contains this panel (to give it the contentPanel's dimensions)
	 * @param mf Link to the MainFrame
	 * @param c Link to the MainController
	 * @param v Flight to show the info of
	 */
	public ViewFlightInfoPanel(Rectangle bounds, MainFrame mf, MainController c, Volo v) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		volo = v; //Flight to show the info of
		
		//setBounds(bounds);
		setBounds(72, 2, 1124, 666); //Debug to show in the design tab,  this row should be replaced with the one above
		setLayout(null); //Set layout
		
		JLabel idLabel = new JLabel("ID: " + v.getID()); //Create label showing the flight's ID
		idLabel.setName("idLabel"); //Set name
		idLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set font
		idLabel.setBounds(255, 70, 350, 22); //Set bounds
		add(idLabel); //Add label
		
		JLabel companyLogoLabel = new JLabel(""); //Create label showing the flight's logo
		companyLogoLabel.setBorder(new LineBorder(new Color(0, 0, 0), 2)); //Set border color to black
		companyLogoLabel.setName("companyLogoLabel"); //Set name
		companyLogoLabel.setBounds(45, 45, 200, 200); //Set bounds
		add(companyLogoLabel); //Add label
		
		JLabel nomeCompagniaLabel = new JLabel("Compagnia: " + volo.getCompagnia().getNome()); //Create label showing the flight's name
		nomeCompagniaLabel.setName("nomeCompagniaLabel"); //Set name
		nomeCompagniaLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set font
		nomeCompagniaLabel.setBounds(255, 103, 350, 22); //Set bounds
		add(nomeCompagniaLabel); //Add label
		
		JLabel gateNumberLabel = new JLabel("Numero gate: " + volo.getGate().getNumeroGate()); //Create label showing the flight's gate number
		gateNumberLabel.setName("gateNumberLabel"); //Set name
		gateNumberLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set font
		gateNumberLabel.setBounds(255, 136, 350, 22); //Set bounds
		add(gateNumberLabel); //Add label
		
		JLabel numeroPrenotazioniLabel = new JLabel("Numero prenotazioni: " + volo.getNumeroPrenotazioni()); //Create label showing the flight's booking number
		numeroPrenotazioniLabel.setName("numeroPrenotazioniLabel"); //Set name
		numeroPrenotazioniLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set font
		numeroPrenotazioniLabel.setBounds(255, 169, 350, 22); //Set bounds
		add(numeroPrenotazioniLabel); //Add label
		
		JLabel orarioLabel = new JLabel("Partenza: " + dateTimeFormat.format(volo.getOrarioDecollo())); //Create label showing the flight's take off time
		orarioLabel.setName("orarioLabel"); //Set name
		orarioLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set font
		orarioLabel.setBounds(255, 202, 350, 22); //Set bounds
		add(orarioLabel); //Add label
		
		JPanel slotPanel = new JPanel(); //Create panel
		slotPanel.setName("slotPanel"); //Set name
		slotPanel.setBounds(45, 256, 560, 120); //Set bounds
		add(slotPanel); //Add panel
		slotPanel.setLayout(null); //Set layout to absolute
		
		JLabel slotLabel = new JLabel("Slot: "); //Create label
		slotLabel.setName("slotLabel"); //Set name
		slotLabel.setHorizontalAlignment(SwingConstants.CENTER); //Set text horizontal alignment
		slotLabel.setFont(new Font("Tahoma", Font.BOLD, 15)); //Set font
		slotLabel.setBounds(0, 0, 560, 26); //Set bounds
		slotPanel.add(slotLabel); //Add label to the slot panel
		
		JLabel tempoStimatoLabel = new JLabel("Tempo stimato: " + dateTimeFormat.format(volo.getSlot().getInizioTempoStimato()) + " - - " + dateTimeFormat.format(volo.getSlot().getFineTempoStimato())); //Create a label showing the flight's estimated start and end time
		tempoStimatoLabel.setName("tempoStimatoLabel"); //Set name
		tempoStimatoLabel.setFont(new Font("Tahoma", Font.BOLD, 14)); //Set font
		tempoStimatoLabel.setBounds(0, 37, 560, 26); //Set bounds
		slotPanel.add(tempoStimatoLabel); //Add label to the slot panel
		
		//Check if the effective time has been inserted, otherwise set the string to "Non definito"
		String inizioTempoEffettivo = (volo.getSlot().getInizioTempoEffettivo() == null)? "Non definito" : dateTimeFormat.format(volo.getSlot().getInizioTempoEffettivo());
		String fineTempoEffettivo = (volo.getSlot().getFineTempoEffettivo() == null)? "Non definito" : dateTimeFormat.format(volo.getSlot().getFineTempoEffettivo());
		
		tempoEffettivoLabel = new JLabel("Tempo effettivo: " + inizioTempoEffettivo + " - - " + fineTempoEffettivo); //Create a label showing the flight's effective start and end time
		tempoEffettivoLabel.setName("tempoEffettivoLabel"); //Set name
		tempoEffettivoLabel.setFont(new Font("Tahoma", Font.BOLD, 14)); //Set font
		tempoEffettivoLabel.setBounds(0, 74, 560, 26); //Set bounds
		slotPanel.add(tempoEffettivoLabel); //Add label to the slot panel
		
		JPanel codePanel = new JPanel(); //Create panel
		codePanel.setName("codePanel"); //Set name
		codePanel.setBounds(45, 387, 560, 268); //Set bounds
		add(codePanel); //Add panel
		codePanel.setLayout(new GridLayout(7, 0, 0, 0)); //Set rows to the amount of queue types
		
		flightStatusLabel = new JLabel(); //Create label
		flightStatusLabel.setName("flightStatusLabel"); //Set name
		flightStatusLabel.setHorizontalAlignment(SwingConstants.CENTER); //Set text horizontal alignment
		flightStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set font
		flightStatusLabel.setBounds(634, 45, 453, 64); //Set bounds
		//Gather flight's status
		if(volo.isPartito()) { //If the flight has taken off
			if(volo.checkIfFlightTookOffLate()) { //Check if it did so late
				flightStatusLabel.setText("Questo volo e' partito in ritardo");
			}else { //Otherwise
				flightStatusLabel.setText("Questo volo e' partito in orario");
			}
		}else { //The flight has not taken off
			if(volo.isCancellato()) { //Check if it has been cancelled
				flightStatusLabel.setText("Questo volo e' stato cancellato");
			}else { //Otherwise
				flightStatusLabel.setText("Questo volo non e' ancora partito");
			}
		}
		add(flightStatusLabel); //Add label
		
		buttonFlightTakenOff = new JButton("Fai partire questo volo"); //Create button
		buttonFlightTakenOff.setName("buttonFlightTakenOff"); //Set name
		//Add action listener
		buttonFlightTakenOff.addMouseListener(new MouseAdapter() {
			//Mouse clicked
			public void mouseClicked(MouseEvent e) {
				setFlightAsTakenOff();
			}
		});
		buttonFlightTakenOff.setFont(new Font("Tahoma", Font.BOLD, 14)); //Set font
		buttonFlightTakenOff.setBounds(765, 139, 191, 64); //Set bounds
		if(!volo.isPartito() && !volo.isCancellato()) { //If the flight has neither taken off or been cancelled
			add(buttonFlightTakenOff); //Add button
		}
		
		buttonCancelFlight = new JButton("Cancella questo volo"); //Create button
		buttonCancelFlight.setName("buttonCancelFlight"); //Set name
		//Add action listener
		buttonCancelFlight.addMouseListener(new MouseAdapter() {
			//Mouse clicked
			public void mouseClicked(MouseEvent e) {
				setFlightAsCancelled();
			}
		});
		buttonCancelFlight.setFont(new Font("Tahoma", Font.BOLD, 14)); //Set font
		buttonCancelFlight.setBounds(765, 214, 191, 64); //Set bound
		if(!volo.isPartito() && !volo.isCancellato()) { //If the flight has neither taken off or been cancelled
			add(buttonCancelFlight); //Add button
		}
		
		buttonEditFlight = new JButton("Modifica volo"); //Create button
		buttonEditFlight.setName("buttonEditFlight"); //Set name
		//Add action listener
		buttonEditFlight.addMouseListener(new MouseAdapter() {
			//Mouse clicked
			public void mouseClicked(MouseEvent e) {
				mainFrame.setContentPanelToEditFlightPanel(v);
			}
		});
		buttonEditFlight.setFont(new Font("Tahoma", Font.BOLD, 14)); //Set font
		buttonEditFlight.setBounds(765, 289, 191, 64); //Set bound
		if(!volo.isPartito() && !volo.isCancellato()) { //If the flight has neither taken off or been cancelled
			add(buttonEditFlight); //Add button
		}
		
		ArrayList<Coda> listaCode = volo.getGate().getListaCode(); //Get all of the flight's queues
		for(Coda coda : listaCode) {
			JLabel lbl = new JLabel(); //Create label
			lbl.setText("Tipo: " + coda.getTipo() + ", persone in coda: " + coda.getPersoneInCoda()); //Set text
			codePanel.add(lbl); //Add label to the queue panel
		}
		
	}
	
	/**
	 * Set a flight as 'partito = 1' inside the database
	 */
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

	/**
	 * Set a flight as 'cancellato = 1' inside the database
	 */
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
