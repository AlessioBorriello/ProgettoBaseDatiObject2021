import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
	
	//Company images
	private Image airfranceLogoImage;
	private Image alitaliaLogoImage;
	private Image easyjetLogoImage;
	private Image ryanairLogoImage;
	
	private Image currentLogoImage; //Currently used image
	
	CustomButton buttonAddQueue; //Button used to add queues (Declared here so it can be accessed by the remove and add queues methods)
	
	private int gatesNumber = MainController.gateAirportNumber; //How many gate there are in the airport
	
	private ArrayList<Coda> queues; // ArrayList containing the queues of a given gate
	private int capacity; //The queues total capacity
	private int bookingAmount; //How many booking there are on the flight
	
	/**
	 * Panel where the user can edit a flight and update it into the database
	 * @param bounds Bounds of the contentPanel that contains this panel (to give it the contentPanel's dimensions)
	 * @param mf Link to the MainFrame
	 * @param v Flight to edit
	 */
	@SuppressWarnings("deprecation")
	public EditFlightPanel(Rectangle bounds, MainFrame mf, Volo v) {
		
		mainFrame = mf; //Link main frame
		
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
		
		CustomSpinner spinnerTakeOffDate = new CustomSpinner(MainController.backgroundColorOne, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); //Create spinner to declare the take off date
		spinnerTakeOffDate.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set spinner model
		spinnerTakeOffDate.setValue(v.getOrarioDecollo());
		spinnerTakeOffDate.setName("spinnerTakeOffDate"); //Name component
		spinnerTakeOffDate.setBounds(310, 231, 150, 24); //Set bounds
		spinnerTakeOffDate.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerTakeOffDate.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerTakeOffDate.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 12));
		add(spinnerTakeOffDate); //Add to panel
		
		CustomSpinner spinnerGate = new CustomSpinner(MainController.backgroundColorOne, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); //Create spinner to declare the gate
		spinnerGate.setName("spinnerGate"); //Name component
		spinnerGate.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(gatesNumber), new Integer(1))); //Set the type of spinner
		spinnerGate.setValue(Integer.valueOf(v.getGate().getNumeroGate()));
		spinnerGate.setBounds(310, 291, 150, 24); //Set bounds
		spinnerGate.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerGate.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerGate.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 12));
		spinnerGate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				queues = mainFrame.getGateFromList((int) spinnerGate.getValue()).getListaCode(); //Set the correct queues based on the gate being selected
				//Get queue's capacity
				capacity = 0;
				for(Coda coda : queues) {
					capacity += coda.getLunghezzaMax();
				}
				repaint();
				revalidate();
			}
	});
		add(spinnerGate); //Add to panel
		
		queues = mainFrame.getGateFromList((int) spinnerGate.getValue()).getListaCode(); //Set the correct queues based on the gate being selected
		//Get queue's capacity
		capacity = 0;
		if(queues != null) {
			for(Coda coda : queues) {
				capacity += coda.getLunghezzaMax();
			}
		}
		
		//Get booking amount
		bookingAmount = v.getNumeroPrenotazioni();
		
		CustomButton buttonEditFlight = new CustomButton("Modifica volo!", null, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
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
				
				//If the booking amount is greater than the capacity of the gate, notify the user
				if(bookingAmount > capacity) {
					if(!mainFrame.createConfirmationFrame("Il numero di prenotazioni supera la capacita' del gate attuale, sei sicuro di voler procedere?")) {
						return;
					}
				}
				
				Thread queryThread = new Thread() {
				      public void run() {
				    	  mainFrame.editFlight(v, nomeCompagnia, data, gate, destinazione); //Update flight with the gathered data
				      }
				};
			    queryThread.start();
			
			}
		});
		
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
	    
	    // Draw queue string
 		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 28));
 		String queueString = "Lista code sul gate";
 		int queueStringLenght = g2d.getFontMetrics(g2d.getFont()).stringWidth(queueString);
 		g2d.drawString(queueString, 870 - (queueStringLenght/2), 120);
 		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 16));
 		g2d.drawString("(Capienza)", 1015, 120);
 		
 		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 28));
 		
 		int index = 0;
 		if(queues != null) {
	 	    for(Coda coda : queues) {
	 	    	
	 	    	//Draw different colored rectangle on even queue's number (Once every 2 queues)
	 	    	if(index%2 == 0) {
	 	    		g2d.setColor(MainController.backgroundColorTwo);
	 	    		g2d.fillRect(720, 146 + (index * 60), 390, 60);
	 	    		
	 	    	}
	 	    	
	 	    	//Draw queue string
	 	    	g2d.setColor(MainController.foregroundColorThree);
	 	    	String s = coda.getTipo() + " (" + coda.getLunghezzaMax() + ")";
	 	    	queueStringLenght = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
	 	    	g2d.drawString(s, 918 - (queueStringLenght/2), 184 + (index * 60));
	 			
	 			index++;
	 		}
 		}
 	    
 	    //Draw total capacity
 	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 21));
 		String capacityString = "Capacita' totale del gate: " + bookingAmount + "/" + capacity;
 		if(bookingAmount > capacity) {
 			g2d.setColor(MainController.flightCancelledColor);
 		}
 		int capacityStringLenght = g2d.getFontMetrics(g2d.getFont()).stringWidth(capacityString);
 		g2d.drawString(capacityString, 915 - (capacityStringLenght/2), 575);
	    
	}
	
}
