import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;

import javax.swing.ScrollPaneConstants;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

@SuppressWarnings("serial")
public class CheckFlightsPanel extends JPanel {

	private MainFrame mainFrame; // Main panel

	private JPanel gridPanel; // Panel containing the FlightPreviewPanels in a grid

	private boolean lookingAtArchive; // If looking at archive

	// Images of the company
	private Image airfranceLogoImage;
	private Image alitaliaLogoImage;
	private Image easyjetLogoImage;
	private Image ryanairLogoImage;

	/**
	 * Panel containing the grid of the FlightPreviewPanels
	 * 
	 * @param bounds           Bounds of the contentPanel that contains this panel
	 * @param mf               Link to the MainFrame
	 * @param lookingAtArchive If the panel is displaying the archive of the flights
	 *                         (the flights where 'partito' = 1)
	 */
	public CheckFlightsPanel(Rectangle bounds, MainFrame mf, boolean lookingAtArchive) {

		mainFrame = mf; // Link main frame
		// mainController = c; //Link main controller
		this.lookingAtArchive = lookingAtArchive;

		// Load company images
		try {
			airfranceLogoImage = ImageIO.read(new File("imgs/company-logos/airfrance-logo.png"));
			alitaliaLogoImage = ImageIO.read(new File("imgs/company-logos/alitalia-logo.png"));
			easyjetLogoImage = ImageIO.read(new File("imgs/company-logos/easyjet-logo.png"));
			ryanairLogoImage = ImageIO.read(new File("imgs/company-logos/ryanair-logo.png"));
		} catch (IOException e) {
			System.out.println(e);
		}

		// Scale company images
		airfranceLogoImage = airfranceLogoImage.getScaledInstance(92, 92, Image.SCALE_SMOOTH);
		alitaliaLogoImage = alitaliaLogoImage.getScaledInstance(92, 92, Image.SCALE_SMOOTH);
		easyjetLogoImage = easyjetLogoImage.getScaledInstance(92, 92, Image.SCALE_SMOOTH);
		ryanairLogoImage = ryanairLogoImage.getScaledInstance(92, 92, Image.SCALE_SMOOTH);

		setBounds(bounds); // Set bounds
		setLayout(new BorderLayout(0, 0)); // Set layout

		JScrollPane scrollPanel = new JScrollPane(); // Create scroll panel
		scrollPanel.setBorder(null); // Set border
		scrollPanel.setVerticalScrollBar(mainFrame.createCustomScrollbar());// Create and set the custom scroll bar
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Don't show
																									// horizontal scroll
																									// bar
		scrollPanel.getVerticalScrollBar().setUnitIncrement(14); // Set scroll bar speed
		scrollPanel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				// Repaint main frame when the mouse wheel is used
				mainFrame.repaint();

			}
		});
		add(scrollPanel, BorderLayout.CENTER); // Add scroll panel

		gridPanel = (new JPanel() {

			public void paintComponent(Graphics g) {

				super.paintComponent(g); // Paint the component normally first

				Graphics2D g2d = (Graphics2D) g;

				// AA
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Text AA
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

				// Draw background
				g2d.setColor(MainController.backgroundColorOne);
				g2d.fillRect(0, 0, getWidth(), getHeight());

				// Draw text
				g2d.setColor(MainController.foregroundColorThree);
				g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 44));
				String s = (!mainFrame.isLookingAtArchive()) ? "Voli programmati" : "Voli in archivio";
				int sLength = g2d.getFontMetrics().stringWidth(s);
				g2d.drawString(s, (getWidth() / 2) - (sLength / 2), 65);

				// Draw line
				g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
				g2d.drawLine((getWidth() / 2) - (sLength / 2), 78, (getWidth() / 2) + (sLength / 2), 78);

				// If no flights are being displayed
				if (mainFrame.getFlightList() != null && mainFrame.getFlightList().size() == 0) {
					String string = "Nessun volo trovato";
					g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 65));
					int stringLength = g2d.getFontMetrics().stringWidth(string);
					g2d.drawString(string, (getWidth() / 2) - (stringLength / 2), (getHeight() / 2));
				}

			}

		}); // Create grid panel
		gridPanel.setName("gridPanel"); // Name component
		scrollPanel.setViewportView(gridPanel); // Make the scroll pane look at the grid panel and add it to the scroll
												// pane

		populateGrid(4, 15, 15, 25, 95); // Create grid of flights info panel based on the listaVoli array

	}

	/**
	 * Populate the grid panel with flight info panels
	 * 
	 * @param gridPanel    Reference to the grid panel
	 * @param columns      How many panels per row
	 * @param hgap         Horizontal gap between each panel
	 * @param vgap         Vertical gap between each panel
	 * @param xStartOffset Starting x offset of the first panel
	 * @param yStartOffset Starting y offset of the first panel
	 */
	public void populateGrid(int columns, int hgap, int vgap, int xStartOffset, int yStartOffset) {

		// Clear grid
		gridPanel.removeAll();

		// Position in the grid
		int xPosition = 0;
		int yPosition = 0;

		// Width and height of the panels
		int width = 0;
		int height = 0;

		ArrayList<Volo> list = mainFrame.getFlightList();
		int panelNumber = (list != null) ? list.size() : 0; // Get the number
																										// of panels to
																										// create

		for (int i = 0; i < panelNumber; i++) {

			// Calculate position in the grid
			xPosition = i % columns;
			yPosition = i / columns;

			// Get correct company image
			Image companyImage;
			switch (list.get(i).getCompagnia().getNome()) {
			case "AirFrance":
				companyImage = airfranceLogoImage;
				break;
			case "Alitalia":
				companyImage = alitaliaLogoImage;
				break;
			case "EasyJet":
				companyImage = easyjetLogoImage;
				break;
			case "Ryanair":
				companyImage = ryanairLogoImage;
				break;
			default:
				companyImage = airfranceLogoImage;
			}

			// Create panel
			FlightPreviewPanel f = new FlightPreviewPanel(mainFrame, mainFrame.getFlightList().get(i), companyImage); // Create new panel

			// Get width and height of the panel
			width = f.getWidth();
			height = f.getHeight();

			// Position the new panel
			f.setBounds(new Rectangle(xStartOffset + (width * xPosition) + (hgap * xPosition),
					yStartOffset + (height * yPosition) + (vgap * yPosition), width, height));
			gridPanel.add(f); // Add panel to the grid panel

		}

		// Calculate new height of the grid panel (after the panels have been added)
		int newGridPanelHeight = yStartOffset + (height * (yPosition + 1)) + (vgap * (yPosition + 1));

		// Create group layout (and assign calculated height)
		GroupLayout gl_gridPanel = new GroupLayout(gridPanel);
		gl_gridPanel.setHorizontalGroup(
				gl_gridPanel.createParallelGroup(Alignment.LEADING).addGap(0, getBounds().width, Short.MAX_VALUE));
		gl_gridPanel.setVerticalGroup(
				gl_gridPanel.createParallelGroup(Alignment.LEADING).addGap(0, newGridPanelHeight, Short.MAX_VALUE));
		gridPanel.setLayout(gl_gridPanel); // Set layout to the grid panel

		repaint();

	}

	/**
	 * Get the grid panel instance
	 * 
	 * @return The grid panel instance
	 */
	public JPanel getGridPanel() {
		return gridPanel;
	}

	/**
	 * Get looking at archive variable
	 * 
	 * @return looking at archive variable
	 */
	public boolean isLookingAtArchive() {
		return lookingAtArchive;
	}

}