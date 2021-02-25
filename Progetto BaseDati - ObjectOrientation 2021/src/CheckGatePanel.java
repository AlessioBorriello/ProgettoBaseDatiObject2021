import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;

public class CheckGatePanel extends JPanel {
	
	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	
	private ArrayList<Volo> flightList;
	
	private ArrayList<Slot> estimatedSlotList = new ArrayList<Slot>();
	private ArrayList<Slot> effectiveSlotList = new ArrayList<Slot>();
	
	private JPanel flightListPanel;
	
	private int[] estimatedAverages = new int[3];
	private int[] effectiveAverages = new int[3];
	
	private int gateNumber;

	public CheckGatePanel(Rectangle bounds, MainFrame mf, MainController c, ArrayList<Volo> flightList, int gateNumber) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		
		this.flightList = flightList;
		
		this.gateNumber = gateNumber;
		
		//setBounds(bounds);
		setBounds(72, 2, 1124, 666); //Debug to show in the design tab,  this row should be replaced with the one above
		setLayout(null);
		
		JPanel flightsPanel = new JPanel();
		flightsPanel.setBounds(763, 420, 228, 180);
		add(flightsPanel);
		flightsPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setBorder(null);
		scrollPanel.setVerticalScrollBar(mainFrame.createCustomScrollbar());
		flightsPanel.add(scrollPanel);
		
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
		
		populateFlightListPanel(flightList);
		
		//Get slots from the flights
		for(Volo v : flightList) {
			if(!v.isCancellato()) {
				estimatedSlotList.add(v.getSlot());
				if(v.isPartito()) {
					effectiveSlotList.add(v.getSlot());
				}
			}
		}
		
		//Calculate averages
		if(estimatedSlotList.size() > 0) {
			estimatedAverages = calculateEstimatedGateAverageUse(estimatedSlotList);
		}else {
			estimatedAverages = null;
		}
		
		if(effectiveSlotList.size() > 0) {
			effectiveAverages = calculateEstimatedGateAverageUse(effectiveSlotList);
		}else {
			effectiveAverages = null;
		}
		
		//Add check boxes
		CustomCheckBox chckbxInclude = new CustomCheckBox("Includi voli partiti e cancellati", null, 16, MainController.foregroundColorThree, false, null, 0); //Create check box
		chckbxInclude.setSelected(true);
		chckbxInclude.setName("chckbxIncludeTakenOffLate"); //Set name
		//Toggle gate spinner
		chckbxInclude.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!chckbxInclude.isSelected()) {
					//Include flights
					populateFlightListPanel(flightList);
				}else {
					//Exclude flights
					ArrayList<Volo> tempFlightList = new ArrayList<Volo>();
					for(Volo v : flightList) {
						if(!v.isCancellato() && !v.isPartito()) { //Add only flights not cancelled and not taken off
							tempFlightList.add(v);
						}
					}
					populateFlightListPanel(tempFlightList);
				}
			}
		});
		chckbxInclude.setBounds(750, 617, 270, 23); //Set bounds
		add(chckbxInclude); //Add the check box
		
	}
	
	public void populateFlightListPanel(ArrayList<Volo> list) {
		
		//Clear panel first
		flightListPanel.removeAll();
		
		int index = 0;
		int height = 30;
		int vGap = 2;
		for(Volo v : list) {
			
			Color borderColor;
			if(v.isPartito()) {
				if(v.checkIfFlightTookOffLate()) {
					borderColor = MainController.flightTakenOffLateColor; //Taken off late
				}else {
					borderColor = MainController.flightTakenOffColor; //Taken off
				}
			}else {
				if(v.isCancellato()) {
					borderColor = MainController.flightCancelledColor; //Cancelled
				}else {
					borderColor = MainController.flightProgrammedColor; //Programmed
				}
			}
			
			CustomButton buttonFlight = new CustomButton(v.getID(), null, mainController.getDifferentAlphaColor(borderColor, 48), 
					MainController.foregroundColorThree, 18, true, borderColor, 2); //Create button create flight
			buttonFlight.setName("buttonFlight"); //Name component
			buttonFlight.setBounds(0, 0 + ((height + vGap) * index), 228, height); //Set bounds
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
					mainFrame.setContentPanelToViewFlightInfoPanel(v);
				}
			});
			flightListPanel.add(buttonFlight);
			
			index++;
		}
		
		//Calculate new height of the grid panel (after the buttons have been added)
		int newGridPanelHeight = ((height + vGap) * index) - vGap;
		
		//Create group layout
		GroupLayout gl_gridPanel = new GroupLayout(flightListPanel);
		gl_gridPanel.setHorizontalGroup(
			gl_gridPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 226, Short.MAX_VALUE)
		);
		gl_gridPanel.setVerticalGroup(
			gl_gridPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, newGridPanelHeight, Short.MAX_VALUE)
		);
		flightListPanel.setLayout(gl_gridPanel); //Set layout to the grid panel
		
	}
	
	public int[] calculateEstimatedGateAverageUse(ArrayList<Slot> list) {
		
		//Note that the slot are ordered by their flight's take off time
		
		Date previousInizioTempoStimato = null;
		Date previousFineTempoStimato = null;
		
		int daySampleSize = 0;
		int weekSampleSize = 0;
		int monthSampleSize = 0;
		for(Slot s : list) {
			
			Date inizioTempoStimato = s.getInizioTempoStimato();
			Date fineTempoStimato = s.getFineTempoStimato();
			
			if(previousInizioTempoStimato == null || dateDayDifference(previousInizioTempoStimato, inizioTempoStimato) > 0 || dateDayDifference(previousFineTempoStimato, fineTempoStimato) > 0) {
				daySampleSize++;
				if(previousInizioTempoStimato == null || dateDayDifference(previousInizioTempoStimato, inizioTempoStimato) > 6 || dateDayDifference(previousFineTempoStimato, fineTempoStimato) > 6) {
					weekSampleSize++;
					if(previousInizioTempoStimato == null || dateDayDifference(previousInizioTempoStimato, inizioTempoStimato) > 30 || dateDayDifference(previousFineTempoStimato, fineTempoStimato) > 30) {
						monthSampleSize++;
					}
				}
			}
			
			//Update previous fields
			previousInizioTempoStimato = inizioTempoStimato;
			previousFineTempoStimato = fineTempoStimato;
			
		}
		
		int[] averages = new int[3];
		averages[0] = (list.size() * 15) / daySampleSize;
		averages[1] = (list.size() * 15) / weekSampleSize;
		averages[2] = (list.size() * 15) / monthSampleSize;
		
		return averages;
		
	}
	
	public int[] calculateEffectiveGateAverageUse(ArrayList<Slot> list) {
		
		//Note that the slot are ordered by their flight's take off time
		
		Date previousInizioTempoEffettivo = null;
		Date previousFineTempoEffettivo = null;
		
		int daySampleSize = 0;
		int weekSampleSize = 0;
		int monthSampleSize = 0;
		for(Slot s : list) {
			
			Date inizioTempoEffettivo = s.getInizioTempoEffettivo();
			Date fineTempoEffettivo = s.getFineTempoEffettivo();
			
			if(previousInizioTempoEffettivo == null || dateDayDifference(previousInizioTempoEffettivo, inizioTempoEffettivo) > 0 || dateDayDifference(previousFineTempoEffettivo, fineTempoEffettivo) > 0) {
				daySampleSize++;
				if(previousInizioTempoEffettivo == null || dateDayDifference(previousInizioTempoEffettivo, inizioTempoEffettivo) > 6 || dateDayDifference(previousFineTempoEffettivo, fineTempoEffettivo) > 6) {
					weekSampleSize++;
					if(previousInizioTempoEffettivo == null || dateDayDifference(previousInizioTempoEffettivo, inizioTempoEffettivo) > 30 || dateDayDifference(previousFineTempoEffettivo, fineTempoEffettivo) > 30) {
						monthSampleSize++;
					}
				}
			}
			
			//Update previous fields
			previousInizioTempoEffettivo = inizioTempoEffettivo;
			previousFineTempoEffettivo = fineTempoEffettivo;
			
		}
		
		int[] averages = new int[3];
		averages[0] = (list.size() * 15) / daySampleSize;
		averages[1] = (list.size() * 15) / weekSampleSize;
		averages[2] = (list.size() * 15) / monthSampleSize;
		
		return averages;
		
	}
	
	public long dateDayDifference(Date date1, Date date2) {
		
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd"); //Date format
		
		LocalDate d1 = LocalDate.parse(dateTimeFormat.format(date1), DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDate d2 = LocalDate.parse(dateTimeFormat.format(date2), DateTimeFormatter.ISO_LOCAL_DATE);
		Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
		return diff.toDays();
		
	}

	public void drawColumnGraph(Graphics2D g2d, ArrayList<Volo> flightList, int xOrigin, int yOrigin, int width, int height, int strokeWidth, int step) {
		
		BasicStroke normalStroke = new BasicStroke(strokeWidth);
		Stroke dashedStroke = new BasicStroke(strokeWidth/2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6}, 0); //Dashed stroke with half the stroke width
		
		//Draw plane
		g2d.setColor(MainController.foregroundColorThree);
		g2d.setStroke(normalStroke);
		g2d.drawLine(xOrigin, yOrigin, xOrigin, yOrigin - height);
		g2d.drawLine(xOrigin, yOrigin, xOrigin + width, yOrigin);
		
		//---------------AirFrance------AlItalia------EasyJet-------RyanAir
		int[][] data = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}}; // {programmed, takenOff, takenOffLate, cancelled}
		
		for(Volo v : flightList) {
			
			int companyIndex;
			switch(v.getCompagnia().getNome()) {
				case "AirFrance": companyIndex = 0; break;
				case "Alitalia": companyIndex = 1; break;
				case "EasyJet": companyIndex = 2; break;
				case "RyanAir": companyIndex = 3; break;
				default: companyIndex = 0;
			}
			
			if(v.isPartito()) { //Flight has taken off
				if(v.isInRitardo()) { //It did so late
					data[companyIndex][2]++;
				}else {
					data[companyIndex][1]++;
				}
			}else { //Flight has not taken off
				if(v.isCancellato()) { //It has been cancelled
					data[companyIndex][3]++;
				}else {
					data[companyIndex][0]++;
				}
			}
			
		}
		
		//Define max value
		int maxValue = data[0][0] + data[0][1] + data[0][2] + data[0][3];
		for(int i = 1; i < 4; i++) {
			
			int newValue = data[i][0] + data[i][1] + data[i][2] + data[i][3];
			if(newValue > maxValue) {
				maxValue = newValue;
			}
			
		}
		
		//Increase max value by a percentage
		maxValue += Math.ceil(maxValue * .2);
		
		//Get segments length (width - a small percentage of the axis length)/maxValue
		int xSegmentLength = Math.round((float)(width - (width * .18f))/4); //4 for each of the 4 companies
		int ySegmentLength = Math.round((float)(height - (height * .05f))/maxValue);
		
		//Where the actual graph starts
		int xGraphOrigin = xOrigin;
		int yGraphOrigin = yOrigin - ((int)normalStroke.getLineWidth()/2);
		
		//Draw background graph dashed lines and border values
		g2d.setColor(new Color(MainController.foregroundColorThree.getRed(), MainController.foregroundColorThree.getGreen(), MainController.foregroundColorThree.getBlue(), 95)); //Lower alpha black
		g2d.setStroke(dashedStroke); //Dashed stroke
		
		for(int i = step; i <= maxValue; i += step) { //Each yStep
			
			g2d.drawLine(xOrigin, yGraphOrigin - (i * ySegmentLength), xOrigin + width, yGraphOrigin - (i * ySegmentLength)); //Dashed line on x axis
			int numberStringLenght = g2d.getFontMetrics().stringWidth(String.valueOf(i));
			g2d.drawString(String.valueOf(i), xOrigin - numberStringLenght - 4, yGraphOrigin - (i * ySegmentLength) + 5);
			
		}
		
		//Draw columns
		int columnWidth = 20;
		for(int i = 0; i < 4; i++) {
			
			int yOffset = 0;
			int totalValue = 0;
			
			Color[] dataColors = {MainController.flightProgrammedColor, MainController.flightTakenOffColor, MainController.flightTakenOffLateColor, MainController.flightCancelledColor}; //Programmed, taken off, taken off late, cancelled
			Color[] companyColors = {MainController.airfranceColor, MainController.alitaliaColor, MainController.easyjetColor, MainController.ryanairColor}; //AirFrance, AlItalia, EasyJet, RyanAir
			
			for(int j = 0; j < 4; j++) {
				g2d.setColor(dataColors[j]);
				g2d.fillRect(xGraphOrigin + xSegmentLength + (xSegmentLength * i) - (columnWidth/2), yGraphOrigin - (ySegmentLength * data[i][j]) - yOffset, columnWidth, (ySegmentLength * data[i][j]));
				yOffset += (ySegmentLength * data[i][j]);
				totalValue += data[i][j];
			}
			
			//Draw company colored outline
			if(totalValue > 0) {
				g2d.setStroke(new BasicStroke(normalStroke.getLineWidth()*2));
				g2d.setColor(companyColors[i]);
				g2d.drawRect(xGraphOrigin + xSegmentLength + (xSegmentLength * i) - (columnWidth/2), yGraphOrigin - (ySegmentLength * totalValue), columnWidth, (ySegmentLength * totalValue));
			}
		}
		
		//Draw graph info
		int positionX = xOrigin + width + 15;
		int positionY = yOrigin - height;
		int yOffset = 30;
		int squareSize = 15;
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 14));
		
		g2d.setColor(MainController.airfranceColor);
		g2d.drawRect(positionX, positionY, squareSize, squareSize);
		g2d.setColor(MainController.foregroundColorThree);
		g2d.drawString("AirFrance", positionX + 25, positionY + 13);
		
		g2d.setColor(MainController.alitaliaColor);
		g2d.drawRect(positionX, positionY + yOffset, squareSize, squareSize);
		g2d.setColor(MainController.foregroundColorThree);
		g2d.drawString("Alitalia", positionX + 25, positionY + yOffset + 13);
		
		g2d.setColor(MainController.easyjetColor);
		g2d.drawRect(positionX, positionY + (yOffset * 2), squareSize, squareSize);
		g2d.setColor(MainController.foregroundColorThree);
		g2d.drawString("EasyJet", positionX + 25, positionY + (yOffset * 2) + 13);
		
		g2d.setColor(MainController.ryanairColor);
		g2d.drawRect(positionX, positionY + (yOffset * 3), squareSize, squareSize);
		g2d.setColor(MainController.foregroundColorThree);
		g2d.drawString("RyanAir", positionX + 25, positionY + (yOffset * 3) + 13);
		
		g2d.setColor(MainController.flightCancelledColor);
		g2d.fillRect(positionX, positionY + (yOffset * 5), squareSize, squareSize);
		g2d.setColor(MainController.foregroundColorThree);
		g2d.drawString("Cancellati", positionX + 25, positionY + (yOffset * 5) + 13);
		
		g2d.setColor(MainController.flightTakenOffLateColor);
		g2d.fillRect(positionX, positionY + (yOffset * 6), squareSize, squareSize);
		g2d.setColor(MainController.foregroundColorThree);
		g2d.drawString("Partiti in ritardo", positionX + 25, positionY + (yOffset * 6) + 13);
		
		g2d.setColor(MainController.flightTakenOffColor);
		g2d.fillRect(positionX, positionY + (yOffset * 7), squareSize, squareSize);
		g2d.setColor(MainController.foregroundColorThree);
		g2d.drawString("Partiti", positionX + 25, positionY + (yOffset * 7) + 13);
		
		g2d.setColor(MainController.flightProgrammedColor);
		g2d.fillRect(positionX, positionY + (yOffset * 8), squareSize, squareSize);
		g2d.setColor(MainController.foregroundColorThree);
		g2d.drawString("Programmati", positionX + 25, positionY + (yOffset * 8) + 13);
		
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
	    
	    //Draw gate number
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 46));
	    g2d.setColor(MainController.foregroundColorThree);
	    g2d.drawString("Informazioni Gate " + String.valueOf(gateNumber), 40, 100);
	    
	    //Draw averages table
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 36));
	    g2d.drawString("Utilizzo medio stimato", 62, 250);
	    g2d.drawString("Utilizzo medio effettivo", 40, 320);
	    
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 28));
	    int distancing = 200;
	    String s = "Giornaliero";
	    g2d.drawString(s, 540, 205);
	    s = "Settimanale";
	    g2d.drawString(s, 530 + (distancing), 205);
	    s = "Mensile";
	    g2d.drawString(s, 540 + (distancing * 2), 205);
	    
	    //Draw table lines
	    Stroke dashedStroke = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{18}, 0);
	    g2d.setStroke(dashedStroke);
	    g2d.drawLine(35, 275, 1070, 275);
	    g2d.drawLine(715, 175, 715, 355);
	    g2d.drawLine(923, 175, 923, 355);
	    
	    //Draw table data
	    //Estimated
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 20));
	    s = estimatedAverages[0] + " Minuti";
	    int sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
	    g2d.drawString(s, 613 - (sLength/2), 250);
	    s = estimatedAverages[1] + " Minuti";
	    sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
	    g2d.drawString(s, 820 - (sLength/2), 250);
	    s = estimatedAverages[2] + " Minuti";
	    sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
	    g2d.drawString(s, 994 - (sLength/2), 250);
	    
	    //Effective
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 20));
	    if(effectiveAverages != null) {
		    s = effectiveAverages[0] + " Minuti";
		    sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
		    g2d.drawString(s, 613 - (sLength/2), 320);
		    s = effectiveAverages[1] + " Minuti";
		    sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
		    g2d.drawString(s, 820 - (sLength/2), 320);
		    s = effectiveAverages[2] + " Minuti";
		    sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
		    g2d.drawString(s, 994 - (sLength/2), 320);
	    }else {
	    	s = "N/A";
		    sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
		    g2d.drawString(s, 618 - (sLength/2), 320);
		    s = "N/A";
		    sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
		    g2d.drawString(s, 825 - (sLength/2), 320);
		    s = "N/A";
		    sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
		    g2d.drawString(s, 999 - (sLength/2), 320);
	    }
	    
	    //Draw column graph
	    drawColumnGraph(g2d, flightList, 40, 635, 450, 260, 2, 1);
	    
	    //Draw flight on top of the flight scroll panel
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 32));
	    g2d.drawString("Voli", 842, 398);
	    
	    //Draw lines
	    g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
	    g2d.drawLine(755, 412, 998, 412);
	    g2d.drawLine(755, 610, 998, 610);
	    
		
	}

}
