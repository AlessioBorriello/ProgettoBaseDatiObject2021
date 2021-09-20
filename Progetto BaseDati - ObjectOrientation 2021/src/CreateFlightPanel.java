import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.SpinnerNumberModel;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@SuppressWarnings("serial")
public class CreateFlightPanel extends JPanel {

	private MainFrame mainFrame; // Main panel
	private ArrayList<String> listaCode = new ArrayList<String>(); // List of the queues

	// Company images
	private Image airfranceLogoImage;
	private Image alitaliaLogoImage;
	private Image easyjetLogoImage;
	private Image ryanairLogoImage;

	private Image currentLogoImage; // Currently used image

	CustomButton buttonAddQueue; // Button used to add queues (Declared here so it can be accessed by the remove
									// and add queues methods)

	private int queueButtonDistance = 59; // Distance between the added queue buttons

	private int gatesNumber = MainController.gateAirportNumber; // How many gate there are in the airport

	private ArrayList<CompagniaAerea> companies; // ArrayList containing the companies

	/**
	 * Panel where the user can create a new flight to add to the database
	 * 
	 * @param bounds Bounds of the contentPanel that contains this panel (to give it
	 *               the contentPanel's dimensions)
	 * @param mf     Link to the MainFrame
	 */
	@SuppressWarnings("deprecation")
	public CreateFlightPanel(Rectangle bounds, MainFrame mf) {

		mainFrame = mf; // Link main frame

		// Load company images
		try {
			airfranceLogoImage = ImageIO.read(new File("imgs/company-logos/airfrance-logo.png"));
			alitaliaLogoImage = ImageIO.read(new File("imgs/company-logos/alitalia-logo.png"));
			easyjetLogoImage = ImageIO.read(new File("imgs/company-logos/easyjet-logo.png"));
			ryanairLogoImage = ImageIO.read(new File("imgs/company-logos/ryanair-logo.png"));
		} catch (IOException e) {
			System.out.println(e);
		}

		// Scale company images
		airfranceLogoImage = airfranceLogoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
		alitaliaLogoImage = alitaliaLogoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
		easyjetLogoImage = easyjetLogoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
		ryanairLogoImage = ryanairLogoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);

		setBounds(bounds); // Set bounds
		setLayout(null); // Set panel layout to absolute

		CustomComboBox cBoxCompany = mainFrame.createCustomComboBox(); // Combo box of the companies
		cBoxCompany.setName("cBoxCompany"); // Name component
		cBoxCompany.setBounds(35, 490, 250, 24); // Set bounds
		cBoxCompany.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Update current company image
				switch (cBoxCompany.getSelectedItem().toString()) {
				case "AirFrance":
					currentLogoImage = airfranceLogoImage;
					break;
				case "Alitalia":
					currentLogoImage = alitaliaLogoImage;
					break;
				case "EasyJet":
					currentLogoImage = easyjetLogoImage;
					break;
				case "Ryanair":
					currentLogoImage = ryanairLogoImage;
					break;
				default:
					currentLogoImage = airfranceLogoImage;
				}
				repaint();
			}
		});
		add(cBoxCompany); // Add to panel
		// Populate combo box
		companies = mainFrame.getListaCompagnie(); // Take companies from mainFrame
		if (companies != null) {
			for (CompagniaAerea comp : companies) {
				// Add each member of the array listaCompagnie to the combo box
				cBoxCompany.addItem(comp.getNome());
			}
		}

		CustomSpinner spinnerTakeOffDate = new CustomSpinner(MainController.backgroundColorOne,
				mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64),
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); // Create spinner
																										// to declare
																										// the take off
																										// date
		spinnerTakeOffDate.setModel(
				new SpinnerDateModel(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()), null,
						null, Calendar.DAY_OF_YEAR)); // Set spinner model
		spinnerTakeOffDate.setName("spinnerTakeOffDate"); // Name component
		spinnerTakeOffDate.setBounds(310, 231, 150, 24); // Set bounds
		spinnerTakeOffDate.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerTakeOffDate.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerTakeOffDate.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 12));
		add(spinnerTakeOffDate); // Add to panel

		CustomSpinner spinnerGate = new CustomSpinner(MainController.backgroundColorOne,
				mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64),
				MainController.foregroundColorThree, 2, true, MainController.foregroundColorThree, 1); // Create spinner
																										// to declare
																										// the gate
		spinnerGate.setName("spinnerGate"); // Name component
		spinnerGate.setModel(
				new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(gatesNumber), new Integer(1))); // Set
																													// the
																													// type
																													// of
																													// spinner
		spinnerGate.setBounds(310, 291, 150, 24); // Set bounds
		spinnerGate.setEditorBackgroundColor(MainController.backgroundColorOne);
		spinnerGate.setEditorForegroundColor(MainController.foregroundColorThree);
		spinnerGate.setEditorFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 12));
		add(spinnerGate); // Add to panel

		CustomComboBox cBoxDestination = mainFrame.createCustomComboBox();
		cBoxDestination.setName("cBoxDestination");
		cBoxDestination.setBounds(310, 351, 150, 24);
		add(cBoxDestination);

		populateDestinationComboBox(cBoxDestination);

		CustomButton buttonCreateFlight = new CustomButton("Crea volo!", null,
				mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64),
				MainController.foregroundColorThree, 22, true, MainController.foregroundColorThree, 1); // Create button
																										// create flight
		buttonCreateFlight.setName("buttonCreateFlight"); // Name component
		buttonCreateFlight.setBounds(349, 456, 300, 58); // Set bounds
		buttonCreateFlight.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonCreateFlight.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonCreateFlight.unselectAnimation(8);
			}
		});
		add(buttonCreateFlight); // Add to panel

		buttonAddQueue = (new CustomButton("", null,
				mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64), null, 0, true,
				MainController.foregroundColorThree, 1) {

			public void paint(Graphics g) {

				super.paint(g); // Paint the component normally first

				Graphics2D g2d = (Graphics2D) g;

				// AA
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Text AA
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

				// Draw plus icon
				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
				g2d.drawLine(getWidth() / 2, getHeight() - 4, getWidth() / 2, 4);
				g2d.drawLine(getWidth() - 4, getHeight() / 2, 4, getHeight() / 2);

			}

		}); // Create button add queue
		buttonAddQueue.setName("buttonAddQueue"); // Name component
		buttonAddQueue.setBounds(720, 182, 25, 25); // Set bounds
		buttonAddQueue.addMouseListener(new MouseAdapter() {
			// Mouse clicked
			public void mouseClicked(MouseEvent e) {
				String choice = mainFrame.createAddQueueFrame(listaCode);
				if (!choice.equals("undo")) { // If the user has not pressed the undo button
					// Check if that type of queue has been added already
					boolean queueAlreadyAdded = false;
					for (String s : listaCode) {
						if (s.equals(choice)) {
							// If the queue has been added already (has been found in the ArrayList
							// listaCode)
							queueAlreadyAdded = true;
							break;
						}
					}
					// If the queue has not been added already
					if (!queueAlreadyAdded) {
						addQueue(choice); // Add a queue
					} else { // If the queue has been added already
						mainFrame.createNotificationFrame("Questa coda è già stata aggiunta!"); // Notify user
					}
				}
			}
		});
		buttonAddQueue.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonAddQueue.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonAddQueue.unselectAnimation(8);
			}
		});
		add(buttonAddQueue); // Add to panel

		// Button action listener
		buttonCreateFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Gather data
				String nomeCompagnia = (String) cBoxCompany.getSelectedItem();
				Date data = (Date) spinnerTakeOffDate.getValue();
				int gate = (int) spinnerGate.getValue();
				String destinazione = (String) cBoxDestination.getSelectedItem();

				Thread queryThread = new Thread() {
					public void run() {
						mainFrame.createFlight(nomeCompagnia, data, gate, destinazione, listaCode); // Create flight
																									// with the gathered
																									// data
					}
				};
				queryThread.start();

			}
		});

	}

	/**
	 * Add a queue to the panelQueues
	 * 
	 * @param type Type of the queue to add to the queue list
	 */
	@SuppressWarnings("deprecation")
	public void addQueue(String type) {

		int yOffset = queueButtonDistance * (listaCode.size());

		listaCode.add(type); // Add the queue to the ArrayList listaCode

		// Determine background color
		Color bgColor = (listaCode.size() % 2 == 0) ? MainController.backgroundColorTwo : null; // If the array size is
																								// not even, set a
																								// different background
																								// color for the button
																								// (Once every 2
																								// buttons)

		// Create button for that queue
		CustomButton buttonAddedQueue = new CustomButton(type, bgColor,
				mainFrame.getDifferentAlphaColor(MainController.foregroundColorThree, 64),
				MainController.foregroundColorThree, 22, true, MainController.foregroundColorThree, 1); // Create button
																										// create flight
		buttonAddedQueue.setName("buttonAddedQueue" + (listaCode.size() - 1)); // Name component
		buttonAddedQueue.setText(type); // So that the type can be found and removed from the listaCode array
		buttonAddedQueue.setBounds(760, 170 + yOffset, 300, 50); // Set bounds
		buttonAddedQueue.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				buttonAddedQueue.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				buttonAddedQueue.unselectAnimation(8);
			}
		});
		buttonAddedQueue.addMouseListener(new MouseAdapter() {
			// When mouse clicked
			public void mouseClicked(MouseEvent e) {
				removeQueue(buttonAddedQueue);
			}
		});
		add(buttonAddedQueue);

		// Move add queue button
		buttonAddQueue.setBounds(buttonAddQueue.getBounds().x, buttonAddQueue.getBounds().y + queueButtonDistance,
				buttonAddQueue.getBounds().width, buttonAddQueue.getBounds().height);

		// Hide button if all the queues have been added
		if (listaCode.size() >= 6) {
			buttonAddQueue.show(false);
		}

		repaint();

	}

	/**
	 * Remove a queue from the panelQueues containing the queues
	 * 
	 * @param queueButton What queue button to remove
	 */
	@SuppressWarnings("deprecation")
	public void removeQueue(CustomButton queueButton) {

		if (mainFrame.createConfirmationFrame("Sei sicuro di voler rimuovere questa coda?")) { // If the user has
																								// confirmed the action

			int buttonNumber = listaCode.indexOf(queueButton.getText());
			listaCode.remove(buttonNumber); // Remove the queue from the ArrayList listaCode
			remove(queueButton); // Remove label

			// Move add queue button
			buttonAddQueue.setBounds(buttonAddQueue.getBounds().x, buttonAddQueue.getBounds().y - queueButtonDistance,
					buttonAddQueue.getBounds().width, buttonAddQueue.getBounds().height);

			// Move added queues buttons after the removed one
			for (int i = buttonNumber + 1; i <= listaCode.size(); i++) {

				CustomButton b = (CustomButton) mainFrame.getComponentByName(this,
						"buttonAddedQueue" + String.valueOf(i)); // Get named button

				if (b != null) { // Button has been found
					b.setBounds(b.getBounds().x, b.getBounds().y - queueButtonDistance, b.getBounds().width,
							b.getBounds().height); // Move it upwards
					b.setName("buttonAddedQueue" + String.valueOf(i - 1)); // Change name
					Color bgColor = (i % 2 == 0) ? MainController.backgroundColorTwo : null; // If the button position
																								// is not even, set a
																								// background color for
																								// the button
					b.setButtonBackgroundColor(bgColor); // Update background color
				}

			}

			// Show button if a queue can still be added
			if (listaCode.size() < 6) {
				buttonAddQueue.show(true);
			}

			repaint(); // Repaint panelQueues

		}

	}

	/**
	 * Add destinations taken from the destinations.txt in to a given combo box
	 * 
	 * @param box The combo box to add the destinations to
	 */
	public void populateDestinationComboBox(CustomComboBox box) {

		try {
			// Open destination text file
			File textFile = new File("txts/destinations.txt");
			Scanner r = new Scanner(textFile); // Create scanner with the file as source
			while (r.hasNextLine()) {
				String destination = r.nextLine(); // Read next line in the file
				box.addItem(destination); // Add item read to the box
			}
			r.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g); // Paint the component normally first

		Graphics2D g2d = (Graphics2D) g;

		// AA
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Text AA
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Draw background
		g2d.setColor(MainController.backgroundColorOne);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		// Draw string
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 46));
		g2d.setColor(MainController.foregroundColorThree);
		g2d.drawString("Crea un nuovo volo", 40, 120);

		// Draw company image
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 22));
		if (currentLogoImage != null) {

			g2d.drawImage(currentLogoImage, 35, (getHeight() / 2) - (currentLogoImage.getHeight(null) / 2) - 30, 285,
					(getHeight() / 2) - (currentLogoImage.getHeight(null) / 2) + 250 - 30, 0, 0,
					currentLogoImage.getWidth(null), currentLogoImage.getHeight(null), this);
			g2d.setStroke(new BasicStroke(4));
			g2d.setColor(MainController.foregroundColorThree);
			g2d.drawRoundRect(35, (getHeight() / 2) - (currentLogoImage.getHeight(null) / 2) - 30, 250, 250, 110, 110);

			// Company string
			String companyString = "Inserisci compagnia";
			int companyStringLenght = g2d.getFontMetrics(g2d.getFont()).stringWidth(companyString);
			g2d.drawString(companyString, 35 + (currentLogoImage.getWidth(null) / 2) - (companyStringLenght / 2), 468);

		}

		// Spinner and combo boxes strings
		g2d.drawString("Orario", 472, 251);
		g2d.drawString("Gate", 472, 311);
		g2d.drawString("Destinazione", 472, 371);

		// Draw separator line
		g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2d.drawLine(700, 125, 700, 545);

		// Draw queue string
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 30));
		g2d.drawString("Lista code", 823, 145);

		// Draw remove queue string
		if (listaCode.size() > 0) {

			g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 14));
			g2d.drawString("Clicca su una per rimuovere", 854, 179 + (queueButtonDistance * listaCode.size()));

		}

	}

}
