import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AppointmentBook {
	/**
	 * @author Muhammad Ali 13156253
	 */

	private Connection myConn;

	public AppointmentBook() {

//		// Declare variables needed for connection
//		String user = "alimu";
//		String password = "prilstiK2";
//		String dburl = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk/alimu";
//
//		// connect to database
//		try {
//			myConn = DriverManager.getConnection(dburl, user, password);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		// create a database connection
		try{
		myConn = DriverManager.getConnection("jdbc:sqlite:app.db");
        Statement statement = myConn.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        statement.executeUpdate("drop table if exists AP_AppointmentBook");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS  AP_AppointmentBook (StartDate DateTime,EndDate DateTime ,Event String)");
		}catch(SQLException e)
        {
	          // if the error message is "out of memory",
	          // it probably means no database file is found
	          System.err.println(e.getMessage());
	        }

	}

	public void add(Appointment addAppt) {

		// The format to make date into
		//SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		// Start Date conversion for storing in mySQL
		java.sql.Timestamp sqlStartDate = null;
		Calendar cal = addAppt.getStartDateTime();
		// #---- This uses the provided calendar for the output -----
		dateFormat.setCalendar(cal);
		String startDate = dateFormat.format(cal.getTime());
		java.util.Date date;

		// End Date conversion for storing in mySQL
		java.sql.Timestamp sqlEndDate = null;
		Calendar cal2 = addAppt.getEndDateTime();
		// #---- This uses the provided calendar for the output -----
		dateFormat.setCalendar(cal2);
		String endDate = dateFormat.format(cal2.getTime());
		java.util.Date date2;

		try {
			// Start Date
			date = dateFormat.parse(startDate);
			sqlStartDate = new java.sql.Timestamp(date.getTime());
			// End Date
			date2 = dateFormat.parse(endDate);
			sqlEndDate = new java.sql.Timestamp(date2.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String query = "Insert into AP_AppointmentBook (StartDate,EndDate,Event)"
				+ " values (?, ?, ?)";

		// create the mySQL insert preparedstatement
		PreparedStatement preparedStmt = null;
		try {
			preparedStmt = myConn.prepareStatement(query);
			preparedStmt.setTimestamp(1, sqlStartDate);
			preparedStmt.setTimestamp(2, sqlEndDate);
			preparedStmt.setString(3, addAppt.getEventTitle());
			preparedStmt.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	public String[] displayAllAppointments() {

		java.sql.Timestamp startDate = null;
		java.sql.Timestamp endDate = null;
		String theEvent = null;
		List<String> tempList = new ArrayList<String>();
		String[] returnArray = null;

		try {
			Statement stmt = myConn.createStatement();
			ResultSet rs;

			rs = stmt
					.executeQuery("SELECT * FROM AP_AppointmentBook ORDER BY StartDate ASC;");

			while (rs.next()) {
				startDate = rs.getTimestamp("startDate");
				endDate = rs.getTimestamp("endDate");
				theEvent = rs.getString("event");

				String x = "Start Date: " + startDate + " End Date: " + endDate
						+ " Event " + theEvent;
				tempList.add(x);
			}

			returnArray = new String[tempList.size()];
			returnArray = tempList.toArray(returnArray);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		// Return result
		return returnArray;

	}

	public String[] displayAppointment(int x) {

		java.sql.Timestamp startDate = null;
		java.sql.Timestamp endDate = null;
		String event = null;
		String[] returnArray = new String[3];

		try {
			Statement stmt = myConn.createStatement();
			ResultSet rs;

			// Create and execute mySQL statement. -1 from x as offset starts
			// from 0. Record 1 = offset 0
			rs = stmt
					.executeQuery("SELECT StartDate, EndDate, Event FROM AP_AppointmentBook ORDER BY StartDate ASC limit 1 offset "
							+ (x - 1));

			while (rs.next()) {
				startDate = rs.getTimestamp("startDate");
				endDate = rs.getTimestamp("endDate");
				event = rs.getString("event");

			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String startStr  = dateFormat.format(startDate);
			String endStr  = dateFormat.format(endDate);
				returnArray[0] = startStr;
				returnArray[1] = endStr;
				returnArray[2] = event;
			//}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// Return result
		return returnArray;

	}

	public void remove(Appointment removeAppt) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		// Start Date
		java.sql.Timestamp sqlStartDate = null;
		Calendar cal = removeAppt.getStartDateTime();
		// #---- This uses the provided calendar for the output -----
		dateFormat.setCalendar(cal);
		String startDate = dateFormat.format(cal.getTime());
		java.util.Date date;

		try {

			date = dateFormat.parse(startDate);
			sqlStartDate = new java.sql.Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// End Date
		java.sql.Timestamp sqlEndDate = null;
		Calendar cal2 = removeAppt.getEndDateTime();
		// #---- This uses the provided calendar for the output -----
		dateFormat.setCalendar(cal2);
		String endDate = dateFormat.format(cal2.getTime());
		java.util.Date date2;

		try {
			date2 = dateFormat.parse(endDate);
			sqlEndDate = new java.sql.Timestamp(date2.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String event = removeAppt.getEventTitle();

		// Create query from values initialized above
		String query = "Delete from AP_AppointmentBook WHERE StartDate = ? And EndDate = ? And Event = ?;";

		// create the mySQL insert preparedstatement
		PreparedStatement preparedStmt = null;

		try {
			preparedStmt = myConn.prepareStatement(query);
			preparedStmt.setTimestamp(1, sqlStartDate);
			preparedStmt.setTimestamp(2, sqlEndDate);
			preparedStmt.setString(3, event);
			preparedStmt.execute();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public boolean[] checkValues(GregorianCalendar checkStart,
			GregorianCalendar checkEnd) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		// Start Date
		java.sql.Timestamp sqlStartDate = null;
		Calendar cal = checkStart;
		// #---- This uses the provided calendar for the output -----
		dateFormat.setCalendar(cal);
		String startDate = dateFormat.format(cal.getTime());
		java.util.Date date;

		// End Date
		java.sql.Timestamp sqlEndDate = null;
		Calendar cal2 = checkEnd;
		// #---- This uses the provided calendar for the output -----
		dateFormat.setCalendar(cal2);
		String endDate = dateFormat.format(cal2.getTime());
		java.util.Date date2;

		try {
			// Start Date
			date = dateFormat.parse(startDate);
			sqlStartDate = new java.sql.Timestamp(date.getTime());
			// End Date
			date2 = dateFormat.parse(endDate);
			sqlEndDate = new java.sql.Timestamp(date2.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Run SQL Statement and initialize boolean
		boolean exist = false;
		try {
			Statement stmt = myConn.createStatement();
			ResultSet rs;

			rs = stmt.executeQuery("Select * from Ap_appointmentbook WHERE ( '"
					+ sqlStartDate + "' <= EndDate AND '" + sqlEndDate
					+ "' >= StartDate)");
			exist = rs.next();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		boolean startEndVal = true;
		if (checkEnd.compareTo(checkStart) >= 0) {
			startEndVal = true;
		} else {
			startEndVal = false;

		}
		// return boolean array
		boolean temp[] = { startEndVal, exist };
		return temp;

	}

	public String[] search(String event) {

		// Initialize variables
		String startDate = null;
		String endDate = null;
		String theEvent = null;
		List<String> tempList = new ArrayList<String>();
		String[] returnArray = null;

		// Create statement and get values and store in arraylist
		try {
			Statement stmt = myConn.createStatement();
			ResultSet rs;

			rs = stmt
					.executeQuery("SELECT * FROM AP_AppointmentBook WHERE event LIKE '%"
							+ event + "%' ORDER BY StartDate ASC");

			while (rs.next()) {
				startDate = rs.getString("startDate");
				endDate = rs.getString("endDate");
				theEvent = rs.getString("event");

				String x = "Start Date: " + startDate + " End Date: " + endDate
						+ " Event " + theEvent;
				tempList.add(x);
			}
			// Arraylist content stored copied into array
			returnArray = new String[tempList.size()];
			returnArray = tempList.toArray(returnArray);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		// Return array
		return returnArray;

	}

	public String[] monthSearch(int month, int year) {

		String startDateStr = null;
		java.sql.Timestamp startDate = null;
		java.sql.Timestamp endDate = null;
		String theEvent = null;
		List<String> tempList = new ArrayList<String>();
		String[] returnArray = null;
		// Create statement and get values and store in arraylist
		try {
			Statement stmt = myConn.createStatement();
			ResultSet rs;

//			rs = stmt
//					.executeQuery("SELECT * FROM AP_AppointmentBook WHERE strftime('%m', StartDate) "
//							+ "IN('"+ 0+Integer.toString(month) + "')"
////							+ " AND strftime('%Y', startDate) = "
////							+ year
//							+ " ORDER BY StartDate ASC;");
			
			rs = stmt
					.executeQuery("SELECT startDate,EndDate, event FROM AP_AppointmentBook "
							
							+ " ORDER BY StartDate ASC;");


			
			
			while (rs.next()) {
				startDateStr = rs.getString("startDate");
				startDate = rs.getTimestamp("startDate");
				endDate = rs.getTimestamp("endDate");
				theEvent = rs.getString("event");

				// Get month and year (Add 1 to month as 0 = January)
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.parseLong(startDateStr));
				int yearSql = cal.get(Calendar.YEAR);
				int monthSql = cal.get(Calendar.MONTH) + 1;
				
				if (month == monthSql && year == yearSql){
					String x = "Start Date: " + startDate + " End Date: " + endDate
							+ " Event " + theEvent;
					tempList.add(x);
				}
				
			}

			returnArray = new String[tempList.size()];
			returnArray = tempList.toArray(returnArray);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		// Return result
		return returnArray;

	}

	public int numOfValues() {
		// Method returns the total number of records stored in database
		int counter = 0;
		try {
			Statement stmt = myConn.createStatement();
			ResultSet rs;

			rs = stmt.executeQuery("SELECT * FROM ap_appointmentbook;");
			// Increment counter for each row found in database
			while (rs.next()) {
				counter++;
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return counter;
	}

	public boolean isInBook(Appointment appt) {

		// The format to make date into
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		// Start Date
		java.sql.Timestamp sqlStartDate = null;
		Calendar cal = appt.getStartDateTime();
		// #---- This uses the provided calendar for the output -----
		dateFormat.setCalendar(cal);
		String startDate = dateFormat.format(cal.getTime());
		java.util.Date date;

		// End Date
		java.sql.Timestamp sqlEndDate = null;
		Calendar cal2 = appt.getEndDateTime();
		// #---- This uses the provided calendar for the output -----
		dateFormat.setCalendar(cal2);
		String endDate = dateFormat.format(cal2.getTime());
		java.util.Date date2;

		try {
			// Start Date
			date = dateFormat.parse(startDate);
			sqlStartDate = new java.sql.Timestamp(date.getTime());
			// End Date
			date2 = dateFormat.parse(endDate);
			sqlEndDate = new java.sql.Timestamp(date2.getTime());
			//
			Statement stmt = myConn.createStatement();
			ResultSet rs;
			rs = stmt
					.executeQuery("SELECT * FROM AP_AppointmentBook WHERE startDate = '"
							+ sqlStartDate
							+ "' AND EndDate = '"
							+ sqlEndDate
							+ "' And Event = '" + appt.getEventTitle() + "' ;");

			// While there are rows return true
			while (rs.next()) {
				return true;
			}

		} catch (ParseException | SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public void saveToTextFile(String location) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(location));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		java.sql.Timestamp startDate = null;
		java.sql.Timestamp endDate = null;
		String event = null;
		try {
			Statement stmt = myConn.createStatement();
			ResultSet rs;

			rs = stmt
					.executeQuery("SELECT StartDate, EndDate, Event FROM AP_AppointmentBook ORDER BY StartDate ASC;");
			while (rs.next()) {
				startDate = rs.getTimestamp("startDate");
				endDate = rs.getTimestamp("endDate");
				event = rs.getString("event");
				pw.println(startDate);
				pw.println(endDate);
				pw.println(event);
			}

		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
		pw.close();
	}

	public void openTextFile(String location) {

		try {

			BufferedReader in = new BufferedReader(new FileReader(location));
			String str;

			// List<String> list2 = new ArrayList<String>();
			int counter = 0;
			GregorianCalendar startDate = null;
			GregorianCalendar endDate = null;
			String event = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			while ((str = in.readLine()) != null) {
				counter++;

				if (counter == 1) {
					java.util.Date date = df.parse(str);
					startDate = new GregorianCalendar();
					startDate.setTime(date);
				} else if (counter == 2) {
					java.util.Date date = df.parse(str);
					endDate = new GregorianCalendar();
					endDate.setTime(date);
				} else if (counter == 3) {
					event = str;
					// Add the appointment and reset counter
					add(new Appointment(startDate, endDate, event));
					counter = 0;
				}

			}
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void saveToCsvFile(String location) {
		PrintWriter pw = null;
		java.sql.Timestamp startDate = null;
		java.sql.Timestamp endDate = null;
		String event = null;
		String COMMA_DELIMITER = ",";
		String NEW_LINE = "\n";
		try {
			pw = new PrintWriter(new FileOutputStream(location));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Statement stmt = myConn.createStatement();
			ResultSet rs;

			rs = stmt
					.executeQuery("SELECT StartDate, EndDate, Event FROM AP_AppointmentBook ORDER BY StartDate ASC;");
			pw.append("Start Date");
			pw.append(COMMA_DELIMITER);
			pw.append("End Date");
			pw.append(COMMA_DELIMITER);
			pw.append("Event");
			pw.append(NEW_LINE);
			while (rs.next()) {
				startDate = rs.getTimestamp("startDate");
				endDate = rs.getTimestamp("endDate");
				event = rs.getString("event");
				
				pw.append(startDate.toString());
				pw.append(COMMA_DELIMITER);
				pw.append(endDate.toString());
				pw.append(COMMA_DELIMITER);
				pw.append(event);
				pw.append(NEW_LINE);
			}

		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
		pw.close();

	}

	public void openCsvFile(String fileName) {
		final String COMMA_DELIMITER = ",";
		BufferedReader fileReader = null;

		try {

			// Create a new list of student to be filled by CSV file data

			String line = "";

			// Create the file reader
			fileReader = new BufferedReader(new FileReader(fileName));

			// Read the CSV file header to skip it
			fileReader.readLine();

			String startDateToken = null;
			String endDateToken = null;
			String eventToken = null;

			GregorianCalendar startDate;
			GregorianCalendar endDate;
			// Read the file line by line starting from the second line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(COMMA_DELIMITER);
				if (tokens.length > 0) {
					// Create a new student object and fill his data
					startDateToken = tokens[0];
					endDateToken = tokens[1];
					eventToken = tokens[2];

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					java.util.Date date = df.parse(startDateToken);
					startDate = new GregorianCalendar();
					startDate.setTime(date);

					java.util.Date date1 = df.parse(endDateToken);
					endDate = new GregorianCalendar();
					endDate.setTime(date1);

					add(new Appointment(startDate, endDate, eventToken));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void saveToIcsFile(String location) {

		String calBegin = "BEGIN:VCALENDAR\r\n";
		String prodid = "PRODID://Muhammad/Assignment//\r\n";
		String version = "VERSION:2.0\r\n";

		String eventBegin = "BEGIN:VEVENT\r\n";

		String eventEnd = "END:VEVENT\r\n";

		String calEnd = "END:VCALENDAR\r\n";

		// iterate through the java resultset

		StringBuilder builder = new StringBuilder();
		builder.append(location);
		try {

			// mySQL SELECT query.

			String query = "SELECT * FROM Ap_appointmentbook";

			Statement st = myConn.createStatement();

			// execute the query, and get a java resultset
			ResultSet rs = st.executeQuery(query);

			File file = new File(builder.toString());

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(calBegin);
			bw.write(prodid);
			bw.write(version);

			// Create counter for UID
			int counter = 0;
			while (rs.next()) {

				counter++;

				bw.write(eventBegin);

				Timestamp startDate = rs.getTimestamp("startDate");
				Timestamp endDate = rs.getTimestamp("endDate");
				String event = rs.getString("event");

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyyMMdd'T'HHmmss'Z'");
				String startDateiCal = simpleDateFormat.format(startDate);
				String endDateiCal = simpleDateFormat.format(endDate);

				// Initialize Variables and add data
				String UID = "UID:uid" + counter + "@example.com\r\n";
				String summary = "SUMMARY:" + event + "\r\n";
				String DTStamp = "DTSTAMP:20160210T170000Z\r\n";
				String DTStart = "DTSTART:" + startDateiCal + "\r\n";
				String DTEnd = "DTEND:" + endDateiCal + "\r\n";

				bw.write(UID);
				bw.write(summary);
				bw.write(DTStamp);
				bw.write(DTStart);
				bw.write(DTEnd);
				bw.write(eventEnd);
			}

			bw.write(calEnd);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
