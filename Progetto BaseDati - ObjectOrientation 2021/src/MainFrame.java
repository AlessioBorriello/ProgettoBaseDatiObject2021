import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.SwingUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLayeredPane;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private MainController mainController; //Linked controller
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Screen dimensions
	private JPanel contentPanel = null; //Panel that changes the content displayed
	private JLayeredPane centerPanel; //Panel containing content panel and dash board
	private ArrayList<CompagniaAerea> listaCompagnie = new ArrayList<CompagniaAerea>(); //Array containing the companies
	private DashboardPanel dashboardPanel; //Dash board panel
	
	final private int backPanelsStackMaxSize = 4; //Max amount of stored panels
	private ArrayList<JPanel> backPanelsStack = new ArrayList<JPanel>(); //Stack containing the panels being discarded when the content panel gets changed
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm"); //Date format
	
	private boolean lookingAtArchive; //To differentiate when on the panel CheckFlightsPanel if looking at the archive or not (as they use the same panel type)
	
	final private int screenWidth = (int)screenSize.getWidth(); //Screen width
	final private int screenHeight = (int)screenSize.getHeight(); //Screen height
	final private int frameWidth = 1200; //Frame width
	final private int frameHeight = 700; //Frame height
	
	private Point mouseClickPoint; //Mouse position
	
	private ArrayList<Volo> flightList = null; //List of the flights to be displayed in the content panel
	
	//Notification panel
	private BufferedImage bufferedBellImage;
	private Image bellImage;
	private ArrayList<String> notificationsList = null; //List of the notifications
	
	/**
	 * Frame containing the entire application
	 * @param c Link to the mainController
	 */
	public MainFrame(MainController c) {
		
		mainController = c; //Link controller and frame
		
		//Get all the companies from the database
		listaCompagnie = new CompagniaAereaDAO().getAllCompagniaAerea();
		
		//Load notification bell image
		try {                
			bufferedBellImage = ImageIO.read(new File("imgs/bell.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
		
		bufferedBellImage = colorizeBufferedImage(bufferedBellImage, MainController.foregroundColorThree); //Colorize bell icon image
		
		//Scale bell icon image
		bellImage = bufferedBellImage.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		
		//Frame properties
		setResizable(false); //Not re sizable
		setUndecorated(true); //Undecorated (No close, minimize buttons)
		setTitle("Title"); //Set frame title
		setBounds((screenWidth/2) - (frameWidth/2), (screenHeight/2) - (frameHeight/2), frameWidth, frameHeight); //Set the frame sizes and position in the middle of the screen
		
		//Main Panel, it contains the center panel and the upper panel
		JPanel mainPanel = new JPanel(); //Create panel
		mainPanel.setBackground(SystemColor.inactiveCaption); //Set background
		mainPanel.setName("mainPanel"); //Set name of the component
		mainPanel.setSize(new Dimension(getWidth(), getHeight())); //Set size variables
		mainPanel.setPreferredSize(new Dimension(getWidth(), getHeight())); //Set preferred size variables
		mainPanel.setBorder(null); //Set border to null
		setContentPane(mainPanel); //Add panel to the frame
		mainPanel.setLayout(null); //Set layout of the main panel
		
		Rectangle buttonMinimizeBounds = new Rectangle(frameWidth - 60, 2, 26, 26); //Button minimize position on the upper panel
		Rectangle buttonCloseBounds = new Rectangle(frameWidth - 30, 2, 26, 26); //Button close position on the upper panel
		Rectangle buttonBackBounds = new Rectangle(frameWidth - 90, 2, 26, 26); //Button back position on the upper panel
		Rectangle buttonOpenNotificationsBounds = new Rectangle(frameWidth - 120, 2, 26, 26); //Button open notification position on the upper panel
		
		JPanel upperPanel = (new JPanel() {
			
			public void paintComponent(Graphics g) {
				
				super.paintComponent(g); //Paint the component normally first
				
				Graphics2D g2d = (Graphics2D)g;
				
				//AA
			    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			    //Text AA
			    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			    
			    //Draw background
			    g2d.setColor(MainController.backgroundColorTwo);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			    
			    //Draw application name
				g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 23));
				g2d.setColor(MainController.foregroundColorThree);
				g2d.drawString("Nome applicazione", 4, (getHeight()/2) + (g2d.getFont().getSize()/2) - 2);
				
				//Draw close button icon
				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
				g2d.drawLine(buttonCloseBounds.x + 6, buttonCloseBounds.y + 6, buttonCloseBounds.x + buttonCloseBounds.width - 6, buttonCloseBounds.y + buttonCloseBounds.height - 6);
				g2d.drawLine(buttonCloseBounds.x + 6, buttonCloseBounds.y + buttonCloseBounds.height - 6, buttonCloseBounds.x + buttonCloseBounds.width - 6, buttonCloseBounds.y + 6);
				
				//Draw minimize button icon
				g2d.drawLine(buttonMinimizeBounds.x + 6, buttonMinimizeBounds.y + buttonMinimizeBounds.height - 6, buttonMinimizeBounds.x + buttonMinimizeBounds.width - 6, buttonMinimizeBounds.y + buttonMinimizeBounds.height - 6);
				
				//Draw back button icon
				g2d.drawLine(buttonBackBounds.x + 6, buttonBackBounds.y + (buttonBackBounds.height/2), buttonBackBounds.x + buttonBackBounds.width - 6, buttonBackBounds.y + (buttonBackBounds.height/2));
				g2d.drawLine(buttonBackBounds.x + 6, buttonBackBounds.y + (buttonBackBounds.height/2), buttonBackBounds.x + (buttonBackBounds.width/2), buttonBackBounds.y + 6);
				g2d.drawLine(buttonBackBounds.x + 6, buttonBackBounds.y + (buttonBackBounds.height/2), buttonBackBounds.x + (buttonBackBounds.width/2), buttonBackBounds.y + buttonBackBounds.height - 6);
				
				g2d.setColor(MainController.foregroundColorThree);
				
				//Draw notification bell icon
				g2d.drawImage(bellImage, buttonOpenNotificationsBounds.x + 4, buttonOpenNotificationsBounds.y + 4, buttonOpenNotificationsBounds.x + buttonOpenNotificationsBounds.width - 4, buttonOpenNotificationsBounds.y + buttonOpenNotificationsBounds.height - 4, 0, 0, bellImage.getWidth(null), bellImage.getHeight(null), this);
				
				//Draw notification number
				if(notificationsList != null && notificationsList.size() > 0) {
					
					g2d.setColor(new Color(180, 0, 0));
					int circleSize = 14;
					int circleOffset = 6;
					Point circlePosition = new Point(buttonOpenNotificationsBounds.x + (buttonOpenNotificationsBounds.width/2) - (circleSize/2) + circleOffset, buttonOpenNotificationsBounds.y + (buttonOpenNotificationsBounds.height/2) - (circleSize/2) - circleOffset);
					g2d.fillOval(circlePosition.x, circlePosition.y, circleSize, circleSize);
					
					g2d.setColor(MainController.foregroundColorThree);
					g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 10));
					String number = String.valueOf(notificationsList.size());
					int stringLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(number);
					g2d.drawString(number, circlePosition.x - (stringLength/2) + (circleSize/2), circlePosition.y + (circleSize/2) + 3);
				
				}
				
			}
			
		}); //Create upper panel
		upperPanel.setName("upperPanel"); //Name component
		final int upperPanelHeight = 30; //Height of the upper panel
		upperPanel.setBounds(0, 0, frameWidth, upperPanelHeight); //Position of the upper panel
		mainPanel.add(upperPanel); //Add upper panel to the main panel
		upperPanel.setLayout(null); //Set layout of the upper panel
		//Drag frame
		upperPanel.addMouseMotionListener(new MouseMotionAdapter() {
			//When dragging
			public void mouseDragged(MouseEvent e) {
		        Point newPoint = e.getLocationOnScreen(); //Position of the event
		        newPoint.translate(-mouseClickPoint.x, -mouseClickPoint.y); //Translate mouse point to the new point
		        setLocation(newPoint); //Set position of the frame
		    }
		});
		upperPanel.addMouseListener(new MouseAdapter(){
			//When clicking
			public void mousePressed(MouseEvent e) {
				mouseClickPoint = e.getPoint(); //Update mouse position
		    }
		});
		
		CustomButton buttonMinimize = new CustomButton("", null, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 21, true, MainController.foregroundColorThree, 2); //Create minimize button
		buttonMinimize.setBounds(buttonMinimizeBounds); //Set the button position to the previously defined position
		buttonMinimize.setName("buttonMinimize"); //Name component
		upperPanel.add(buttonMinimize); //Add minimize button to the upper panel
		//Mouse listeners of minimize button
		buttonMinimize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED); //Minimize frame
			}
		});
		buttonMinimize.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonMinimize.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonMinimize.unselectAnimation(8);
			}
		});
		
		CustomButton buttonClose = new CustomButton("", null, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 21, true, MainController.foregroundColorThree, 2); //Create close button
		buttonClose.setBounds(buttonCloseBounds); //Set the button position to the previously defined position
		buttonClose.setName("buttonClose"); //Name component
		upperPanel.add(buttonClose); //Add close button to the upper panel
		//Mouse listeners of close button
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(createConfirmationFrame("Sei sicuro di voler uscire?")) {
					setVisible(false); //Make frame invisible
					dispose(); //Dispose of it
					System.exit(0); //Close application
				}
			}
		});
		buttonClose.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonClose.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonClose.unselectAnimation(8);
			}
		});
		
		CustomButton buttonBack = new CustomButton("", null, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 21, true, MainController.foregroundColorThree, 2); //Create close button
		buttonBack.setBounds(buttonBackBounds); //Set the button position to the previously defined position
		buttonBack.setName("buttonBack"); //Name component
		upperPanel.add(buttonBack); //Add close button to the control panel
		//Mouse listeners of close button
		buttonBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(backPanelsStack.size() > 0) {
					JPanel p = backPanelsStack.get(backPanelsStack.size() - 1);
					changeContentPanel(p, true, true); //Go back
					backPanelsStack.remove(backPanelsStack.size() - 1);
				}
			}
		});
		buttonBack.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if(backPanelsStack.size() > 0) {
					buttonBack.selectAnimation(8);
				}
			}

			public void mouseExited(MouseEvent e) {
				buttonBack.unselectAnimation(8);
			}
		});
		
		CustomButton buttonOpenNotifications = new CustomButton("", null, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 21, false, null, 0); //Create close button
		buttonOpenNotifications.setBounds(buttonOpenNotificationsBounds); //Set the button position to the previously defined position
		buttonOpenNotifications.setName("buttonOpenNotifications"); //Name component
		upperPanel.add(buttonOpenNotifications); //Add button to the upper panel
		buttonOpenNotifications.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonOpenNotifications.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonOpenNotifications.unselectAnimation(8);
			}
		});
		
		//Layered pane that contains the content panel, the panel that holds the current panel being used by the user and the dash board
		centerPanel = (new JLayeredPane() {
			
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
			    
			}
			
		}); //Create centerPanel
		centerPanel.setName("centerPanel"); //Name component
		centerPanel.setBounds(0, upperPanelHeight, frameWidth, frameHeight - upperPanelHeight); //Position of the central panel
		centerPanel.setPreferredSize(new Dimension(frameWidth, frameHeight - upperPanelHeight)); //Set center panel preferred size
		mainPanel.add(centerPanel); //Add center panel to the main panel
		
		//Panel hanging on the side of the center panel until the mouse hovers over it, it then animates to open
		dashboardPanel = new DashboardPanel(centerPanel.getPreferredSize().height, this, mainController);
		dashboardPanel.setName("dashboardPanel");
		centerPanel.add(dashboardPanel);
		
		//Panel that holds the current panel being used by the user
		contentPanel = new JPanel(); //Create content panel
		contentPanel.setName("contentPanel"); //Name component
		contentPanel.setBounds(72, 2, centerPanel.getPreferredSize().width - 72 - 4, centerPanel.getPreferredSize().height - 4); //Position the content panel
		centerPanel.add(contentPanel); //Add the content panel to the center panel
		contentPanel.setLayout(new BorderLayout(0, 0)); //Set content panel's layout
		
		//Generate random flights
		//debugGenerateRandomFlights(10);
		
		//Start a research to get all of the flights (as the panel starts not looking at the archive (setContentPanelToCheckFlightsPanel(false, false) on next line)) that have not taken off or have been cancelled
		Thread queryThread = new Thread() {
		      public void run() {
		    	  flightList = searchFlights("", "", -1, null, null, true, true, true, true, true, true, true);
		    	  redrawCheckFlightsPanel();
		      }
		};
	    queryThread.start();
		
	    changeContentPanel(new CheckFlightsPanel(new Rectangle(72, 2, 1124, 666), this, mainController, false), false, false); //Set contentPanel to CheckFlightsPanel, not looking at archive
	    contentPanel = (CheckFlightsPanel)contentPanel; //Cast content panel to the correct panel type (only necessary in this case)
		
		//Check if the dash board has not closed correctly every 1000ms (if mouse exited event gets skipped)
		checkDashboardStatus(1000);
		
		//Get notifications list
		notificationsList = checkForNotifications();
		
		//Update notifications list
		class CheckNotifications extends TimerTask {
			
			//Override run method
			public void run() {
				notificationsList = checkForNotifications();
				buttonOpenNotifications.repaint();
				upperPanel.repaint();
			}
		}
	
		CheckNotifications checkNotifications = new CheckNotifications();
		Timer t = new Timer();
		int seconds = 60; //Interval to check for notifications
		t.schedule(checkNotifications, (seconds - LocalDateTime.now().atZone(ZoneId.systemDefault()).getSecond())*1000, seconds*1000);
		
		//Open notifications
		MainFrame mfLink = this; //Link with this main frame for the notification frame
		buttonOpenNotifications.addActionListener(new ActionListener() {
			
			NotificationsPanel notificationPanel;
			
			public void actionPerformed(ActionEvent e) {
				if(notificationPanel == null) {
					
					notificationPanel = new NotificationsPanel(mainController, mfLink, notificationsList);
					notificationPanel.setLocation(buttonOpenNotifications.getBounds().x - 215, 5);
					//notificationPanel.setBounds(buttonOpenNotifications.getBounds().x - 215, 5, 240, 400);
					centerPanel.add(notificationPanel);
					centerPanel.setLayer(notificationPanel, 1); //Bring the panel forward (higher number = towards the top)
					centerPanel.repaint();
					
				}else {
					
					centerPanel.remove(notificationPanel);
					notificationPanel =  null;
					centerPanel.repaint();
					
				}
			}
		
		});
		
	}
	
	/**
	 * Change content panel to the given new panel
	 * @param newPanel the panel to change the content panel to
	 * @param askConfirmation If before changing the user should be prompted with a confirmation frame, it does so only if the user is currently
	 * @param fromBackButton if the method gets called from the back button, does not add the panel being replaced to the backPanelsStack
	 * on a create flight or edit flight panel
	 * @return If the panel got changed
	 */
	public boolean changeContentPanel(JPanel newPanel, boolean askConfirmation, boolean fromBackButton) {
	
		//Check if the current panel is already in place in the content panel (They have the same class name)
		if(contentPanel.getClass().getName().equals(newPanel.getClass().getName())) {
			
			//Differentiate if looking at the archive or not (if new panel is CheckFlightsPanel)
			if(newPanel.getClass().getName().equals("CheckFlightsPanel")) {
				
				CheckFlightsPanel checkFlightsPanel = (CheckFlightsPanel)newPanel; //Cast to CheckFlightsPanel class
				if(this.lookingAtArchive == checkFlightsPanel.isLookingAtArchive()) { //Already looking at the same type of CheckFlightsPanel (lookingAtArchive on main frame and the one in the new panel are the same)
					
					System.out.println("Already on this panel!");
					return false; //Don't replace the content panel
				
				}
				
			//Differentiate if looking at the same flight or not (if the new panel and old panel are ViewFlightInfoPanel)
			}else if(newPanel.getClass().getName().equals("ViewFlightInfoPanel")) {
				
				ViewFlightInfoPanel newViewFlightInfoPanel = (ViewFlightInfoPanel)newPanel; //Cast the new panel to ViewFlightInfoPanel class
				ViewFlightInfoPanel oldViewFlightInfoPanel = (ViewFlightInfoPanel)contentPanel; //Cast the content panel (the one to be substituted if this check is false) to ViewFlightInfoPanel class
				if(newViewFlightInfoPanel.getFlightBeingViewed().getID().equals(oldViewFlightInfoPanel.getFlightBeingViewed().getID())) {
					
					System.out.println("Already on this panel!");
					return false; //Don't replace the content panel
					
				}
				
			}else { //New panel is not CheckFlightsPanel nor ViewFlightInfoPanel, don't change the panel
				
				System.out.println("Already on this panel!");
				return false; //Don't replace the content panel
				
			}
			
		}
		
		//If creating or editing a flight
		if(askConfirmation) {
			
			if(contentPanel.getClass().getName().equals("CreateFlightPanel")) {
				
				if(!createConfirmationFrame("Stai creando un volo, sei sicuro di voler uscire dalla pagina di creazione?")) {
					return false;
				}
				
			}
			if(contentPanel.getClass().getName().equals("EditFlightPanel")) {
				
				if(!createConfirmationFrame("Stai modificando un volo, sei sicuro di voler uscire dalla pagina di modifica?")) {
					return false;
				}
				
			}
			
		}
		
		Rectangle bounds = new Rectangle(contentPanel.getBounds()); //Get bounds of the content panel
		centerPanel.remove(contentPanel); //Remove content panel from the center panel
		
		//Add panel being removed (currently stored in contentPanel) to the stack (exclude when contentPanel is of the class JPanel as it is the first panel being added to the contentPanel)
		if(!contentPanel.getClass().toString().equals("class javax.swing.JPanel") && !fromBackButton) {
			addPanelToBackPanelsStack(contentPanel);
		}
		
		contentPanel = newPanel; //Create new panel and store it in the contentPanel
		
		String name = newPanel.getClass().getName();
		name = name.substring(0, 1).toLowerCase() + name.substring(1); //Change first letter of the name to lower case
		
		contentPanel.setBounds(bounds); //Position the content panel based on the bounds gathered beforehand
		contentPanel.setName(name); //Set name
		centerPanel.add(contentPanel); //Add new content panel to the center panel
		
		centerPanel.repaint(); //Repaint center panel
		contentPanel.repaint(); //Repaint content panel
		contentPanel.revalidate(); //Re validate content panel
		
		//Update looking at archive here if the new panel is a check flights panel
		if(newPanel.getClass().getName().equals("CheckFlightsPanel")) {
			
			CheckFlightsPanel checkFlightsPanel = (CheckFlightsPanel)newPanel; //Cast to CheckFlightsPanel class
			this.lookingAtArchive = checkFlightsPanel.isLookingAtArchive(); //Set if looking at the archive or not
			
		}

		dashboardPanel.toggleSearchPanel((newPanel.getClass().getName().equals("CheckFlightsPanel"))? true : false); //Show search panel on dash board if the new panel is of the CheckFlightsPanel class
		
		//If coming from the back button and the class is a CheckFlightsPanel, update the archive only check boxes in the search panel (they are normally updated when the buttons on the dash board are pressed)
		if(fromBackButton && newPanel.getClass().getName().equals("CheckFlightsPanel")) {
			CheckFlightsPanel checkFlightsPanel = (CheckFlightsPanel)newPanel; //Cast to CheckFlightsPanel class
			dashboardPanel.getSearchPanel().toggleArchiveOnlyCheckBoxes(checkFlightsPanel.isLookingAtArchive());
		}
		
		return true;
		
	}
	
	/**
	 * Add a panel to the back panels stack
	 * @param p The panel to add
	 */
	public void addPanelToBackPanelsStack(JPanel p) {
		
		if(backPanelsStack.size() < backPanelsStackMaxSize) {
			backPanelsStack.add(p); //Stack not full, simply add on top
		}else {
			//Shift all the panels down once
			for(int i = 0; i < backPanelsStack.size() - 1; i++) {
				backPanelsStack.set(i, backPanelsStack.get(i + 1));
			}
			//Set highest of the stack as the new panel
			backPanelsStack.set(backPanelsStack.size() - 1, p);
		}
		
	}
	
	/**
	 * Create a notification frame
	 * @param notification Notification text to show the user
	 */
	public void createNotificationFrame(String notification) {
		
		//Create frame and set it visible
		NotificationFrame frame = new NotificationFrame(notification, this);
		frame.setVisible(true);
		
	}

	/**
	 * Create confirmation frame
	 * @param notification Notification text to show the user
	 * @return What the user has chosen
	 */
	public boolean createConfirmationFrame(String notification) {
		
		//Create frame and set it visible
		ConfirmationFrame frame = new ConfirmationFrame(notification, this);
		frame.setVisible(true);
		
		return frame.getAnswer(); //Return user choice
		
	}
	
	/**
	 * Create a query based on the arguments passed and get a list of flights satisfying the query
	 * @param idField ID the flights has to have (at least part of it) to pass the query (set as "" to ignore)
	 * @param gateNumber Number the flight has to have to pass the query (set as -1 to ignore)
	 * @param dateStart Lower end of the date the flight has to have to pass the query (set as null to ignore)
	 * @param dateEnd Higher end of the date the flight has to have to pass the query (set as null to ignore)
	 * @param airFrance Include flights with this company name
	 * @param alitalia Include flights with this company name
	 * @param easyJet Include flights with this company name
	 * @param ryanair Include flights with this company name
	 * @param cancelled Include flights cancelled
	 * @param delayed Include flights that took off late
	 * @param inTime Include flights that took off on time
	 * @return List of the flights that passed the generated query
	 */
	public ArrayList<Volo> searchFlights(String idField, String destinationField, int gateNumber, Date dateStart, Date dateEnd, boolean airFrance, boolean alitalia, boolean easyJet, boolean ryanair, boolean cancelled, boolean delayed, boolean inTime) {
		
		//Query generation setup
		boolean inArchive = lookingAtArchive; //If looking at the archive or not
		
		idField = (!idField.contentEquals(""))? idField : null; //If the field is empty, set it as null
		destinationField = (!destinationField.contentEquals(""))? destinationField : null; //If the field is empty, set it as null
		
		String dateStartString = (dateStart != null)? dateTimeFormat.format(dateStart) : null; //If the date is not null get it's string version, set the string as null otherwise
		String dateEndString = (dateEnd != null)? dateTimeFormat.format(dateEnd) : null; //If the date is not null get it's string version, set the string as null otherwise
		
		String query = "SELECT * FROM volo INNER JOIN gate ON volo.idvolo = gate.IDVolo INNER JOIN slot ON volo.idvolo = slot.IDVolo INNER JOIN coda ON volo.idvolo = coda.IDVolo WHERE ("; //Initialize query string
		
		//ID field
		String idQuery;
		if(idField != null){
			idQuery = "(volo.idvolo LIKE '%" + idField + "%' OR '" + idField + "' IS NULL)"; //If idField was not empty
		}else{
			idQuery = "(volo.idvolo LIKE '" + idField + "' OR " + idField + " IS NULL)"; //If idField was empty (this is always true)
		}
		
		//Destination field
		String destinationQuery;
		if(destinationField != null){
			destinationQuery = "(destinazione LIKE '%" + destinationField + "%' OR '" + destinationField + "' IS NULL)"; //If destinationField was not empty
		}else{
			destinationQuery = "(destinazione LIKE '" + destinationField + "' OR " + destinationField + " IS NULL)"; //If destinationField was empty (this is always true)
		}
		
		//Gate number
		String gateNumberQuery = (gateNumber != -1)? "(numeroGate = " + gateNumber + ")" : "(1 = 1)"; //Add (numeroGate = gateNumber) condition if gateNumber != -1, otherwise add (1 = 1) this is always true
		
		//Start and End time
		String startTimeQuery = (dateStartString != null)? "(dataPartenza >= '" + dateStartString + "')" : "(NULL IS NULL)"; //Add (dataPartenza >= dateStartString) condition if dateStartString is not null, otherwise add (NULL IS NULL) this is always true
		String endTimeQuery = (dateEndString != null)? "(dataPartenza <= '" + dateEndString + "')" : "(NULL IS NULL)"; //Add (dataPartenza >= dateEndString) condition if dateEndString is not null, otherwise add (NULL IS NULL) this is always true
		
		//Companies
		String companyQuery;
		companyQuery = (!airFrance)? "(nomeCompagnia != 'AirFrance') AND " : "(1 = 1) AND "; //Exclude that company if the boolean is false, otherwise add (1 = 1) this is always true
		companyQuery += (!alitalia)? "(nomeCompagnia != 'Alitalia') AND " : "(1 = 1) AND "; //Exclude that company if the boolean is false, otherwise add (1 = 1) this is always true
		companyQuery += (!ryanair)? "(nomeCompagnia != 'Ryanair') AND " : "(1 = 1) AND "; //Exclude that company if the boolean is false, otherwise add (1 = 1) this is always true
		companyQuery += (!easyJet)? "(nomeCompagnia != 'EasyJet')" : "(1 = 1)"; //Exclude that company if the boolean is false, otherwise add (1 = 1) this is always true
		
		String archiveOnlyQuery = "(partito = 0 AND cancellato = 0)"; //If not in archive
		if(inArchive) {
			
			archiveOnlyQuery = "(partito = 1 OR cancellato = 1) AND "; //If in archive
			archiveOnlyQuery += (!cancelled)? "(cancellato = 0) AND " : "(1 = 1) AND "; //Exclude the cancelled flights if the boolean is false, otherwise add (1 = 1) this is always true
			archiveOnlyQuery += (!delayed && !inTime)? "(partito = 0)" : "(1 = 1)"; //Exclude the taken off flights if the both boolean are false, otherwise add (1 = 1) this is always true
		
			//If in archive and either the delayed or the inTime check boxes are ticked (not both), then the flights where 'partito' = 1 are included in the query, discriminate between taken off in time and late
			if((delayed || inTime) && !(delayed && inTime)) {
				if(!delayed) { //Exclude delayed flights if delayed is false
					archiveOnlyQuery += " AND ((fineTempoStimato >= fineTempoEffettivo)"; //Only non delayed flights
					archiveOnlyQuery += (cancelled)? " OR (cancellato = 1))" : ")"; //Add cancelled back if the cancelled boolean is true (since they get removed by the above condition) otherwise simply close brackets
				}else { //Exclude inTime flights if inTime is false
					archiveOnlyQuery += " AND ((fineTempoStimato < fineTempoEffettivo)"; //Only delayed flights
					archiveOnlyQuery += (cancelled)? " OR (cancellato = 1))" : ")"; //Add cancelled back if the cancelled boolean is true (since they get removed by the above condition) otherwise simply close brackets
				}
			}
			
		}
		
		//Add bits to the query
		query += idQuery + " AND \n" + destinationQuery + " AND \n" + startTimeQuery + " AND \n" + endTimeQuery + " AND \n" + gateNumberQuery + " AND \n" + companyQuery + " AND \n" + archiveOnlyQuery + ") ORDER BY dataPartenza ASC";
		//System.out.println(query);
		
		//Execute query
		ArrayList<Volo> newFlightList = (new VoloDAO().searchFlight(this, query));
		
		return newFlightList;
		
	}

	/**
	 * Get the flight panel and (if found) re populate it's grid
	 */
	public void redrawCheckFlightsPanel() {
		
		CheckFlightsPanel flightPanel = (CheckFlightsPanel)mainController.getComponentByName(this, "checkFlightsPanel"); //Get panel
		if(flightPanel != null) { //If it has been found
			flightPanel.populateGrid(flightList, 4, 15, 15, 25, 95); //Populate grid with the mainFrame's flight list
			repaint();
			revalidate();
		}
		
	}

	/**
	 * Check repeatedly if the dash board is open while it should be closed
	 * @param interval The interval between each check
	 */
	public void checkDashboardStatus(int interval) {
		
		CompletableFuture.runAsync(() -> { //Create asynchronous thread
			while(true) {
				try {
					Thread.sleep(interval); //Wait
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
				//Check if the dash board is extended
				if(dashboardPanel.getAnimationStatus() == dashboardAnimationStatus.extended) {
					//Check if it should be
					Point p = new Point(MouseInfo.getPointerInfo().getLocation()); //Get Mouse point
			        SwingUtilities.convertPointFromScreen(p, dashboardPanel); //Convert p from a screen coordinates to a component's coordinate system
			        if(!dashboardPanel.contains(p)) { //If the point is not on the dash board
			        	dashboardPanel.closeDashboardAnimation(); //Close dash board
			        }
				}
			}
		});
		
	}
	
	/**
	 * Create an instance of a custom scroll bar with preset settings
	 * @return The scroll bar
	 */
	public CustomScrollBar createCustomScrollbar() {
		
		//Create the scroll bar with these default settings and return it
		return new CustomScrollBar(13, 15, //Bar width and button height
									MainController.backgroundColorOne, //Background color
									MainController.foregroundColorThree, //Thumb color
									true, //Rounded thumb
									true, MainController.foregroundColorTwo, 1, //Thumb border boolean, color and thickness
									false, null, //Highlight track boolean and color
									MainController.backgroundColorOne, //Buttons background color
									mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64), //hovering color
									true, MainController.foregroundColorThree, 2); //Border boolean, border color and border thickness
		
	}
	
	/**
	 * Create an instance of a custom combo box with preset settings
	 * @return The combo box
	 */
	public CustomComboBox createCustomComboBox() {
		
		//Create the combo box with these default settings and return it
		return new CustomComboBox(MainController.foregroundColorThree, 2,
				MainController.backgroundColorOne, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64),
				createCustomScrollbar(), MainController.backgroundColorOne, MainController.foregroundColorThree,
				new Font(MainController.fontOne.getFontName(), Font.PLAIN, 14));
		
	}
	
	/**
	 * Subdivide a string in to a series of sub strings whenever the pixel width of said string is higher than a given width
	 * @param g2d Graphics2D instance
	 * @param s String to subdivide
	 * @param maxWidth Width the string should not be greater than
	 * @return An array list of sub strings that make out the entire passed string but whose pixel width are never greater than the passed max width
	 */
	public ArrayList<String> subdivideString(Graphics2D g2d, String s, int maxWidth) {
		
		String[] singleWordsArray = s.split(" "); //Divide in the single words (Split when there is a space)
		
		ArrayList<String> returnList = new ArrayList<String>();
		
		String tempString = singleWordsArray[0]; //First word
		int wordIndex = 1;
		while(wordIndex < singleWordsArray.length) { //For all the words in the string
			
			//Add word by word
			tempString += " " + singleWordsArray[wordIndex]; //Add the next word to a temp string
			
			if(g2d.getFontMetrics(g2d.getFont()).stringWidth(tempString) > maxWidth) { //If it's too long
				returnList.add(tempString); //Add the temp string to the return list
				tempString = ""; //Reset temp string (so it's pixel's width is 0)
			}
			
			wordIndex++; //Next word
			
		}
		
		if(!tempString.equals("")) { //If there are leftover words
			returnList.add(tempString); //Add the temp string (that now contains the leftover words) to the list
		}
		
		return returnList;
		
	}
	
	/**
	 * Check if in the database there are flights whose take off time is before the current system time
	 * if so, it puts them in a string array list
	 * @return The string array list containing the flights whose take off time is before the current system time
	 */
	public ArrayList<String> checkForNotifications() {
		
		ArrayList<String> returnList = new ArrayList<String>();
		
		Date currentTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()); //Get system time and convert it to the Date form

		returnList = new VoloDAO().getIDListOfFlightsTakeOffTimePassed(currentTime);
		
		return returnList;
		
	}
	
	/**
	 * Colorize an image with a given color
	 * @param image The image to colorize
	 * @param color The color the image should be turned in
	 * @return The dyed image
	 */
	public BufferedImage colorizeBufferedImage(BufferedImage image, Color color) {
		
		//Get image size
	    int w = image.getWidth();
	    int h = image.getHeight();
	    
	    BufferedImage dyed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB); //Initialize the new dyed image
	    Graphics2D g = dyed.createGraphics(); //Create graphics from the dyed image
	    
	    //Draw the image and set alpha composite
	    g.drawImage(image, 0, 0, null);
	    g.setComposite(AlphaComposite.SrcAtop);
	    //Set color
	    g.setColor(color);
	    //Fill a rectangle with said color of the image size
	    g.fillRect(0, 0, w, h);
	    g.dispose(); //Dispose graphics
	    
	    return dyed;
	  
	}
	
	/**
	 * Generate a given amount of random flights to add into the database
	 * @param flightAmount The amount of flights to generate
	 */
	public void debugGenerateRandomFlights(int flightAmount) {
		
		//Create destinations array
		ArrayList<String> destinations = new ArrayList<String>();
		try {
			File textFile = new File("txts/destinations.txt");
			Scanner reader = new Scanner(textFile);
			while (reader.hasNextLine()) {
				destinations.add(reader.nextLine());
			}
			reader.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//Create possible queue list
		ArrayList<String> possibleQueues = new ArrayList<String>();
		possibleQueues.add("Famiglia");
		possibleQueues.add("Priority");
		possibleQueues.add("Diversamente Abili");
		possibleQueues.add("Business Class");
		possibleQueues.add("Standard Class");
		possibleQueues.add("Economy Class");
		
		for(int i = 0; i < flightAmount; i++) {
			
			//Random generation
			Random r = new Random();
			
			//Gate
			int gate = r.nextInt((MainController.gateAirportNumber + 1) - 1) + 1; //Range 1 to 12
			
			//Company
			int nomeCompagniaInt = r.nextInt(4); //Range 0 to 3
			String nomeCompagnia;
			switch(nomeCompagniaInt) {
				case 0: nomeCompagnia = "AirFrance"; break;
				case 1: nomeCompagnia = "Alitalia"; break;
				case 2: nomeCompagnia = "EasyJet"; break;
				case 3: nomeCompagnia = "Ryanair"; break;
				default: nomeCompagnia = "AirFrance";
			}
			
			//Destination
			int destinationNumber = r.nextInt(destinations.size() - 1) + 1; //Range 1 to destination.size() - 1;
			String destinazione = destinations.get(destinationNumber);
			
			//Take off date
			Date date1 = null;
		    Date date2 = null;
			try {
				date1 = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2019"); //Lower range
				date2 = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2022"); //Higher range
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    
			Date data = new Date(ThreadLocalRandom.current().nextLong(date1.getTime(), date2.getTime()));
			
			//String list of queues
			ArrayList<String> listaCode = new ArrayList<String>();
			Collections.shuffle(possibleQueues); //Shuffle possible queues
			
			int queueAmount = r.nextInt(7 - 1) + 1; //Range 1 to 6
			//Add the first queueAmount of queues to the listaCode
			for(int j = 0; j < queueAmount; j++) {
				listaCode.add(possibleQueues.get(j));
			}
			
			//Creation
			//Get company class from it's name
			CompagniaAerea compagnia = null;
			for(CompagniaAerea c : getListaCompagnie()) {
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
				coda.setPersoneInCoda(r.nextInt(101)); //Range 0 to 100
				try {
					coda.setTipo(s);
				} catch (NonExistentQueueTypeException e) {
					e.printStackTrace();
				}
				list.add(coda);
			}
			
			//Create gate
			Gate g = new Gate();
			g.setListaCode(list);
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
			if(mainController.checkIfSlotIsTaken(s, gate, null)) {
				break;
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
			v.setNumeroPrenotazioni(0);
			
			//Insert in the database
			VoloDAO dao = new VoloDAO();
			dao.insertFlight(this, v);
			
			//Chance to make the flight taken off or cancelled
			boolean allowTakenOffOrCancelled = true;
			if(allowTakenOffOrCancelled) {
				
				int chance = r.nextInt(101 - 1) + 1; //Range 1 to 100

				//Cancelled flights (15%)
				if(chance <= 15) {
					
					new VoloDAO().setFlightAsCancelled(this, v);
					v.setCancellato(true);
					
				}else if(chance <= 50) { //Taken off flights (35%)
					
					//Generate take off date
					Date takeOffDate = v.getOrarioDecollo();
					Calendar calendar = Calendar.getInstance(); //Create a calendar instance
					calendar.setTime(takeOffDate);
					//30% chance to add something to the take off date to make the flight take off late
					if((r.nextInt(101 - 1) + 1) <= 30) {
						
						int minutesToAdd = r.nextInt(301 - 15) + 15; //Add from between 15 and 300 minutes to the take off date
						calendar.add(Calendar.MINUTE, minutesToAdd); //Add minutes
						takeOffDate = calendar.getTime(); //Set the modified date as the new take off time
						
					}
					
					//Get flight's slot
					Slot slot = (new SlotDAO().getSlotByID(v.getID()));
					
					//If the flight took off in the estimated slot time
					if(takeOffDate.before(slot.getFineTempoStimato())) {
						
						//Effective and estimated times coincide
						slot.setInizioTempoEffettivo(slot.getInizioTempoStimato());
						slot.setFineTempoEffettivo(slot.getFineTempoStimato());
						
					}else { //Otherwise
						
						//The flight took off later than the end of the estimated slot, therefore it did so in another slot time, calculate it
						calendar.setTime(takeOffDate); //Set the calendar time to the passed date
						
						//Get lower range
						calendar.add(Calendar.MINUTE, -5);
						Date inizioTempoEffettivo = new Date();
						inizioTempoEffettivo = calendar.getTime();
						slot.setInizioTempoEffettivo(inizioTempoEffettivo);
						
						//Get higher range
						calendar.add(Calendar.MINUTE, 15);
						Date fineTempoEffettivo = new Date();
						fineTempoEffettivo = calendar.getTime();
						slot.setFineTempoEffettivo(fineTempoEffettivo);
						
					}
					
					v.setSlot(slot);
					
					//Update slot in database
					SlotDAO daoSlot = new SlotDAO();
					daoSlot.updateTempoEffettivo(this, slot, v.getID());
					
					//Update flight in database
					new VoloDAO().setFlightAsTakenOff(this, v);
					v.setPartito(true);
				}
				
			}
			
			//Print flight info
			v.printFlightInfo();
			System.out.println("");
			
		}
		
	}
	
	//Setters and getters
	public ArrayList<Volo> getFlightList() {
		return flightList;
	}
	public void setFlightList(ArrayList<Volo> list) {
		flightList = list;
	}
	public boolean isLookingAtArchive() {
		return lookingAtArchive;
	}
	public DashboardPanel getDashboardPanel() {
		return dashboardPanel;
	}
	public void setNotificationList(ArrayList<String> notificationList) {
		this.notificationsList = notificationList;
	}
	public ArrayList<CompagniaAerea> getListaCompagnie() {
		return listaCompagnie;
	}

	
}

enum hoveringAnimationStatus {
	
	selected, //Mouse hovering on panel, animation finished
	selecting, //Mouse hovering on panel, animation in progress
	unselected, //Mouse not hovering on panel, animation finished
	unselecting //Mouse not hovering on panel, animation in progress
	
}