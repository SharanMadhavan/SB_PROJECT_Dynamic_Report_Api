package com.jrtp.ms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jrtp.model.SearchInputs;
import com.jrtp.model.SearchResults;
import com.jrtp.service.CourseDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/report/api")
public class ReportOperationsController {
	
	@Autowired
	private CourseDetailsService service;
	
	@GetMapping("/categories")
	public ResponseEntity<?> fetchCourseCategories(){
		try {
			
			Set<String> courseinfo = service.showAllCourseCategories();
			return new ResponseEntity<Set<String>>(courseinfo,HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/training-modes")
	public ResponseEntity<?> fetchTrainingModes(){
		try {
			
			Set<String> trainingmodes = service.showAllTraningModes();
			return new ResponseEntity<Set<String>>(trainingmodes,HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/faculties")
	public ResponseEntity<?> fetchAllFaculties(){
		try {
			
			Set<String> faculties = service.showAllFaculties();
			return new ResponseEntity<Set<String>>(faculties,HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/search")
	public ResponseEntity<?> fetchCoursesByFilter(@RequestBody SearchInputs inputs){
		try {
			List<SearchResults> list = service.showCoursesByFilter(inputs);
			return new ResponseEntity<List<SearchResults>>(list,HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/pdf-report")
	public void showPdfReport(@RequestBody SearchInputs inputs,HttpServletResponse res) {
		try {
			// set the response content type
			res.setContentType("application/pdf");
			// set the content dispositon header to response content going to browser as downlodable file
			res.setHeader("Content-Disposition", "attachment;fileName=courses.pdf");
			//use service
			service.generatePdfReport(inputs, res);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping("/excel-report")
	public void showExcelReport(@RequestBody SearchInputs inputs,HttpServletResponse res) {
		try {
			// set the response content type
			res.setContentType("application/vnd.ms-excel");
			// set the content dispositon header to response content going to browser as downlodable file
			res.setHeader("Content-Disposition", "attachment;fileName=courses.xls");
			//use service
			service.generateExcelReport(inputs, res);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
