import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.Border;

import javax.swing.SwingConstants;

public class StatisticsPanel extends JPanel {

	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	
	private int gatesNumber = 12; //How many gate there are in the airport
	private int flightTotal = 0;
	
	private class Pie {
		
		private String name;
		private int value;
		private Color color;
		
		public Pie(String name, int value, Color c) {
			
			this.name = name;
			this.value = value;
			this.color = c;
			
		}
		
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public Color getColor() {
			return color;
		}
		public void setColor(Color color) {
			this.color = color;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}

	public StatisticsPanel(Rectangle bounds, MainFrame mf, MainController c) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		
		//Get flight's number
		for(int i = 0; i < 4; i++) {
			flightTotal += mainFrame.getListaCompagnie().get(i).getNumeroVoli();
		}
		
		//setBounds(bounds);
		setBounds(72, 2, 1124, 666); //Debug to show in the design tab,  this row should be replaced with the one above
		setLayout(null);
		
		populateGatesGrid(gatesNumber, 6, 15, 15, 125, (getBounds().height/2) - 10);
		
	}

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
			GatePanel p = new GatePanel(mainController, mainFrame, gateIndex);
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
	public void drawPieChart(Graphics2D g2d, int angleOffset, int centerX, int centerY, int width, int height,  ArrayList<Pie> pies, boolean drawOutline, boolean draw3D, int depth, boolean drawNames, Font f) {
		
		angleOffset %= 360; //Modulate angleOffset to 360
		angleOffset = (angleOffset < 0)? (360 + angleOffset) : angleOffset; //If the angleOffset is negative add 360 to it (same resulting angle, but positive)
		Color outlineColor = MainController.foregroundColorThree;
		
		int startAngle = angleOffset;
		int total = 0; //Total of the values
		
		if(!draw3D) { //Set depth as 0 if not drawing 3D effect
			depth = 0;
		}
		
		//Get sum of values
		for(Pie p : pies) {
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
			
			for(Pie p : pies) { //Draw bottom of the pie
				
				float pieAngle = (float)(360 * p.getValue())/total; //Get end angle of the pie
				g2d.setPaint(p.getColor().darker().darker()); //Set color to a darker version of the pie color
				
				g2d.fillArc(startX, startY + depth, width, height, startAngle, (int)Math.ceil(pieAngle)); //Draw arc
				
				startAngle += pieAngle; //Next pie will start at the end of this one
				
			}
			
			startAngle = angleOffset; //Reset angle
			//Draw depth polygons
			for(Pie p : pies) {
				
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
			
			for(Pie p : pies) {
				
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
		for(Pie p : pies) { //Draw top of the pie
			
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
		g2d.drawString(s, -655, 70);
		g2d.rotate(Math.toRadians(-angle)); //Rotate back
		
		//Draw lines
		g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2d.drawLine(120, (getHeight()/2) - 40, getWidth() - 25, (getHeight()/2) - 40);
		
		//Draw pie chart
		ArrayList<Pie> companyData = new ArrayList<Pie>();
		
		companyData.add(new Pie("AirFrance", mainFrame.getListaCompagnie().get(0).getNumeroVoli(), MainController.airfranceColor));
		companyData.add(new Pie("Alitalia", mainFrame.getListaCompagnie().get(1).getNumeroVoli(), MainController.alitaliaColor));
		companyData.add(new Pie("EasyJet", mainFrame.getListaCompagnie().get(2).getNumeroVoli(), MainController.easyjetColor));
		companyData.add(new Pie("RyanAir", mainFrame.getListaCompagnie().get(3).getNumeroVoli(), MainController.ryanairColor));
		
		drawPieChart(g2d, 45, 840, 150, 200, 100, companyData, true, true, 25, true, new Font(MainController.fontOne.getFontName(), Font.BOLD, 16));
		
		//Draw flight total number
		g2d.setColor(MainController.foregroundColorThree);
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 40));
		s = "Numero di voli presenti";
		g2d.drawString(s, 70, 130);
		int sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
		g2d.drawString(String.valueOf(flightTotal), 70 + (sLength/2) - (g2d.getFontMetrics(g2d.getFont()).stringWidth(String.valueOf(flightTotal))/2), 190);
	
	}

}

