import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
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
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateFlightPanel extends JPanel {

	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	private ArrayList<CompagniaAerea> listaCompagnie = new ArrayList<CompagniaAerea>(); //Array containing the companies
	private int queueNumber = 0; //How many queues have been added
	private ArrayList<String> listaCode = new ArrayList<String>();
	
	private JPanel panelQueues;
	
	public CreateFlightPanel(Rectangle bounds, MainFrame mf, MainController c) {
		
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
		
		//Debug population (Replace with a DAO)
		for(int i = 1; i < 6; i++) {
			CompagniaAerea compagnia = new CompagniaAerea();
			compagnia.setNome("Compagnia numero " + i);
			listaCompagnie.add(compagnia);
		}
		
		JComboBox cBoxCompany = new JComboBox(); //Combo box of the companies
		cBoxCompany.setName("cBoxCompany"); //Name component
		cBoxCompany.setBounds(389, 80, 148, 22); //Set bounds
		add(cBoxCompany); //Add to panel
		//Populate combo box
		for(CompagniaAerea comp : listaCompagnie) {
			//Add each member of the array listaCompagnie to the combobox
			cBoxCompany.addItem(comp.getNome());
		}
		
		JLabel lblInsertTakeOffTime = new JLabel("Inserisci data e orario partenza:"); //Create label insert take off time
		lblInsertTakeOffTime.setName("lblInsertTakeOffTime"); //Name component
		lblInsertTakeOffTime.setHorizontalAlignment(SwingConstants.TRAILING); //Set text h alignment to trailing
		lblInsertTakeOffTime.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set text font
		lblInsertTakeOffTime.setBounds(64, 130, 315, 34); //Set bounds
		add(lblInsertTakeOffTime); //Add to panel
		
		JSpinner spinnerTakeOffTime = new JSpinner(); //Create spinner to declare the take off time
		spinnerTakeOffTime.setName("spinnerTakeOffTime"); //Name component
		spinnerTakeOffTime.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set the type of spinner
		spinnerTakeOffTime.setFont(new Font("Tahoma", Font.BOLD, 11)); //Set text font
		spinnerTakeOffTime.setBounds(389, 140, 148, 20); //Set bounds
		add(spinnerTakeOffTime); //Add to panel
		
		JLabel lblInsertGate = new JLabel("Inserisci gate:"); //Create label insert gate
		lblInsertGate.setName("lblInsertGate"); //Name component
		lblInsertGate.setHorizontalAlignment(SwingConstants.TRAILING); //Set text h alignment to trailing
		lblInsertGate.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set text font
		lblInsertGate.setBounds(64, 190, 315, 34); //Set bounds
		add(lblInsertGate); //Add to panel
		
		JSpinner spinnerGate = new JSpinner(); //Create spinner to declare the gate
		spinnerGate.setName("spinnerGate"); //Name component
		spinnerGate.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1))); //Set the type of spinner
		spinnerGate.setBounds(389, 200, 148, 20); //Set bounds
		add(spinnerGate); //Add to panel
		
		JButton buttonCreateFlight = new JButton("Crea nuovo volo"); //Create button create flight
		buttonCreateFlight.setName("buttonCreateFlight"); //Name component
		buttonCreateFlight.setFont(new Font("Tahoma", Font.BOLD, 17)); //Set text font
		buttonCreateFlight.setBounds(243, 522, 294, 58); //Set bounds
		add(buttonCreateFlight); //Add to panel
		
		JLabel lblQueues = new JLabel("Code inserite: (Clicca su una per rimuovere)"); //Create label queues
		lblQueues.setName("lblQueues"); //Name component
		lblQueues.setHorizontalAlignment(SwingConstants.CENTER); //Set text h alignment to center
		lblQueues.setFont(new Font("Tahoma", Font.BOLD, 14)); //Set text font
		lblQueues.setBounds(642, 71, 315, 34); //Set bounds
		add(lblQueues); //Add to panel
		
		JLabel lblAddQueue = new JLabel("+ Aggiungi coda"); //Create label insert queue
		lblAddQueue.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				createAddQueuePanel();
			}
		});
		lblAddQueue.setName("lblAddQueue"); //Name component
		lblAddQueue.setForeground(new Color(90, 90, 200, 255)); //Set color of the text
		lblAddQueue.setHorizontalAlignment(SwingConstants.TRAILING); //Set text h alignment to trailing
		lblAddQueue.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set text font
		lblAddQueue.setBounds(389, 250, 148, 34); //Set bounds
		add(lblAddQueue); //Add to panel
		
		panelQueues = new JPanel(); //Create new panel
		panelQueues.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null)); //Set border
		panelQueues.setBounds(642, 107, 315, 473); //Set bounds
		add(panelQueues); //Add to panel
		panelQueues.setLayout(new GridLayout(20, 0, 0, 0)); //Set panel's layout
		
		
		buttonCreateFlight.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			
				//Gather data
				String nomeCompagnia = (String)cBoxCompany.getSelectedItem();
				Date data = (Date)spinnerTakeOffTime.getValue();
				int gate = (int)spinnerGate.getValue();
				createFlight(nomeCompagnia, data, gate);
			
			}
		});
		
	}
	
	public void createAddQueuePanel() {
		
		AddQueuePanel frame = new AddQueuePanel(mainFrame); //Create a popup panel
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
				mainFrame.createNotificationFrame("Questa coda è già stata aggiunta!"); //Notify user
			}
		}
		
	}
	
	public void addQueue(String type) {
		
		listaCode.add(type); //Add the queue to the ArrayList listaCode
		
		JLabel lblAddedQueue = new JLabel(type); //Create label with the queue type chosen
		lblAddedQueue.setHorizontalAlignment(SwingConstants.CENTER); //Set text h alignment to center
		lblAddedQueue.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set text font
		lblAddedQueue.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				removeQueue(lblAddedQueue);
			}
		});
		panelQueues.add(lblAddedQueue); //Add label to the panelQueues panel
		panelQueues.repaint(); //Repaint panelQueues
		panelQueues.revalidate(); //Revalidate panelQueues
		
	}
	
	public void removeQueue(JLabel queueLabel) {
		
		ConfirmationFrame frame = new ConfirmationFrame("Sei sicuro di voler eliminare questa coda?", mainFrame); //Create confirmation frame
		frame.setVisible(true); //Set confirmation frame visible
		if(frame.getAnswer()) { //If the user has confirmed the action
			
			listaCode.remove(listaCode.indexOf(queueLabel.getText())); //Remove the queue from the ArrayList listaCode
			panelQueues.remove(queueLabel); //Remove label from the panelQueues
			panelQueues.repaint(); //Repaint panelQueues
			panelQueues.revalidate(); //Revalidate panelQueues
		
		}
		
	}
	
	public void createFlight(String nomeCompagnia, Date data, int gate) {
		
		//Get company class from it's name
		CompagniaAerea compagnia = null;
		for(CompagniaAerea c : listaCompagnie) {
			if(c.getNome().equals(nomeCompagnia)) {
				compagnia = c;
				break;
			}
		}
		
		System.out.println("Compagnia: " + compagnia.getNome() + " Data: " + data + " gate: " + gate + " Code: " + listaCode);
		
	}

}
