package com.jrtp.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="course_Details_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer courseId;
	@Column(length = 30)
	private String courseName;
	@Column(length = 30)
	private String courseCategory;
	@Column(length = 30)
	private String location;
	@Column(length = 30)
	private String facultyName;
	private double fee;
	private String adminContact;
	@Column(length = 30)
	private String adminName;
	@Column(length = 30)
	private String trainingMode;
	private LocalDateTime startDate;
	@Column(length = 10)
	private String courseStatus;
	@CreationTimestamp
	@Column(insertable = true, updatable = false)
	private LocalDateTime creationDate;
	@UpdateTimestamp
	@Column(insertable = false,updatable = true)
	private LocalDateTime updationDate;
	@Column(length = 30)
	private String createdBy;
	@Column(length = 30)
	private String updatedBy;
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseCategory() {
		return courseCategory;
	}
	public void setCourseCategory(String courseCategory) {
		this.courseCategory = courseCategory;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getFacultyName() {
		return facultyName;
	}
	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public String getAdminContact() {
		return adminContact;
	}
	public void setAdminContact(String adminContact) {
		this.adminContact = adminContact;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getTrainingMode() {
		return trainingMode;
	}
	public void setTrainingMode(String trainingMode) {
		this.trainingMode = trainingMode;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public LocalDateTime getUpdationDate() {
		return updationDate;
	}
	public void setUpdationDate(LocalDateTime updationDate) {
		this.updationDate = updationDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	@Override
	public String toString() {
		return "CourseDetails [courseId=" + courseId + ", courseName=" + courseName + ", courseCategory="
				+ courseCategory + ", location=" + location + ", facultyName=" + facultyName + ", fee=" + fee
				+ ", adminContact=" + adminContact + ", adminName=" + adminName + ", trainingMode=" + trainingMode
				+ ", startDate=" + startDate + ", courseStatus=" + courseStatus + ", creationDate=" + creationDate
				+ ", updationDate=" + updationDate + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
	}
	
	
	
	
}
