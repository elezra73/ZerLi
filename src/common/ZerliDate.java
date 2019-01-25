package common;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * ZerliDate class purpose is to give unified Date services to the project.
 * 
 * @author Roma
 * @author Avner
 * 
 */
public class ZerliDate implements Serializable {


	private static final long serialVersionUID = -4521297920348472662L;
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String TIME_FORMAT = "HH:mm";
	public static final String FULL_DATE_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;		// as used by SQL DB as well.
	
	private static final DateFormat mDateFormat = new SimpleDateFormat(DATE_FORMAT);
	private static final DateFormat mTimeFormat = new SimpleDateFormat(TIME_FORMAT);
	private static final DateFormat mFullDateFormat = new SimpleDateFormat(FULL_DATE_FORMAT);
	
	private String mFullDate;

	/**
	 * Create ZerliDate instance with current date and time.
	 */
	public ZerliDate() {
		Date current = new Date();
		mFullDate = mFullDateFormat.format(current);
	}

	/**
	 * get current qurater (1 to 4)
	 * @return current Quarter
	 * @throws ParseException ParseException
	 */
	public int getQuarter() throws ParseException {
		Calendar cal = Calendar.getInstance();
		Date date = mDateFormat.parse(getDate());
	    cal.setTime(date);
	    return (cal.get(Calendar.MONTH) / 3) + 1;
	}
	
	/**
	 * Copy constructor
	 * @param fullDate full date string
	 * @throws ParseException  ParseException
	 */
	public ZerliDate(String fullDate) throws ParseException {
		setFullDate(fullDate);
	}
	
	/**
	 * Copy constructor
	 * @param other other zerli date
	 */
	public ZerliDate(ZerliDate other) {
		this.mFullDate = other.getFullDate();	// Is already safe. No need to setFullDate().
	}

	/**
	 * @return the full date in the format "dd/MM/yyyy HH:mm"
	 */
	public String getFullDate() {
		return mFullDate;
	}

	/**
	 * @return the date in the format: "dd/MM/yyyy"
	 */
	public String getDate() {
		return mFullDate.substring(0, DATE_FORMAT.length());
	}
	
	/**
	 * @return the time in the format "HH:mm"
	 */
	public String getTime()
	{
		return mFullDate.substring(DATE_FORMAT.length()+1, mFullDate.length());
	}
	

	/**
	 * Append full Date.
	 * @param fullDate get fullDate
	 * @throws ParseException ParseException
	 */
	private void setFullDate(String fullDate) throws ParseException {
		if (!isValidFullDateString(fullDate)) 
			throw new ParseException("Invalid full date format: "+fullDate, 0);
		mFullDate = fullDate;
	}


	
	/**
	 * checks whether fullDate String is valid FULL_DATE_FORMAT.
	 * @param fullDate get fullDate
	 * @return if full date is valid 
	 */
	public boolean isValidFullDateString(String fullDate) {
		try {
			mFullDateFormat.parse(fullDate);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * checks whether date String is valid DATE_FORMAT.
	 * @param date get date
	 * @return if date is valid
	 */
	public boolean isValidDateString(String date) {
		try {
			mDateFormat.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * checks whether time String is valid TIME_FORMAT.
	 * @param time get time
	 * @return is time is valid
	 */
	public boolean isValidTimeString(String time) {
		try {
			mTimeFormat.parse(time);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * 
	 * @return the day of the date in String format
	 */
	public String getDayInString()
	{
		return this.getDate().substring(0, 2);
	}
	public String getMonthInString()
	{
		return this.getDate().substring(3, 5);
	}
	public String getYearInString()
	{
		return this.getDate().substring(6, 10);
	}
	public int getDayInInteger()
	{
		return Integer.parseInt(this.getDayInString());
	}
	public int getMonthInInteger()
	{
		return Integer.parseInt(this.getMonthInString());
	}
	public int getYearInInteger()
	{
		return Integer.parseInt(this.getYearInString());
	}
	/**
	 * 
	 * @return only the hour in String instance
	 */
	public String  getHourInString()
	{
		return this.getTime().substring(0, 2);
	}
	/**
	 * 
	 * @return only the minutes in String Format
	 */
	public String getMinutesInString()
	{
		return this.getTime().substring(3, 5);
	}
	/**
	 * 
	 * @return only the hour in Integer instance
	 */
	public int getHourInInTeger()
	{
		return Integer.parseInt(this.getHourInString());
	}
	/**
	 * 
	 * @return only the hour in Integer instance
	 */
	public int getMinuteInInteger()
	{
		return Integer.parseInt(this.getMinutesInString());
	}
	

	/**
	 * Compare by time
	 * @param other get date
	 * @return negative if this other time, 0 if equals, positive if this other time.
	 * @throws ParseException ParseException
	 */
	public int compareTime(ZerliDate other) throws ParseException {
		return comperator(this.getTime(), other.getTime(), mTimeFormat);
	}
	
	/**
	 * negative if this date &lt; other date, 0 if equals, positive if this date &gt; other date.
	 * @param other other date
	 * @return negative if this date &lt; other date, 0 if equals, positive if this date &gt; other date.
	 * @throws ParseException ParseException
	 */
	public int compareDateWithoutTime(ZerliDate other) throws ParseException {
		return comperator(this.getDate(), other.getDate(), mDateFormat);
	}
	
	/**
	 * negative if this full date &lt; other full date, 0 if equals, positive if this full date &gt; other full date.
	 * @param other other date
	 * @return negative if this full date &lt; other full date, 0 if equals, positive if this full date &gt; other full date.
	 * @throws ParseException ParseException
	 */
	public int compareTo(ZerliDate other) throws ParseException {
		return comperator(this.getFullDate(), other.getFullDate(), mFullDateFormat);
	}
	
	/**
	 * A comperator to compare dates by specific date format
	 * @param first date string
	 * @param second date string
	 * @param format date format
	 * @return A comperator to compare dates by specific date format
	 * @throws ParseException ParseException
	 */
	private int comperator(String first, String second, DateFormat format) throws ParseException {
		DateFormat dateFormat = format;
		Date firstDate = dateFormat.parse(first);
		Date secondDate = dateFormat.parse(second);
		return firstDate.compareTo(secondDate);
	}
	
	
	/**
	 * Get difference between dates in Minutes!
	 * you can do the following:
	 * 
	 * long diffSeconds = diff / 1000;
	 * long diffMinutes = diff / (60 * 1000);
	 * long diffHours = diff / (60 * 60 * 1000);
	 * long diffDays = diff / (24 * 60 * 60 * 1000);
	 * @param other other date
	 * @return difference in miliseconds
	 * @throws ParseException ParseException
	 */
	private long getDifference(ZerliDate other) throws ParseException {
		Date thisDate = mFullDateFormat.parse(this.getFullDate());
		Date otherDate = mFullDateFormat.parse(other.getFullDate());
		return thisDate.getTime() - otherDate.getTime();
	}
	
	/**
	 * Get difference in minutes. positive if this &gt; other
	 * @param other other date
	 * @return difference in minutes
	 * @throws ParseException  ParseException
	 */
	public long getDifferenceInMinutes(ZerliDate other) throws ParseException {
		return getDifference(other) / (60 * 1000);
	}
	
	/**
	 * Get difference in hours. positive if this &gt; other
	 * @param other other date 
	 * @return Get difference in hours. positive if this &gt; other
	 * @throws ParseException  ParseException
	 */
	public long getDifferenceInHours(ZerliDate other) throws ParseException {
		return getDifference(other) / (60 * 60 * 1000);
	}
	
	/**
	 * Get difference in days. positive if this &gt; other
	 * @param other other date
	 * @return Get difference in days. positive if this &gt; other
	 * @throws ParseException ParseException
	 */
	public long getDifferenceInDays(ZerliDate other) throws ParseException {
		return getDifference(other) / (24 * 60 * 60 * 1000);
	}
	
	/**
	 * only for a delivery time 
	 * 
	 * @return if the delivery time is 3 hour or more.
	 */
	public boolean isSafeDevliveryTime()
	{
		ZerliDate now = new ZerliDate();
		try {
			return this.getDifferenceInHours(now)<=3;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
			
		}
		
	}
	
	/**
	 * Add or subtract minutes from the date.
	 * @param minutes minutes
	 * @throws ParseException ParseException
	 */
	public void addMinutes(int minutes) throws ParseException {
		modifyDate(Calendar.MINUTE, minutes);
	}
	
	/**
	 * Add or subtract hours from the date.
	 * @param hours hours
	 * @throws ParseException ParseException
	 */
	public void addHours(int hours) throws ParseException {
		modifyDate(Calendar.HOUR, hours);
	}
	
	/**
	 * Add or subtract days from the date.
	 * @param days to add. negative number would decrement the days.
	 * @throws ParseException  ParseException
	 */
	public void addDays(int days) throws ParseException {
		modifyDate(Calendar.DAY_OF_MONTH, days);
	}
	
	/**
	 * Add or subtract months from the date.
	 * @param months to add. negative number would decrement the months.
	 * @throws ParseException  ParseException
	 */
	public void addMonths(int months) throws ParseException {
		modifyDate(Calendar.MONTH, months);
	}
	
	/**
	 * Add or subtract years from the date.
	 * @param years to add. negative number would decrement the years.
	 * @throws ParseException  ParseException
	 */
	public void addYears(int years) throws ParseException {
		modifyDate(Calendar.YEAR, years);
	}
	
	/**
	 * This function modifies the date. Adds or Subtract a value from a date field.
	 * @param field to change (day / month / year / hour/ min).
	 * @param val add or subtract the val from the field.
	 * @throws ParseException ParseException
	 */
	private void modifyDate(int field, int val) throws ParseException {
		Calendar cal = Calendar.getInstance();
		Date date = mFullDateFormat.parse(mFullDate);
	    cal.setTime(date);
	    cal.add(field, val); 
	    date = cal.getTime();
	    mFullDate = mFullDateFormat.format(date).toString();
	}
	

	
	
	@Override
	public String toString()
	{
		return this.getFullDate();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mFullDate == null) ? 0 : mFullDate.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZerliDate other = (ZerliDate) obj;
		if (mFullDate == null) {
			if (other.mFullDate != null)
				return false;
		} else if (!mFullDate.equals(other.mFullDate))
			return false;
		return true;
	}
	
	
}	// ZerliDate