import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SpinnerNumberModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SearchPanel extends JPanel {
	
	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	
	private JPanel archiveOnlyPanel; //Panel that shows additional options if looking at an archive
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //Date format
	
	//Research elements
	private JTextField idField;
	private JSpinner spinnerGate;
	private JCheckBox chckbxAirFrance;
	private JCheckBox chckbxAlitalia;
	private JCheckBox chckbxEasyJet;
	private JCheckBox chckbxRyanair;
	private JSpinner spinnerTimeLower;
	private JSpinner spinnerTimeHigher;
	private JCheckBox chckbxCancelled;
	private JCheckBox chckbxDelayed;
	private JCheckBox chckbxInTime;

	/**
	 * Panel where the user can make researches to be shown in the checkFlightsPanel
	 * @param mc Link to the mainController
	 * @param mf Link to the mainFrame
	 */
	public SearchPanel(MainController mc, MainFrame mf) {
		
		mainFrame = mf; //Set the main frame
		mainController = mc; //Set the main controller
		
		setSize(new Dimension(294, 401)); //Set size
		setLayout(null); //Set layout
		
		idField = new JTextField(); //Create text field
		idField.setName("idField"); //Set name
		idField.setBounds(34, 60, 120, 20); //Set bounds
		//Limit text length to 8
		idField.addKeyListener(new KeyAdapter() {
	        //When a key is typed
			public void keyTyped(KeyEvent e) {
	            if(idField.getText().length() >= 8) { // limit to 8
	                e.consume(); //Consume key press event
	            }
	        }
	    });
		add(idField); //Add text field
		
		spinnerGate = new JSpinner(); //Create spinner
		spinnerGate.setName("spinnerGate"); //Set name
		spinnerGate.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1))); //Set spinner name
		spinnerGate.setEnabled(false); //Set enabled to false (not activated)
		spinnerGate.setBounds(71, 85, 49, 24); //Set bounds
		add(spinnerGate); //Add spinner
		
		JCheckBox chckbxGate = new JCheckBox("Gate"); //Create check box
		chckbxGate.setName("chckbxGate"); //Set name
		//Toggle gate spinner
		chckbxGate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spinnerGate.setEnabled(chckbxGate.isSelected()); //Set the spinner gate as enabled
			}
		});
		chckbxGate.setBounds(10, 86, 60, 23); //Set bounds
		add(chckbxGate); //Add the check box
		
		chckbxAirFrance = new JCheckBox("AirFrance"); //Create check box
		chckbxAirFrance.setName("chckbxAirFrance"); //Set name
		chckbxAirFrance.setSelected(true); //Set selected to true
		chckbxAirFrance.setBounds(10, 190, 120, 23); //Set bounds
		add(chckbxAirFrance); //Add check box
		
		chckbxAlitalia = new JCheckBox("Alitalia"); //Create check box
		chckbxAlitalia.setName("chckbxAlitalia"); //Set name
		chckbxAlitalia.setSelected(true); //Set selected to true
		chckbxAlitalia.setBounds(10, 212, 120, 23); //Set bounds
		add(chckbxAlitalia); //Add check box
		
		chckbxEasyJet = new JCheckBox("EasyJet"); //Create check box
		chckbxEasyJet.setName("chckbxEasyJet"); //Set name
		chckbxEasyJet.setSelected(true); //Set selected to true
		chckbxEasyJet.setBounds(10, 234, 120, 23); //Set bounds
		add(chckbxEasyJet); //Add check box
		
		chckbxRyanair = new JCheckBox("Ryanair"); //Create check box
		chckbxRyanair.setName("chckbxRyanair"); //Set name
		chckbxRyanair.setSelected(true); //Set selected to true
		chckbxRyanair.setBounds(10, 256, 120, 23); //Set bounds
		add(chckbxRyanair); //Add check box
		
		JLabel lblId = new JLabel("ID"); //Create label
		lblId.setName("lblId"); //Set name
		lblId.setBounds(15, 62, 18, 14); //Set bounds
		add(lblId); //Add label
		
		spinnerTimeLower = new JSpinner(); //Create spinner
		spinnerTimeLower.setName("spinnerTimeLower"); //Set name
		spinnerTimeLower.setEnabled(false); //Set enabled to false (not activated)
		spinnerTimeLower.setBounds(37, 123, 104, 20); //Set bounds
		spinnerTimeLower.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set spinner model
		add(spinnerTimeLower); //Add spinner
		
		spinnerTimeHigher = new JSpinner(); //Create spinner
		spinnerTimeHigher.setName("spinnerTimeHigher"); //Set name
		spinnerTimeHigher.setEnabled(false); //Set enabled to false (not activated)
		spinnerTimeHigher.setBounds(167, 123, 104, 20); //Set bounds
		spinnerTimeHigher.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set spinner model
		add(spinnerTimeHigher); //Add spinner
		
		JCheckBox chckbxTime = new JCheckBox(""); //Create check box
		chckbxTime.setName("chckbxTime"); //Set name
		//Toggle spinnerTimeLower and spinnerTimeHigher
		chckbxTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean selected = chckbxTime.isSelected(); //Get check box value
				spinnerTimeLower.setEnabled(selected); //Set the spinnerTimeLower as enabled
				spinnerTimeHigher.setEnabled(selected); ////Set the spinnerTimeLower as enabled
			}
		});
		chckbxTime.setBounds(10, 120, 21, 23); //Set bounds
		add(chckbxTime); //Add check box
		
		JButton buttonSearch = new JButton("Cerca"); //Create search button
		buttonSearch.setName("buttonSearch"); //Set name
		buttonSearch.setBounds(167, 341, 104, 34); //Set bounds
		add(buttonSearch); //Add search button
		
		JLabel lblCompanies = new JLabel("Compagnie:"); //Create label
		lblCompanies.setName("lblCompanies"); //Set name
		lblCompanies.setFont(new Font("Tahoma", Font.BOLD, 14)); //Set font
		lblCompanies.setBounds(10, 162, 91, 21); //Set bounds
		add(lblCompanies); //Add label
		
		JLabel lblSearch = new JLabel("Ricerca"); //Create label
		lblSearch.setName("lblSearch"); //Set name
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 18)); //Set font
		lblSearch.setHorizontalAlignment(SwingConstants.CENTER); //Set label's horizontal's alignment
		lblSearch.setBounds(10, 11, 274, 34); //Set bounds
		add(lblSearch); //Add label
		
		archiveOnlyPanel = new JPanel(); //Create new panel
		archiveOnlyPanel.setName("archiveOnlyPanel"); //Set name
		archiveOnlyPanel.setBounds(0, 286, 161, 98); //Set bounds
		add(archiveOnlyPanel); //Add panel
		archiveOnlyPanel.setLayout(null); //Set panel's layout
		toggleArchiveOnlyPanel(mainFrame.isLookingAtArchive()); //Check (based on mainFrame's lookingAtArchive booleans) if the panel has to be shown or not
		
		JLabel lblInclude = new JLabel("Includi:"); //Create label
		lblInclude.setName("lblInclude"); //Set name
		lblInclude.setFont(new Font("Tahoma", Font.BOLD, 14)); //Set font
		lblInclude.setBounds(10, 0, 91, 21); //Set bounds
		archiveOnlyPanel.add(lblInclude); //Add label
		
		chckbxCancelled = new JCheckBox("Voli cancellati"); //Create check box
		chckbxCancelled.setName("chckbxCancelled"); //Set name
		chckbxCancelled.setSelected(true); //Set selected to true
		chckbxCancelled.setBounds(10, 22, 145, 21); //Set bounds
		archiveOnlyPanel.add(chckbxCancelled); //Add check box to the archiveOnlyPanel
		
		chckbxDelayed = new JCheckBox("Voli partiti in ritardo"); //Create check box
		chckbxDelayed.setName("chckbxDelayed"); //Set name
		chckbxDelayed.setSelected(true); //Set selected to true
		chckbxDelayed.setBounds(10, 46, 145, 21); //Set bounds
		archiveOnlyPanel.add(chckbxDelayed); //Add check box to the archiveOnlyPanel
		
		chckbxInTime = new JCheckBox("Voli partiti in orario"); //Create check box
		chckbxInTime.setName("chckbxInTime"); //Set name
		chckbxInTime.setSelected(true); //Set selected to true
		chckbxInTime.setBounds(10, 70, 145, 21); //Set bounds
		archiveOnlyPanel.add(chckbxInTime); //Add check box to the archiveOnlyPanel
		
		//Add mouse listener to the button search
		buttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeSearch();
			}
		});
		
	}
	
	/**
	 * Gather all of the component's data and do a research in the database
	 */
	public void makeSearch() {
		
		//Gather data
		String idFieldString = idField.getText();
		int gateNumber = (spinnerGate.isEnabled())? (int)spinnerGate.getValue() : -1; //If the spinner is enabled get it's value, otherwise set gateNumber as -1
		Date dateStart = (spinnerTimeLower.isEnabled())? (Date)spinnerTimeLower.getValue() : null; //If the spinner is enabled get it's value, otherwise set dateStart as null
		Date dateEnd = (spinnerTimeHigher.isEnabled())? (Date)spinnerTimeHigher.getValue() : null; //If the spinner is enabled get it's value, otherwise set dateEnd as null
		boolean airFrance = chckbxAirFrance.isSelected();
		boolean alitalia = chckbxAlitalia.isSelected();
		boolean easyJet = chckbxEasyJet.isSelected();
		boolean ryanair = chckbxRyanair.isSelected();
		boolean cancelled = chckbxCancelled.isSelected();
		boolean delayed = chckbxDelayed.isSelected();
		boolean inTime = chckbxInTime.isSelected();
		
		//Update flight list in the main frame
		mainFrame.setFlightList(mainFrame.searchFlights(idFieldString, gateNumber, dateStart, dateEnd, airFrance, alitalia, easyJet, ryanair, cancelled, delayed, inTime));
		//Update the panel
		mainFrame.redrawCheckFlightsPanel();
		
	}
	
	/**
	 * Toggle between showing and not showing the archiveOnlyPanel
	 * @param active If the panel should be shown or not
	 */
	public void toggleArchiveOnlyPanel(boolean active) {
		archiveOnlyPanel.show(active);
	}

}
