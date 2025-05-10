package com.jrtp.service;

import java.awt.Color;
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
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jrtp.entity.CourseDetails;
import com.jrtp.model.SearchInputs;
import com.jrtp.model.SearchResults;
import com.jrtp.repository.CourseDetailsRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
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
		
		
		// ignoring the primitive values like 0.0 for the fee 
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreNullValues()
				.withIgnorePaths("fee");
		
		// adding the  not null entity object to the example obj
		Example<CourseDetails> example = Example.of(entity,matcher);
		
		
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
	public void generatePdfReport(SearchInputs inputs, HttpServletResponse res) throws Exception {
		//  get the search result
		List<SearchResults> listresult = showCoursesByFilter(inputs);
		// create document obj openPdf
		Document documet = new Document(PageSize.A4);
		// get pdf writer  to write to the document  and  response object
		PdfWriter.getInstance(documet,res.getOutputStream());
		// open the document
		documet.open();
		// define font for th page
		Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
		font.setSize(30);
		font.setColor(Color.RED);
		
		/// create the paragraph having the content with above font , colour
		Paragraph para = new Paragraph("Search report of courses",font);
		para.setAlignment(Paragraph.ALIGN_CENTER);
		// add paragraph to document
		documet.add(para);
		
		// display search result as a pdf table
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(70);
		table.setWidths(new float[] {3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f} );
		table.setSpacingBefore(2.0f);
		
		// prepare heading rows cells in the pdf table
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.gray);
		cell.setPadding(5);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		cellFont.setColor(Color.BLACK);
		cell.setPhrase(new Phrase("courseId",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseName",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseCategory",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("location",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("facultyName",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("fee",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("adminContact",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("trainingMode",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("startDate",cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseStatus",cellFont));
		table.addCell(cell);
		
		// add data cells to the pdftable
		listresult.forEach(result -> {
			table.addCell(String.valueOf(result.getCourseId()));
			table.addCell(result.getCourseName());
			table.addCell(result.getCourseCategory());
			table.addCell(result.getFacultyName());
			table.addCell(result.getLocation());
			table.addCell(String.valueOf(result.getFee()));
			table.addCell(result.getCourseStatus());
			table.addCell(result.getTrainingMode());
			table.addCell(result.getStartDate().toString());
			table.addCell(String.valueOf(result.getAdminContact()));
		});
		// add table document 
		documet.add(table);
		// close the documet
		documet.close();
	}

	@Override
	public void generateExcelReport(SearchInputs inputs, HttpServletResponse res) throws Exception{
		
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
		//get output stream pointing to response obj
		ServletOutputStream outputstrem = res.getOutputStream();
		//write the work book data response object using the above stream.
		
		workbook.write(outputstrem);
		
		// clost the output stream and workbook
		outputstrem.close();
		workbook.close();
	}

	@Override
	public void generatePdfReportForAllCourses(HttpServletResponse res) throws Exception {
		
		//  get the search result
		List<CourseDetails> results = courseRepo.findAll();
			List<SearchResults> listresult = new ArrayList<SearchResults>();
					results.forEach(result->{
				SearchResults sresult= new SearchResults();
				BeanUtils.copyProperties(result, sresult);
				listresult.add(sresult);
			});
			// create document obj openPdf
			Document documet = new Document(PageSize.A4);
			// get pdf writer  to write to the document  and  response object
			PdfWriter.getInstance(documet,res.getOutputStream());
			// open the document
			documet.open();
			// define font for th page
			Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
			font.setSize(30);
			font.setColor(Color.RED);
			
			/// create the paragraph having the content with above font , colour
			Paragraph para = new Paragraph("Search report of courses",font);
			para.setAlignment(Paragraph.ALIGN_CENTER);
			// add paragraph to document
			documet.add(para);
			
			// display search result as a pdf table
			PdfPTable table = new PdfPTable(10);
			table.setWidthPercentage(70);
			table.setWidths(new float[] {3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f} );
			table.setSpacingBefore(2.0f);
			
			// prepare heading rows cells in the pdf table
			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(Color.gray);
			cell.setPadding(5);
			Font cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			cellFont.setColor(Color.BLACK);
			cell.setPhrase(new Phrase("courseId",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("courseName",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("courseCategory",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("location",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("facultyName",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("fee",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("adminContact",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("trainingMode",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("startDate",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("courseStatus",cellFont));
			table.addCell(cell);
			
			// add data cells to the pdftable
			listresult.forEach(result -> {
				table.addCell(String.valueOf(result.getCourseId()));
				table.addCell(result.getCourseName());
				table.addCell(result.getCourseCategory());
				table.addCell(result.getFacultyName());
				table.addCell(result.getLocation());
				table.addCell(String.valueOf(result.getFee()));
				table.addCell(result.getCourseStatus());
				table.addCell(result.getTrainingMode());
				table.addCell(result.getStartDate().toString());
				table.addCell(String.valueOf(result.getAdminContact()));
			});
			// add table document 
			documet.add(table);
			// close the documet
			documet.close();
	}

	@Override
	public void generateExcelReportForAllCourses(HttpServletResponse res) throws Exception {
		List<CourseDetails> results = courseRepo.findAll();
		List<SearchResults> listResults = new ArrayList<SearchResults>();
		results.forEach(result->{
			SearchResults sresult = new SearchResults();
			BeanUtils.copyProperties(result, sresult);
			listResults.add(sresult);
		});
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
		//get output stream pointing to response obj
		ServletOutputStream outputstrem = res.getOutputStream();
		//write the work book data response object using the above stream.
		
		workbook.write(outputstrem);
		
		// clost the output stream and workbook
		outputstrem.close();
		workbook.close();
		
	}

}
