import javax.swing.JPanel;
import java.awt.SystemColor;
import javax.swing.JButton;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import java.awt.Point;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DashboardPanel extends JPanel {

	private int height; //Height of the dashboard
	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	
	//Animation
	final private int startingPositionX = -230; //Starting x of the dashboard panel
	final private int endingPositionX = 2; //Ending x of the dashboard panel
	private int currentPositionX = startingPositionX; //Current x of the dashboard panel (starts fully retracted (starting position))
	
	final private float animationMultiplierSpeedMax = 6.0f; //Max value of the multiplier of the animation speed
	final private float animationMultiplierSpeedAmount = .28f; //Amount to increase for each TimerTask the multiplier value
	private float animationMultiplierSpeedCurrent = 1.0f; //Current value of the multiplier of the animation speed
	
	private dashboardAnimationStatus status = dashboardAnimationStatus.retracted; //Status of the animation (starts fully retracted)
	
	/**
	 * Dashboard on the side with controls the user can interact with
	 * @param h Dashboard height
	 * @param mf Link to the mainFrame
	 * @param mc Link to the mainController
	 */
	public DashboardPanel(int h, MainFrame mf, MainController mc) {
		
		height = h; //Set height value of the dashboard
		mainFrame = mf; //Set the main frame
		mainController = mc; //Set the main controller
		
		setBackground(SystemColor.activeCaptionBorder); //Set background
		//setBounds(startingPositionX,  2,  300, height - 4); //Position board in the starting position
		setBounds(startingPositionX,  2,  300, 670); //Debug to show dashboard in the design tab, this row should be replaced with the one above
		setLayout(null); //Set layout to absolute
		
		
		SearchPanel searchPanel = new SearchPanel(mainController, mainFrame); //Create search panel
		searchPanel.setLocation(new Point(2, 2)); //Position panel
		searchPanel.setName("searchPanel"); //Set component name
		add(searchPanel);
		
		JPanel dashboardControlPanel = new JPanel(); //Create dasboard control panel
		dashboardControlPanel.setName("dashboardControlPanel"); //Set component name
		dashboardControlPanel.setBounds(2, 408, 296, 259); //Position dashboard control panel
		add(dashboardControlPanel); //Add dashboard control panel to the dashboard
		dashboardControlPanel.setLayout(null); //Set the dash board control's layout to absolute
		
		JButton buttonCheckFlights = new JButton("Check flights"); //Create check flights button
		buttonCheckFlights.setName("buttonCheckFlights"); //Set component name
		//Button action listener
		buttonCheckFlights.addMouseListener(new MouseAdapter() {
			//Mouse clicked
			public void mouseClicked(MouseEvent e) {
				
				//Set panel on main frame to the correct panel (not looking at the archive)
				if(mf.setContentPanelToCheckFlightsPanel(false)) { //If the panel gets changed
					
					SearchPanel searchPanel = (SearchPanel)mainController.getComponentByName(mainFrame, "searchPanel"); //Get searchPanel from the mainFrame
					if(searchPanel != null) { //If the searchPanel gets found
						searchPanel.makeSearch(); //Make search
					}
					
					//Update dashboard
					searchPanel.toggleArchiveOnlyPanel(false);
					repaint();
					revalidate();
					
				}
				
			}
		});
		buttonCheckFlights.setBounds(10, 11, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonCheckFlights); //Add to dashboardControlPanel
		
		JButton buttonFlightsArchive = new JButton("Flights archive"); //Create check flights archive button
		//Button action listener
		buttonFlightsArchive.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//Mouse clicked
				
				//Set panel on main frame to the correct panel (looking at the archive)
				if(mf.setContentPanelToCheckFlightsPanel(true)) { //If the panel gets changed
					
					SearchPanel searchPanel = (SearchPanel)mainController.getComponentByName(mainFrame, "searchPanel"); //Get searchPanel from the mainFrame
					if(searchPanel != null) { //If the searchPanel gets found
						searchPanel.makeSearch(); //Make search
					}
					
					//Update dashboard
					searchPanel.toggleArchiveOnlyPanel(true);
					repaint();
					revalidate();
					
				}
				
			}
		});
		buttonFlightsArchive.setName("buttonFlightsArchive"); //Set component name
		buttonFlightsArchive.setBounds(10, 72, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonFlightsArchive); //Add to dashboardControlPanel
		
		JButton buttonCreateNewFlight = new JButton("Create new flight"); //Create create new flight button
		buttonCreateNewFlight.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			
				//Set panel on main frame to the correct panel
				mf.setContentPanelToCreateFlightsPanel();
			
			}
		});
		buttonCreateNewFlight.setName("buttonCreateNewFlight"); //Set component name
		buttonCreateNewFlight.setBounds(10, 133, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonCreateNewFlight); //Add to dashboardControlPanel
		
		JButton buttonCheckStatistics = new JButton("Check gate statistics"); //Create check stats button
		buttonCheckStatistics.setName("buttonCheckStatistics"); //Set component name
		buttonCheckStatistics.setBounds(10, 194, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonCheckStatistics); //Add to dashboardControlPanel
		
		//Mouse listeners of the dashboard
		addMouseListener(new MouseAdapter() {
			
			//When mouse enters the dashboard
			public void mouseEntered(MouseEvent e) {
				openDashboardAnimation();
			}

			//When mouse exits the dashboard
			public void mouseExited(MouseEvent e) {
				
				//Check if the mouse has actually left the dashboard or it's just hovering over one of it's components (i.e buttons)
				Point p = new Point(e.getLocationOnScreen()); //Get position of the occurred event (mouse position)
		        SwingUtilities.convertPointFromScreen(p, e.getComponent()); //Convert p from a screen coordinates to a component's coordinate system
		        if(e.getComponent().contains(p)) { //If the point is still inside the dashboard (it's hovering over one of it's components)
		        	return; //Exit event and ignore rest of the code
		        }
		        
		        closeDashboardAnimation();
		        
			}
			
		});
		
	}
	
	/**
	 * Move the dashboard's x position to the indicated value
	 * @param positionX Where to position the dashboard
	 */
	public void moveDashboard(int positionX) {
		setLocation(positionX, getLocation().y); //Set the location
	}

	/**
	 * Makes the dashboard open with it's animation
	 */
	public void openDashboardAnimation() {
		
		//If the dashboard is not already extended
		if(status != dashboardAnimationStatus.extended) {
			
			//Extend animation
			status = dashboardAnimationStatus.extending; //Set status to extending status (animating)
			class AnimationExtending extends TimerTask { //Create animation class
				
				//Override run method
				public void run() {
					
					currentPositionX += 2 * animationMultiplierSpeedCurrent; //Increase dashboard's current position based on the multiplier
					moveDashboard(currentPositionX); //Move dashboard
					
					//If the multiplier has not reached it's max
					if(animationMultiplierSpeedCurrent < animationMultiplierSpeedMax) {
						
						animationMultiplierSpeedCurrent += animationMultiplierSpeedAmount; //Increase multiplier
						
					}
					
					//If animation is interrupted (it's status is no longer extending)
					if(status != dashboardAnimationStatus.extending) {
						
						animationMultiplierSpeedCurrent = 1.0f; //Reset the multiplier
						this.cancel(); //Stop animation
						
					}else if(currentPositionX >= endingPositionX) { //If the current position has reached it's destination (ending position)
						
						animationMultiplierSpeedCurrent = 1.0f; //Reset the multiplier
						currentPositionX = endingPositionX; //Set the current position to the ending position
						moveDashboard(currentPositionX); //Move dashboard
						status = dashboardAnimationStatus.extended; //Set status to extended status (animation complete)
						this.cancel(); //Stop animation
						
					}
					
				}
				
			}
		
			AnimationExtending animation = new AnimationExtending(); //Create animation instance
			Timer t = new Timer(); //Create timer
			t.schedule(animation, 10, 10); //Schedule the animation task in the timer with a delay of 10ms and a repeat of 10ms
			animationMultiplierSpeedCurrent = 1.0f; //Reset animation speed multiplier
			
		}
		
	}
	
	/**
	 * Makes the dashboard close with it's animation
	 */
	public void closeDashboardAnimation() {
		
		//If the dashboard is not already retracted
		if(status != dashboardAnimationStatus.retracted) {
			
			//Retract animation
			status = dashboardAnimationStatus.retracting;  //Set status to retracting status (animating)
			class AnimationRetracting extends TimerTask { //Create animation class
				
				//Override run method
				public void run() {
					
					currentPositionX -= 2 * animationMultiplierSpeedCurrent; //Decrease dashboard's current position based on the multiplier
					moveDashboard(currentPositionX); //Move dashboard
					
					//If the multiplier has not reached it's max
					if(animationMultiplierSpeedCurrent < animationMultiplierSpeedMax) {
						
						animationMultiplierSpeedCurrent += animationMultiplierSpeedAmount; //Increase multiplier
						
					}
					
					//If animation is interrupted (it's status is no longer retracting)
					if(status != dashboardAnimationStatus.retracting) {
						
						animationMultiplierSpeedCurrent = 1.0f; //Reset the multiplier
						this.cancel(); //Stop animation
						
					}else if(currentPositionX <= startingPositionX) { //If the current position has reached it's destination (starting position)
						
						animationMultiplierSpeedCurrent = 1.0f; //Reset the multiplier
						currentPositionX = startingPositionX; //Set the current position to the starting position
						moveDashboard(currentPositionX); //Move dashboard
						status = dashboardAnimationStatus.retracted; //Set status to retracted status (animation complete)
						this.cancel(); //Stop animation
						
					}
					
				}
				
			}
		
			AnimationRetracting animation = new AnimationRetracting(); //Create animation instance
			Timer t = new Timer(); //Create timer
			t.schedule(animation, 10, 10); //Schedule the animation task in the timer with a delay of 10ms and a repeat of 10ms
			animationMultiplierSpeedCurrent = 1.0f; //Reset animation speed multiplier
			
		}
		
	}

}

//Enumeration of the animation status
enum dashboardAnimationStatus{
	retracted, //Fully retracted, not animating
	retracting, //Retracting, animating
	extended, //Fully extended, not animating
	extending //Extending, animating
}
