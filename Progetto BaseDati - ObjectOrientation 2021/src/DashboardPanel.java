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
	final private float animationMultiplierSpeedAmount = .28f; //Amount to increase for each TimerTask the mutiplier value
	private float animationMultiplierSpeedCurrent = 1.0f; //Current value of the multiplier of the animation speed
	
	private animationStatus status = animationStatus.retracted; //Status of the animation (starts fully retracted)
	
	public DashboardPanel(int h, MainFrame mf, MainController mc) {
		
		height = h; //Set height value of the dashboard
		mainFrame = mf; //Set the main frame
		mainController = mc; //Set the main controller
		
		setBackground(SystemColor.activeCaptionBorder); //Set background
		//setBounds(startingPositionX,  2,  300, height - 4); //Position board in the starting position
		setBounds(startingPositionX,  2,  300, 670); //Debug to show dashboard in the design tab, this row should be replaced with the one above
		setLayout(null); //Set layout to absolute
		
		
		JPanel infoPanel = new JPanel(); //Create info panel
		infoPanel.setBounds(2, 2, 296, 331); //Position info panel
		infoPanel.setName("infoPanel"); //Set component name
		add(infoPanel); //Add info panel to the dashboard
		infoPanel.setLayout(new BorderLayout(0, 0)); //Set the info panel's layout to border layout

		JLabel lblTest = new JLabel("Dashboard"); //Create lbl test and set it's text
		lblTest.setHorizontalAlignment(SwingConstants.CENTER); //Set text alignment
		infoPanel.add(lblTest);
		
		JPanel dashboardControlPanel = new JPanel(); //Create dasboard control panel
		dashboardControlPanel.setName("dashboardControlPanel"); //Set component name
		dashboardControlPanel.setBounds(2, 336, 296, 331); //Position dashboard control panel
		add(dashboardControlPanel); //Add dashboard control panel to the dashboard
		dashboardControlPanel.setLayout(null); //Set the dash board control's layout to absolute
		
		JButton buttonCheckFlights = new JButton("Check flights"); //Create check flights button
		buttonCheckFlights.setName("buttonCheckFlights"); //Set component name
		buttonCheckFlights.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//Set panel on main frame to the correct panel
				mf.setContentPanelToCheckFlightsPanel();
				
			}
		});
		buttonCheckFlights.setBounds(10, 40, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonCheckFlights); //Add to dashboardControlPanel
		
		JButton buttonFlightsArchive = new JButton("Flights archive"); //Create check flights archive button
		buttonFlightsArchive.setName("buttonFlightsArchive"); //Set component name
		buttonFlightsArchive.setBounds(10, 100, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonFlightsArchive); //Add to dashboardControlPanel
		
		JButton buttonCreateNewFlight = new JButton("Create new flight"); //Create create new flight button
		buttonCreateNewFlight.setName("buttonCreateNewFlight"); //Set component name
		buttonCreateNewFlight.setBounds(10, 160, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonCreateNewFlight); //Add to dashboardControlPanel
		
		JButton buttonCheckStatistics = new JButton("Check gate statistics"); //Create check stats button
		buttonCheckStatistics.setName("buttonCheckStatistics"); //Set component name
		buttonCheckStatistics.setBounds(10, 220, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonCheckStatistics); //Add to dashboardControlPanel
		
		//Mouse listeners of the dashboard
		addMouseListener(new MouseAdapter() {
			
			//When mouse enters the dashboard
			public void mouseEntered(MouseEvent e) {
				
				//If the dashboard is not already extended
				if(status != animationStatus.extended) {
					
					//Extend animation
					status = animationStatus.extending; //Set status to extending status (animating)
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
							if(status != animationStatus.extending) {
								
								animationMultiplierSpeedCurrent = 1.0f; //Reset the multiplier
								this.cancel(); //Stop animation
								
							}else if(currentPositionX >= endingPositionX) { //If the current position has reached it's destination (ending position)
								
								animationMultiplierSpeedCurrent = 1.0f; //Reset the multiplier
								currentPositionX = endingPositionX; //Set the current position to the ending position
								moveDashboard(currentPositionX); //Move dashboard
								status = animationStatus.extended; //Set status to extended status (animation complete)
								this.cancel(); //Stop animation
								
							}
							
						}
						
					}
				
					AnimationExtending animation = new AnimationExtending(); //Create animation instance
					Timer t = new Timer(); //Create timer
					t.schedule(animation, 10, 10); //Schedule the animation task in the timer with a delay of 10ms and a repeat of 10ms
					
				}
			}

			//When mouse exits the dashboard
			public void mouseExited(MouseEvent e) {
				
				//Check if the mouse has actually left the dashboard or it's just hovering over one of it's components (i.e buttons)
				Point p = new Point(e.getLocationOnScreen()); //Get position of the occurred event (mouse position)
		        SwingUtilities.convertPointFromScreen(p, e.getComponent()); //Convert p from a screen coordinates to a component's coordinate system
		        if(e.getComponent().contains(p)) { //If the point is still inside the dashboard (it's hovering over one of it's components)
		        	return; //Exit event and ignore rest of the code
		        }
		        
		        //If the dashboard is not already retracted
				if(status != animationStatus.retracted) {
					
					//Retract animation
					status = animationStatus.retracting;  //Set status to retracting status (animating)
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
							if(status != animationStatus.retracting) {
								
								animationMultiplierSpeedCurrent = 1.0f; //Reset the multiplier
								this.cancel(); //Stop animation
								
							}else if(currentPositionX <= startingPositionX) { //If the current position has reached it's destination (starting position)
								
								animationMultiplierSpeedCurrent = 1.0f; //Reset the multiplier
								currentPositionX = startingPositionX; //Set the current position to the starting position
								moveDashboard(currentPositionX); //Move dashboard
								status = animationStatus.retracted; //Set status to retracted status (animation complete)
								this.cancel(); //Stop animation
								
							}
							
						}
						
					}
				
					AnimationRetracting animation = new AnimationRetracting(); //Create animation instance
					Timer t = new Timer(); //Create timer
					t.schedule(animation, 10, 10); //Schedule the animation task in the timer with a delay of 10ms and a repeat of 10ms
					
				}
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
}

//Enumeration of the animation status
enum animationStatus{
	retracted, //Fully retracted, not animating
	retracting, //Retracting, animating
	extended, //Fully extended, not animating
	extending //Extending, animating
}
