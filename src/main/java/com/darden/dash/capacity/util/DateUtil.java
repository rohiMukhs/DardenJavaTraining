package com.darden.dash.capacity.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Skashala
 * @date 16-May-2022
 *
 *       This class is used for convert the Date and Time from one format to
 *       other format
 */
public class DateUtil implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	private static final long serialVersionUID = 1L;

	/**
	 * This method is used to convert instant time to string time format
	 * using the DateTimeFormatter class.
	 * 
	 * @param instant Instant value containing the value of time to be 
	 * 					converted.
	 * 
	 * @return String containing the value of time converted from 
	 * 					instant format.
	 */
	public static String instantToTime(Instant instant) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CapacityConstants.H_MM)
				.withZone(ZoneId.systemDefault());
		return formatter.format(instant);
	}

	/**
	 * This method is used to convert Date to String format
	 * using the SimpleDateFormat class.
	 * 
	 * @param date Date value containing the value of date to be
	 * 						converted.
	 * 
	 * @return String containing the value of date converted from 
	 * 						Date format.
	 */
	public static String instantToDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(CapacityConstants.YYYY_MM_DD);
		return formatter.format(date);
	}

	/**
	 * This method is use to covert date format to local date
	 * 
	 * @param date Date value containing the value of date to be
	 * 						converted.
	 * 
	 * @return LocalDate containing the value of date converted from 
	 * 						Date format.
	 */
	public static LocalDate convertDatetoLocalDate(Date date) {
		return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	/**
	 * This method is use to covert string format to local date
	 * using the DateTimeFormatter class.
	 * 
	 * @param date String value containing the value of date to be
	 * 						converted.
	 * 
	 * @return LocalDate containing the value of date converted from 
	 * 						String format.
	 */
	public static LocalDate convertStringtoLocalDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CapacityConstants.MM_DD_YYYY);
		return LocalDate.parse(date, formatter);
	}

	/**
	 * This method is to convert string date format to Date format
	 * using the SimpleDateFormat class.
	 * 
	 * @param dateString String value containing the value of date to be
	 * 						converted.
	 * 
	 * @return Date containing the value of date converted from 
	 * 						String format.
	 */
	public static Date stringToDate(String dateString) {
		Date formatter = null;
		try {
			formatter = new SimpleDateFormat(CapacityConstants.MM_DD_YYYY).parse(dateString);
		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return formatter;
	}

}
