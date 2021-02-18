import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

public class AddQueueFrame extends JDialog {
	
	private MainFrame mainFrame; //Link to the mainFrame
	private String choice; //String containing the returned choice of the user

	/**
	 * Frame where the user is prompted to choose a queue to add or undo the action
	 * @param mf Link to the MainFrame
	 */
	public AddQueueFrame(MainFrame mf) {
		
		mainFrame = mf; //Link this frame to the mainFrame
		
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
		
		JLabel lblAddQueue = new JLabel("Seleziona una coda da aggiungere"); //Create label
		lblAddQueue.setFont(new Font("Tahoma", Font.BOLD, 16)); //Set label's text font
		lblAddQueue.setHorizontalAlignment(SwingConstants.CENTER); //Set label's horizontal alignment
		lblAddQueue.setHorizontalTextPosition(SwingConstants.LEADING); //Set label's text position to leading
		lblAddQueue.setBounds(10, 11, 390, 61); //Set label bounds
		contentPanel.add(lblAddQueue); //Add label to the contentPanel
			
		JComboBox cBoxAddQueue = new JComboBox(); //Create combo box
		//Debug population
		for(int i = 1; i < 6; i++) {
			cBoxAddQueue.addItem("Coda " + i);
		}
		cBoxAddQueue.setBounds(110, 83, 190, 22); //Set combo box bounds
		contentPanel.add(cBoxAddQueue); //Add combo box to the content panel
		
		JPanel buttonPanel = new JPanel(); //Create buttonPanel
		buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null)); //Set buttonPanel's border
		buttonPanel.setBackground(new Color(70, 130, 180)); //Set buttonPanel's background color
		add(buttonPanel, BorderLayout.SOUTH); //Add buttonPanel to the frame
		
		JButton addButton = new JButton("AGGIUNGI"); //Create addButton
		addButton.setFocusable(false); //Set as non focusable
		addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Set cursor as HAND_CURSOR when the mouse hovers on the button
		//Button's action listeners
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Set frame as invisible
				choice = cBoxAddQueue.getSelectedItem().toString(); //Set choice string to the selected item in the combo box
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
				choice = "undo"; //Set choice string as "undo"
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
					.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addGap(52))
		);
		//GroupLayout vertical properties
		gl_buttonPane.setVerticalGroup(
			gl_buttonPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addContainerGap(19, Short.MAX_VALUE)
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
						.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		buttonPanel.setLayout(gl_buttonPane); //Set buttonPanel's layout to the GroupLayout just created
		
	}
	
	public String getChoice() {
		return choice;
	}

}
