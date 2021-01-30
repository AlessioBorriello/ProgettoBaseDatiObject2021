import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class FlightInfoPanel extends JPanel {

	private int xGridPosition = 0;
	private int yGridPosition = 0;
	private int panelNumber = 0;
	private int width = 255;
	private int height = 255;

	public FlightInfoPanel(int panelNumber, int xGridPosition, int yGridPosition) {
		
		this.xGridPosition = xGridPosition;
		this.yGridPosition = yGridPosition;
		this.panelNumber = panelNumber;
		
		setSize(width, height);
		setBackground(new Color(200, 200, 255, 255));
		setBorder(new LineBorder(new Color(0, 0, 0), 2));
		
		JLabel l = new JLabel(String.valueOf(panelNumber));
		add(l);

	}

}
