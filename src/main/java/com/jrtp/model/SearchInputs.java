package com.jrtp.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SearchInputs {
	private String courseCategory;
	private String facultyName;
	private String trainingMode;
	private LocalDateTime startsOn;
	public String getCourseCategory() {
		return courseCategory;
	}
	public void setCourseCategory(String courseCategory) {
		this.courseCategory = courseCategory;
	}
	public String getFacultyName() {
		return facultyName;
	}
	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}
	public String getTrainingMode() {
		return trainingMode;
	}
	public void setTrainingMode(String trainingMode) {
		this.trainingMode = trainingMode;
	}
	public LocalDateTime getStartsOn() {
		return startsOn;
	}
	public void setStartsOn(LocalDateTime startsOn) {
		this.startsOn = startsOn;
	}
	@Override
	public String toString() {
		return "SearchInputs [courseCategory=" + courseCategory + ", facultyName=" + facultyName + ", trainingMode="
				+ trainingMode + ", startsOn=" + startsOn + "]";
	}
	
	
	
	

}
