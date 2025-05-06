package com.jrtp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.directory.SearchResult;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.util.StringUtils;

import com.jrtp.entity.CourseDetails;
import com.jrtp.model.SearchInputs;
import com.jrtp.model.SearchResults;
import com.jrtp.repository.CourseDetailsRepository;

import jakarta.servlet.http.HttpServletResponse;

public class CourseDetailsServiceIMPL implements CourseDetailsService {
	@Autowired
	private CourseDetailsRepository courseRepo;

	@Override
	public Set<String> showAllCourseCategories() {
		return courseRepo.getUniqueCourseCategories();
	}

	@Override
	public Set<String> showAllTraningModes() {
		return courseRepo.getUniqueTraninigModes();
	}

	@Override
	public Set<String> showAllFaculties() {
		return courseRepo.getUniquerCourseFacultyNames();
	}

	@Override
	public List<SearchResults> showCoursesByFilter(SearchInputs inputs) {
		
		
		// taking the course details entity obj and set the searchinputs properties to the entity obj if those values are not null
		// after set then, add this entity obj to the example obj
		CourseDetails entity = new CourseDetails();
		String categories = inputs.getCourseCategory();
		if(StringUtils.hasLength(categories))
			entity.setCourseCategory(categories);
		
		
		String facultyName = inputs.getFacultyName();
		if(facultyName!=null && facultyName.length()!=0 && !facultyName.equals(""))
			entity.setFacultyName(facultyName);
		
		String trainingMode = inputs.getTrainingMode();
		if(trainingMode!=null && trainingMode.length()!=0 && !trainingMode.equals(""))
			entity.setTrainingMode(trainingMode);
		
		LocalDateTime startsOn = inputs.getStartsOn();
		if(startsOn!=null)
			entity.setStartDate(startsOn);
		
		// adding the  not null entity object to the example obj
		Example<CourseDetails> example = Example.of(entity);
		
		
		  // if no filter will select it will show all the data of the courseDetails 
		// perform the search operation with filters data of the example enttity obj
		  List<CourseDetails> coursedetailslist = courseRepo.findAll(example);
		  List<SearchResults> searchlist = new ArrayList();
		  
		  coursedetailslist.forEach(courses ->{ 
			  SearchResults result = new SearchResults();
			  BeanUtils.copyProperties(courses, result);
		         searchlist.add(result);
		  
		  });
		 
		
		/*
		 * List<CourseDetails> coursedetailslist = courseRepo.findAll();
		 * List<SearchResults> searchlist = coursedetailslist.stream().map(course -> {
		 * SearchResults results = new SearchResults(); BeanUtils.copyProperties(course,
		 * results); return results; }).collect(Collectors.toList());
		 */
		
		return searchlist;
	}

	@Override
	public void generatePdfReport(SearchInputs inputs, HttpServletResponse res) {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateExcelReport(SearchInputs inputs, HttpServletResponse res) {
		
		List<SearchResults> listResults = showCoursesByFilter(inputs);
		// in the excel there is workbook and bottom sheets are there and also rows and cells they are staritng with index 0
		// creation of the workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// creation of the sheet
		HSSFSheet sheet = workbook.createSheet("CourseDetails");
		// heading row creation
		HSSFRow headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("courseId");
		headerRow.createCell(1).setCellValue("courseName");
		headerRow.createCell(2).setCellValue("courseCategory");
		headerRow.createCell(3).setCellValue("location");
		headerRow.createCell(4).setCellValue("facultyName");
		headerRow.createCell(5).setCellValue("fee");
		headerRow.createCell(6).setCellValue("adminContact");
		headerRow.createCell(7).setCellValue("trainingMode");
		headerRow.createCell(8).setCellValue("startDate");
		headerRow.createCell(9).setCellValue("courseStatus");
		
		// 
		
		int i=1;
		for(SearchResults result : listResults) {
			HSSFRow datarow = sheet.createRow(i);
			datarow.createCell(0).setCellValue(result.getCourseId());
			datarow.createCell(1).setCellValue(result.getCourseName());
			datarow.createCell(2).setCellValue(result.getCourseCategory());
			datarow.createCell(3).setCellValue(result.getLocation());
			datarow.createCell(4).setCellValue(result.getFacultyName());
			datarow.createCell(5).setCellValue(result.getFee());
			datarow.createCell(6).setCellValue(result.getAdminContact());
			datarow.createCell(7).setCellValue(result.getTrainingMode());
			datarow.createCell(8).setCellValue(result.getStartDate());
			datarow.createCell(9).setCellValue(result.getCourseStatus());
			i++;
		}

	}

}
