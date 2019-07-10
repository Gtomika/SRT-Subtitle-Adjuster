package application;

import java.time.format.DateTimeFormatter;

/**
 * Contains utility methods for operating on time values.e
 * 
 * @author Gáspár Tamás
 */
public class TimeUtils {

	/**
	 * A time formatter tuned to the standart timestamps of an SRT file.
	 */
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss,SSS"); //00:03:32,500 for example
	
	/**
	 * Converts amounts of miliseconds to nanoseconds.
	 */
	public static int nanoFromMili(int miliSecs) {
		return 1000000*miliSecs;
	}
	
	/**
	 * Converts amounts of nanoseconds to miliseconds.
	 */
	public static int miliFromNano(int nanoSecs) {
		return new Integer(new Double(nanoSecs / 1000000.0).intValue());
	}
}
