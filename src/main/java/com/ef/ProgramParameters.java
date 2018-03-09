package com.ef;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ef.exception.InvalidParserParamterException;

public class ProgramParameters {
	private static final String ARGUMENT_REGEX = "^--(.+?)=(.+?)$";
	private static final String START_DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";

	private String pathToAccessLog = null;
	private LocalDateTime startDate = null;
	private String duration = null;
	private Integer threshhold = null;

	public ProgramParameters(String[] args) {
		parseAndStoreArguments(args);
	}

	private void parseAndStoreArguments(String[] args) {
		Pattern argPattern = Pattern.compile(ARGUMENT_REGEX);
		for (String argument : args) {
			Matcher argMatcher = argPattern.matcher(argument);
			if (!argMatcher.matches()) {
				throw new InvalidParserParamterException(
						String.format("Expected string in format \"%s\" but was \"s%\"", ARGUMENT_REGEX, argument));
			}

			String parameterName = argMatcher.group(1);
			String parameterValue = argMatcher.group(2);

			switch (parameterName) {
				case "accesslog":
					this.pathToAccessLog = parameterValue;
					break;
				case "startDate":
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(START_DATE_FORMAT);
					startDate = LocalDateTime.parse(parameterValue, dateTimeFormatter);
					break;
				case "duration":
					this.duration = parameterValue;
					break;
				case "threshold":
					this.threshhold = Integer.parseInt(parameterValue);
					break;
				default:
					throw new InvalidParserParamterException(
						String.format("Parameter with name \"%s\" not recognized.", parameterName));
			}

		}
	}

	public String getPathToAccessLog() {
		return pathToAccessLog;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public LocalDateTime getEndDate() {
		if (this.startDate == null) {
			return null;
		}
		LocalDateTime endDate = null;
		
		switch (this.duration) {
			case "hourly":
				endDate = this.startDate.plusHours(1);
				break;
			case "daily":
				endDate = this.startDate.plusDays(1);
				break;
			default:
				// LOG or throw
				break;
		}

		return endDate;
	}

	public Integer getThreshhold() {
		return threshhold;
	}
}
