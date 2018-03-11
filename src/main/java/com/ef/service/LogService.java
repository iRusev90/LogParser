package com.ef.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ef.db.GenericGateway;
import com.ef.entity.BanEntry;
import com.ef.entity.Log;
import com.ef.finder.LogFinder;

@Service
public class LogService {
	private static final String LOG_FILE_DELIMITER = "\\|";
	private static final String LOG_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final int LOG_FILE_SECTIONS_COUNT = LogFileParts.values().length;

	@Autowired
	private GenericGateway gateway;

	@Autowired
	private LogFinder logFinder;

	private DateTimeFormatter dateTimeFormatter;

	public LogService() {
		dateTimeFormatter = DateTimeFormatter.ofPattern(LOG_TIMESTAMP_FORMAT);
	}

	@Transactional
	public void saveLogsFromFiles(List<String> pathsToFiles) throws IOException {
		for (String pathToFile : pathsToFiles) {
			try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
				String line;

				while ((line = br.readLine()) != null) {
					Log logEntry = parseAccessLogLineToLog(line);
					gateway.save(logEntry);
				}
			}
		}
	}

	private Log parseAccessLogLineToLog(String accessLogLine) {
		String[] logParts = accessLogLine.split(LOG_FILE_DELIMITER);

		if (logParts.length != LOG_FILE_SECTIONS_COUNT) {
			// TODO throw
		}

		String timeStampString = logParts[LogFileParts.DATE.getIndex()];
		LocalDateTime timestamp = LocalDateTime.parse(timeStampString, dateTimeFormatter);
		String ip = logParts[LogFileParts.IP.getIndex()];
		String request = logParts[LogFileParts.REQUEST.getIndex()];
		String status = logParts[LogFileParts.STATUS.getIndex()];
		String agent = logParts[LogFileParts.AGENT.getIndex()];

		Log log = new Log();
		log.setTimestamp(timestamp);
		log.setIp(ip);
		log.setRequest(request);
		log.setStatus(status);
		log.setAgent(agent);

		return log;
	}

	
	@Transactional
	public void banAndShowIps(LocalDateTime startDate, String duration, Integer threshhold) {
		LocalDateTime endDate = getEndDate(startDate, duration);
		List<String> ips = logFinder.findLogsForTimeframeWithEntriesOverThreshhold(startDate, endDate, threshhold);
		
		for (String ip : ips) {
			BanEntry banEntry = new BanEntry();
			
			banEntry.setIp(ip);
			
			banEntry.setComment(String.format("Exceeds the maximum allowed %s requests for the period between %s and %s", threshhold, startDate, endDate));
			gateway.save(banEntry);
			System.out.println(ip);
		}
	}
	
	private  LocalDateTime getEndDate(LocalDateTime startDate, String duration) {
		if (startDate == null) {
			return null;
		}
		LocalDateTime endDate = null;
		
		switch (duration) {
			case "hourly":
				endDate = startDate.plusHours(1);
				break;
			case "daily":
				endDate = startDate.plusDays(1);
				break;
			default:
				// LOG or throw
				break;
		}

		return endDate;
	}

	private static enum LogFileParts {
		DATE(0), IP(1), REQUEST(2), STATUS(3), AGENT(4);

		private int index;

		LogFileParts(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}
	}
}
