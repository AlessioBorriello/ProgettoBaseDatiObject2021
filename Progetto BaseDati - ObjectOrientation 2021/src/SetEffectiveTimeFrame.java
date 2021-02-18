import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
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

public class SetEffectiveTimeFrame extends JDialog {
	
	private MainFrame mainFrame; //Link to the mainFrame
	private Date time; //Date containing the returned date chosen by the user

	/**
	 * Frame where the user is prompted to choose a date to be returned
	 * @param mf Link to the mainFrame
	 * @param data Starting data that the spinner has to display
	 */
	public SetEffectiveTimeFrame(MainFrame mf, Date data) {
		
		mainFrame = mf; //Link to the mainFrame
		
		setType(Type.POPUP); //Set frame type to pop-up
		setAlwaysOnTop(true); //Set to stay always on top
		setUndecorated(true); //Set undecorated
		setModal(true); //Blocks input to other frames
		setSize(410, 227); //Set size
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); //Set operation on close (dispose)
		setBounds(mainFrame.getLocation().x + (mainFrame.getWidth()/2) - (this.getWidth()/2), mainFrame.getLocation().y + (mainFrame.getHeight()/2) - (this.getHeight()/2), 410, 227); //Put frame to the center of the mainFrame
		setLayout(new BorderLayout()); //Set layout
		
		JPanel contentPanel = new JPanel(); //Create contentPanel
		contentPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null)); //Set contentPanel's border
		add(contentPanel, BorderLayout.CENTER); //Add contentPanel to the frame
		contentPanel.setLayout(null); //Set contentPanel's layout to absolute
		
		JLabel setEffectiveTimeLabel = new JLabel("Imposta tempo partenza aereo"); //Create label
		setEffectiveTimeLabel.setFont(new Font("Tahoma", Font.BOLD, 16)); //Set label's text font
		setEffectiveTimeLabel.setHorizontalAlignment(SwingConstants.CENTER); //Set label's horizontal alignment
		setEffectiveTimeLabel.setHorizontalTextPosition(SwingConstants.LEADING); //Set label's text position to leading
		setEffectiveTimeLabel.setBounds(10, 11, 390, 61); //Set label bounds
		contentPanel.add(setEffectiveTimeLabel); //Add label to the contentPanel
	
	
		JSpinner spinnerTakeOffDate = new JSpinner(); //Create date spinner
		spinnerTakeOffDate.setName("spinnerTakeOffDate"); //Set name
		spinnerTakeOffDate.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR)); //Set spinner's model
		spinnerTakeOffDate.setValue(data); //Set starting date of the spinner
		spinnerTakeOffDate.setFont(new Font("Tahoma", Font.BOLD, 11)); //Set spinner's font
		spinnerTakeOffDate.setBounds(142, 83, 148, 20); //Set bounds
		contentPanel.add(spinnerTakeOffDate); //Add spinner to the contentPanel
	
	
	
		JPanel buttonPanel = new JPanel(); //Create buttonPanel
		buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null)); //Set buttonPanel's border
		buttonPanel.setBackground(new Color(70, 130, 180)); //Set buttonPanel's background color
		add(buttonPanel, BorderLayout.SOUTH); //Add buttonPanel to the frame
		
		JButton setButton = new JButton("FATTO"); //Create setButton
		setButton.setFocusable(false); //Set as non focusable
		setButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Set cursor as HAND_CURSOR when the mouse hovers on the button
		//Button's action listeners
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Set frame as invisible
				time = (Date)spinnerTakeOffDate.getValue(); //Get return date from the spinner
				dispose(); //Dispose frame
			}
		});

		
		JButton undoButton = new JButton("ANNULLA"); //Create undoButton
		undoButton.setFocusable(false); //Set as non focusable
		undoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Set cursor as HAND_CURSOR when the mouse hovers on the button
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
	
	public Date getDate() {
		return time;
	}

}
