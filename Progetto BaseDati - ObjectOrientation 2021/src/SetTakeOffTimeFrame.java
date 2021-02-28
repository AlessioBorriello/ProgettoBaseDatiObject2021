import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EtchedBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class SetTakeOffTimeFrame extends JDialog {
	
	private MainFrame mainFrame; //Link to the mainFrame
	private Date time; //Date containing the returned date chosen by the user

	/**
	 * Frame where the user is prompted to choose a date to be returned
	 * @param mf Link to the mainFrame
	 * @param data Starting date that the spinner has to display
	 */
	public SetTakeOffTimeFrame(MainFrame mf, Date data) {
		
		mainFrame = mf; //Link to the mainFrame
		
		//Calculate minimum date for the spinner (5 minutes before the date passed as argument (A flight can't take off before the start of it's estimated slot time))
		Calendar c = Calendar.getInstance(); //Create a calendar instance
		c.setTime(data); //Set the calendar time to the passed date
		c.add(Calendar.MINUTE, -5); //Remove 5 minutes
		Date minimumDate = new Date();
		minimumDate = c.getTime();
		
		setType(Type.POPUP); //Set frame type to pop-up
		setAlwaysOnTop(true); //Set to stay always on top
		setUndecorated(true); //Set undecorated
		setModal(true); //Blocks input to other frames
		setSize(410, 227); //Set size
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); //Set operation on close (dispose)
		setBounds(mainFrame.getLocation().x + (mainFrame.getWidth()/2) - (this.getWidth()/2), mainFrame.getLocation().y + (mainFrame.getHeight()/2) - (this.getHeight()/2), 410, 227); //Put frame to the center of the mainFrame
		setLayout(new BorderLayout()); //Set layout
		
		JPanel contentPanel = (new JPanel() {
			
			public void paintComponent(Graphics g) { //Access component paint method
				
				super.paintComponent(g); //Paint the component normally first
				
				Graphics2D g2d = (Graphics2D)g;
				
				//AA
			    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			    //Text AA
			    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			    
			    //Draw background
			    GradientPaint gPaint = new GradientPaint(getWidth()/2, -200, MainController.backgroundColorTwo, getWidth()/2, getHeight(), MainController.backgroundColorOne);
			    g2d.setPaint(gPaint);
			    g2d.fillRect(0, 0, getWidth(), getHeight());
			    
			    //Draw border
			    g2d.setColor(MainController.foregroundColorThree);
			    g2d.setStroke(new BasicStroke(3));
			    g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			    
			    //Draw the string
			    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 22));
			    g2d.setColor(MainController.foregroundColorThree);
			    int length = g2d.getFontMetrics(g2d.getFont()).stringWidth("Imposta orario");
			    g2d.drawString("Imposta orario", (getWidth()/2) - (length/2), 50);
			    length = g2d.getFontMetrics(g2d.getFont()).stringWidth("partenza");
			    g2d.drawString("partenza", (getWidth()/2) - (length/2), 80);
			    
			}
			
		}); //Create contentPanel
		add(contentPanel, BorderLayout.CENTER); //Add contentPanel to the frame
		contentPanel.setLayout(null); //Set contentPanel's layout to absolute
	
		CustomSpinner spinnerTakeOffDate = new CustomSpinner(MainController.backgroundColorOne, 
				new Color(MainController.foregroundColorThree.getRed(), MainController.foregroundColorThree.getGreen(), MainController.foregroundColorThree.getBlue(), 64), 
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); //Create date spinner
		spinnerTakeOffDate.setName("spinnerTakeOffDate"); //Set name
		spinnerTakeOffDate.setModel(new SpinnerDateModel(data, minimumDate, null, Calendar.DAY_OF_YEAR)); //Set spinner's model
		spinnerTakeOffDate.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerTakeOffDate.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerTakeOffDate.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 13));
		spinnerTakeOffDate.setBounds((getWidth()/2) - 100, 108, 200, 22); //Set bounds
		contentPanel.add(spinnerTakeOffDate); //Add spinner to the contentPanel
	
		
		JPanel buttonPanel = (new JPanel() {
			
			public void paintComponent(Graphics g) { //Access component paint method
				
				super.paintComponent(g); //Paint the component normally first
				
				Graphics2D g2d = (Graphics2D)g;
				
				//AA
			    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			    //Text AA
			    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			    
			    //Draw background
			    g2d.setColor(MainController.backgroundColorTwo);
			    g2d.fillRect(0, 0, getWidth(), getHeight());
			    
			    //Draw border
			    g2d.setColor(MainController.foregroundColorThree);
			    g2d.setStroke(new BasicStroke(3));
			    g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			    
			}
			
		}); //Create buttonPanel
		add(buttonPanel, BorderLayout.SOUTH); //Add buttonPanel to the frame
		
		CustomButton setButton = new CustomButton("CONFERMA", null, 
				new Color(MainController.foregroundColorThree.getRed(), MainController.foregroundColorThree.getGreen(), MainController.foregroundColorThree.getBlue(), 64), 
				MainController.foregroundColorThree, 18, true, MainController.foregroundColorThree, 1); //Create setButton
		setButton.setFocusable(false); //Set as non focusable
		setButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				setButton.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				setButton.unselectAnimation(8);
			}
		});
		//Button's action listeners
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Set frame as invisible
				time = (Date)spinnerTakeOffDate.getValue(); //Get return date from the spinner
				dispose(); //Dispose frame
			}
		});

		
		CustomButton undoButton = new CustomButton("ANNULLA", null, 
				new Color(MainController.foregroundColorThree.getRed(), MainController.foregroundColorThree.getGreen(), MainController.foregroundColorThree.getBlue(), 64), 
				MainController.foregroundColorThree, 18, true, MainController.foregroundColorThree, 1); //Create undoButton
		undoButton.setFocusable(false); //Set as non focusable
		undoButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				undoButton.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				undoButton.unselectAnimation(8);
			}
		});
		//Button's action listeners
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Set frame as invisible
				time = null; //Set return date as null
				dispose(); //Dispose frame
			}
		});
		
		//Layout creation
		GroupLayout gl_buttonPane = new GroupLayout(buttonPanel); //Create new GroupLayout for the buttonPanel
		//GroupLayout horizontal properties
		gl_buttonPane.setHorizontalGroup(
			gl_buttonPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addGap(52)
					.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
					.addComponent(setButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addGap(52))
		);
		//GroupLayout vertical properties
		gl_buttonPane.setVerticalGroup(
			gl_buttonPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addContainerGap(19, Short.MAX_VALUE)
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
						.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(setButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		buttonPanel.setLayout(gl_buttonPane); //Set buttonPanel's layout to the GroupLayout just created
		
		
	}
	
	/**
	 * Get the date chosen in the spinner by the user
	 * @return The date chosen in the spinner by the user
	 */
	public Date getDate() {
		return time;
	}

}
