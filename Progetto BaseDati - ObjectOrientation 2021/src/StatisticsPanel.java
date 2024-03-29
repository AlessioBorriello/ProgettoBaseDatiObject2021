import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StatisticsPanel extends JPanel {

	private MainFrame mainFrame; //Main panel
	
	private int gatesNumber = 12; //How many gate there are in the airport
	private int flightTotal = 0; //How many flights there are in the whole airport
	
	private class PieSlice {
		
		private String name; //Name of the slice
		private int value; //Value of the slice
		private Color color; //Color of the slice
		
		/**
		 * Create a new pie slice and set it's name, value and color
		 * @param name Name of the slice
		 * @param value Value of the slice
		 * @param c Color of the slice
		 */
		public PieSlice(String name, int value, Color c) {
			
			this.name = name;
			this.value = value;
			this.color = c;
			
		}
		
		//Setters and getters
		public int getValue() {
			return value;
		}
		public Color getColor() {
			return color;
		}
		public String getName() {
			return name;
		}
		
	}

	/**
	 * Panel that shows the amount of flights in the database and a pie chart representation of it, it also shows the
	 * amount of flights each of the 12 gates has, allowing the user to choose a gate to see more info about it into the CheckGatePanel
	 * @param bounds Bounds of the contentPanel that contains this panel
	 * @param mf Link to the MainFrame
	 */
	public StatisticsPanel(Rectangle bounds, MainFrame mf) {
		
		mainFrame = mf; //Link main frame
		
		//Get number of flights for each of the companies in the airport
		for(int i = 0; i < 4; i++) {
			if(mainFrame.getListaCompagnie() != null) {
				flightTotal += mainFrame.getListaCompagnie().get(i).getNumeroVoli();
			}
		}
		
		setBounds(bounds); //Set bounds
		setLayout(null); //Set bounds
		
		populateGatesGrid(gatesNumber, 6, 15, 15, 125, (getBounds().height/2) - 10); //Create grid of gates panel
		
	}

	/**
	 * Create a grid of GatePanels, one for each of the 12 gates in the airport
	 * @param gatesNumber Amount of panels to create
	 * @param columns How many panels per row
	 * @param hgap Horizontal gap between each panel
	 * @param vgap Vertical gap between each panel
	 * @param xStartOffset Starting x offset of the first panel
	 * @param yStartOffset Starting y offset of the first panel
	 */
	public void populateGatesGrid(int gatesNumber, int columns, int hgap, int vgap, int xStartOffset, int yStartOffset) {
		
		//Position in the grid
		int xPosition = 0;
		int yPosition = 0;
		
		//Width and height of the panels
		int width = 150;
		int height = 150;
		
		int panelNumber = gatesNumber; //Get the number of panels to create
		
		for(int i = 0; i < panelNumber; i++) {
			
			//Calculate position in the grid
			xPosition = i % columns;
			yPosition = i / columns;
			
			int gateIndex = i + 1;
			GatePanel p = new GatePanel(mainFrame, gateIndex);
			//Position the new panel
			p.setBounds(new Rectangle(xStartOffset + (width * xPosition) + (hgap * xPosition), yStartOffset + (height * yPosition) + (vgap * yPosition), width, height));
			p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			p.addMouseListener(new MouseAdapter() {
				//Mouse entered
				public void mouseEntered(MouseEvent e) {
					p.selectAnimation(12);
				}
				//Mouse exited
				public void mouseExited(MouseEvent e) {
					p.unselectAnimation(12);
				}
			});
			add(p); //Add panel to the grid panel
			
		}
	
	}

	/**
	 * Draws a pie chart of a given set of pie slices data
	 * @param g2d Graphic2D instance
	 * @param angleOffset Starting angle of the pie
	 * @param centerX X position of the center
	 * @param centerY Y position of the center
	 * @param width Width of the pie
	 * @param height Height of the pie
	 * @param pies List of the pie slices to draw the chart out of
	 * @param drawOutline If the pie should have an outline
	 * @param draw3D If the pie should be drawn with a 3D effect
	 * @param depth Depth of the 3rd dimension of the pie
	 * @param drawNames If the pie should show the names of the singular pie slice
	 * @param f Font to draw the names with
	 */
	public void drawPieChart(Graphics2D g2d, int angleOffset, int centerX, int centerY, int width, int height,  ArrayList<PieSlice> pies, boolean drawOutline, boolean draw3D, int depth, boolean drawNames, Font f) {
		
		angleOffset %= 360; //Modulate angleOffset to 360
		angleOffset = (angleOffset < 0)? (360 + angleOffset) : angleOffset; //If the angleOffset is negative add 360 to it (same resulting angle, but positive)
		Color outlineColor = MainController.foregroundColorThree;
		
		int startAngle = angleOffset;
		int total = 0; //Total of the values
		
		if(!draw3D) { //Set depth as 0 if not drawing 3D effect
			depth = 0;
		}
		
		//Get sum of values
		for(PieSlice p : pies) {
			total += p.getValue();
		}
		
		//If the total is 0
		if(total == 0) {
			return; //Don't do anything
		}
		
		//Set stroke
		g2d.setStroke(new BasicStroke(2));
		
		//Calculate graph position
		int startX = centerX - (width/2);
		int startY = centerY - (height/2);
		
		if(draw3D) { //If drawing 3D effect
			
			for(PieSlice p : pies) { //Draw bottom of the pie
				
				float pieAngle = (float)(360 * p.getValue())/total; //Get end angle of the pie
				g2d.setPaint(p.getColor().darker().darker()); //Set color to a darker version of the pie color
				
				g2d.fillArc(startX, startY + depth, width, height, startAngle, (int)Math.ceil(pieAngle)); //Draw arc
				
				startAngle += pieAngle; //Next pie will start at the end of this one
				
			}
			
			startAngle = angleOffset; //Reset angle
			//Draw depth polygons
			for(PieSlice p : pies) {
				
				float pieAngle = (float)(360 * p.getValue())/total; //Get end angle of the pie
				g2d.setPaint(p.getColor().darker().darker()); //Set color to a darker version of the pie color
				
				//Get position of the 2 points where the arc starts and ends
				int x1 = (int)((width/2) * Math.cos(Math.toRadians(startAngle)));
				int y1 = (int)((height/2) * Math.sin(Math.toRadians(startAngle)));
				int x2 = (int)((width/2) * Math.cos(Math.toRadians(startAngle + pieAngle)));
				int y2 = (int)((height/2) * Math.sin(Math.toRadians(startAngle + pieAngle)));
				
				
				if((startAngle % 360 < 180 && (startAngle + pieAngle) % 360 > 180)) { //If one angle is less than 180 and the other is larger than 180 then they are on opposite vertical sides (one on top, the other on the bottom)
					x1 = -width/2; //X position to the left of the pie
					y1 = 0; //Y position as 0 (vertical center of the pie)
				}else if((startAngle < 360 && startAngle + pieAngle > 360) || Math.signum(startAngle) != Math.signum(pieAngle + startAngle)) { //Like other if but with 360 OR one of the angle have opposite signs
					x2 = width/2; //X position to the right of the pie
					y2 = 0; //Y position as 0 (vertical center of the pie)
				}
				
				//Create polygon array
				int[] pointsX = {centerX + x1, centerX + x1, centerX + x2, centerX + x2};
				int[] pointsY = {centerY - y1, centerY - y1 + depth, centerY - y2 + depth, centerY - y2};
				
				//If the first top point of the polygon and the second one are both positive
				if(!(Math.signum(y1) >= 0 && Math.signum(y2) >= 0)) {
					g2d.fillPolygon(pointsX, pointsY, 4); //Draw polygon
					g2d.drawPolyline(pointsX, pointsY, 4);
				}
				
				startAngle += pieAngle; //Next pie will start at the end of this one
				
			}
			
			if(drawOutline) { //Draw bottom outline
				
				g2d.setPaint(outlineColor);
				g2d.drawArc(startX, startY + depth, width, height, 180, 180);
				
				//Side lines
				g2d.drawLine(centerX - (width/2), centerY, centerX - (width/2), centerY + depth);
				g2d.drawLine(centerX + (width/2), centerY, centerX + (width/2), centerY + depth);
				
			}
			
		}
		
		//Draw names
		if(drawNames) {
			
			g2d.setFont(f);
			
			for(PieSlice p : pies) {
				
				float pieAngle = (float)(360 * p.getValue())/total; //Get end angle of the pie
				
				String name = p.getName();
				String percentage = String.format("%.2f", ((float)p.getValue()/total)*100); //Get percentage of the pie
				
				//Don't continue if the percentage is 0
				if(((float)p.getValue()/total)*100 == 0) {
					break;
				}
				
				name += " (" + percentage + "%)"; //Add it to the name
				int stringLenght = g2d.getFontMetrics().stringWidth(name); //Get string pixel length
				
				//Calculate name lines
				int x1 = (int)((width/2) * Math.cos(Math.toRadians(startAngle + (pieAngle/2))));
				int y1 = (int)((height/2) * Math.sin(Math.toRadians(startAngle + (pieAngle/2))));
				
				//Draw name lines
				g2d.setColor(outlineColor);
				float lenghtMultiplier = 1.1f + (.6f * ((float)Math.sin(Math.toRadians((startAngle + (pieAngle/2))%180))));
				g2d.drawLine(centerX + x1, centerY - y1 + depth/2, (int)(centerX + x1*lenghtMultiplier), (int)(centerY - y1*lenghtMultiplier + depth/2));
				g2d.drawLine((int)(centerX + x1*lenghtMultiplier), (int)(centerY - y1*lenghtMultiplier + depth/2), (int)(centerX + x1*lenghtMultiplier) + stringLenght * (((int)Math.signum(x1) != 0)? (int)Math.signum(x1) : (int)Math.signum(y1)), (int)(centerY - y1*lenghtMultiplier + depth/2));
				
				//Draw names
				g2d.setPaint(outlineColor);
				g2d.drawString(name, (int)(centerX + x1*lenghtMultiplier) - (((int)Math.signum(x1) < 0)? stringLenght : 0) - (((int)Math.signum(x1) == 0 && (int)Math.signum(y1) < 0)? stringLenght : 0), (int)(centerY - y1*lenghtMultiplier + depth/2) - 3);
				
				startAngle += pieAngle; //Next pie will start at the end of this one
			
			}
			
		}
		
		startAngle = angleOffset; //Reset angle
		for(PieSlice p : pies) { //Draw top of the pie
			
			float pieAngle = (float)(360 * p.getValue())/total; //Get end angle of the pie
			
			
			g2d.setPaint(p.getColor()); //Set color
			g2d.fillArc(startX, startY, width, height, startAngle, (int)Math.ceil(pieAngle)); //Draw arc
			
			//Draw outlines
			if(drawOutline) {
				
				g2d.setPaint(outlineColor);
				
				//Calculate lines
				int x1 = (int)((width/2) * Math.cos(Math.toRadians(startAngle)));
				int y1 = (int)((height/2) * Math.sin(Math.toRadians(startAngle)));
				
				//Draw line
				g2d.drawLine(centerX, centerY, centerX + x1, centerY - y1);
				
				//3D line
				if(draw3D && centerY - y1 > centerY) {
					
					g2d.drawLine(centerX + x1, centerY - y1, centerX + x1, centerY - y1 + depth);
					
				}
			
			}
			
			startAngle += pieAngle; //Next pie will start at the end of this one
			
		}
		
		if(drawOutline) { //Draw top outline
			
			g2d.setPaint(outlineColor);
			g2d.drawOval(startX, startY, width, height);
			
		}
		
	}

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
	    
	    //Draw gate string
		g2d.setColor(MainController.foregroundColorThree);
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 34));
		
		String s = "Seleziona un gate";
		
		int angle = -90;
		g2d.rotate(Math.toRadians(angle)); //Rotate
		g2d.drawString(s, -655, 70); //Draw string rotated -90 degrees
		g2d.rotate(Math.toRadians(-angle)); //Rotate back
		
		//Draw lines
		g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2d.drawLine(120, (getHeight()/2) - 40, getWidth() - 25, (getHeight()/2) - 40);
		
		//Create an array list of pie slices
		ArrayList<PieSlice> companyDataPie = new ArrayList<PieSlice>();
		
		//Add slices to the pie
		if(mainFrame.getListaCompagnie() != null) {
			companyDataPie.add(new PieSlice("AirFrance", mainFrame.getListaCompagnie().get(0).getNumeroVoli(), MainController.airfranceColor)); //Add AirFrance slice, with it's flight number and color
			companyDataPie.add(new PieSlice("Alitalia", mainFrame.getListaCompagnie().get(1).getNumeroVoli(), MainController.alitaliaColor)); //Add Alitalia slice, with it's flight number and color
			companyDataPie.add(new PieSlice("EasyJet", mainFrame.getListaCompagnie().get(2).getNumeroVoli(), MainController.easyjetColor)); //Add EasyJet slice, with it's flight number and color
			companyDataPie.add(new PieSlice("RyanAir", mainFrame.getListaCompagnie().get(3).getNumeroVoli(), MainController.ryanairColor)); //Add RyanAir slice, with it's flight number and color
			
			drawPieChart(g2d, 45, 840, 150, 200, 100, companyDataPie, true, true, 25, true, new Font(MainController.fontOne.getFontName(), Font.BOLD, 16)); //Draw pie
		}
		
		//Draw flight total number in the airport
		g2d.setColor(MainController.foregroundColorThree);
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 40));
		s = "Numero di voli presenti";
		g2d.drawString(s, 70, 130);
		int sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
		g2d.drawString(String.valueOf(flightTotal), 70 + (sLength/2) - (g2d.getFontMetrics(g2d.getFont()).stringWidth(String.valueOf(flightTotal))/2), 190);
	
	}

}

