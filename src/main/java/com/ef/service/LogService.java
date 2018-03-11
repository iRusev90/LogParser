package com.ef.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ef.entity.LogEntry;

@Service
public class LogService {
	private static final String LOG_FILE_DELIMITER = "\\|";
	private static final String LOG_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final int LOG_FILE_SECTIONS_COUNT = LogFileParts.values().length;
	
	private DateTimeFormatter dateTimeFormatter;
	
	public LogService() {
		dateTimeFormatter = DateTimeFormatter.ofPattern(LOG_TIMESTAMP_FORMAT);
	}

	public void saveLogsFromFiles(List<String> pathsToFiles) throws IOException {
		for (String pathToFile : pathsToFiles) {
			try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
				String line;

				while ((line = br.readLine()) != null) {
					LogEntry logEntry = parseAccessLogLineToLogEntry(line);
					System.out.println(logEntry);
				}
			}
		}
	}

	private LogEntry parseAccessLogLineToLogEntry(String accessLogLine) {
		String[] logParts = accessLogLine.split(LOG_FILE_DELIMITER);
		
		if (logParts.length != LOG_FILE_SECTIONS_COUNT) {
			//TODO throw
		}
		
		
		String timeStampString = logParts[LogFileParts.DATE.getIndex()];
		LocalDateTime timestamp = LocalDateTime.parse(timeStampString, dateTimeFormatter);
		String ip = logParts[LogFileParts.IP.getIndex()];
		String request = logParts[LogFileParts.Request.getIndex()];
		String status = logParts[LogFileParts.Status.getIndex()];
		String userAgent = logParts[LogFileParts.UserAgent.getIndex()];
		
		System.out.println(timestamp);
		System.out.println(ip);
		System.out.println(request);
		System.out.println(status);
		System.out.println(userAgent);
		
		return null;
	}

	private static enum LogFileParts {
		DATE(0), IP(1), Request(2), Status(3), UserAgent(4);

		private int index;

		LogFileParts(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}
	}
}
