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

public class SearchPanel extends JPanel {
	
	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	
	private JPanel archiveOnlyPanel; //Panel that shows additional options if looking at an archive
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	//Research elements
	JTextField idField;
	JSpinner spinnerGate;
	JCheckBox chckbxAirFrance;
	JCheckBox chckbxAlitalia;
	JCheckBox chckbxEasyJet;
	JCheckBox chckbxRyanair;
	JSpinner spinnerTimeLower;
	JSpinner spinnerTimeHigher;
	JCheckBox chckbxCancelled;
	JCheckBox chckbxDelayed;
	JCheckBox chckbxInTime;

	public SearchPanel(MainController mc, MainFrame mf) {
		
		mainFrame = mf; //Set the main frame
		mainController = mc; //Set the main controller
		
		setSize(new Dimension(294, 401)); //Set size
		setLayout(null); //Set layout
		
		idField = new JTextField();
		idField.setName("idField");
		idField.setBounds(34, 60, 120, 20);
		//Limit text lenght to 8
		idField.addKeyListener(new KeyAdapter() {
	        public void keyTyped(KeyEvent e) {
	            if(idField.getText().length() >= 8) { // limit to 8
	                e.consume(); //Consume key press event
	            }
	        }
	    });
		add(idField);
		idField.setColumns(10);
		
		spinnerGate = new JSpinner();
		spinnerGate.setName("spinnerGate");
		spinnerGate.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		spinnerGate.setEnabled(false);
		spinnerGate.setBounds(71, 85, 49, 24);
		add(spinnerGate);
		
		JCheckBox chckbxGate = new JCheckBox("Gate");
		chckbxGate.setName("chckbxGate");
		//Toggle gate spinner
		chckbxGate.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				spinnerGate.setEnabled(chckbxGate.isSelected());
			}
		});
		chckbxGate.setBounds(10, 86, 60, 23);
		add(chckbxGate);
		
		chckbxAirFrance = new JCheckBox("AirFrance");
		chckbxAirFrance.setName("chckbxAirFrance");
		chckbxAirFrance.setSelected(true);
		chckbxAirFrance.setBounds(10, 190, 120, 23);
		add(chckbxAirFrance);
		
		chckbxAlitalia = new JCheckBox("Alitalia");
		chckbxAlitalia.setName("chckbxAlitalia");
		chckbxAlitalia.setSelected(true);
		chckbxAlitalia.setBounds(10, 212, 120, 23);
		add(chckbxAlitalia);
		
		chckbxEasyJet = new JCheckBox("EasyJet");
		chckbxEasyJet.setName("chckbxEasyJet");
		chckbxEasyJet.setSelected(true);
		chckbxEasyJet.setBounds(10, 234, 120, 23);
		add(chckbxEasyJet);
		
		chckbxRyanair = new JCheckBox("Ryanair");
		chckbxRyanair.setName("chckbxRyanair");
		chckbxRyanair.setSelected(true);
		chckbxRyanair.setBounds(10, 256, 120, 23);
		add(chckbxRyanair);
		
		JLabel lblId = new JLabel("ID");
		lblId.setName("lblId");
		lblId.setBounds(15, 62, 18, 14);
		add(lblId);
		
		spinnerTimeLower = new JSpinner();
		spinnerTimeLower.setName("spinnerTimeLower");
		spinnerTimeLower.setEnabled(false);
		spinnerTimeLower.setBounds(37, 123, 104, 20);
		spinnerTimeLower.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set spinner model
		add(spinnerTimeLower);
		
		spinnerTimeHigher = new JSpinner();
		spinnerTimeHigher.setName("spinnerTimeHigher");
		spinnerTimeHigher.setEnabled(false);
		spinnerTimeHigher.setBounds(167, 123, 104, 20);
		spinnerTimeHigher.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set spinner model
		add(spinnerTimeHigher);
		
		JLabel lblTimeSeparator = new JLabel("-");
		lblTimeSeparator.setName("lblTimeSeparator");
		lblTimeSeparator.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeSeparator.setBounds(145, 125, 14, 14);
		add(lblTimeSeparator);
		
		JCheckBox chckbxTime = new JCheckBox("");
		chckbxTime.setName("chckbxTime");
		//Toggle spinnerTimeLower and spinnerTimeHigher
		chckbxTime.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				boolean selected = chckbxTime.isSelected();
				spinnerTimeLower.setEnabled(selected);
				spinnerTimeHigher.setEnabled(selected);
			}
		});
		chckbxTime.setBounds(10, 120, 21, 23);
		add(chckbxTime);
		
		JButton buttonSearch = new JButton("Cerca");
		buttonSearch.setName("buttonSearch");
		buttonSearch.setBounds(167, 341, 104, 34);
		add(buttonSearch);
		
		JLabel lblCompanies = new JLabel("Compagnie:");
		lblCompanies.setName("lblCompanies");
		lblCompanies.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCompanies.setBounds(10, 162, 91, 21);
		add(lblCompanies);
		
		JLabel lblSearch = new JLabel("Ricerca");
		lblSearch.setName("lblSearch");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSearch.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearch.setBounds(10, 11, 274, 34);
		add(lblSearch);
		
		archiveOnlyPanel = new JPanel();
		archiveOnlyPanel.setName("archiveOnlyPanel");
		archiveOnlyPanel.setBounds(0, 286, 161, 98);
		add(archiveOnlyPanel);
		archiveOnlyPanel.setLayout(null);
		toggleArchiveOnlyPanel(mainFrame.isLookingAtArchive());
		
		JLabel lblInclude = new JLabel("Includi:");
		lblInclude.setName("lblInclude");
		lblInclude.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblInclude.setBounds(10, 0, 91, 21);
		archiveOnlyPanel.add(lblInclude);
		
		chckbxCancelled = new JCheckBox("Voli cancellati");
		chckbxCancelled.setName("chckbxCancelled");
		chckbxCancelled.setSelected(true);
		chckbxCancelled.setBounds(10, 22, 145, 21);
		archiveOnlyPanel.add(chckbxCancelled);
		
		chckbxDelayed = new JCheckBox("Voli partiti in ritardo");
		chckbxDelayed.setName("chckbxDelayed");
		chckbxDelayed.setSelected(true);
		chckbxDelayed.setBounds(10, 46, 145, 21);
		archiveOnlyPanel.add(chckbxDelayed);
		
		chckbxInTime = new JCheckBox("Voli partiti in orario");
		chckbxInTime.setName("chckbxInTime");
		chckbxInTime.setSelected(true);
		chckbxInTime.setBounds(10, 70, 145, 21);
		archiveOnlyPanel.add(chckbxInTime);
		
		buttonSearch.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				makeSearch();
				
			}
		});
		
	}
	
	public void makeSearch() {
		
		//Gather info
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
	
	public void toggleArchiveOnlyPanel(boolean active) {
		archiveOnlyPanel.show(active);
	}

}
