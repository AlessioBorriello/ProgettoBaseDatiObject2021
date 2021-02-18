import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.text.DateFormatter;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JTable;

public class EditFlightPanel extends JPanel {

	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	private ArrayList<CompagniaAerea> listaCompagnie = new ArrayList<CompagniaAerea>(); //Array containing the companies
	private int queueNumber = 0; //How many queues have been added
	private ArrayList<String> listaCode = new ArrayList<String>(); //List of the queues
	
	private int gatesNumber = 12; //How many gate there are in the airport
	
	private JPanel panelQueues; //Panel containing the queues added
	
	/**
	 * Panel where the user can edit a flight and update it into the database
	 * @param bounds Bounds of the contentPanel that contains this panel (to give it the contentPanel's dimensions)
	 * @param mf Link to the MainFrame
	 * @param c Link to the MainController
	 * @param v Flight to edit
	 */
	public EditFlightPanel(Rectangle bounds, MainFrame mf, MainController c, Volo v) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		
		//setBounds(bounds);
		setBounds(72, 2, 1124, 666); //To have a preview in design, replace with the row above
		setLayout(null); //Set panel layout to absolute
		
		JLabel lblInsertCompany = new JLabel("Inserisci compagnia aerea:"); //Create label insert company
		lblInsertCompany.setName("lblInsertCompany"); //Name component
		lblInsertCompany.setHorizontalAlignment(SwingConstants.TRAILING); //Set text h alignment to trailing
		lblInsertCompany.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set text font
		lblInsertCompany.setBounds(64, 71, 315, 34); //Set bounds
		add(lblInsertCompany); //Add to panel
		
		//Get all the companies from the database
		CompagniaAereaDAO dao = new CompagniaAereaDAO();
		listaCompagnie = dao.getAllCompagniaAerea();
		
		JComboBox cBoxCompany = new JComboBox(); //Combo box of the companies
		cBoxCompany.setName("cBoxCompany"); //Name component
		cBoxCompany.setBounds(389, 80, 148, 22); //Set bounds
		add(cBoxCompany); //Add to panel
		//Populate combo box
		int companyIndex = 0; //Index where the company of the flight is located
		int tempIndex = 0; //Index to increment in the for loop
		for(CompagniaAerea comp : listaCompagnie) {
			//Add each member of the array listaCompagnie to the combobox
			cBoxCompany.addItem(comp.getNome());
			if(comp.getNome().equals(v.getCompagnia().getNome())) {
				companyIndex = tempIndex; //Set the correct index
			}
			tempIndex++; //Increment temporary index
		}
		//Set the comboBox
		cBoxCompany.setSelectedIndex(companyIndex);
		
		JLabel lblInsertTakeOffDate = new JLabel("Inserisci data partenza:"); //Create label insert take off date
		lblInsertTakeOffDate.setName("lblInsertTakeOffDate"); //Name component
		lblInsertTakeOffDate.setHorizontalAlignment(SwingConstants.TRAILING); //Set text h alignment to trailing
		lblInsertTakeOffDate.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set text font
		lblInsertTakeOffDate.setBounds(64, 130, 315, 34); //Set bounds
		add(lblInsertTakeOffDate); //Add to panel
		
		JSpinner spinnerTakeOffDate = new JSpinner(); //Create spinner to declare the take off date
		spinnerTakeOffDate.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set spinner model
		spinnerTakeOffDate.setValue(v.getOrarioDecollo());
		spinnerTakeOffDate.setName("spinnerTakeOffDate"); //Name component
		spinnerTakeOffDate.setFont(new Font("Tahoma", Font.BOLD, 11)); //Set text font
		spinnerTakeOffDate.setBounds(389, 140, 148, 20); //Set bounds
		add(spinnerTakeOffDate); //Add to panel
		
		JLabel lblInsertGate = new JLabel("Inserisci gate:"); //Create label insert gate
		lblInsertGate.setName("lblInsertGate"); //Name component
		lblInsertGate.setHorizontalAlignment(SwingConstants.TRAILING); //Set text h alignment to trailing
		lblInsertGate.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set text font
		lblInsertGate.setBounds(64, 185, 315, 34); //Set bounds
		add(lblInsertGate); //Add to panel
		
		JSpinner spinnerGate = new JSpinner(); //Create spinner to declare the gate
		spinnerGate.setName("spinnerGate"); //Name component
		spinnerGate.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(gatesNumber), new Integer(1))); //Set the type of spinner
		spinnerGate.setValue(Integer.valueOf(v.getGate().getNumeroGate()));
		spinnerGate.setBounds(389, 195, 148, 20); //Set bounds
		add(spinnerGate); //Add to panel
		
		JButton buttonEditFlight = new JButton("Modifica volo"); //Create button create flight
		buttonEditFlight.setName("buttonCreateFlight"); //Name component
		buttonEditFlight.setFont(new Font("Tahoma", Font.BOLD, 17)); //Set text font
		buttonEditFlight.setBounds(243, 522, 294, 58); //Set bounds
		add(buttonEditFlight); //Add to panel
		
		JLabel lblQueues = new JLabel("Code inserite: (Clicca su una per rimuovere)"); //Create label queues
		lblQueues.setName("lblQueues"); //Name component
		lblQueues.setHorizontalAlignment(SwingConstants.CENTER); //Set text h alignment to center
		lblQueues.setFont(new Font("Tahoma", Font.BOLD, 14)); //Set text font
		lblQueues.setBounds(642, 71, 315, 34); //Set bounds
		add(lblQueues); //Add to panel
		
		JLabel lblAddQueue = new JLabel("+ Aggiungi coda"); //Create label insert queue
		//Label action listener
		lblAddQueue.addMouseListener(new MouseAdapter() {
			//Mouse clicked
			public void mouseClicked(MouseEvent e) {
				createAddQueueFrame();
			}
		});
		lblAddQueue.setName("lblAddQueue"); //Name component
		lblAddQueue.setForeground(new Color(90, 90, 200, 255)); //Set color of the text
		lblAddQueue.setHorizontalAlignment(SwingConstants.TRAILING); //Set text h alignment to trailing
		lblAddQueue.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set text font
		lblAddQueue.setBounds(389, 246, 148, 34); //Set bounds
		add(lblAddQueue); //Add to panel
		
		panelQueues = new JPanel(); //Create new panel
		panelQueues.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null)); //Set border
		panelQueues.setBounds(642, 107, 315, 473); //Set bounds
		//Add queues to the panel
		for(Coda coda : (v.getGate().getListaCode())) {
			addQueue(coda.getTipo());
		}
		add(panelQueues); //Add to panel
		panelQueues.setLayout(new GridLayout(20, 0, 0, 0)); //Set layout
		
		//Button action listener
		buttonEditFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				//Gather data
				String nomeCompagnia = (String)cBoxCompany.getSelectedItem();
				Date data = (Date)spinnerTakeOffDate.getValue();
				int gate = (int)spinnerGate.getValue();
				
				editFlight(v, nomeCompagnia, data, gate); //Update flight with the gathered data
			
			}
		});
		
	}
	
	/**
	 * Create a frame prompting the user to choose a queue to add
	 */
	public void createAddQueueFrame() {
		
		AddQueueFrame frame = new AddQueueFrame(mainFrame); //Create a popup panel
		frame.setVisible(true); //Make it visible
		String choice = frame.getChoice(); //Get the choice taken from the frame
		if(!choice.equals("undo")) { //If the user has not pressed the undo button
			//Check if that type of queue has been added already
			boolean queueAlreadyAdded = false;
			for(String s : listaCode) {
				if(s.equals(choice)) {
					//If the queue has been added already (has been found in the ArrayList listaCode)
					queueAlreadyAdded = true;
					break;
				}
			}
			//If the queue has not been added already
			if(!queueAlreadyAdded) {
				addQueue(choice); //Add a queue
			}else { //If the queue has been added already
				mainFrame.createNotificationFrame("Questa coda � gi� stata aggiunta!"); //Notify user
			}
		}
		
	}
	
	/**
	 * Add a queue to the panelQueues
	 * @param type
	 */
	public void addQueue(String type) {
		
		listaCode.add(type); //Add the queue to the ArrayList listaCode
		
		JLabel lblAddedQueue = new JLabel(type); //Create label with the queue type chosen
		lblAddedQueue.setHorizontalAlignment(SwingConstants.CENTER); //Set text h alignment to center
		lblAddedQueue.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set text font
		//Label action listener
		lblAddedQueue.addMouseListener(new MouseAdapter() {
			//Mouse clicked
			public void mouseClicked(MouseEvent e) {
				removeQueue(lblAddedQueue);
			}
		});
		panelQueues.add(lblAddedQueue); //Add label to the panelQueues panel
		panelQueues.repaint(); //Repaint panelQueues
		panelQueues.revalidate(); //Revalidate panelQueues
		
	}
	
	/**
	 * Remove a queue from the panelQueues containing the queues
	 * @param queueLabel What label to remove from the panel
	 */
	public void removeQueue(JLabel queueLabel) {
		
		ConfirmationFrame frame = new ConfirmationFrame("Sei sicuro di voler rimuovere questa coda?", mainFrame); //Create confirmation frame
		frame.setVisible(true); //Set confirmation frame visible
		if(frame.getAnswer()) { //If the user has confirmed the action
			
			listaCode.remove(listaCode.indexOf(queueLabel.getText())); //Remove the queue from the ArrayList listaCode
			panelQueues.remove(queueLabel); //Remove label from the panelQueues
			panelQueues.repaint(); //Repaint panelQueues
			panelQueues.revalidate(); //Revalidate panelQueues
		
		}
		
	}
	
	/**
	 * Update the passed flight in the database
	 * @param v Old flight instance
	 * @param nomeCompagnia Company name of the updated flight
	 * @param data Take off date of the updated flight
	 * @param gate Gate where the updated flight's embark takes place
	 */
	public void editFlight(Volo v, String nomeCompagnia, Date data, int gate) {
		
		//Get company class from it's name
		CompagniaAerea compagnia = null;
		for(CompagniaAerea c : listaCompagnie) {
			if(c.getNome().equals(nomeCompagnia)) {
				compagnia = c;
				break;
			}
		}
		
		//Calculate the range of the slot
		Calendar c = Calendar.getInstance(); //Create a calendar instance
		c.setTime(data); //Set the calendar time to the passed date
		
		//Get lower range
		c.add(Calendar.MINUTE, -5);
		Date inizioTempoStimato = new Date();
		inizioTempoStimato = c.getTime();
		
		//Get higher range
		c.add(Calendar.MINUTE, 15);
		Date fineTempoStimato = new Date();
		fineTempoStimato = c.getTime();
		
		//Create queue list
		ArrayList<Coda> list = new ArrayList<Coda>();
		for(String s : listaCode) {
			Coda coda = new Coda();
			coda.setPersoneInCoda(0);
			coda.setTipo(s);
			list.add(coda);
		}
		
		//Check if there is at least one queue
		if(list.size() == 0) {
			mainFrame.createNotificationFrame("Seleziona almeno una coda!");
			return;
		}
		
		//Create gate
		Gate g = new Gate();
		g.setListaCode(list);
		g.setNumeroGate(gate);
		
		//Create slot
		Slot s = new Slot();
		s.setInizioTempoStimato(inizioTempoStimato);
		s.setFineTempoStimato(fineTempoStimato);
		
		//Check if the gate at that slot is available
		if(mainController.checkIfSlotIsTaken(s, gate, v)) {
			mainFrame.createNotificationFrame("Il gate selezionato non e' disponibile a quell'ora!");
			return;
		}
		
		//Create edited flight
		Volo editedVolo = new Volo();
		editedVolo.setID(v.getID());
		editedVolo.setCompagnia(compagnia);
		editedVolo.setGate(g);
		editedVolo.setNumeroPrenotazioni(0);
		editedVolo.setOrarioDecollo(data);
		editedVolo.setPartito(false);
		editedVolo.setSlot(s);
		
		//Update in the database
		VoloDAO dao = new VoloDAO();
		dao.updateFlight(mainFrame, v, editedVolo);
		
	}

}
