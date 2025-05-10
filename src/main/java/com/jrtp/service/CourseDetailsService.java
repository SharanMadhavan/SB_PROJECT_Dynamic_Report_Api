package com.jrtp.service;

import java.util.List;
import java.util.Set;


import com.jrtp.model.SearchInputs;
import com.jrtp.model.SearchResults;

import jakarta.servlet.http.HttpServletResponse;

public interface CourseDetailsService {
	public Set<String> showAllCourseCategories();
	public Set<String> showAllTraningModes();
	public Set<String> showAllFaculties();
	
	public List<SearchResults> showCoursesByFilter(SearchInputs inputs);
	
	public void generatePdfReport(SearchInputs inputs,HttpServletResponse res) throws Exception;
	public void generateExcelReport(SearchInputs inputs, HttpServletResponse res) throws Exception;
	
	public void generatePdfReportForAllCourses(HttpServletResponse res) throws Exception;
	public void generateExcelReportForAllCourses(HttpServletResponse res)throws Exception;

}
