import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;

import java.awt.Color;
import javax.swing.SpringLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.CardLayout;
import javax.swing.JLayeredPane;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;


public class MainFrame extends JFrame {

	private MainController mainController; //Linked controller
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Screen dimensions
	private JPanel contentPanel; //Panel that changes the content displayed
	private JLayeredPane centerPanel; //Panel containing content panel and dash board
	private ArrayList<CompagniaAerea> listaCompagnie = new ArrayList<CompagniaAerea>(); //Array containing the companies
	private DashboardPanel dashboardPanel; //Dash board panel
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //Date format
	
	private JPanel currentPanel = null; //Current panel being displayed in the content panel
	private boolean lookingAtArchive; //To differentiate when on the panel CheckFlightsPanel if looking at the archive or not
	
	final private int screenWidth = (int)screenSize.getWidth(); //Screen width
	final private int screenHeight = (int)screenSize.getHeight(); //Screen height
	final private int frameWidth = 1200; //Frame width
	final private int frameHeight = 700; //Frame height
	
	private Point mouseClickPoint; //Mouse position
	final private int maxDashboardWidth = 200; //Max width of the dash board
	final private int minDashboardWidth = 30; //Minimum width of the dash board
	private int dashboardWidth = minDashboardWidth; //Current width of the dash board
	
	private ArrayList<Volo> flightList = null; //List of the flights to be displayed in the content panel

	/**
	 * Frame containing the entire application
	 * @param c Link to the mainController
	 */
	public MainFrame(MainController c) {
		
		mainController = c; //Link controller and frame
		
		//Get all the companies from the database
		CompagniaAereaDAO dao = new CompagniaAereaDAO();
		listaCompagnie = dao.getAllCompagniaAerea();
		
		//Frame properties
		setResizable(false); //Not re sizable
		setUndecorated(true); //Undecorated (No close, minimize buttons)
		setTitle("Title"); //Set frame title
		setBounds((screenWidth/2) - (frameWidth/2), (screenHeight/2) - (frameHeight/2), frameWidth, frameHeight); //Set the frame sizes and position in the middle of the screen
		
		//Main Panel
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
		upperPanel.add(buttonMinimize); //Add minimize button to the control panel
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
		upperPanel.add(buttonClose); //Add close button to the control panel
		//Mouse listeners of close button
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Make frame invisible
				dispose(); //Dispose of it
				System.exit(0); //Close application
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
		
		dashboardPanel = new DashboardPanel(centerPanel.getPreferredSize().height, this, mainController);
		dashboardPanel.setName("dashboardPanel");
		centerPanel.add(dashboardPanel);
		
		contentPanel = new JPanel(); //Create content panel
		contentPanel.setName("contentPanel"); //Name component
		contentPanel.setBounds(72, 2, centerPanel.getPreferredSize().width - 72 - 4, centerPanel.getPreferredSize().height - 4); //Position the content panel
		centerPanel.add(contentPanel); //Add the content panel to the center panel
		contentPanel.setLayout(new BorderLayout(0, 0)); //Set content panel's layout
		
		flightList = searchFlights("", "", -1, null, null, true, true, true, true, true, true, true); //Start a research to get all of the flights
		
		setContentPanelToCheckFlightsPanel(false); //Set content panel at the start of the application to the CheckFlightsPanel
		
		//Check if the dash board has not closed correctly every 1000ms (if mouse exited event gets skipped)
		checkDashboardStatus(1000);
		
	}
	
	/**
	 * Set contentPanel to the CheckFlightsPanel
	 * @param lookingAtArchive If the panel has to show the archive of the flights (where 'partito' or 'cancellato' = 1)
	 * @return If the panel got changed
	 */
	public boolean setContentPanelToCheckFlightsPanel(boolean lookingAtArchive) {
		
		//Check if the current panel is already in place in the content panel (They have the same class name)
		if(currentPanel != null && currentPanel.getClass().getName().equals("CheckFlightsPanel")) {
			//Differentiate if looking at the archive or not
			if(this.lookingAtArchive == lookingAtArchive) { //Already looking at the same type of CheckFlightsPanel (lookingAtArchive here and the one passed are the same)
				System.out.println("Already on this panel!");
				return false; //Don't replace the content panel
			}
		}
		
		Rectangle bounds = new Rectangle(contentPanel.getBounds()); //Get bounds of the content panel
		centerPanel.remove(contentPanel); //Remove content panel from the center panel
		
		contentPanel = new CheckFlightsPanel(bounds, this, mainController, lookingAtArchive); //Create new panel and store it in the contentPanel
		
		contentPanel.setBounds(bounds); //Position the content panel based on the bounds gathered beforehand
		contentPanel.setName("checkFlightsPanel"); //Set name
		centerPanel.add(contentPanel); //Add new content panel to the center panel
		
		centerPanel.repaint(); //Repaint center panel
		contentPanel.repaint(); //Repaint content panel
		contentPanel.revalidate(); //Re validate content panel
		
		currentPanel = contentPanel; //Update current panel
		this.lookingAtArchive = lookingAtArchive; //Set if looking at the archive or not
		
		dashboardPanel.toggleSearchPanel(true); //Show search panel on dash board
		
		return true;
		
	}

	/**
	 * Set contentPanel to the CreateFlightsPanel
	 * @return If the panel got changed
	 */
	public boolean setContentPanelToCreateFlightsPanel() {
		
		//Check if the current panel is already in place in the content panel (They have the same class name)
		if(currentPanel != null && currentPanel.getClass().getName().equals("CreateFlightPanel")) {
			System.out.println("Already on this panel!");
			return false; //Dont replace the content panel
		}
		
		Rectangle bounds = new Rectangle(contentPanel.getBounds()); //Get bounds of the content panel
		centerPanel.remove(contentPanel); //Remove content panel from the center panel
		
		contentPanel = new CreateFlightPanel(bounds, this, mainController); //Create new panel and store it in the contentPanel
		
		contentPanel.setBounds(bounds); //Position the content panel based on the bounds gathered beforehand
		contentPanel.setName("createFlightsPanel"); //Set name
		centerPanel.add(contentPanel); //Add new content panel to the center panel
		
		contentPanel.repaint(); //Repaint content panel
		contentPanel.revalidate(); //Re validate content panel
		centerPanel.repaint(); //Repaint center panel
		
		currentPanel = contentPanel; //Update current panel
		
		dashboardPanel.toggleSearchPanel(false); //Hide search panel on dash board
		
		return true;
		
	}
	
	/**
	 * Set contentPanel to the EditFlightPanel
	 * @return If the panel got changed
	 */
	public boolean setContentPanelToEditFlightPanel(Volo flightToUpdate) {
		
		if(flightToUpdate == null) {
			return false;
		}
		
		//Check if the current panel is already in place in the content panel (They have the same class name)
		if(currentPanel != null && currentPanel.getClass().getName().equals("EditFlightPanel")) {
			System.out.println("Already on this panel!");
			return false; //Don't replace the content panel
		}
		
		Rectangle bounds = new Rectangle(contentPanel.getBounds()); //Get bounds of the content panel
		centerPanel.remove(contentPanel); //Remove content panel from the center panel
		
		contentPanel = new EditFlightPanel(bounds, this, mainController, flightToUpdate); //Create new panel and store it in the contentPanel
		
		contentPanel.setBounds(bounds); //Position the content panel based on the bounds gathered beforehand
		contentPanel.setName("editFlightPanel"); //Set name
		centerPanel.add(contentPanel); //Add new content panel to the center panel
		
		contentPanel.repaint(); //Repaint content panel
		contentPanel.revalidate(); //Re validate content panel
		centerPanel.repaint(); //Repaint center panel
		
		currentPanel = contentPanel; //Update current panel
		
		dashboardPanel.toggleSearchPanel(false); //Hide search panel on dash board
		
		return true;
		
	}

	/**
	 * Set contentPanel to the ViewFlightInfoPanel
	 * @return If the panel got changed
	 */
	public boolean setContentPanelToViewFlightInfoPanel(Volo volo) {
		
		if(volo == null) {
			return false;
		}
		
		//Check if the current panel is already in place in the content panel (They have the same class name)
		if(currentPanel != null && currentPanel.getClass().getName().equals("ViewFlightInfoPanel")) {
			System.out.println("Already on this panel!");
			return false; //Dont replace the content panel
		}
		
		Rectangle bounds = new Rectangle(contentPanel.getBounds()); //Get bounds of the content panel
		centerPanel.remove(contentPanel); //Remove content panel from the center panel
		
		contentPanel = new ViewFlightInfoPanel(bounds, this, mainController, volo); //Create new panel and store it in the contentPanel
		
		contentPanel.setBounds(bounds); //Position the content panel based on the bounds gathered beforehand
		contentPanel.setName("viewFlightsPanel"); //Set name
		centerPanel.add(contentPanel); //Add new content panel to the center panel
		
		contentPanel.repaint(); //Repaint content panel
		contentPanel.revalidate(); //Re validate content panel
		centerPanel.repaint(); //Repaint center panel
		
		currentPanel = contentPanel; //Update current panel
		
		dashboardPanel.toggleSearchPanel(false); //Hide search panel on dash board
		
		return true;
		
	}
	
	/**
	 * Set contentPanel to the StatisticsPanel
	 * @return If the panel got changed
	 */
	public boolean setContentPanelToStatisticsPanel() {
		
		//Check if the current panel is already in place in the content panel (They have the same class name)
		if(currentPanel != null && currentPanel.getClass().getName().equals("StatisticsPanel")) {
			System.out.println("Already on this panel!");
			return false; //Don t replace the content panel
		}
		
		Rectangle bounds = new Rectangle(contentPanel.getBounds()); //Get bounds of the content panel
		centerPanel.remove(contentPanel); //Remove content panel from the center panel
		
		contentPanel = new StatisticsPanel(bounds, this, mainController); //Create new panel and store it in the contentPanel
		
		contentPanel.setBounds(bounds); //Position the content panel based on the bounds gathered beforehand
		contentPanel.setName("statisticsPanel"); //Set name
		centerPanel.add(contentPanel); //Add new content panel to the center panel
		
		contentPanel.repaint(); //Repaint content panel
		contentPanel.revalidate(); //Re validate content panel
		centerPanel.repaint(); //Repaint center panel
		
		currentPanel = contentPanel; //Update current panel
		
		dashboardPanel.toggleSearchPanel(false); //Hide search panel on dash board
		
		return true;
		
	}
	
	/**
	 * Set contentPanel to the CheckGatePanel
	 * @return If the panel got changed
	 */
	public boolean setContentPanelToCheckGatePanel(ArrayList<Volo> flightList, int gateNumber) {
		
		//Check if the current panel is already in place in the content panel (They have the same class name)
		if(currentPanel != null && currentPanel.getClass().getName().equals("CheckGatePanel")) {
			System.out.println("Already on this panel!");
			return false; //Don t replace the content panel
		}
		
		Rectangle bounds = new Rectangle(contentPanel.getBounds()); //Get bounds of the content panel
		centerPanel.remove(contentPanel); //Remove content panel from the center panel
		
		contentPanel = new CheckGatePanel(bounds, this, mainController, flightList, gateNumber); //Create new panel and store it in the contentPanel
		
		contentPanel.setBounds(bounds); //Position the content panel based on the bounds gathered beforehand
		contentPanel.setName("checkGatePanel"); //Set name
		centerPanel.add(contentPanel); //Add new content panel to the center panel
		
		contentPanel.repaint(); //Repaint content panel
		contentPanel.revalidate(); //Re validate content panel
		centerPanel.repaint(); //Repaint center panel
		
		currentPanel = contentPanel; //Update current panel
		
		dashboardPanel.toggleSearchPanel(false); //Hide search panel on dash board
		
		return true;
		
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
		
		String query = "SELECT * FROM volo INNER JOIN gate ON volo.id = gate.IDVolo INNER JOIN slot ON volo.id = slot.IDVolo WHERE ("; //Initialize query string
		
		//ID field
		String idQuery;
		if(idField != null){
			idQuery = "(id LIKE '%" + idField + "%' OR '" + idField + "' IS NULL)"; //If idField was not empty
		}else{
			idQuery = "(id LIKE '" + idField + "' OR " + idField + " IS NULL)"; //If idField was empty (this is always true)
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
		query += idQuery + " AND \n" + destinationQuery + " AND \n" + startTimeQuery + " AND \n" + endTimeQuery + " AND \n" + gateNumberQuery + " AND \n" + companyQuery + " AND \n" + archiveOnlyQuery + ")";
		//System.out.println(query);
		
		//Execute query
		ArrayList<Volo> newFlightList = (new VoloDAO().searchFlight(query));
		
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
	
	public CustomComboBox createCustomComboBox() {
		
		//Create the combo box with these default settings and return it
		return new CustomComboBox(MainController.foregroundColorThree, 2,
				MainController.backgroundColorOne, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 64),
				createCustomScrollbar(), MainController.backgroundColorOne, MainController.foregroundColorThree,
				new Font(MainController.fontOne.getFontName(), Font.PLAIN, 14));
		
	}
	
	public ArrayList<String> subdivideString(Graphics2D g2d, String s, int maxWidth) {
		
		String[] singleWordsArray = s.split(" "); //Divide in the single words
		
		ArrayList<String> returnList = new ArrayList<String>(); //return list of sub strings
		
		String tempString = singleWordsArray[0]; //First word
		int wordIndex = 1;
		while(wordIndex < singleWordsArray.length) { //For all the letters
			
			//Add word by word
			tempString += " " + singleWordsArray[wordIndex]; //Add the next word
			
			if(g2d.getFontMetrics(g2d.getFont()).stringWidth(tempString) > maxWidth) { //If it's too long
				returnList.add(tempString); //Add temp string to the list
				tempString = ""; //Reset temp string
			}
			
			wordIndex++;
			
		}
		
		if(!tempString.equals("")) { //If there are leftover letters
			returnList.add(tempString); //Add temp string to the list
		}
		
		return returnList;
		
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

