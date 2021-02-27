import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.text.DateFormatter;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.swing.JTable;

public class CreateFlightPanel extends JPanel {

	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	private ArrayList<String> listaCode = new ArrayList<String>(); //List of the queues
	
	//Company images
	private Image airfranceLogoImage;
	private Image alitaliaLogoImage;
	private Image easyjetLogoImage;
	private Image ryanairLogoImage;
	
	private Image currentLogoImage; //Currently used image
	
	CustomButton buttonAddQueue; //Button used to add queues (Declared here so it can be accessed by the remove and add queues methods)
	
	private int queueButtonDistance = 59; //Distance between the added queue buttons
	
	private int gatesNumber = 12; //How many gate there are in the airport
	
	/**
	 * Panel where the user can create a new flight to add to the database
	 * @param bounds Bounds of the contentPanel that contains this panel (to give it the contentPanel's dimensions)
	 * @param mf Link to the MainFrame
	 * @param c Link to the MainController
	 */
	public CreateFlightPanel(Rectangle bounds, MainFrame mf, MainController c) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		
		//Load company images
		try {                
			airfranceLogoImage = ImageIO.read(new File("imgs/company-logos/airfrance-logo.png"));
			alitaliaLogoImage = ImageIO.read(new File("imgs/company-logos/alitalia-logo.png"));
			easyjetLogoImage = ImageIO.read(new File("imgs/company-logos/easyjet-logo.png"));
			ryanairLogoImage = ImageIO.read(new File("imgs/company-logos/ryanair-logo.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
		
		//Scale company images
		airfranceLogoImage = airfranceLogoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
		alitaliaLogoImage = alitaliaLogoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
		easyjetLogoImage = easyjetLogoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
		ryanairLogoImage = ryanairLogoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
		
		//setBounds(bounds);
		setBounds(72, 2, 1124, 666); //To have a preview in design, replace with the row above
		setLayout(null); //Set panel layout to absolute
		
		CustomComboBox cBoxCompany = mainFrame.createCustomComboBox(); //Combo box of the companies
		cBoxCompany.setName("cBoxCompany"); //Name component
		cBoxCompany.setBounds(35, 490, 250, 24); //Set bounds
		cBoxCompany.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Update current company image
				switch(cBoxCompany.getSelectedItem().toString()) {
					case "AirFrance": currentLogoImage = airfranceLogoImage; break;
					case "Alitalia": currentLogoImage = alitaliaLogoImage; break;
					case "EasyJet": currentLogoImage = easyjetLogoImage; break;
					case "Ryanair": currentLogoImage = ryanairLogoImage; break;
					default: currentLogoImage = airfranceLogoImage;
				}
				repaint();
			}
		});
		add(cBoxCompany); //Add to panel
		//Populate combo box
		for(CompagniaAerea comp : mainFrame.getListaCompagnie()) {
			//Add each member of the array listaCompagnie to the combo box
			cBoxCompany.addItem(comp.getNome());
		}
		
		CustomSpinner spinnerTakeOffDate = new CustomSpinner(MainController.backgroundColorOne, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); //Create spinner to declare the take off date
		spinnerTakeOffDate.setModel(new SpinnerDateModel(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()), null, null, Calendar.DAY_OF_YEAR)); //Set spinner model
		spinnerTakeOffDate.setName("spinnerTakeOffDate"); //Name component
		spinnerTakeOffDate.setBounds(310, 231, 150, 24); //Set bounds
		spinnerTakeOffDate.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerTakeOffDate.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerTakeOffDate.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 12));
		add(spinnerTakeOffDate); //Add to panel
		
		CustomSpinner spinnerGate = new CustomSpinner(MainController.backgroundColorOne, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); //Create spinner to declare the gate
		spinnerGate.setName("spinnerGate"); //Name component
		spinnerGate.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(gatesNumber), new Integer(1))); //Set the type of spinner
		spinnerGate.setBounds(310, 291, 150, 24); //Set bounds
		spinnerGate.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerGate.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerGate.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 12));
		add(spinnerGate); //Add to panel
		
		CustomComboBox cBoxDestination = mainFrame.createCustomComboBox();
		cBoxDestination.setName("cBoxDestination");
		cBoxDestination.setBounds(310, 351, 150, 24);
		add(cBoxDestination);
		
		populateDestinationComboBox(cBoxDestination);
		
		CustomButton buttonCreateFlight = new CustomButton("Crea volo!", null, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 22, true, MainController.foregroundColorThree, 1); //Create button create flight
		buttonCreateFlight.setName("buttonCreateFlight"); //Name component
		buttonCreateFlight.setBounds(349, 456, 300, 58); //Set bounds
		buttonCreateFlight.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonCreateFlight.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonCreateFlight.unselectAnimation(8);
			}
		});
		add(buttonCreateFlight); //Add to panel
		
		buttonAddQueue = (new CustomButton("", null, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
													null, 0, true, MainController.foregroundColorThree, 1) {
			
			public void paint(Graphics g) {
				
				super.paint(g); //Paint the component normally first
				
				Graphics2D g2d = (Graphics2D)g;
				
				//AA
			    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			    //Text AA
			    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				
				//Draw plus icon
				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
				g2d.drawLine(getWidth()/2, getHeight() - 4, getWidth()/2, 4);
				g2d.drawLine(getWidth() - 4, getHeight()/2, 4, getHeight()/2);
				
			}
			
		}); //Create button add queue
		buttonAddQueue.setName("buttonAddQueue"); //Name component
		buttonAddQueue.setBounds(720, 182, 25, 25); //Set bounds
		buttonAddQueue.addMouseListener(new MouseAdapter() {
			//Mouse clicked
			public void mouseClicked(MouseEvent e) {
				createAddQueueFrame();
			}
		});
		buttonAddQueue.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonAddQueue.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonAddQueue.unselectAnimation(8);
			}
		});
		add(buttonAddQueue); //Add to panel
		
		//Button action listener
		buttonCreateFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Gather data
				String nomeCompagnia = (String)cBoxCompany.getSelectedItem();
				Date data = (Date)spinnerTakeOffDate.getValue();
				int gate = (int)spinnerGate.getValue();
				String destinazione = (String)cBoxDestination.getSelectedItem();
				
				createFlight(nomeCompagnia, data, gate, destinazione); //Create flight with the gathered data
			}
		});
		
	}
	
	/**
	 * Create a frame prompting the user to choose a queue to add
	 */
	public void createAddQueueFrame() {
		
		AddQueueFrame frame = new AddQueueFrame(mainFrame, listaCode); //Create a popup panel
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
		
	/**
	 * Add a queue to the panelQueues
	 * @param type
	 */
	public void addQueue(String type) {
		
		int yOffset = queueButtonDistance * (listaCode.size());
		
		listaCode.add(type); //Add the queue to the ArrayList listaCode
		
		//Determine background color
		Color bgColor = (listaCode.size()%2 == 0)? MainController.backgroundColorTwo : null; //If the array size is not even, set a background color for the button
		
		//Create button for that queue
		CustomButton buttonAddedQueue = new CustomButton(type, bgColor, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 22, true, MainController.foregroundColorThree, 1); //Create button create flight
		buttonAddedQueue.setName("buttonAddedQueue" + (listaCode.size() - 1)); //Name component
		buttonAddedQueue.setText(type); //So that the type can be found and removed from the listaCode array
		buttonAddedQueue.setBounds(760, 170 + yOffset, 300, 50); //Set bounds
		buttonAddedQueue.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonAddedQueue.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonAddedQueue.unselectAnimation(8);
			}
		});
		buttonAddedQueue.addMouseListener(new MouseAdapter() {
			//When mouse clicked
			public void mouseClicked(MouseEvent e) {
				removeQueue(buttonAddedQueue);
			}
		});
		add(buttonAddedQueue);
		
		//Move add queue button
		buttonAddQueue.setBounds(buttonAddQueue.getBounds().x, buttonAddQueue.getBounds().y + queueButtonDistance, buttonAddQueue.getBounds().width, buttonAddQueue.getBounds().height);
		
		//Hide button if all the queues have been added
		if(listaCode.size() >= 6) {
			buttonAddQueue.show(false);
		}
		
		repaint();
		
	}

	/**
	 * Remove a queue from the panelQueues containing the queues
	 * @param queueLabel What label to remove from the panel
	 */
	public void removeQueue(CustomButton queueButton) {
		
		ConfirmationFrame frame = new ConfirmationFrame("Sei sicuro di voler rimuovere questa coda?", mainFrame); //Create confirmation frame
		frame.setVisible(true); //Set confirmation frame visible
		if(frame.getAnswer()) { //If the user has confirmed the action
			
			int buttonNumber = listaCode.indexOf(queueButton.getText());
			listaCode.remove(buttonNumber); //Remove the queue from the ArrayList listaCode
			remove(queueButton); //Remove label
			
			//Move add queue button
			buttonAddQueue.setBounds(buttonAddQueue.getBounds().x, buttonAddQueue.getBounds().y - queueButtonDistance, buttonAddQueue.getBounds().width, buttonAddQueue.getBounds().height);
			
			//Move added queues buttons after the removed one
			for(int i = buttonNumber + 1; i <= listaCode.size(); i++) {
				
				CustomButton b = (CustomButton)mainController.getComponentByName(this, "buttonAddedQueue" + String.valueOf(i)); //Get named button
				
				if(b != null) { //Button has been found
					b.setBounds(b.getBounds().x, b.getBounds().y - queueButtonDistance, b.getBounds().width, b.getBounds().height); //Move it upwards
					b.setName("buttonAddedQueue" + String.valueOf(i - 1)); //Change name
					Color bgColor = (i%2 == 0)? MainController.backgroundColorTwo : null; //If the button position is not even, set a background color for the button
					b.setButtonBackgroundColor(bgColor); //Update background color
				}
				
			}
			
			//Show button if a queue can still be added
			if(listaCode.size() < 6) {
				buttonAddQueue.show(true);
			}
			
			repaint(); //Repaint panelQueues
		
		}
		
	}
	
	/**
	 * Create a flight and add it to the database
	 * @param nomeCompagnia Company name of the flight
	 * @param data Take off date of the flight
	 * @param gate Gate where the flight's embark takes place
	 */
	public void createFlight(String nomeCompagnia, Date data, int gate, String destinazione) {
		
		//Get company class from it's name
		CompagniaAerea compagnia = null;
		for(CompagniaAerea c : mainFrame.getListaCompagnie()) {
			if(c.getNome().equals(nomeCompagnia)) {
				compagnia = c;
				break;
			}
		}
		
		//Generate ID
		String id = mainController.generateIDString(8);
		
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
		if(mainController.checkIfSlotIsTaken(s, gate, null)) {
			mainFrame.createNotificationFrame("Il gate selezionato non e' disponibile a quell'ora!");
			return;
		}
		
		//Create flight
		Volo v = new Volo();
		v.setID(id);
		v.setCompagnia(compagnia);
		compagnia.setNumeroVoli(compagnia.getNumeroVoli() + 1);
		v.setGate(g);
		v.setDestinazione(destinazione);
		v.setOrarioDecollo(data);
		v.setPartito(false);
		v.setSlot(s);
		
		//Calculate number of bookings
		int sum = 0;
		for(Coda coda : v.getGate().getListaCode()) {
			sum += coda.getPersoneInCoda();
		}
		v.setNumeroPrenotazioni(sum);
		
		//Insert in the database
		VoloDAO dao = new VoloDAO();
		dao.insertFlight(mainFrame, v);
		
		//Go to checkFlightsPanel
		if(mainFrame.setContentPanelToCheckFlightsPanel(false)) { //If the panel gets changed
			
			SearchPanel searchPanel = (SearchPanel)mainController.getComponentByName(mainFrame, "searchPanel"); //Get searchPanel from the mainFrame
			if(searchPanel != null) { //If the searchPanel gets found
				searchPanel.makeSearch(); //Make search
			}
			
			//Update minimum and max dates spinners
			searchPanel.setMinimumAndMaxDatesAndUpdateSpinners(new VoloDAO().getMinAndMaxTakeOffTime());
			
			//Update dash board
			searchPanel.toggleArchiveOnlyCheckBoxes(false);
			repaint();
			revalidate();
			
		}
		
	}

	public void populateDestinationComboBox(CustomComboBox box) {
		
		box.addItem("Londra");
		box.addItem("Roma");
		box.addItem("Barcellona");
		box.addItem("Parigi");
		box.addItem("Pechino");
		box.addItem("Il Cairo");
		box.addItem("Washington");
		box.addItem("Mosca");
		box.addItem("Belgrado");
		box.addItem("Dublino");
		box.addItem("Berlino");
		
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g); //Paint the component normally first
		
		Graphics2D g2d = (Graphics2D)g;
		
		//AA
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    //Text AA
	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    
	    //Draw background
	    g2d.setColor(MainController.backgroundColorOne);
	    g2d.fillRect(0, 0, getWidth(), getHeight());
	    
	    //Draw string
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 46));
	    g2d.setColor(MainController.foregroundColorThree);
	    g2d.drawString("Crea un nuovo volo", 40, 120);
		
	    //Draw company image
	    g2d.drawImage(currentLogoImage, 35, (getHeight()/2) - (currentLogoImage.getHeight(null)/2) - 30, 285, (getHeight()/2) - (currentLogoImage.getHeight(null)/2) + 250 - 30, 0, 0, currentLogoImage.getWidth(null), currentLogoImage.getHeight(null), this);
	    g2d.setStroke(new BasicStroke(4));
	    g2d.setColor(MainController.foregroundColorThree);
	    g2d.drawRoundRect(35, (getHeight()/2) - (currentLogoImage.getHeight(null)/2) - 30, 250, 250, 110, 110);
	    
	    //Company string
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 22));
	    String companyString = "Inserisci compagnia";
	    int companyStringLenght = g2d.getFontMetrics(g2d.getFont()).stringWidth(companyString);
	    g2d.drawString(companyString, 35 + (currentLogoImage.getWidth(null)/2) - (companyStringLenght/2), 468);
	    
	    //Spinner and combo boxes strings
	    g2d.drawString("Orario", 472, 251);
	    g2d.drawString("Gate", 472, 311);
	    g2d.drawString("Destinazione", 472, 371);
	    
	    //Draw separator line
	    g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
	    g2d.drawLine(700, 125, 700, 545);
	    
	    //Draw queue string
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 30));
	    g2d.drawString("Lista code", 823, 145);
	    
	    //Draw remove queue string
	    if(listaCode.size() > 0) {
	    	
	    	g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 14));
		    g2d.drawString("Clicca su una per rimuovere", 854, 179 + (queueButtonDistance * listaCode.size()));
	    	
	    }
	    
	}
	
}

//Disable typing in the spinner
/*
DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
formatter.setAllowsInvalid(false);
formatter.setOverwriteMode(true);
*/

