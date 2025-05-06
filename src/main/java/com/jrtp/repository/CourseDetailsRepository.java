package com.jrtp.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jrtp.entity.CourseDetails;

public interface CourseDetailsRepository extends JpaRepository<CourseDetails, Integer> {
	
	@Query(value = "select distinct(courseCategory) from course_Details_info")
	public Set<String> getUniqueCourseCategories();
	
	@Query(value = "select distinct(facultyName) from course_Details_info")
	public Set<String> getUniquerCourseFacultyNames();
	
	@Query(value = "select distinct(trainingMode) from course_Details_info")
	public Set<String> getUniqueTraninigModes();
	

}
