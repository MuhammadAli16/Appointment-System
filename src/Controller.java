import java.util.GregorianCalendar;

public class Controller {
	/**
	 * @author Muhammad
	 * Used to test the system
	 */

	public static void main(String[] args) {

		// 8+1 = 9 so month is October or 10th month as Jan is 0
		Appointment a1 = new Appointment(new GregorianCalendar(2016, 8 + 1, 14, 10, 30),
				new GregorianCalendar(2016, 10, 14, 11, 30), "Dentist");
		Appointment a1Duplicate = new Appointment(new GregorianCalendar(2016, 8 + 1, 14, 10, 30),
				new GregorianCalendar(2016, 10, 14, 11, 30), "Dentist");
		Appointment a2 = new Appointment(new GregorianCalendar(2016, 8 + 1, 20, 9, 00),
				new GregorianCalendar(2016, 10, 20, 10, 10), "OOWP Lecture");
		Appointment a3 = new Appointment(new GregorianCalendar(2016, 8 + 1, 21, 14, 00),
				new GregorianCalendar(2016, 10, 21, 16, 00), "Tutorial");
		AppointmentBook appBook = new AppointmentBook();

		appBook.add(a1);
		appBook.add(a2);
		appBook.add(a3);
		appBook.displayAllAppointments().toString();
		appBook.remove(a1);

		// Display All Appointments
		System.out.println("All Appointments");
		for (int x = 0; x < appBook.displayAllAppointments().length; x++) {
			System.out.println(appBook.displayAllAppointments()[x]);
		}
		
		// Show total number of appointments
		System.out.println("Total Number of Appointments: " + appBook.numOfValues());
		// Check if appointments exist
		System.out.println("Is a2 in book: " + appBook.isInBook(a2));
		System.out.println("Is a1 in book: " + appBook.isInBook(a1));
		
		// Check if a1Duplicate is correct format and that if it conflicts with other records
		System.out.println("Start Date Less Than End Date: " + appBook.checkValues(a1Duplicate.getStartDateTime(),
				a1Duplicate.getEndDateTime())[0]);
		System.out.println("Conflicts: " + appBook.checkValues(a1Duplicate.getStartDateTime(),
				a1Duplicate.getEndDateTime())[1]);
		
		// Check if search returns correct result
		System.out.println("Results for searching Tutorial");
		for (int x = 0; x < appBook.search("Tutorial").length; x++) {
			System.out.println(appBook.search("Tutorial")[x]);
		}
		
		// Check if month search returns correct result
//		System.out.println("Results for searching appointments in January 2016");
//		for (int x = 0; x < appBook.monthSearch(1,2016).length; x++) {
//			System.out.println(appBook.monthSearch(1,2016)[x]);
//		}
  		
		
		// Remove test data
		appBook.remove(a2);
		appBook.remove(a3);


	}

}