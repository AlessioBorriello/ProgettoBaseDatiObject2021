import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.SpinnerNumberModel;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class EditFlightPanel extends JPanel {

	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	private Volo v; //Flight being edited
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
	 * Panel where the user can edit a flight and update it into the database
	 * @param bounds Bounds of the contentPanel that contains this panel (to give it the contentPanel's dimensions)
	 * @param mf Link to the MainFrame
	 * @param c Link to the MainController
	 * @param v Flight to edit
	 */
	@SuppressWarnings("deprecation")
	public EditFlightPanel(Rectangle bounds, MainFrame mf, MainController c, Volo v) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		this.v = v; //Flight to edit
		
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
		
		setBounds(bounds); //Set bounds
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
		
		//Populate combo box and find the correct company name in it
		int companyIndex = 0; //Index where the company of the flight is located
		int tempIndex = 0; //Index to increment in the for loop
		
		for(CompagniaAerea comp : mainFrame.getListaCompagnie()) {
			//Add each member of the array listaCompagnie to the combo box
			cBoxCompany.addItem(comp.getNome());
			if(comp.getNome().equals(v.getCompagnia().getNome())) {
				companyIndex = tempIndex; //Set the correct index
			}
			tempIndex++; //Increment temporary index
		}
		//Set the correct company in the combo box
		cBoxCompany.setSelectedIndex(companyIndex);
		
		CustomSpinner spinnerTakeOffDate = new CustomSpinner(MainController.backgroundColorOne, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); //Create spinner to declare the take off date
		spinnerTakeOffDate.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set spinner model
		spinnerTakeOffDate.setValue(v.getOrarioDecollo());
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
		spinnerGate.setValue(Integer.valueOf(v.getGate().getNumeroGate()));
		spinnerGate.setBounds(310, 291, 150, 24); //Set bounds
		spinnerGate.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerGate.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerGate.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 12));
		add(spinnerGate); //Add to panel
		
		CustomButton buttonEditFlight = new CustomButton("Modifica volo!", null, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 22, true, MainController.foregroundColorThree, 1); //Create button create flight
		buttonEditFlight.setName("buttonEditFlight"); //Name component
		buttonEditFlight.setBounds(349, 456, 300, 58); //Set bounds
		buttonEditFlight.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonEditFlight.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonEditFlight.unselectAnimation(8);
			}
		});
		add(buttonEditFlight); //Add to panel
		
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
		
		//Add queues of the flight being edited
		for(Coda coda : (v.getGate().getListaCode())) {
			addQueue(coda.getTipo());
		}
		
		//Hide add queue button if all the queues have already been added
		if(listaCode.size() >= 6) {
			buttonAddQueue.show(false);
		}
		
		CustomComboBox cBoxDestination = mainFrame.createCustomComboBox();
		cBoxDestination.setName("cBoxDestination");
		cBoxDestination.setBounds(310, 351, 150, 24);
		add(cBoxDestination);
		
		populateDestinationComboBox(cBoxDestination);
		
		//Set flight's destination in the destination combo box
		for (int i = 0; i < cBoxDestination.getItemCount(); i++) {
			if(v.getDestinazione().equals(cBoxDestination.getItemAt(i))) {
				cBoxDestination.setSelectedIndex(i);
				break;
			}
		}
		
		//Button action listener
		buttonEditFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				//Gather data
				String nomeCompagnia = (String)cBoxCompany.getSelectedItem();
				Date data = (Date)spinnerTakeOffDate.getValue();
				int gate = (int)spinnerGate.getValue();
				String destinazione = (String)cBoxDestination.getSelectedItem();
				
				Thread queryThread = new Thread() {
				      public void run() {
				    	  editFlight(v, nomeCompagnia, data, gate, destinazione); //Update flight with the gathered data
				      }
				};
			    queryThread.start();
			
			}
		});
		
	}
	
	/**
	 * Create a frame prompting the user to choose a queue to add
	 */
	public void createAddQueueFrame() {
		
		AddQueueFrame frame = new AddQueueFrame(mainFrame, listaCode); //Create a pop up panel
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
	 * @param type Type of the queue to add to the queue list
	 */
	@SuppressWarnings("deprecation")
	public void addQueue(String type) {
		
		int yOffset = queueButtonDistance * (listaCode.size());
		
		listaCode.add(type); //Add the queue to the ArrayList listaCode
		
		//Determine background color
		Color bgColor = (listaCode.size()%2 == 0)? MainController.backgroundColorTwo : null; //If the array size is not even, set a different background color for the button (Once every 2 buttons)
		
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
		
		//Move add queue button down
		buttonAddQueue.setBounds(buttonAddQueue.getBounds().x, buttonAddQueue.getBounds().y + queueButtonDistance, buttonAddQueue.getBounds().width, buttonAddQueue.getBounds().height);
		
		//Hide add queue button if all the queues have been added
		if(listaCode.size() >= 6) {
			buttonAddQueue.show(false);
		}
		
		repaint();
		
	}
	
	/**
	 * Remove a queue from the panelQueues containing the queues
	 * @param queueButton What queue button to remove
	 */
	@SuppressWarnings("deprecation")
	public void removeQueue(CustomButton queueButton) {
		
		String type = queueButton.getText(); //Type of the queue being removed
		int queueLength = 0; //How many people were in that queue
		//Get all the queues of the flight being edited
		for(Coda c : v.getGate().getListaCode()) {
			if(type.equals(c.getTipo())) { //If the type of the queue being removed is found in the queues of the flight being edited
				queueLength = c.getPersoneInCoda();
			}
		}
		
		String notification = (queueLength > 0)? "Sei sicuro di voler rimuovere questa coda (Ci sono " + queueLength + " persone in questa coda)?" : "Sei sicuro di voler rimuovere questa coda?"; //If there were people in the queue being removed
		
		ConfirmationFrame frame = new ConfirmationFrame(notification, mainFrame); //Create confirmation frame
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
	 * Update the passed flight in the database
	 * @param v Old flight instance to update
	 * @param nomeCompagnia Company name of the updated flight
	 * @param data Take off date of the updated flight
	 * @param gate Gate where the updated flight's embark takes place
	 */
	public void editFlight(Volo v, String nomeCompagnia, Date data, int gate, String destinazione) {
		
		//Get company class from it's name
		CompagniaAerea compagnia = null;
		for(CompagniaAerea c : mainFrame.getListaCompagnie()) {
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
		ArrayList<Coda> newQueueList = new ArrayList<Coda>();
		for(String s : listaCode) {
			Coda coda = new Coda();
			try {
				coda.setTipo(s);
			} catch (NonExistentQueueTypeException e) {
				e.printStackTrace();
			}
			coda.setPersoneInCoda(0);
			
			//Get queues of the non updated flight
			for(Coda oldCoda : v.getGate().getListaCode()) {
				
				if(coda.getTipo().equals(oldCoda.getTipo())) { //If one of the queues in the updated flight was already in the non updated one
					coda.setPersoneInCoda(oldCoda.getPersoneInCoda()); //Set it's length (to not loose bookings)
				}
				
			}
			
			newQueueList.add(coda);
			
		}
		
		//Check if there is at least one queue
		if(newQueueList.size() == 0) {
			mainFrame.createNotificationFrame("Seleziona almeno una coda!");
			return;
		}
		
		//Create gate
		Gate g = new Gate();
		g.setListaCode(newQueueList);
		try {
			g.setNumeroGate(gate);
		} catch (NonExistentGateException e) {
			e.printStackTrace();
		}
		
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
		editedVolo.setDestinazione(destinazione);
		editedVolo.setOrarioDecollo(data);
		editedVolo.setPartito(false);
		editedVolo.setSlot(s);
		
		editedVolo.printFlightInfo();
		
		//Calculate number of bookings
		int sum = 0;
		for(Coda coda : v.getGate().getListaCode()) {
			sum += coda.getPersoneInCoda();
		}
		editedVolo.setNumeroPrenotazioni(sum);
		
		//Update in the database
		VoloDAO dao = new VoloDAO();
		dao.updateFlight(mainFrame, editedVolo);
		
		//Update company flight count
		CompagniaAereaDAO daoCompany = new CompagniaAereaDAO();
		daoCompany.decreaseCompagniaAereaFlightCount(mainFrame, v.getCompagnia().getNome()); //Decrease old company count
		daoCompany.increaseCompagniaAereaFlightCount(mainFrame, editedVolo.getCompagnia().getNome()); //Increase new company count
		
		//Change panel to the ViewFlightPanel
		mainFrame.changeContentPanel(new ViewFlightInfoPanel(new Rectangle(72, 2, 1124, 666), mainFrame, mainController, editedVolo), false, false);
		
	}

	/**
	 * Add destinations in to a given combo box
	 * @param box The combo box to add the destinations to
	 */
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
	    g2d.drawString("Modifica un volo", 40, 120);
		
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
