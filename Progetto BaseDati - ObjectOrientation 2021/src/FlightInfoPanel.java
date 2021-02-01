import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class FlightInfoPanel extends JPanel {
	
	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller

	//Position in the grid
	private int panelNumber = 0;
	int gridPositionX = 0;
	int gridPositionY = 0;
	
	//Flight instance to show its info
	private Volo volo;
	
	//Dimensions of panel
	private int width = 255;
	private int height = 255;

	public FlightInfoPanel(MainFrame mf, MainController c, Volo v, int panelNumber, int gridPositionX, int gridPositionY) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		volo = v; //Flight instance to show its info
		this.panelNumber = panelNumber; //Number of this panel in the grid
		//Grid coordinates
		this.gridPositionX = gridPositionX;
		this.gridPositionY = gridPositionY;
		
		setSize(width, height); //Set panel size
		setBackground(new Color(200, 200, 255, 255)); //Set background color
		setBorder(new LineBorder(new Color(0, 0, 0), 2)); //Set border
		
		JLabel idLabel = new JLabel("ID: " + volo.getID()); //Create label with Flight ID
		add(idLabel); //Add label to the panel

	}

}
