import java.awt.BasicStroke;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.LineBorder;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SpinnerNumberModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class SearchPanel extends JPanel {
	
	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
		
	//Research elements
	private JTextField idField;
	private JTextField destinationField;
	private CustomSpinner spinnerGate;
	private CustomCheckBox chckbxAirFrance;
	private CustomCheckBox chckbxAlitalia;
	private CustomCheckBox chckbxEasyJet;
	private CustomCheckBox chckbxRyanair;
	private CustomSpinner spinnerTimeLower;
	private CustomSpinner spinnerTimeHigher;
	private CustomCheckBox chckbxCancelled;
	private CustomCheckBox chckbxDelayed;
	private CustomCheckBox chckbxInTime;

	/**
	 * Panel where the user can make researches to be shown in the checkFlightsPanel
	 * @param mc Link to the mainController
	 * @param mf Link to the mainFrame
	 */
	@SuppressWarnings("deprecation")
	public SearchPanel(MainController mc, MainFrame mf) {
		
		mainFrame = mf; //Set the main frame
		mainController = mc; //Set the main controller
		
		setSize(new Dimension(294, 401)); //Set size
		setLayout(null); //Set layout
		
		idField = new JTextField(); //Create text field
		idField.setBackground(MainController.backgroundColorOne);
		idField.setBorder(new LineBorder(MainController.foregroundColorThree, 2));
		idField.setFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 12));
		idField.setHorizontalAlignment(JTextField.TRAILING);
		idField.setForeground(MainController.foregroundColorThree);
		idField.setName("idField"); //Set name
		idField.setBounds(38, 53, 122, 20); //Set bounds
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
		
		destinationField = new JTextField(); //Create text field
		destinationField.setBackground(MainController.backgroundColorOne);
		destinationField.setBorder(new LineBorder(MainController.foregroundColorThree, 2));
		destinationField.setFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 12));
		destinationField.setHorizontalAlignment(JTextField.TRAILING);
		destinationField.setForeground(MainController.foregroundColorThree);
		destinationField.setName("destinationField"); //Set name
		destinationField.setBounds(115, 98, 122, 20); //Set bounds
		add(destinationField); //Add text field
		
		spinnerGate = new CustomSpinner(MainController.backgroundColorOne, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); //Create spinner
		spinnerGate.setName("spinnerGate"); //Set name
		spinnerGate.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(MainController.gateAirportNumber), new Integer(1))); //Set spinner name
		spinnerGate.setEnabled(false); //Set enabled to false (not activated)
		spinnerGate.setBounds(73, 145, 49, 24); //Set bounds
		spinnerGate.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerGate.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerGate.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 12));
		add(spinnerGate); //Add spinner
		
		CustomCheckBox chckbxGate = new CustomCheckBox("Gate", null, 14, MainController.foregroundColorThree, false, null, 0); //Create check box
		chckbxGate.setName("chckbxGate"); //Set name
		//Toggle gate spinner
		chckbxGate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spinnerGate.setEnabled(!chckbxGate.isSelected()); //Set the spinner gate as enabled (not(!) because this happens before the action listener inside the CustomCheckBox gets called)
			}
		});
		chckbxGate.setBounds(10, 145, 60, 23); //Set bounds
		add(chckbxGate); //Add the check box
		
		chckbxAirFrance = new CustomCheckBox("AirFrance", null, 14, MainController.foregroundColorThree, false, null, 0); //Create check box
		chckbxAirFrance.setName("chckbxAirFrance"); //Set name
		chckbxAirFrance.setSelected(true); //Set selected to true
		chckbxAirFrance.setBounds(10, 255, 120, 23); //Set bounds
		add(chckbxAirFrance); //Add check box
		
		chckbxAlitalia = new CustomCheckBox("Alitalia", null, 14, MainController.foregroundColorThree, false, null, 0); //Create check box
		chckbxAlitalia.setName("chckbxAlitalia"); //Set name
		chckbxAlitalia.setSelected(true); //Set selected to true
		chckbxAlitalia.setBounds(10, 277, 120, 23); //Set bounds
		add(chckbxAlitalia); //Add check box
		
		chckbxEasyJet = new CustomCheckBox("EasyJet", null, 14, MainController.foregroundColorThree, false, null, 0); //Create check box
		chckbxEasyJet.setName("chckbxEasyJet"); //Set name
		chckbxEasyJet.setSelected(true); //Set selected to true
		chckbxEasyJet.setBounds(10, 299, 120, 23); //Set bounds
		add(chckbxEasyJet); //Add check box
		
		chckbxRyanair = new CustomCheckBox("Ryanair", null, 14, MainController.foregroundColorThree, false, null, 0); //Create check box
		chckbxRyanair.setName("chckbxRyanair"); //Set name
		chckbxRyanair.setSelected(true); //Set selected to true
		chckbxRyanair.setBounds(10, 321, 120, 23); //Set bounds
		add(chckbxRyanair); //Add check box
		
		spinnerTimeLower = new CustomSpinner(MainController.backgroundColorOne, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); //Create spinner
		spinnerTimeLower.setName("spinnerTimeLower"); //Set name
		spinnerTimeLower.setEnabled(false); //Set enabled to false (not activated)
		spinnerTimeLower.setBounds(37, 192, 104, 24); //Set bounds
		spinnerTimeLower.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set spinner model
		spinnerTimeLower.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerTimeLower.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerTimeLower.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 10));
		add(spinnerTimeLower); //Add spinner
		
		spinnerTimeHigher = new CustomSpinner(MainController.backgroundColorOne, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); //Create spinner
		spinnerTimeHigher.setName("spinnerTimeHigher"); //Set name
		spinnerTimeHigher.setEnabled(false); //Set enabled to false (not activated)
		spinnerTimeHigher.setBounds(167, 192, 104, 24); //Set bounds
		spinnerTimeHigher.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set spinner model
		spinnerTimeHigher.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerTimeHigher.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerTimeHigher.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 10));
		add(spinnerTimeHigher); //Add spinner
		
		CustomCheckBox chckbxTime = new CustomCheckBox("", null, 14, MainController.foregroundColorThree, false, null, 0); //Create check box
		chckbxTime.setName("chckbxTime"); //Set name
		//Toggle spinnerTimeLower and spinnerTimeHigher
		chckbxTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean selected = !chckbxTime.isSelected(); //Get check box value (not(!) because this happens before the action listener inside the CustomCheckBox gets called)
				spinnerTimeLower.setEnabled(selected); //Set the spinnerTimeLower as enabled
				spinnerTimeHigher.setEnabled(selected); ////Set the spinnerTimeLower as enabled
			}
		});
		chckbxTime.setBounds(10, 192, 21, 23); //Set bounds
		add(chckbxTime); //Add check box
		
		CustomButton buttonSearch = new CustomButton("Cerca", null, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 21, true, MainController.foregroundColorThree, 2); //Create search button
		buttonSearch.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonSearch.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonSearch.unselectAnimation(8);
			}
		});
		buttonSearch.setName("buttonSearch"); //Set name
		buttonSearch.setBounds(167, 343, 104, 42); //Set bounds
		add(buttonSearch); //Add search button
		
		chckbxCancelled = new CustomCheckBox("Voli cancellati", null, 13, MainController.foregroundColorThree, false, null, 0); //Create check box
		chckbxCancelled.setName("chckbxCancelled"); //Set name
		chckbxCancelled.setSelected(true); //Set selected to true
		chckbxCancelled.setBounds(132, 255, 145, 21); //Set bounds
		add(chckbxCancelled); //Add check box to the archiveOnlyPanel
		
		chckbxDelayed = new CustomCheckBox("Voli partiti in ritardo", null, 12, MainController.foregroundColorThree, false, null, 0); //Create check box
		chckbxDelayed.setName("chckbxDelayed"); //Set name
		chckbxDelayed.setSelected(true); //Set selected to true
		chckbxDelayed.setBounds(132, 277, 145, 21); //Set bounds
		add(chckbxDelayed); //Add check box to the archiveOnlyPanel
		
		chckbxInTime = new CustomCheckBox("Voli partiti in orario", null, 13, MainController.foregroundColorThree, false, null, 0); //Create check box
		chckbxInTime.setName("chckbxInTime"); //Set name
		chckbxInTime.setSelected(true); //Set selected to true
		chckbxInTime.setBounds(132, 299, 145, 21); //Set bounds
		add(chckbxInTime); //Add check box to the archiveOnlyPanel
		
		toggleArchiveOnlyCheckBoxes(mainFrame.isLookingAtArchive()); //Check (based on mainFrame's lookingAtArchive booleans) if the archive only check boxes have to be shown
		
		//Add mouse listener to the button search
		buttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread queryThread = new Thread() {
				      public void run() {
				    	  searchFlights();
				      }
				};
			    queryThread.start();
			}
		});
		
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g); //Paint the component normally first
		
		Graphics2D g2d = (Graphics2D)g;
		
		//AA
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    //Text AA
	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    
	    //Draw background
	    GradientPaint gPaint = new GradientPaint(getWidth()/2, 0, MainController.backgroundColorTwo, getWidth()/2, getHeight(), MainController.backgroundColorOne);
	    g2d.setPaint(gPaint);
	    g2d.fillRect(0, 0, getWidth(), getHeight());
	    
	    //Draw string
	    g2d.setColor(MainController.foregroundColorThree);
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 24));
	    String s = "Ricerca voli";
	    int sLength = g2d.getFontMetrics().stringWidth(s);
	    g2d.drawString(s, (getWidth()/2) - (sLength/2), 28);
	    
	    //Draw time separator
	    g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(148, 202, 159, 202);
	    
	    //Draw labels
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 14));
	    g2d.drawString("ID", 15, 68);
	    g2d.drawString("Destinazione", 15, 113);
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 18));
	    g2d.drawString("Compagnie", 15, 250);
    	if(mainFrame.isLookingAtArchive()) {
    		g2d.drawString("Includi", 135, 250);
    		//Draw line separating archive only check boxes
    		g2d.setStroke(new BasicStroke(2));
    		g2d.drawLine(125, 237, 125, 345);
    	}
	    
	}
	
	/**
	 * Gather all of the component's data and do a research in the database
	 */
	public void searchFlights() {
		
		//Gather data
		String idFieldString = idField.getText();
		String destinationFieldString = destinationField.getText();
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
		mainFrame.setFlightList(mainFrame.searchFlights(idFieldString, destinationFieldString, gateNumber, dateStart, dateEnd, airFrance, alitalia, easyJet, ryanair, cancelled, delayed, inTime));
		//Update the flight list panel
		mainFrame.redrawCheckFlightsPanel();
		
	}
	
	/**
	 * Toggle between showing and not showing the archive only check boxes
	 * @param active If the archive only check boxes should be shown or not
	 */
	@SuppressWarnings("deprecation")
	public void toggleArchiveOnlyCheckBoxes(boolean active) {
		chckbxCancelled.show(active);
		chckbxDelayed.show(active);
		chckbxInTime.show(active);
	}
	
}
