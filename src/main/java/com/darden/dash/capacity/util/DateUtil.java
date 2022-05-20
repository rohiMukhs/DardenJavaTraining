package com.darden.dash.capacity.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author Skashala
 * @date 16-May-2022
 *
 *       This class is used for convert the Date and Time  from one format to other format
 */
public class DateUtil implements Serializable {
	

	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	private static final long serialVersionUID = 1L;
	
/**
 * This method is to convert instant time to string time format
 * 
 * @param instant
 * @return String
 */
	public static String instantToTime(Instant instant) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CapacityConstants.H_MM).withZone(ZoneId.systemDefault());
		return formatter.format(instant);
	}
	
	/**
	 * This method is to convert Date to String format
	 * 
	 * @param instant
	 * @return String
	 */
	public static String instantToDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(CapacityConstants.YYYY_MM_DD);
		return formatter.format(date);
	}
	
	/**
	 * This method is to convert string to Date format
	 * 
	 * @param instant
	 * @return Date
	 */
	public static Date stringToDate(String string) {
		Date formatter = null;
		try {
			formatter = new SimpleDateFormat(CapacityConstants.MM_DD_YYYY).parse(string);
		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return formatter;
	}

}
