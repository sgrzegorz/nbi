package resource;

import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.Exchange;
import model.Parser;
import model.Program;


@Path("/question")
public class ClientQuestion {
	String startdate;
	String enddate;
	int number;
	String date;
	String code;

	@GET
	@Path("/0")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_HTML)
	public String goldRate(@QueryParam("year") String year, @QueryParam("month") String month,@QueryParam("day") String day) {

		
		if(year == null) System.out.println("hejjjjjjjjj");
		if(!year.isEmpty() && !month.isEmpty() && !day.isEmpty()) {
			String [] args= new String[]{"-0","2019","10","10"};
			
			Parser p= Parser.getInstance(args);
			String startdate=p.getStartdate();
			String enddate=p.getEnddate();
			int number=p.getNumber();
			String date=p.getDate();
			String code=p.getCode();
			System.out.println();
			
			System.out.println(Exchange.goldRate(date));
			return Exchange.goldRate(date);
		}	
		return new Scanner(ClientQuestion.class.getResourceAsStream("page1.html"), "UTF-8").useDelimiter("\\A").next();
	}
	
	
	public void parseInputData(String []args) {
		Parser p= Parser.getInstance(args);
		startdate=p.getStartdate();
		enddate=p.getEnddate();
		number=p.getNumber();
		date=p.getDate();
		code=p.getCode();
	}
	
	@GET
	@Path("/1")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_HTML)
	public String currencyRate(@QueryParam("year") String year, @QueryParam("month") String month,@QueryParam("day") String day) {

		
		if(!year.isEmpty() && !month.isEmpty() && !day.isEmpty()) {
			String [] args= new String[]{"-0","2019","10","10"};
			parseInputData(args);
			
			System.out.println(Exchange.goldRate(date));
			return Exchange.goldRate(date);
		}	
		return new Scanner(ClientQuestion.class.getResourceAsStream("page1.html"), "UTF-8").useDelimiter("\\A").next();
	}
	
	
	@GET
	@Path("/2")
	@Produces(MediaType.TEXT_HTML)
	public String getMessages1() {
		
		String text= new Scanner(ClientQuestion.class.getResourceAsStream("selection.html"), "UTF-8").useDelimiter("\\A").next();
		return text;
	}
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getMainPage() {
		
		String text= new Scanner(ClientQuestion.class.getResourceAsStream("mainpage.html"), "UTF-8").useDelimiter("\\A").next();
		return text;
	}
	
	
	
	public String getDataFromNbi() {
	
		String text= new Scanner(ClientQuestion.class.getResourceAsStream("selection.html"), "UTF-8").useDelimiter("\\A").next();
		return text;
	}
	
	
	
}
