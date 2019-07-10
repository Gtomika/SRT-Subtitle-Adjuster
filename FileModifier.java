package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import application.ModifierController.DelaySpeedUp;

/**
 * This class computes the new subtitle timings and modifies the SRT file.
 * 
 * @author Gáspár Tamás
 */
public class FileModifier {

	/**
	 * The STR file.
	 */
	private File srtFile;
	/**
	 * Stores the amount of delay or speed up needed.
	 */
	private int seconds, miliseconds;
	/**
	 * Stores if speed up or delay is needed.
	 */
	private DelaySpeedUp ds;
	
	public FileModifier(File srtFile, int seconds, int miliseconds, DelaySpeedUp ds) {
		this.srtFile = srtFile;
		this.seconds = seconds;
		this.miliseconds = miliseconds;
		this.ds = ds;
	}
	
	/**
	 * Converts a given timestamp (HH:MM:SS,MS format) according to {@link #miliseconds}, {@link #seconds} and {@link #ds}.
	 */
	private String convertTimestamp(String timestamp) {
		LocalTime time = LocalTime.parse(timestamp, TimeUtils.FORMATTER);
		if(ds == DelaySpeedUp.DELAY) {
			LocalTime temp = time.plusSeconds(seconds).plusNanos(TimeUtils.nanoFromMili(miliseconds));  //add seconds and milliseconds
			if(time.compareTo(temp) > 0) { //if the new time is smaller, meaning it "overflow" 23:59:59,999 
				time = LocalTime.parse("23:59:59:999", TimeUtils.FORMATTER);
			} else { //no overflow
				time = temp;
			}
		} else { //speed up
			LocalTime temp = time.minusSeconds(seconds).minusNanos(TimeUtils.nanoFromMili(miliseconds));  //subtract seconds and milliseconds
			if(temp.compareTo(time) > 0) { //if the new time is bigger it "underflow" 00:00:00,000
				time = LocalTime.parse("00:00:00,000", TimeUtils.FORMATTER);
			} else { //no underflow
				time = temp;
			}
		}
		return time.format(TimeUtils.FORMATTER); //convert back to string
	}
	
	/**
	 * Decides if a line is a subtitle timestamp, by checking if it contains the {@code -->} character chain.
	 */
	private boolean isTimestampLine(String line) {
		return line.contains("-->");
	}
	
	/**
	 * Replaces the timestamps in the SRT file accoirding to the parameters of this modifier object.
	 * 
	 * @return true if all timestamps were ucessfully modified.
	 * @throws IOException 
	 */
	public boolean replaceTimestamps() throws IOException {
		boolean completeSucess = true;
		List<String> lines = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(srtFile)); //reading like this to avoid encoding problems
		String line = null;
		while ((line = reader.readLine()) != null) {
			if(isTimestampLine(line)) {
				String[] split = line.split("-->");
				String timestampFrom = split[0].endsWith(" ") ? split[0].substring(0, split[0].length()-1) : split[0];//remove whitespace before --> if needed
				String timestampTo = split[1].startsWith(" ") ? split[1].substring(1, split[1].length()) : split[1];//remove whitespace after --> if needed
				try {
					line = convertTimestamp(timestampFrom) + " --> " + convertTimestamp(timestampTo); //convert timestamps and reconstruct line
				} catch(DateTimeParseException e) {
					completeSucess = false;
				}
			}
			lines.add(line); //save modified line
		}
		reader.close();
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(srtFile))); //write modified lines
		for(String modifiedLine: lines) {
			writer.write(modifiedLine);
			writer.newLine();
		}
		writer.close();
		//Files.write(Paths.get(srtFile.toURI()), lines, StandardCharsets.US_ASCII); 
		return completeSucess;
	}
}
