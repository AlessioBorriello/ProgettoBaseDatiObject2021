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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.CardLayout;
import javax.swing.JLayeredPane;
import java.awt.SystemColor;


public class MainFrame extends JFrame {

	private MainController controller; //Linked controller
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Screen dimensions
	private JPanel contentPanel; //Panel that changes
	private JLayeredPane centerPanel; //Panel containing content panel and dashboard
	
	private JPanel currentPanel = null; //Current panel being displayed in the content panel
	
	final private int screenWidth = (int)screenSize.getWidth(); //Screen width
	final private int screenHeight = (int)screenSize.getHeight(); //Screen height
	final private int frameWidth = 1200; //Frame width
	final private int frameHeight = 700; //Frame height
	
	private Point mouseClickPoint; //Mouse position
	final private int maxDashboardWidth = 200; //Max width of the dashboard
	final private int minDashboardWidth = 30; //Min width of the dashboard
	private int dashboardWidth = minDashboardWidth; //Current width of the dashboard

	public MainFrame(MainController c) {
		
		
		controller = c; //Link controller and frame
		
		//Frame proprieties
		setResizable(false); //Not resizable
		setUndecorated(true); //Undecorated (No close, minimize buttons)
		setTitle("Title"); //Set frame title
		setBounds((screenWidth/2) - (frameWidth/2), (screenHeight/2) - (frameHeight/2), frameWidth, frameHeight); //Set the frame sizes and position (not fullscreen)
		
		//Main Panel
		JPanel mainPanel = new JPanel(); //Create panel
		mainPanel.setBackground(SystemColor.inactiveCaption); //Set background
		mainPanel.setName("mainPanel"); //Set name of the component
		mainPanel.setSize(new Dimension(getWidth(), getHeight())); //Set size variables
		mainPanel.setPreferredSize(new Dimension(getWidth(), getHeight())); //Set preferred size variables
		mainPanel.setBorder(null); //Set border to null
		setContentPane(mainPanel); //Add panel to the frame
		mainPanel.setLayout(null); //Set layout of the main panel
		
		JPanel upperPanel = new JPanel(); //Create upper panel
		upperPanel.setBackground(SystemColor.textHighlight); //Set background
		upperPanel.setName("upperPanel"); //Name component
		final int upperPanelHeight = 30; //Height of the upper panel
		upperPanel.setBounds(0, 0, frameWidth, upperPanelHeight); //Position of the upper panel
		mainPanel.add(upperPanel); //Add upper panel to the main panel
		upperPanel.setLayout(new BorderLayout(0, 0)); //Set layout of the upper panel
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
		
		JPanel controlPanel = new JPanel(); //Create control panel
		controlPanel.setName("upperPanel"); //Name component
		controlPanel.setBackground(upperPanel.getBackground()); //Set background
		upperPanel.add(controlPanel, BorderLayout.EAST); //Add control panel to the upper panel, east position of it's border layout
		controlPanel.setLayout(new GridLayout(1, 0, 4, 0)); //Set layout of the control panel
		
		JButton buttonMinimize = new JButton("Minimize"); //Create minimize button
		buttonMinimize.setName("buttonMinimize"); //Name component
		controlPanel.add(buttonMinimize); //Add minimize button to the control panel
		//Mouse listeners of minimize button
		buttonMinimize.addMouseListener(new MouseAdapter() {
			//Mouse clicked event
			public void mouseClicked(MouseEvent e) {
				setState(ICONIFIED); //Minimize frame
			}
		});
		
		JButton buttonClose = new JButton("Close"); //Create close button
		buttonClose.setName("buttonClose"); //Name component
		controlPanel.add(buttonClose); //Add close button to the control panel
		//Mouse listeners of close button
		buttonClose.addMouseListener(new MouseAdapter() {
			//Mouse clicked event
			public void mouseClicked(MouseEvent e) {
				setVisible(false); //Make frame invisible
				dispose(); //Dispose of it
				System.exit(0); //Close application
			}
		});
		
		centerPanel = new JLayeredPane(); //Create centerPanel
		centerPanel.setName("centerPanel"); //Name component
		centerPanel.setBounds(0, upperPanelHeight, frameWidth, frameHeight - upperPanelHeight); //Position of the central panel
		centerPanel.setPreferredSize(new Dimension(frameWidth, frameHeight - upperPanelHeight)); //Set center panel preferred size
		mainPanel.add(centerPanel); //Add center panel to the main panel
		
		DashboardPanel dashboardPanel = new DashboardPanel(centerPanel.getPreferredSize().height, this, controller);
		dashboardPanel.setName("dashboardPanel");
		centerPanel.add(dashboardPanel);
		
		contentPanel = new JPanel(); //Create content panel
		contentPanel.setName("contentPanel"); //Name component
		contentPanel.setBackground(SystemColor.controlHighlight); //Set background
		contentPanel.setBounds(72, 2, centerPanel.getPreferredSize().width - 72 - 4, centerPanel.getPreferredSize().height - 4); //Position the content panel
		centerPanel.add(contentPanel); //Add the content panel to the center panel
		contentPanel.setLayout(new BorderLayout(0, 0)); //Set content panel's layout
		
		setContentPanelToCheckFlightsPanel();
		//changeContentPanel();
		
	}

	public boolean setContentPanelToCheckFlightsPanel() {
		
		//Check if the current panel is already in place in the content panel (They have the same class name)
		if(currentPanel != null && currentPanel.getClass().getName().equals("CheckFlightsPanel")) {
			System.out.println("Already on this panel!");
			return false; //Dont replace the content panel
		}
		
		Rectangle bounds = new Rectangle(contentPanel.getBounds()); //Get bounds of the content panel
		centerPanel.remove(contentPanel); //Remove content panel from the center panel
		
		contentPanel = new CheckFlightsPanel(bounds, this, controller); //Create new panel and store it in the contentPanel
		
		contentPanel.setBounds(bounds); //Position the content panel based on the bounds gathered beforehand
		centerPanel.add(contentPanel); //Add new content panel to the center panel
		
		contentPanel.repaint(); //Repaint content panel
		contentPanel.revalidate(); //Revalidate content panel
		centerPanel.repaint(); //Repaint center panel
		
		currentPanel = contentPanel; //Update current panel
		return true;
		
	}

	public boolean setContentPanelToCreateFlightsPanel() {
		
		//Check if the current panel is already in place in the content panel (They have the same class name)
		if(currentPanel != null && currentPanel.getClass().getName().equals("CreateFlightPanel")) {
			System.out.println("Already on this panel!");
			return false; //Dont replace the content panel
		}
		
		Rectangle bounds = new Rectangle(contentPanel.getBounds()); //Get bounds of the content panel
		centerPanel.remove(contentPanel); //Remove content panel from the center panel
		
		contentPanel = new CreateFlightPanel(bounds, this, controller); //Create new panel and store it in the contentPanel
		
		contentPanel.setBounds(bounds); //Position the content panel based on the bounds gathered beforehand
		centerPanel.add(contentPanel); //Add new content panel to the center panel
		
		contentPanel.repaint(); //Repaint content panel
		contentPanel.revalidate(); //Revalidate content panel
		centerPanel.repaint(); //Repaint center panel
		
		currentPanel = contentPanel; //Update current panel
		return true;
		
	}

}

