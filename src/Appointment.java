import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Appointment {
	/**
	 * @author Muhammad Ali 13156253
	 */
	private GregorianCalendar startDateTime;
	private GregorianCalendar endDateTime;
	private String eventTitle;

	public Appointment() {

	}

	public Appointment(GregorianCalendar startDateTime, GregorianCalendar endDateTime, String eventTitle) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.eventTitle = eventTitle;
	}

	public GregorianCalendar getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(GregorianCalendar startDateTime) {

		this.startDateTime = startDateTime;
	}

	public GregorianCalendar getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(GregorianCalendar endDateTime) {
		this.endDateTime = endDateTime;

	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String toString() {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		Calendar cal = startDateTime;
		// #---- This uses the provided calendar for the output -----
		dateFormat.setCalendar(cal);
		String startDate = dateFormat.format(cal.getTime());

		Calendar cal2 = endDateTime;
		// #---- This uses the provided calendar for the output -----
		dateFormat.setCalendar(cal2);
		String endDate = dateFormat.format(cal.getTime());

		return "Start Date: " + startDate + "\tEnd Date: " + endDate + "\tEvent: " + getEventTitle();

	}

}
