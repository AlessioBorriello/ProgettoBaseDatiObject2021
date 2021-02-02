import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
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

	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private JButton cancellaButton;
	
	boolean answer = false; //False = annulla, True = ok;

	public ConfirmationFrame(String notification, MainFrame mf) {
		
		MainFrame mainFrame = mf;
		
		setType(Type.POPUP);
		setAlwaysOnTop(true);
		setUndecorated(true);
		setModal(true);
		setSize(410, 227);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(mainFrame.getLocation().x + (mainFrame.getWidth()/2) - (this.getWidth()/2), mainFrame.getLocation().y + (mainFrame.getHeight()/2) - (this.getHeight()/2), 410, 227);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNotification = new JLabel("Notification");
			lblNotification.setBorder(null);
			lblNotification.setText(notification);
			lblNotification.setFont(new Font("Georgia", Font.PLAIN, 14));
			lblNotification.setForeground(new Color(76, 81, 109));
			lblNotification.setHorizontalAlignment(SwingConstants.CENTER);
			lblNotification.setBounds(10, 11, 389, 143);
			contentPanel.add(lblNotification);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			buttonPane.setBackground(new Color(70, 130, 180));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setFocusable(false);
				okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				okButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						setVisible(false);
						answer = true;
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			
			cancellaButton = new JButton("ANNULLA");
			cancellaButton.setFocusable(false);
			cancellaButton.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					setVisible(false);
					answer = false;
					dispose();
				}
			});
			cancellaButton.setActionCommand("CANCELLA");
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(52)
						.addComponent(cancellaButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
						.addComponent(okButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
						.addGap(52))
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addContainerGap(19, Short.MAX_VALUE)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
							.addComponent(cancellaButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addComponent(okButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
						.addContainerGap())
			);
			buttonPane.setLayout(gl_buttonPane);
		}
		
	}
	
	//Getter risposta data al pannello (Annulla o Ok)
	public boolean getAnswer() {
		return answer;
	}

}
