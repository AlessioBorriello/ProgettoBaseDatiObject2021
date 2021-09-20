import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLayeredPane;

@SuppressWarnings("serial")
public class NotificationsPanel extends JPanel{

	private MainFrame mainFrame;

	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");  
	LocalDateTime currentTime = LocalDateTime.now();  
	
	private ArrayList<String> flightIDList;
	
	private JPanel flightListPanel;
	private JScrollPane scrollPanel;
	
	private int maxHeight = 400; //Maximum panel's height before the list of flights shows a scroll bar
	private int minimumHeight = 140; //Minimum height of the panel
	
	/**
	 * Panel showing the current system time and in a list all the flights in the database that should have taken off already (take off time < current time)
	 * @param mainFrame Link to the MainFrame
	 * @param flightIDList List of the ID's of the flights in the database whose take off time is < than the current time
	 */
	public NotificationsPanel(MainController mainController, MainFrame mainFrame, ArrayList<String> flightIDList) {
		
		this.mainFrame = mainFrame;
		this.flightIDList = flightIDList;
		
		setSize(240, minimumHeight); //Set size of the panel
		setLayout(null); //Set layout to absolute
		
		class IncreaseTime extends TimerTask {
			
			//Override run method
			public void run() {
				currentTime = currentTime.plusSeconds(1); //Increase current time by 1 second
				repaint(); //Repaint panel
			}
		}
	
		IncreaseTime increaseTime = new IncreaseTime();
		Timer t = new Timer();
		t.schedule(increaseTime, 1000, 1000); //Increase time once a second
		
		scrollPanel = new JScrollPane(); //Create scroll panel
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); //Never use horizontal scroll bar
		scrollPanel.getVerticalScrollBar().setUnitIncrement(14); //Set scroll bar speed
		scrollPanel.setBorder(null); //Set border to null
		scrollPanel.setVerticalScrollBar(mainFrame.createCustomScrollbar()); //Set vertical scroll bar to a custom scroll bar
		scrollPanel.setBounds(4, 100, getWidth() - 6, minimumHeight - 102); //Set scroll panel bounds, it starts 100 pixels lower than the panel and it's height is the minimum - 102
		add(scrollPanel); //Add scroll panel
		
		flightListPanel = (new JPanel() {
			
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
			
		}); //Create list panel containing the ID of the flights being notified about
		flightListPanel.setLayout(null); //Set layout to absolute
		scrollPanel.setViewportView(flightListPanel); //Make the scroll panel watch this panel
		
		populateFlightList(flightIDList); //Populate the flight list panel with the ID of the flights being notified about
		
	}
	
	/**
	 * Create a button for each of the ID in the given list and add them to the flight list panel, it than calculates it's new height and applies it
	 * if the height after the buttons have been added is higher than the max height, then it sets it's height to the max and the scroll panel starts
	 * showing it's scroll bar, to allow the user to navigate the rest of the buttons
	 * @param flightIDList List of ID's of the flight to create the buttons out of
	 */
	public void populateFlightList(ArrayList<String> flightIDList) {
		
		NotificationsPanel thisPanel = this; //Link to this panel (to remove it if the user clicks on a button)
		
		int index = 0; //Button index in the list
		int height = 50; //Button height
		int vGap = 2; //Gap between each button
		
		//For every string in the id list
		for(String s : flightIDList) {
			//Create a custom button
			CustomButton buttonFlight = new CustomButton(s, null, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 48), 
					MainController.foregroundColorThree, 18, true, MainController.foregroundColorThree, 2); //Create button create flight
			buttonFlight.setName("buttonFlight"); //Name component
			buttonFlight.setBounds(0, 0 + ((height + vGap) * index), getWidth() - 8, height); //Set bounds
			buttonFlight.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					buttonFlight.selectAnimation(8);
				}

				public void mouseExited(MouseEvent e) {
					buttonFlight.unselectAnimation(8);
				}
			});
			buttonFlight.addMouseListener(new MouseAdapter() {
				//When mouse clicked
				public void mouseClicked(MouseEvent e) {
					//Go to view flight info
					if(mainFrame.changeContentPanel(new ViewFlightInfoPanel(new Rectangle(72, 2, 1124, 666), mainFrame, new VoloDAO().getFlightByID(mainFrame, s)), false, false)) {
						JLayeredPane centerPanel = (JLayeredPane)mainFrame.getComponentByName(mainFrame, "centerPanel"); //Get center panel
						if(centerPanel != null) {
							centerPanel.remove(thisPanel); //Remove this panel from the centerPanel
						}
					}
					
				}
			});
			flightListPanel.add(buttonFlight);
			
			index++;
		}
		
		//Calculate new height of the list (after the buttons have been added)
		int newListPanelHeight = ((height + vGap) * index);
		
		//Calculate new panels heights clamping it so that it's height does not get higher than the max height variable (+102 because the scroll panel starts 100 pixels lower than the notification panel)
		int newPanelHeight = (newListPanelHeight + 102 > maxHeight)? maxHeight : 102 + newListPanelHeight; //Max clamp
		newPanelHeight = (newPanelHeight < minimumHeight)? minimumHeight : newPanelHeight; //Minimum clamp
		
		//Set sizes
		setSize(getSize().width, newPanelHeight);
		scrollPanel.setSize(scrollPanel.getSize().width, newPanelHeight - 102);
		
		//Create group layout
		GroupLayout gl_gridPanel = new GroupLayout(flightListPanel);
		gl_gridPanel.setHorizontalGroup(
			gl_gridPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, getWidth(), Short.MAX_VALUE)
		);
		gl_gridPanel.setVerticalGroup(
			gl_gridPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, newListPanelHeight, Short.MAX_VALUE)
		);
		flightListPanel.setLayout(gl_gridPanel); //Set layout to the grid panel
		
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		//AA
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    //Text AA
	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		//Background
		g2d.setColor(MainController.backgroundColorOne);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		//Draw border
		g2d.setColor(MainController.foregroundColorThree);
		g2d.setStroke(new BasicStroke(3));
		g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
		
		//Draw time
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 18));
		String timeString = dateFormat.format(currentTime);
		int timeStringLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(timeString);
		g2d.drawString(timeString, (getWidth()/2) - (timeStringLength/2), 30);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(20, 40, getWidth() - 20, 40);
		
		//If no notifications
		if(flightIDList.size() == 0) {
			String s = "Nessun volo dovrebbe";
			int sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
			g2d.drawString(s, (getWidth()/2) - (sLength/2), 65);
			s = "essere partito";
			sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
			g2d.drawString(s, (getWidth()/2) - (sLength/2), 85);
		}else { //Otherwise
			String s = "Questi voli dovrebbero";
			int sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
			g2d.drawString(s, (getWidth()/2) - (sLength/2), 65);
			s = "essere partiti";
			sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
			g2d.drawString(s, (getWidth()/2) - (sLength/2), 85);
		}
			
	}
	
}
