import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;

public class FlightPreviewPanel extends JPanel {
	
	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller

	//Position in the grid
	private int panelNumber = 0;
	int gridPositionX = 0;
	int gridPositionY = 0;
	
	//Flight instance to show its info
	private Volo volo;
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm"); //Date format
	
	//Dimensions of panel
	private int width = 255;
	private int height = 255;

	public FlightPreviewPanel(MainFrame mf, MainController c, Volo v, int panelNumber, int gridPositionX, int gridPositionY) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		volo = v; //Flight instance to show its info
		this.panelNumber = panelNumber; //Number of this panel in the grid
		//Grid coordinates
		this.gridPositionX = gridPositionX;
		this.gridPositionY = gridPositionY;
		
		setSize(width, height); //Set panel size
		setBackground(new Color(200, 200, 255, 255)); //Set background color
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Set cursor to hand when hovering
		setBorder(new LineBorder(new Color(0, 0, 0), 2)); //Set border
		setLayout(null);
		
		JLabel idLabel = new JLabel("ID: " + volo.getID()); //Create label with Flight ID
		idLabel.setBounds(20, 51, 225, 14); //Set bounds
		add(idLabel); //Add label to the panel
		
		JLabel compagniaLabel = new JLabel("Compagnia: " + volo.getCompagnia().getNome()); //Create label with Company name
		compagniaLabel.setBounds(20, 76, 225, 14); //Set bounds
		add(compagniaLabel); //Add label to the panel
		
		JLabel gateLabel = new JLabel("Gate numero: " + volo.getGate().getNumeroGate()); //Create label with Gate number
		gateLabel.setBounds(20, 101, 225, 14); //Set bounds
		add(gateLabel); //Add label to the panel
		
		JLabel orarioLabel = new JLabel("Orario partenza: " + dateTimeFormat.format(volo.getOrarioDecollo())); //Create label with Take-off time
		orarioLabel.setBounds(20, 126, 225, 14); //Set bounds
		add(orarioLabel); //Add label to the panel
		
		JLabel numeroPrenotazioniLabel = new JLabel("Numero prenotazioni: " + volo.getNumeroPrenotazioni()); //Create label with booking number
		numeroPrenotazioniLabel.setBounds(20, 151, 225, 14); //Set bounds
		add(numeroPrenotazioniLabel); //Add label to the panel
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//Change panel to the EditFlightPanel
				mainFrame.setContentPanelToViewFlightInfoPanel(v);
			}
		});

	}

}
