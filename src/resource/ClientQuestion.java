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
import model.CommandLineParsingProgram;

@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_HTML)
@Path("/question")
public class ClientQuestion {
	String startdate;
	String enddate;
	int number;
	String date;
	String code;
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getMainPage() {
		
		String text= new Scanner(ClientQuestion.class.getResourceAsStream("mainpage.html"), "UTF-8").useDelimiter("\\A").next();
		return text;
	}
	
	@GET
	@Path("/0")
	public String goldRate(@QueryParam("year") String year, @QueryParam("month") String month,@QueryParam("day") String day) {

		
		if(isNotEmpty(year) && isNotEmpty(month)&& isNotEmpty(day)) {
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
		return new Scanner(ClientQuestion.class.getResourceAsStream("page0.html"), "UTF-8").useDelimiter("\\A").next();
	}
	
	
	
	@GET
	@Path("/1")
	public String currencyRate(@QueryParam("code") String code,@QueryParam("year") String year, @QueryParam("month") String month,@QueryParam("day") String day) {

		
		if(isNotEmpty(code) &&isNotEmpty(year) && isNotEmpty(month)&& isNotEmpty(day)) {
			String [] args= new String[]{"-1",code,year,month,day};
			parseInputData(args);
			System.out.println(code);
			System.out.println(date);

			return Exchange.currencyRate(code, date);
		}	
		return new Scanner(ClientQuestion.class.getResourceAsStream("page1.html"), "UTF-8").useDelimiter("\\A").next();
	}
	
	@GET
	@Path("/2")
	public String averageGoldRate(@QueryParam("year1") String year1, @QueryParam("month1") String month1,@QueryParam("day1") String day1,@QueryParam("year2") String year2, @QueryParam("month2") String month2,@QueryParam("day2") String day2){

		
		if(isNotEmpty(year1) &&isNotEmpty(month1) && isNotEmpty(day1)&& isNotEmpty(year2) &&isNotEmpty(month2) && isNotEmpty(day2)) {
			String [] args= new String[]{"-2",year1,month1,day1,year2,month2,day2};
			parseInputData(args);
			
			return Exchange.averageGoldRate(startdate, enddate);
		}	
		return new Scanner(ClientQuestion.class.getResourceAsStream("page2.html"), "UTF-8").useDelimiter("\\A").next();
	}
	
	
	@GET
	@Path("/3")
	public String biggestAmplitudeChange(@QueryParam("year") String year, @QueryParam("month") String month,@QueryParam("day") String day){

		
		if(isNotEmpty(year) &&isNotEmpty(month) && isNotEmpty(day)) {
			String [] args= new String[]{"-2",year,month,day};
			parseInputData(args);
			
			return Exchange.getMostUnstableCurrencySinceDate(date);
		}	
		return new Scanner(ClientQuestion.class.getResourceAsStream("page3.html"), "UTF-8").useDelimiter("\\A").next();
	}
	
	@GET
	@Path("/4")
	public String lowestCurrencyRate(@QueryParam("year") String year, @QueryParam("month") String month,@QueryParam("day") String day){

		
		if(isNotEmpty(year) &&isNotEmpty(month) && isNotEmpty(day)) {
			String [] args= new String[]{"-4",year,month,day};
			parseInputData(args);
			
			return Exchange.lowestCurrencyRate(date);
		}	
		return new Scanner(ClientQuestion.class.getResourceAsStream("page4.html"), "UTF-8").useDelimiter("\\A").next();
	}
	
	@GET
	@Path("/5")
	public String sortCurrencySpread(@QueryParam("year") String year, @QueryParam("month") String month,@QueryParam("day") String day,@QueryParam("n") int number){

		
		if(isNotEmpty(year) &&isNotEmpty(month) && isNotEmpty(day)&& number>0) {
			String [] args= new String[]{"-5",year,month,day,String.parse(number)};
			parseInputData(args);
			
			return 	Exchange.sortCurrencySpread(date, number);
		}	
		return new Scanner(ClientQuestion.class.getResourceAsStream("page5.html"), "UTF-8").useDelimiter("\\A").next();
	}
	
	@GET
	@Path("/6")
	public String biggestAmplitudeChange(@QueryParam("code") String code){

		
		if(isNotEmpty(code)) {
			String [] args= new String[]{"-6",code};
			parseInputData(args);
			
			return Exchange.extremaDates(code);
		}	
		return new Scanner(ClientQuestion.class.getResourceAsStream("page6.html"), "UTF-8").useDelimiter("\\A").next();
	}
	
	
	
	public boolean isNotEmpty(String string) {
		return (string!=null)&&(!string.isEmpty());
	}
	
	public void parseInputData(String []args) {
		Parser p= Parser.getInstance(args);
		startdate=p.getStartdate();
		enddate=p.getEnddate();
		number=p.getNumber();
		date=p.getDate();
		code=p.getCode();
	}
	
	
	
}
