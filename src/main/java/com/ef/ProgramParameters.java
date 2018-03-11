package com.ef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProgramParameters {

	private List<String> pathsToAccessLogs = new ArrayList<String>();
	private LocalDateTime startDate = null;
	private String duration = null;
	private Integer threshhold = null;

	public ProgramParameters() {

	}

	public List<String> getPathsToAccessLogs() {
		return pathsToAccessLogs;
	}

	public void setPathsToAccessLogs(List<String> pathsToAccessLogs) {
		this.pathsToAccessLogs = pathsToAccessLogs;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Integer getThreshhold() {
		return threshhold;
	}

	public void setThreshhold(Integer threshhold) {
		this.threshhold = threshhold;
	}

}
