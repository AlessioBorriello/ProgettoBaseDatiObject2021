import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.SwingUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


@SuppressWarnings("serial")
public class DashboardPanel extends JPanel {

	private MainFrame mainFrame; //Main frame
	
	private SearchPanel searchPanel; //Search panel, declared here to be accessed by methods
	
	//Animation
	final private int startingPositionX = -230; //Starting x of the dash board panel
	final private int endingPositionX = 2; //Ending x of the dash board panel
	private int currentPositionX = startingPositionX; //Current x of the dash board panel (starts fully retracted (starting position))
	
	final private float animationMultiplierSpeedMax = 6.0f; //Max value of the multiplier of the animation speed
	final private float animationMultiplierSpeedAmount = .28f; //Amount to increase for each TimerTask the multiplier value
	private float animationMultiplierSpeedCurrent = 1.0f; //Current value of the multiplier of the animation speed
	
	private dashboardAnimationStatus status = dashboardAnimationStatus.retracted; //Status of the animation (starts fully retracted)
	
	/**
	 * Dash board on the side with controls the user can interact with
	 * @param h Dash board height
	 * @param mf Link to the mainFrame
	 */
	public DashboardPanel(int h, MainFrame mf) {
		
		mainFrame = mf; //Set the main frame
		
		setBounds(startingPositionX,  2,  300, 670); //Position board in the starting position
		setLayout(null); //Set layout to absolute
		
		//Create panel that allows the user to do a research in the database
		searchPanel = new SearchPanel(mainFrame);
		searchPanel.setLocation(new Point(2, 2)); //Position panel
		searchPanel.setSize(new Dimension(294, 401)); //Set size
		searchPanel.setName("searchPanel"); //Set component name
		add(searchPanel);
		
		//Create panel that shows when the search panel is hidden
		JPanel noSearchAvailablePanel = (new JPanel() {
			
			public void paintComponent(Graphics g) { //Access component paint method
				
				super.paintComponent(g); //Paint the component normally first
				
				Graphics2D g2d = (Graphics2D)g;
				
				//AA
			    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			    //Text AA
			    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			    
			    //Draw background
			    GradientPaint gPaint = new GradientPaint(getWidth()/2, 0, MainController.backgroundColorTwo, getWidth()/2, getHeight(), MainController.backgroundColorOne);
			    g2d.setPaint(gPaint);
			    g2d.fillRect(0, 0, getWidth(), getHeight());
			    
			    //Draw string
			    String s = "Ricerca non disponibile";
			    Font f = new Font(MainController.fontOne.getFontName(), Font.BOLD, 22);
			    g2d.setFont(f);
			    int sLength = g2d.getFontMetrics(f).stringWidth(s);
			    g2d.setColor(MainController.foregroundColorThree);
			    g2d.drawString(s, (getWidth()/2) - (sLength/2), (getHeight()/2) - 4);
			    
			}
			
		}); 
		noSearchAvailablePanel.setLocation(new Point(2, 2)); //Position panel
		noSearchAvailablePanel.setSize(new Dimension(294, 401)); //Set size
		noSearchAvailablePanel.setName("noSearchAvailablePanel"); //Set component name
		add(noSearchAvailablePanel);
		
		//Create control panel, panel containing the buttons used to navigate the application
		JPanel dashboardControlPanel = (new JPanel() {
			
			public void paintComponent(Graphics g) {
				
				super.paintComponent(g); //Paint the component normally first
				
				Graphics2D g2d = (Graphics2D)g;
				
				//AA
			    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			   //Text AA
			    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			    
			    //Draw background
			    GradientPaint gPaint = new GradientPaint(getWidth()/2, 0, MainController.backgroundColorOne, getWidth()/2, getHeight(), MainController.backgroundColorTwo);
			    g2d.setPaint(gPaint);
			    g2d.fillRect(0, 0, getWidth(), getHeight());
			    
			}
			
		}); //Create dash board control panel
		dashboardControlPanel.setName("dashboardControlPanel"); //Set component name
		dashboardControlPanel.setBounds(2, 408, 294, 258); //Position dash board control panel
		add(dashboardControlPanel); //Add dash board control panel to the dash board
		dashboardControlPanel.setLayout(null); //Set the dash board control's layout to absolute
		
		CustomButton buttonCheckFlights = new CustomButton("Controlla voli", null, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 22, false, null, 0); //Create check flights button
		buttonCheckFlights.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonCheckFlights.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonCheckFlights.unselectAnimation(8);
			}
		});
		buttonCheckFlights.setName("buttonCheckFlights"); //Set component name
		//Button action listener
		buttonCheckFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Set panel on main frame to the correct panel (not looking at the archive)
				if(mf.changeContentPanel(new CheckFlightsPanel(new Rectangle(72, 2, 1124, 666), mf, false), true, false)) { //If the panel gets changed
					
					if(searchPanel != null) { //If the searchPanel gets found
						Thread queryThread = new Thread() {
						      public void run() {
						    	  searchPanel.searchFlights(); //Make search
						      }
						};
					    queryThread.start();
					}
					
					//Update dash board
					searchPanel.toggleArchiveOnlyCheckBoxes(false);
					repaint();
					revalidate();
					
				}
				
			}
		});
		buttonCheckFlights.setBounds(10, 11, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonCheckFlights); //Add to dashboardControlPanel
		
		CustomButton buttonFlightsArchive = new CustomButton("Archivio voli", null, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 22, false, null, 0); //Create check flights archive button
		buttonFlightsArchive.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonFlightsArchive.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonFlightsArchive.unselectAnimation(8);
			}
		});
		//Button action listener
		buttonFlightsArchive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Set panel on main frame to the correct panel (looking at the archive)
				if(mf.changeContentPanel(new CheckFlightsPanel(new Rectangle(72, 2, 1124, 666), mf, true), true, false)) { //If the panel gets changed
					
					SearchPanel searchPanel = (SearchPanel)mainFrame.getComponentByName(mainFrame, "searchPanel"); //Get searchPanel from the mainFrame
					if(searchPanel != null) { //If the searchPanel gets found
						Thread queryThread = new Thread() {
						      public void run() {
						    	  searchPanel.searchFlights(); //Make search
						      }
						};
					    queryThread.start();
					}
					
					//Update dash board
					searchPanel.toggleArchiveOnlyCheckBoxes(true);
					repaint();
					revalidate();
					
				}
				
			}
		});
		buttonFlightsArchive.setName("buttonFlightsArchive"); //Set component name
		buttonFlightsArchive.setBounds(10, 72, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonFlightsArchive); //Add to dashboardControlPanel
		
		CustomButton buttonCreateNewFlight = new CustomButton("Crea nuovo volo", null, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 22, false, null, 0); //Create create new flight button
		buttonCreateNewFlight.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonCreateNewFlight.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonCreateNewFlight.unselectAnimation(8);
			}
		});
		buttonCreateNewFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				//Set panel on main frame to the correct panel
				if(mf.getListaCompagnie() != null) {
					mf.changeContentPanel(new CreateFlightPanel(new Rectangle(72, 2, 1124, 666), mf), true, false);
				}
				
			}
		});
		buttonCreateNewFlight.setName("buttonCreateNewFlight"); //Set component name
		buttonCreateNewFlight.setBounds(10, 133, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonCreateNewFlight); //Add to dashboardControlPanel
		
		CustomButton buttonCheckStatistics = new CustomButton("Statistiche aereoporto", null, mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), 
				MainController.foregroundColorThree, 19, false, null, 0); //Create check statistics button
		buttonCheckStatistics.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonCheckStatistics.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonCheckStatistics.unselectAnimation(8);
			}
		});
		buttonCheckStatistics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				//Set panel on main frame to the correct panel
				mf.changeContentPanel(new StatisticsPanel(new Rectangle(72, 2, 1124, 666), mf), true, false);
			
			}
		});
		buttonCheckStatistics.setName("buttonCheckStatistics"); //Set component name
		buttonCheckStatistics.setBounds(10, 194, 276, 50); //Set position and bounds
		dashboardControlPanel.add(buttonCheckStatistics); //Add to dashboardControlPanel
		
		//Mouse listeners of the dash board
		addMouseListener(new MouseAdapter() {
			
			//When mouse enters the dash board
			public void mouseEntered(MouseEvent e) {
				openDashboardAnimation();
			}

			//When mouse exits the dash board
			public void mouseExited(MouseEvent e) {
				
				//Check if the mouse has actually left the dash board or it's just hovering over one of it's components (i.e buttons)
				Point p = new Point(e.getLocationOnScreen()); //Get position of the occurred event (mouse position)
		        SwingUtilities.convertPointFromScreen(p, e.getComponent()); //Convert p from a screen coordinates to a component's coordinate system
		        if(e.getComponent().contains(p)) { //If the point is still inside the dash board (it's hovering over one of it's components)
		        	return; //Exit event and ignore rest of the code
		        }
		        
		        closeDashboardAnimation();
		        
			}
			
		});
		
	}
	
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
	    
	}
	
	/**
	 * Move the dashboard's x position to the indicated value
	 * @param positionX Where to position the dashboard
	 */
	public void moveDashboard(int positionX) {
		setLocation(positionX, getLocation().y); //Set the location
	}

	/**
	 * Makes the dash board open with it's animation
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
					moveDashboard(currentPositionX); //Move dash board
					
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
						moveDashboard(currentPositionX); //Move dash board
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
	 * Makes the dash board close with it's animation
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
					moveDashboard(currentPositionX); //Move dash board
					
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
						moveDashboard(currentPositionX); //Move dash board
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

	//Getter search panel
	public SearchPanel getSearchPanel() {
		return searchPanel;
	}
	
	//Getter animation status
	public dashboardAnimationStatus getAnimationStatus() {
		return status;
	}

	/**
	 * Toggle between showing and not showing the searchPanel
	 * @param active If the panel should be shown or not
	 */
	@SuppressWarnings("deprecation")
	public void toggleSearchPanel(boolean active) {
		searchPanel.show(active);
	}

}

//Enumeration of the animation status
enum dashboardAnimationStatus{
	retracted, //Fully retracted, not animating
	retracting, //Retracting, animating
	extended, //Fully extended, not animating
	extending //Extending, animating
}