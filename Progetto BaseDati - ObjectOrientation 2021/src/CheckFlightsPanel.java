import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;

import javax.swing.ScrollPaneConstants;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;

public class CheckFlightsPanel extends JPanel {
	
	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	
	private JPanel gridPanel;

	public CheckFlightsPanel(Rectangle bounds, MainFrame mf, MainController c, boolean lookingAtArchive) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		
		//setBounds(bounds);
		setBounds(72, 2, 1124, 666); //Debug to show in the design tab,  this row should be replaced with the one above
		setLayout(new BorderLayout(0, 0)); //Set layout
		
		JScrollPane scrollPanel = new JScrollPane(); //Create scroll panel
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); //Dont show horizontal scrollbar
		scrollPanel.getVerticalScrollBar().setUnitIncrement(14); //Set scrollbar speed
		add(scrollPanel, BorderLayout.CENTER); //Add scroll panel
		
		gridPanel = new JPanel(); //Create grid panel
		gridPanel.setName("gridPanel"); //Name component
		scrollPanel.setViewportView(gridPanel); //Make the scroll pane look at the grid panel and add it to the scroll pane
		
		populateGrid(mainFrame.getFlightList(), 4, 15, 15, 25, 25); //Create grid of flights info panel based on the listaVoli array
		
	}
	
	/**
	 * Populate the grid panel with flight info panels
	 * @param gridPanel Reference to the grid panel
	 * @param array The array containing the flights
	 * @param columns How many panels per row
	 * @param hgap Horizontal gap between each panel
	 * @param vgap Vertical gap between each panel
	 * @param xStartOffset Starting x offset of the first panel
	 * @param yStartOffset Starting y offset of the first panel
	 */
	public void populateGrid(ArrayList<Volo> array, int columns, int hgap, int vgap, int xStartOffset, int yStartOffset) {
		
		//Clear grid before anything
		gridPanel.removeAll();
		
		//Position in the grid
		int xPosition = 0;
		int yPosition = 0;
		
		//Width and height of the panels
		int width = 0;
		int height = 0;
		
		int panelNumber = (array != null) ? array.size() : 0; //Get the number of panels to create
		
		for(int i = 0; i < panelNumber; i++) {
			
			//Calculate position in the grid
			xPosition = i % columns;
			yPosition = i / columns;
			
			FlightPreviewPanel f = new FlightPreviewPanel(mainFrame, mainController, array.get(i), i, xPosition, yPosition); //Create new panel
			
			//Get width and height of the panel
			width = f.getWidth();
			height = f.getHeight();
			
			//Position the new panel
			f.setBounds(new Rectangle(xStartOffset + (width * xPosition) + (hgap * xPosition), yStartOffset + (height * yPosition) + (vgap * yPosition), width, height));
			gridPanel.add(f); //Add panel to the grid panel
			
		}
		
		//Calculate new height of the grid panel (after the panels have been added)
		int newGridPanelHeight = yStartOffset + (height * (yPosition + 1)) + (vgap * (yPosition + 1));
		
		//Create group layout
		GroupLayout gl_gridPanel = new GroupLayout(gridPanel);
		gl_gridPanel.setHorizontalGroup(
			gl_gridPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, getBounds().width, Short.MAX_VALUE)
		);
		gl_gridPanel.setVerticalGroup(
			gl_gridPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, newGridPanelHeight, Short.MAX_VALUE)
		);
		gridPanel.setLayout(gl_gridPanel); //Set layout to the grid panel
	
	}

	public JPanel getGridPanel() {
		return gridPanel;
	}

}
