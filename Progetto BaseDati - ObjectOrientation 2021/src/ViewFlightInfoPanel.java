import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.Image;

import javax.imageio.ImageIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class ViewFlightInfoPanel extends JPanel {

	private MainFrame mainFrame; //Main panel
	
	//Flight instance to show its info
	private Volo volo;
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm"); //Date format
	
	//Buttons, declared here so that they can be accessed by the methods
	private CustomButton buttonFlightTakenOff;
	private CustomButton buttonCancelFlight;
	private CustomButton buttonEditFlight;
	
	private Image companyImage; //Company image
	
	private ArrayList<Coda> listaCode; //List of queues of the flight

	/**
	 * Panel that shows the info of a given flight in detail
	 * @param bounds Bounds of the contentPanel that contains this panel (to give it the contentPanel's dimensions)
	 * @param mf Link to the MainFrame
	 * @param v Flight to show the info of
	 */
	public ViewFlightInfoPanel(Rectangle bounds, MainFrame mf, Volo v) {
		
		mainFrame = mf; //Link main frame
		//mainController = c; //Link main controller
		volo = v; //Flight to show the info of
		
		setBounds(bounds); //Set bounds
		setLayout(null); //Set layout
		
		//Get correct company image path
		String imagePath;
		switch(volo.getCompagnia().getNome()) {
			case "AirFrance": imagePath = "imgs/company-logos/airfrance-logo.png"; break;
			case "Alitalia": imagePath = "imgs/company-logos/alitalia-logo.png"; break;
			case "EasyJet": imagePath = "imgs/company-logos/easyjet-logo.png"; break;
			case "Ryanair": imagePath = "imgs/company-logos/ryanair-logo.png"; break;
			default: imagePath = "imgs/company-logos/airfrance-logo.png";
		}
		
		//Load correct company image
		try {                
			companyImage = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			System.out.println(e);
		}
		
		//Scale company image
		companyImage = companyImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
		
		buttonFlightTakenOff = new CustomButton("Fai partire volo", null, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 18, true, MainController.foregroundColorThree, 1); //Create button
		buttonFlightTakenOff.setName("buttonFlightTakenOff"); //Set name
		//Add action listener
		buttonFlightTakenOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setFlightAsTakenOff(v);
			}
		});
		buttonFlightTakenOff.setBounds(302, 420, 168, 60); //Set bounds
		buttonFlightTakenOff.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonFlightTakenOff.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonFlightTakenOff.unselectAnimation(8);
			}
		});
		if(!volo.isPartito() && !volo.isCancellato()) { //If the flight has neither taken off or been cancelled
			add(buttonFlightTakenOff); //Add button
		}
		
		buttonCancelFlight = new CustomButton("Cancella volo", null, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 18, true, MainController.foregroundColorThree, 1); //Create button
		buttonCancelFlight.setName("buttonCancelFlight"); //Set name
		//Add action listener
		buttonCancelFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setFlightAsCancelled(v);
			}
		});
		buttonCancelFlight.setBounds(489, 420, 168, 60); //Set bound
		buttonCancelFlight.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonCancelFlight.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonCancelFlight.unselectAnimation(8);
			}
		});
		if(!volo.isPartito() && !volo.isCancellato()) { //If the flight has neither taken off or been cancelled
			add(buttonCancelFlight); //Add button
		}
		
		buttonEditFlight = new CustomButton("Modifica volo", null, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 22, true, MainController.foregroundColorThree, 1); //Create button
		buttonEditFlight.setName("buttonEditFlight"); //Set name
		//Add action listener
		buttonEditFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.changeContentPanel(new EditFlightPanel(new Rectangle(72, 2, 1124, 666), mainFrame, volo), false, false);
			}
		});
		buttonEditFlight.setBounds(805, 548, 210, 74); //Set bound
		buttonEditFlight.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonEditFlight.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonEditFlight.unselectAnimation(8);
			}
		});
		if(!volo.isPartito() && !volo.isCancellato()) { //If the flight has neither taken off or been cancelled
			add(buttonEditFlight); //Add button
		}
		
		listaCode = volo.getGate().getListaCode(); //Get all of the flight's queues
		
	}
	
	public void removeButtons() {
		
		//Remove buttons
		remove(buttonFlightTakenOff);
		remove(buttonCancelFlight);
		remove(buttonEditFlight);
		
	}

	/**
	 * Calculate the color of the border of the panel based on the flight's status, programmed, taken off, taken off late and cancelled
	 * @return The color of the border of the panel representing the flight's status
	 */
	public Color getBorderColor() {
		
		if(volo.isPartito()) { //If the flight has taken off
			if(volo.checkIfFlightTookOffLate()) { //Check if it did so late
				return MainController.flightTakenOffLateColor;
			}else { //Otherwise
				return MainController.flightTakenOffColor;
			}
		}else { //The flight has not taken off
			if(volo.isCancellato()) { //Check if it has been cancelled
				return MainController.flightCancelledColor;
			}else { //Otherwise
				return MainController.flightProgrammedColor;
			}
		}
		
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
	    
	    //Draw company image
	    g2d.drawImage(companyImage, 35, (getHeight()/2) - (companyImage.getHeight(null)/2) - 30, 285, (getHeight()/2) - (companyImage.getHeight(null)/2) + 250 - 30, 0, 0, companyImage.getWidth(null), companyImage.getHeight(null), this);
	    g2d.setStroke(new BasicStroke(4));
	    g2d.setColor(getBorderColor());
	    g2d.drawRoundRect(35, (getHeight()/2) - (companyImage.getHeight(null)/2) - 30, 250, 250, 110, 110);
	    
	    //ID string
	    g2d.setColor(MainController.foregroundColorThree);
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 42));
	    g2d.drawString("Volo ID: " + volo.getID(), 35, 110);
	    
	    //Company string
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 21));
	    String companyString = "Compagnia";
	    int companyStringLenght = g2d.getFontMetrics(g2d.getFont()).stringWidth(companyString);
	    g2d.drawString(companyString, 35 + (companyImage.getWidth(null)/2) - (companyStringLenght/2), 460);
	    
	    String companyNameString = volo.getCompagnia().getNome();
	    int companyNameStringLenght = g2d.getFontMetrics(g2d.getFont()).stringWidth(companyNameString);
	    g2d.drawString(companyNameString, 35 + (companyImage.getWidth(null)/2) - (companyNameStringLenght/2), 490);
	    
	    //Time, gate and destination strings
	    g2d.drawString("Orario: " + dateTimeFormat.format(volo.getOrarioDecollo()), 310, 255);
	    g2d.drawString("Gate: " + volo.getGate().getNumeroGate(), 310, 290);
	    g2d.drawString("Destinazione: " + volo.getDestinazione(), 310, 325);
	    g2d.drawString("Numero prenotazioni: " + volo.getNumeroPrenotazioni(), 310, 360);
	    
	    //Draw separator lines
	    g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
	    g2d.drawLine(35, 514, 670, 514);
	    g2d.drawLine(700, 100, 700, 484);
	    g2d.drawLine(730, 514, 1100, 514);
	    int circleWidth = 10;
	    g2d.fillOval(700 - (circleWidth/2), 514 - (circleWidth/2), circleWidth, circleWidth);
	    
	    //Draw slots
	    g2d.drawString("Slot:", 35, 555);
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 18));
	    g2d.drawString("Tempo stimato: " + dateTimeFormat.format(volo.getSlot().getInizioTempoStimato()) + " ||| " + dateTimeFormat.format(volo.getSlot().getFineTempoStimato()), 60, 585);
	    //Check if the effective time has been inserted, otherwise set the string to "Non definito"
  		String inizioTempoEffettivo = (volo.getSlot().getInizioTempoEffettivo() == null)? "Non definito" : dateTimeFormat.format(volo.getSlot().getInizioTempoEffettivo());
  		String fineTempoEffettivo = (volo.getSlot().getFineTempoEffettivo() == null)? "Non definito" : dateTimeFormat.format(volo.getSlot().getFineTempoEffettivo());
	    g2d.drawString("Tempo effettivo: " + inizioTempoEffettivo + " ||| " + fineTempoEffettivo, 60, 609);
	    g2d.setStroke(new BasicStroke(2));
	    circleWidth = 7;
	    g2d.drawOval(50 - (circleWidth/2), 579 - (circleWidth/2), circleWidth, circleWidth);
	    g2d.drawOval(50 - (circleWidth/2), 603 - (circleWidth/2), circleWidth, circleWidth);
	    
	    //Queues
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 30));
	    g2d.drawString("Lista code", 823, 100);
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 18));
	    int index = 0;
	    for(Coda coda : listaCode) {
	    	
	    	//Draw different colored rectangle on uneven queue's number (Once every 2 queues)
	    	if(index%2 == 1) {
	    		g2d.setColor(MainController.backgroundColorTwo);
	    		g2d.fillRect(720, 116 + (index * 60), 390, 60);
	    		
	    	}
	    	
	    	//Draw queue string
	    	g2d.setColor(MainController.foregroundColorThree);
	    	String s = coda.getTipo() + ", persone in coda: " + coda.getPersoneInCoda();
	    	int queueStringLenght = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
	    	g2d.drawString(s, 908 - (queueStringLenght/2), 152 + (index * 60));
			
			index++;
		}
	    
	}
	
	/**
	 * Get the flight being displayed currently in the panel
	 * @return The flight being displayed currently in the panel
	 */
	public Volo getFlightBeingViewed() {
		return this.volo;
	}
	
}
