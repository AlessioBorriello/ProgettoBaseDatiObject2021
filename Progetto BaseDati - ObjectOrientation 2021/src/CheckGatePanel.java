import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;

public class CheckGatePanel extends JPanel {
	
	private MainFrame mainFrame; //Main panel
	private MainController mainController; //Main controller
	private ArrayList<Slot> estimatedSlotList = new ArrayList<Slot>();
	private ArrayList<Slot> effectiveSlotList = new ArrayList<Slot>();
	
	private JPanel flightListPanel;

	public CheckGatePanel(Rectangle bounds, MainFrame mf, MainController c, ArrayList<Volo> flightList, int gateNumber) {
		
		mainFrame = mf; //Link main frame
		mainController = c; //Link main controller
		
		//setBounds(bounds);
		setBounds(72, 2, 1124, 666); //Debug to show in the design tab,  this row should be replaced with the one above
		setLayout(null);
		
		JLabel lblGateNumber = new JLabel("Gate: " + String.valueOf(gateNumber));
		lblGateNumber.setFont(new Font("Tahoma", Font.BOLD, 42));
		lblGateNumber.setBounds(10, 11, 247, 81);
		add(lblGateNumber);
		
		JPanel flightsPanel = new JPanel();
		flightsPanel.setBounds(710, 145, 228, 408);
		add(flightsPanel);
		flightsPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		flightsPanel.add(scrollPanel);
		
		flightListPanel = new JPanel();
		flightListPanel.setLayout(null);
		scrollPanel.setViewportView(flightListPanel);
		
		JLabel labelEstimatedAverage = new JLabel("Utilizzo medio stimato: ");
		labelEstimatedAverage.setHorizontalAlignment(SwingConstants.TRAILING);
		labelEstimatedAverage.setFont(new Font("Tahoma", Font.BOLD, 22));
		labelEstimatedAverage.setBounds(10, 236, 279, 34);
		add(labelEstimatedAverage);
		
		JLabel labelEffectiveAverage = new JLabel("Utilizzo medio effettivo: ");
		labelEffectiveAverage.setHorizontalAlignment(SwingConstants.TRAILING);
		labelEffectiveAverage.setFont(new Font("Tahoma", Font.BOLD, 22));
		labelEffectiveAverage.setBounds(10, 281, 279, 34);
		add(labelEffectiveAverage);
		
		JLabel labelDay = new JLabel("Giornaliero");
		labelDay.setHorizontalAlignment(SwingConstants.CENTER);
		labelDay.setFont(new Font("Tahoma", Font.BOLD, 14));
		labelDay.setBounds(313, 207, 81, 26);
		add(labelDay);
		
		JLabel labelWeek = new JLabel("Settimanale");
		labelWeek.setHorizontalAlignment(SwingConstants.CENTER);
		labelWeek.setFont(new Font("Tahoma", Font.BOLD, 14));
		labelWeek.setBounds(404, 207, 88, 26);
		add(labelWeek);
		
		JLabel labelMonth = new JLabel("Mensile");
		labelMonth.setHorizontalAlignment(SwingConstants.CENTER);
		labelMonth.setFont(new Font("Tahoma", Font.BOLD, 14));
		labelMonth.setBounds(502, 207, 81, 26);
		add(labelMonth);
		
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
		
		int[] estimatedAverages = new int[3];
		if(estimatedSlotList.size() > 0) {
			estimatedAverages = calculateEstimatedGateAverageUse(estimatedSlotList);
		}else {
			estimatedAverages = null;
		}
		int[] effectiveAverages = new int[3];
		if(effectiveSlotList.size() > 0) {
			effectiveAverages = calculateEstimatedGateAverageUse(effectiveSlotList);
		}else {
			effectiveAverages = null;
		}
		
		
		//Get averages and put them in final variables
		int[] estimatedA = estimatedAverages;
		int[] effectiveA = effectiveAverages;
		JPanel panelAverages = (new JPanel() {
			
			public void paintComponent(Graphics g) {
				
				Graphics2D g2d = (Graphics2D)g;
				if(estimatedA != null) {
					g2d.setColor(Color.black);
					g2d.setFont(new Font("Tahoma", Font.BOLD, 14));
					g2d.drawString(String.valueOf(estimatedA[0]), 46, 15);
					g2d.drawString(String.valueOf(estimatedA[1]), 142, 15);
					g2d.drawString(String.valueOf(estimatedA[2]), 230, 15);
					g2d.drawString("Minuti", 280, 15);
				}else {
					g2d.setColor(Color.black);
					g2d.setFont(new Font("Tahoma", Font.BOLD, 14));
					g2d.drawString("Minuti", 90, 15);
				}
				
				if(effectiveA != null) {
					g2d.setColor(Color.black);
					g2d.setFont(new Font("Tahoma", Font.BOLD, 14));
					g2d.drawString(String.valueOf(effectiveA[0]), 46, 58);
					g2d.drawString(String.valueOf(effectiveA[1]), 142, 58);
					g2d.drawString(String.valueOf(effectiveA[2]), 230, 58);
					g2d.drawString("Minuti", 280, 58);
				}else {
					g2d.setColor(Color.black);
					g2d.setFont(new Font("Tahoma", Font.BOLD, 14));
					g2d.drawString("Dati non disponibili", 86, 58);
				}
				
			}
			
		});
		panelAverages.setBounds(299, 247, 342, 68);
		add(panelAverages);
		
	}
	
	public void populateFlightListPanel(ArrayList<Volo> list) {
		
		int index = 0;
		int height = 22;
		for(Volo v : list) {
			
			Color borderColor;
			if(v.isPartito()) {
				if(v.checkIfFlightTookOffLate()) {
					borderColor = new Color(255, 255, 0); //Yellow border
				}else {
					borderColor = new Color(0, 0, 255); //Blue border
				}
			}else {
				if(v.isCancellato()) {
					borderColor = new Color(255, 0, 0); //Red border
				}else {
					borderColor = new Color(0, 0, 0); //Black border
				}
			}
			
			JLabel l = new JLabel(v.getID());
			l.setBounds(0, 0 + (height * index), 226, height - 2);
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setBorder(new LineBorder((borderColor), 2));
			l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			l.addMouseListener(new MouseAdapter() {
				//When mouse clicked
				public void mouseClicked(MouseEvent e) {
					mainFrame.setContentPanelToViewFlightInfoPanel(v);
				}
			});
			flightListPanel.add(l);
			
			index++;
		}
		
		//Calculate new height of the grid panel (after the panels have been added)
		int newGridPanelHeight = height * index;
		
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
}
