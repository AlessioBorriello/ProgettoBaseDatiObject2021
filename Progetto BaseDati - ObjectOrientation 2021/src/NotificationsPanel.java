import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLayeredPane;

public class NotificationsPanel extends JPanel{

	private MainController mainController;
	private MainFrame mainFrame;

	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");  
	LocalDateTime currentTime = LocalDateTime.now();  
	
	private ArrayList<String> flightIDList;
	
	private JPanel flightListPanel;
	private JScrollPane scrollPanel;
	
	private int maxHeight = 400;
	private int minimumHeight = 140;
	
	public NotificationsPanel(MainController mainController, MainFrame mainFrame, ArrayList<String> flightIDList) {
		
		this.mainController = mainController;
		this.mainFrame = mainFrame;
		this.flightIDList = flightIDList;
		
		setSize(240, minimumHeight);
		setLayout(null);
		
		class IncreaseTime extends TimerTask {
			
			//Override run method
			public void run() {
				currentTime = currentTime.plusSeconds(1);
				repaint();
			}
		}
	
		IncreaseTime increaseTime = new IncreaseTime();
		Timer t = new Timer();
		t.schedule(increaseTime, 1000, 1000);
		
		scrollPanel = new JScrollPane();
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setBorder(null);
		scrollPanel.setVerticalScrollBar(mainFrame.createCustomScrollbar());
		scrollPanel.setBounds(4, 100, getWidth() - 6, minimumHeight - 102);
		add(scrollPanel);
		
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
			
		});
		flightListPanel.setLayout(null);
		scrollPanel.setViewportView(flightListPanel);
		
		populateFlightList(flightIDList);
		
	}
	
	public void populateFlightList(ArrayList<String> flightIDList) {
		
		NotificationsPanel thisPanel = this; //Link to this panel
		
		int index = 0;
		int height = 50;
		int vGap = 2;
		for(String s : flightIDList) {
			CustomButton buttonFlight = new CustomButton(s, null, mainController.getDifferentAlphaColor(MainController.foregroundColorThree, 48), 
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
					mainFrame.setContentPanelToViewFlightInfoPanel(new VoloDAO().getFlightByID(s)); //Go to view flight info
					JLayeredPane centerPanel = (JLayeredPane)mainController.getComponentByName(mainFrame, "centerPanel"); //Get center panel
					if(centerPanel != null) {
						centerPanel.remove(thisPanel); //Remove this panel from the centerPanel
					}
					
				}
			});
			flightListPanel.add(buttonFlight);
			
			index++;
		}
		
		//Calculate new height of the list panel (after the buttons have been added)
		int newListPanelHeight = ((height + vGap) * index);
		
		//Change panels height
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
		
		//No notifications
		if(flightIDList.size() == 0) {
			String s = "Nessun volo dovrebbe";
			int sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
			g2d.drawString(s, (getWidth()/2) - (sLength/2), 65);
			s = "essere partito";
			sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
			g2d.drawString(s, (getWidth()/2) - (sLength/2), 85);
		}else {
			String s = "Questi voli dovrebbero";
			int sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
			g2d.drawString(s, (getWidth()/2) - (sLength/2), 65);
			s = "essere partiti";
			sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
			g2d.drawString(s, (getWidth()/2) - (sLength/2), 85);
		}
			
	}
	
}
