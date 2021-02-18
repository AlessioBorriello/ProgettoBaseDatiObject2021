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

public class ConfirmationFrame extends JDialog {
	
	private MainFrame mainFrame; //Link to the mainFrame
	private boolean answer = false; //Boolean containing the choice of the user (False = undo, True = confirm)

	/**
	 * Frame showing a confirmation choice to the user to confirm or undo
	 * @param notification Notification text to show the user
	 * @param mf Link to the MainFrame
	 */
	public ConfirmationFrame(String notification, MainFrame mf) {
		
		mainFrame = mf; //Link the frame and the mainFrame
		
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
		
		JLabel lblNotification = new JLabel("Notification"); //Create label
		lblNotification.setText(notification); //Set label's text to the passed notification
		lblNotification.setFont(new Font("Georgia", Font.PLAIN, 14)); //Set label's text font
		lblNotification.setForeground(new Color(76, 81, 109)); //Set label's text color
		lblNotification.setHorizontalAlignment(SwingConstants.CENTER); //Set label's horizontal alignment
		lblNotification.setBounds(10, 11, 389, 143); //Set label bounds
		contentPanel.add(lblNotification); //Add label to the contentPanel
			
		JPanel buttonPanel = new JPanel(); //Create buttonPanel
		buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null)); //Set buttonPanel's border
		buttonPanel.setBackground(new Color(70, 130, 180)); //Set buttonPanel's background color
		add(buttonPanel, BorderLayout.SOUTH); //Add buttonPanel to the frame

		JButton okButton = new JButton("OK"); //Create okButton
		okButton.setFocusable(false); //Set as non focusable
		okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Set cursor as HAND_CURSOR when the mouse hovers on the button
		//Button's action listeners
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Set frame as invisible
				answer = true; //Set answer to true (confirm)
				dispose(); //Dispose frame
			}
		});
		
		JButton cancellaButton = new JButton("ANNULLA"); //Create cancellaButton
		cancellaButton.setFocusable(false); //Set as non focusable
		cancellaButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Set cursor as HAND_CURSOR when the mouse hovers on the button
		//Button's action listeners
		cancellaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Set frame as invisible
				answer = false; //Set answer to true (undo)
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
					.addComponent(cancellaButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
					.addComponent(okButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addGap(52))
		);
		//GroupLayout vertical properties
		gl_buttonPane.setVerticalGroup(
			gl_buttonPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addContainerGap(19, Short.MAX_VALUE)
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
						.addComponent(cancellaButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(okButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		buttonPanel.setLayout(gl_buttonPane); //Set buttonPanel's layout to the GroupLayout just created
		
	}
	
	public boolean getAnswer() {
		return answer;
	}

}
