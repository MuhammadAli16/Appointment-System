import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainForm extends JFrame {
	/**
	 * @author Muhammad Ali 13156253
	 */

	AppointmentBook appBook = new AppointmentBook();
	Appointment app = new Appointment();

	private JLabel count, countMax, sD, eD, aYear, startMonth, startDate, startHour, startMin, theEnd, endYear,
			endMonth, endDate, endHour, endMin, event; // Declare
	private JList allApps;
	private JTextField filename = new JTextField(), dir = new JTextField();
	private JTextField startDateText, eventSearch, eventText; // Declare

	private JButton next, previous, save, delete, search, viewAllApps, viewMonthlyApps;
	private int counter, theNum, theNumMax;

	private String[] yearArray = new String[100];

	private String[] thirtyOneArray = new String[31];
	private String[] thirtyArray = new String[30];
	private String[] twentyNineArray = new String[29];
	private String[] twentyEightArray = new String[28];

	private String[] monthArray = new String[13];
	private String[] hourArray = new String[61];
	private String[] minArray = new String[61];

	private final JComboBox<String> comboMonthSearch = new JComboBox<String>(
			new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" });
	private final JComboBox<String> comboMonth = new JComboBox<String>(
			new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" });
	private final JComboBox<String> comboMonth2 = new JComboBox<String>(
			new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" });
	private final JComboBox<String> comboHour = new JComboBox<String>(
			new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
					"16", "17", "18", "19", "20", "21", "22", "23", "24" });
	private final JComboBox<String> comboHour2 = new JComboBox<String>(
			new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
					"16", "17", "18", "19", "20", "21", "22", "23", "24" });
	private JComboBox<String> comboMin, comboMin2, comboDate, comboDate2, comboYear, comboYear2;

	private JTabbedPane tabbedPane;
	private JPanel panel1, panel2, panel3;

	private JMenu file;
	private JMenuBar menuBar;

	public MainForm() {

		/////////
		super("Appointment System Assignment");
		setLayout(new BorderLayout());

		menuBar = new JMenuBar();
		add(menuBar, BorderLayout.BEFORE_FIRST_LINE);

		file = new JMenu("File");
		menuBar.add(file);

		JMenuItem open = new JMenuItem("Open");
		file.add(open);

		// File formats to accept
		FileFilter filter = new FileNameExtensionFilter("TXT, CSV", "txt", "text", "csv");

		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser c = new JFileChooser();
				c.setAcceptAllFileFilterUsed(false);
				// c.addChoosableFileFilter(filter);
				c.setFileFilter(filter);
				// Demonstrate "Open" dialog:
				int rVal = c.showOpenDialog(null);

				
				if (rVal == JFileChooser.APPROVE_OPTION) {
					System.out.println(c.getName() + "sfsfdfs");			
					if (c.getName().endsWith(".txt")) {
						appBook.openTextFile(c.getCurrentDirectory().toString() + "/" + c.getSelectedFile().getName());
					} else if (c.getName().endsWith(".csv")) {
						appBook.openCsvFile(c.getCurrentDirectory().toString() + "/" + c.getSelectedFile().getName());
					}

				}

			}
		});

		JMenu saveAs = new JMenu("Save As");
		file.add(saveAs);

		JMenuItem saveTxt = new JMenuItem(".txt");
		saveAs.add(saveTxt);
		saveTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser c = new JFileChooser();
				// Demonstrate "Save" dialog:
				int rVal = c.showSaveDialog(MainForm.this);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					appBook.saveToTextFile(
							c.getCurrentDirectory().toString() + "/" + c.getSelectedFile().getName() + ".txt");

				}
			}
		});

		JMenuItem saveCsv = new JMenuItem(".csv");
		saveAs.add(saveCsv);
		saveCsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser c = new JFileChooser();
				// Demonstrate "Save" dialog:
				int rVal = c.showSaveDialog(MainForm.this);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					appBook.saveToCsvFile(
							c.getCurrentDirectory().toString() + "/" + c.getSelectedFile().getName() + ".csv");

				}
			}
		});

		JMenuItem saveIcs = new JMenuItem(".ics");
		saveAs.add(saveIcs);
		saveIcs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser c = new JFileChooser();
				// Demonstrate "Save" dialog:
				int rVal = c.showSaveDialog(MainForm.this);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					appBook.saveToIcsFile(
							c.getCurrentDirectory().toString() + "/" + c.getSelectedFile().getName() + ".ics");

				}
			}
		});

		// add ability to exit from JMenu
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		file.add(exit);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		getContentPane().add(topPanel);

		createPage1();
		createPage2();
		createPage3();
		updateValues();

		// Display different parts of system in different tabs
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("View Single", panel1);
		tabbedPane.addTab("Add", panel2);
		tabbedPane.addTab("View All", panel3);
		topPanel.add(tabbedPane, BorderLayout.CENTER);

	}

	public void createPage1() {
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(0, 2));

		// The Data
		sD = new JLabel("Press Next To View Appointments");
		panel1.add(sD);
		eD = new JLabel("");
		panel1.add(eD);
		count = new JLabel("");
		panel1.add(count);

		String strX = String.valueOf(theNumMax);
		countMax = new JLabel("Out of " + strX);
		panel1.add(countMax);

		next = new JButton("Next"); // construct Button
		panel1.add(next);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				theNum = ++counter;

				if (theNum != 0) {
					String arrayVal1 = appBook.displayAppointment(theNum)[0];
					String arrayVal2 = appBook.displayAppointment(theNum)[1];
					String arrayVal3 = appBook.displayAppointment(theNum)[2];

					sD.setText("<html>Start Date: " + arrayVal1 + "<br>End Date: " + arrayVal2 + "<br>Event: "
							+ arrayVal3 + "</html>");
				} else {
					sD.setText("Press Next To View Appointments");

				}
				updateValues();

			}
		});

		previous = new JButton("Previous"); // construct Button
		panel1.add(previous);
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				theNum = --counter;

				if (theNum != 0) {
					String arrayVal1 = appBook.displayAppointment(theNum)[0];
					String arrayVal2 = appBook.displayAppointment(theNum)[1];
					String arrayVal3 = appBook.displayAppointment(theNum)[2];

					sD.setText("<html>Start Date: " + arrayVal1 + "<br>End Date: " + arrayVal2 + "<br>Event: "
							+ arrayVal3 + "</html>");
				} else {
					sD.setText("Press Next To View Appointments");
				}
				updateValues();

			}
		});

		delete = new JButton("Delete"); // construct Button
		panel1.add(delete);
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int response = JOptionPane.showConfirmDialog(MainForm.this, "Do you want to Delete appointment?",
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {

					String arrayValStart = appBook.displayAppointment(theNum)[0];
					String arrayValEnd = appBook.displayAppointment(theNum)[1];
					String arrayValEvent = appBook.displayAppointment(theNum)[2];
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

					// arrayValStart to gregorian
					Date date = null;
					try {
						date = df.parse(arrayValStart);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(date);

					// arrayValEnd to gregorian
					Date date2 = null;
					try {
						date2 = df.parse(arrayValEnd);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					GregorianCalendar cal2 = new GregorianCalendar();
					cal2.setTime(date2);

					// Delete Appointment
					appBook.remove(new Appointment(cal, cal2, arrayValEvent));

					// Update Current record after deleting
					if (theNum != 0) {
						// Minus one if deleting last record
						if (theNum == theNumMax) {
							theNum = --counter;
						}
						String arrayVal1 = appBook.displayAppointment(theNum)[0];
						String arrayVal2 = appBook.displayAppointment(theNum)[1];
						String arrayVal3 = appBook.displayAppointment(theNum)[2];

						sD.setText("<html>Start Date: " + arrayVal1 + "<br>End Date: " + arrayVal2 + "<br>Event: "
								+ arrayVal3 + "</html>");
					} else {
						sD.setText("Press Next To View Appointments");
					}

					updateValues();
				}
			}
		});

	}

	public void createPage2() {
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(0, 2));
		panel2.setSize(0, 1);

		// Populate the year and minute array
		for (int i = 0; i < yearArray.length; i++) {
			yearArray[i] = Integer.toString(2016 + i);
		}
		for (int i = 0; i < monthArray.length; i++) {
			monthArray[i] = Integer.toString(i);
		}
		for (int i = 0; i < thirtyOneArray.length; i++) {
			thirtyOneArray[i] = Integer.toString(i + 1);
		}
		for (int i = 0; i < thirtyArray.length; i++) {
			thirtyArray[i] = Integer.toString(i + 1);
		}
		for (int i = 0; i < twentyNineArray.length; i++) {
			twentyNineArray[i] = Integer.toString(i + 1);
		}
		for (int i = 0; i < twentyEightArray.length; i++) {
			twentyEightArray[i] = Integer.toString(i + 1);
		}
		for (int i = 0; i < hourArray.length; i++) {
			hourArray[i] = Integer.toString(i);
		}
		for (int i = 0; i < minArray.length; i++) {
			minArray[i] = Integer.toString(i);
		}

		// Declare Combo models
		JComboBox<String> comboYear = new JComboBox<String>();
		JComboBox<String> comboYear2 = new JComboBox<String>();
		DefaultComboBoxModel<String> theYears = new DefaultComboBoxModel<String>(yearArray);
		DefaultComboBoxModel<String> theYears2 = new DefaultComboBoxModel<String>(yearArray);
		comboYear.setModel(theYears);
		comboYear2.setModel(theYears2);

		DefaultComboBoxModel<String> thirtyOne = new DefaultComboBoxModel<String>(thirtyOneArray);
		DefaultComboBoxModel<String> thirtyOne2 = new DefaultComboBoxModel<String>(thirtyOneArray);
		DefaultComboBoxModel<String> thirty = new DefaultComboBoxModel<String>(thirtyArray);
		DefaultComboBoxModel<String> thirty2 = new DefaultComboBoxModel<String>(thirtyArray);
		DefaultComboBoxModel<String> twentyNine = new DefaultComboBoxModel<String>(twentyNineArray);
		DefaultComboBoxModel<String> twentyNine2 = new DefaultComboBoxModel<String>(twentyNineArray);
		DefaultComboBoxModel<String> twentyEight = new DefaultComboBoxModel<String>(twentyEightArray);
		DefaultComboBoxModel<String> twentyEight2 = new DefaultComboBoxModel<String>(twentyEightArray);

		DefaultComboBoxModel<String> theMin = new DefaultComboBoxModel<String>(minArray);
		DefaultComboBoxModel<String> theMin2 = new DefaultComboBoxModel<String>(minArray);

		JComboBox<String> comboDate = new JComboBox<String>();
		comboDate.setModel(thirtyOne);

		startDate = new JLabel("Start Date"); // construct Label
		panel2.add(startDate); // "super" Frame adds Label
		
		// Gap to seperate start of start date input
		JLabel gapStart = new JLabel("");
		panel2.add(gapStart);

		aYear = new JLabel("Year", SwingConstants.RIGHT); // construct Label
		panel2.add(aYear);
		panel2.add(comboYear);

		startMonth = new JLabel("Month", SwingConstants.RIGHT); // construct
																// Label
		panel2.add(startMonth);
		panel2.add(comboMonth);

		startDate = new JLabel("Date", SwingConstants.RIGHT); // construct Label
		panel2.add(startDate);
		panel2.add(comboDate);

		startHour = new JLabel("Hour", SwingConstants.RIGHT); // construct Label
		panel2.add(startHour);
		panel2.add(comboHour);

		JComboBox<String> comboMin = new JComboBox<String>();
		comboMin.setModel(theMin);
		startMin = new JLabel("Minute", SwingConstants.RIGHT); // construct
																// Label
		panel2.add(startMin);
		panel2.add(comboMin);

		comboMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if ("01".equals(comboMonth.getSelectedItem()) || "03".equals(comboMonth.getSelectedItem())
						|| "05".equals(comboMonth.getSelectedItem()) || "07".equals(comboMonth.getSelectedItem())
						|| "08".equals(comboMonth.getSelectedItem()) || "10".equals(comboMonth.getSelectedItem())
						|| "12".equals(comboMonth.getSelectedItem())) {
					comboDate.setModel(thirtyOne);
				} else if ("04".equals(comboMonth.getSelectedItem()) || "06".equals(comboMonth.getSelectedItem())
						|| "09".equals(comboMonth.getSelectedItem()) || "11".equals(comboMonth.getSelectedItem())) {
					comboDate.setModel(thirty);
				} else if ("02".equals(comboMonth.getSelectedItem())) {
					if ((Integer.parseInt((String) comboYear.getSelectedItem()) % 4 == 0)
							&& (Integer.parseInt((String) comboYear.getSelectedItem()) % 100 != 0)
							|| Integer.parseInt((String) comboYear.getSelectedItem()) % 400 == 0) {
						comboDate.setModel(twentyNine);
					} else {
						comboDate.setModel(twentyEight);
					}
				}

			}
		});

		comboYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// COMBOYEAR ACTION LISTER
				// If leap year then make February have 29 days else 28 days
				if ("02".equals(comboMonth.getSelectedItem())) {
					if ((Integer.parseInt((String) comboYear.getSelectedItem()) % 4 == 0)
							&& (Integer.parseInt((String) comboYear.getSelectedItem()) % 100 != 0)
							|| Integer.parseInt((String) comboYear.getSelectedItem()) % 400 == 0) {
						comboDate.setModel(twentyNine);
					} else {
						comboDate.setModel(twentyEight);
					}
				}

			}
		});

		// THE END DATE

		JComboBox<String> comboDate2 = new JComboBox<String>();
		comboDate2.setModel(thirtyOne2);

		endDate = new JLabel("End Date");
		panel2.add(endDate); // "super" Frame adds Label
		
		// Gap to seperate start of start date input
		JLabel gapEnd = new JLabel("");
		panel2.add(gapEnd);

		endYear = new JLabel("Year", SwingConstants.RIGHT); // construct Label
		panel2.add(endYear);
		panel2.add(comboYear2);

		endMonth = new JLabel("Month", SwingConstants.RIGHT); // construct Label
		panel2.add(endMonth);
		panel2.add(comboMonth2);

		endDate = new JLabel("Date", SwingConstants.RIGHT); // construct Label
		panel2.add(endDate);
		panel2.add(comboDate2);

		endHour = new JLabel("Hour", SwingConstants.RIGHT); // construct Label
		panel2.add(endHour);
		panel2.add(comboHour2);

		JComboBox<String> comboMin2 = new JComboBox<String>();

		comboMin2.setModel(theMin2);
		endMin = new JLabel("Minute", SwingConstants.RIGHT); // construct Label
		panel2.add(endMin);
		panel2.add(comboMin2);

		comboMonth2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if ("01".equals(comboMonth2.getSelectedItem()) || "03".equals(comboMonth2.getSelectedItem())
						|| "05".equals(comboMonth2.getSelectedItem()) || "07".equals(comboMonth2.getSelectedItem())
						|| "08".equals(comboMonth2.getSelectedItem()) || "10".equals(comboMonth2.getSelectedItem())
						|| "12".equals(comboMonth2.getSelectedItem())) {
					comboDate2.setModel(thirtyOne2);
				} else if ("04".equals(comboMonth2.getSelectedItem()) || "06".equals(comboMonth2.getSelectedItem())
						|| "09".equals(comboMonth2.getSelectedItem()) || "11".equals(comboMonth2.getSelectedItem())) {
					comboDate2.setModel(thirty2);
				} else if ("02".equals(comboMonth2.getSelectedItem())) {
					if ((Integer.parseInt((String) comboYear2.getSelectedItem()) % 4 == 0)
							&& (Integer.parseInt((String) comboYear2.getSelectedItem()) % 100 != 0)
							|| Integer.parseInt((String) comboYear2.getSelectedItem()) % 400 == 0) {
						comboDate2.setModel(twentyNine2);
					} else {
						comboDate2.setModel(twentyEight2);
					}
				}

			}
		});

		comboYear2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// COMBOYEAR ACTION LISTER
				// If leap year then make February have 29 days else 28 days
				if ("02".equals(comboMonth2.getSelectedItem())) {
					if ((Integer.parseInt((String) comboYear2.getSelectedItem()) % 4 == 0)
							&& (Integer.parseInt((String) comboYear2.getSelectedItem()) % 100 != 0)
							|| Integer.parseInt((String) comboYear2.getSelectedItem()) % 400 == 0) {
						comboDate2.setModel(twentyNine2);
					} else {
						comboDate2.setModel(twentyEight2);
					}
				}

			}
		});

		// Add Event Field and save button

		event = new JLabel("Event"); // construct Label
		panel2.add(event); // "super" Frame adds Label

		eventText = new JTextField("", 10); // construct TextField
		panel2.add(eventText); // "super" Frame adds tfCount

		save = new JButton("Save"); // construct Button
		panel2.add(save);

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int year = Integer.parseInt((String) comboYear.getSelectedItem());
				int month = Integer.parseInt((String) comboMonth.getSelectedItem());
				int date = Integer.parseInt((String) comboDate.getSelectedItem());
				int hour = Integer.parseInt((String) comboHour.getSelectedItem());
				int min = Integer.parseInt((String) comboMin.getSelectedItem());

				int year2 = Integer.parseInt((String) comboYear2.getSelectedItem());
				int month2 = Integer.parseInt((String) comboMonth2.getSelectedItem());
				int date2 = Integer.parseInt((String) comboDate2.getSelectedItem());
				int hour2 = Integer.parseInt((String) comboHour2.getSelectedItem());
				int min2 = Integer.parseInt((String) comboMin2.getSelectedItem());

				String event = eventText.getText();

				// array index 0 = start end check, 1 = conflict

				if (appBook.checkValues((new GregorianCalendar(year, month - 1, date, hour, min)),
						(new GregorianCalendar(year2, month2 - 1, date2, hour2, min2)))[0] == true) {

					// If appointment conflicts
					if (appBook.checkValues((new GregorianCalendar(year, month - 1, date, hour, min)),
							(new GregorianCalendar(year2, month2 - 1, date2, hour2, min2)))[1] == true) {
						int response = JOptionPane.showConfirmDialog(MainForm.this,
								"ERROR: Appointment conflict. Do you still want to add appointment?", "Error",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (response == JOptionPane.YES_OPTION) {
							Appointment a1 = new Appointment(new GregorianCalendar(year, month - 1, date, hour, min),
									new GregorianCalendar(year2, month2 - 1, date2, hour2, min2), event);
							appBook.add(a1);
							JOptionPane.showMessageDialog(MainForm.this, "Successfully added!!");
						}
					} else {
						Appointment a1 = new Appointment(new GregorianCalendar(year, month - 1, date, hour, min),
								new GregorianCalendar(year2, month2 - 1, date2, hour2, min2), event);
						appBook.add(a1);
						JOptionPane.showMessageDialog(MainForm.this, "Successfully added!!");
					}

				} else {
					JOptionPane.showMessageDialog(MainForm.this, "ERROR: End date less than start date!!");
				}

				updateValues();

			}
		});

	}

	public void createPage3() {
		panel3 = new JPanel();
		panel3.setLayout(new FlowLayout());

		// Show message if no appointments!
		// if (allApps == null) {
		eventSearch = new JTextField(20);
		panel3.add(eventSearch);

		search = new JButton("Search");
		panel3.add(search);

		viewAllApps = new JButton("All Appointments");
		panel3.add(viewAllApps);

		// Month Search
		// Month ComboBox populated with months
		panel3.add(comboMonthSearch);

		for (int i = 0; i < yearArray.length; i++) {
			yearArray[i] = Integer.toString(2016 + i);
		}
		JComboBox yearSearchCombo = new JComboBox(yearArray);
		panel3.add(yearSearchCombo);

		viewMonthlyApps = new JButton("Month Search");
		panel3.add(viewMonthlyApps);

		allApps = new JList();
		panel3.add(allApps);
		viewAllApps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DefaultListModel<String> model = new DefaultListModel<String>();
				for (String s : appBook.displayAllAppointments()) {
					model.addElement(s);
				}
				allApps.setModel(model);
				panel3.add(allApps);
			}
		});

		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DefaultListModel<String> model = new DefaultListModel<String>();
				for (String s : appBook.search(eventSearch.getText())) {
					model.addElement(s);
				}

				allApps.setModel(model);
				panel3.add(allApps);
			}
		});

		viewMonthlyApps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int month = Integer.parseInt((String) comboMonthSearch.getSelectedItem());
				int year = Integer.parseInt((String) yearSearchCombo.getSelectedItem());

				DefaultListModel<String> model = new DefaultListModel<String>();
				for (String s : appBook.monthSearch(month, year)) {
					model.addElement(s);
				}

				allApps.setModel(model);
				panel3.add(allApps);

			}
		});

	}

	public void updateValues() {

		String theNumStr = Integer.toString(theNum);
		count.setText("Record Number: " + theNumStr);

		// Get total number of records
		theNumMax = appBook.numOfValues();

		String strX = String.valueOf(theNumMax);
		countMax.setText("Out of " + strX);

		// disable previous button
		if (theNum == 0) {
			previous.setEnabled(false);
		} else {
			previous.setEnabled(true);
		}

		if ((theNum) == theNumMax) {
			next.setEnabled(false);
		} else {
			next.setEnabled(true);
		}

	}

}
