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
import model.CommandLineParser;

@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_HTML)
@Path("/question")
public class ClientQuestion {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getMainPage() {
		return new Scanner(ClientQuestion.class.getResourceAsStream("mainpage.html"), "UTF-8").useDelimiter("\\A").next();
	}

	@GET
	@Path("/0")
	public String goldRate(@QueryParam("year") String year, @QueryParam("month") String month,
			@QueryParam("day") String day) {

		if (isNotEmpty(year) && isNotEmpty(month) && isNotEmpty(day)) {
//			String [] args= new String[]{"-0","2019","10","10"};
			String[] args = new String[] { "-0", year, month, day };
			return CommandLineParser.executeCommandLineTask(Parser.getInstance(args));

		}
		return new Scanner(ClientQuestion.class.getResourceAsStream("page0.html"), "UTF-8").useDelimiter("\\A").next();
	}

	@GET
	@Path("/1")
	public String currencyRate(@QueryParam("code") String code, @QueryParam("year") String year,
			@QueryParam("month") String month, @QueryParam("day") String day) {

		if (isNotEmpty(code) && isNotEmpty(year) && isNotEmpty(month) && isNotEmpty(day)) {
			String[] args = new String[] { "-1", code, year, month, day };
			return CommandLineParser.executeCommandLineTask(Parser.getInstance(args));

		}
		return new Scanner(ClientQuestion.class.getResourceAsStream("page1.html"), "UTF-8").useDelimiter("\\A").next();
	}

	@GET
	@Path("/2")
	public String averageGoldRate(@QueryParam("year1") String year1, @QueryParam("month1") String month1,
			@QueryParam("day1") String day1, @QueryParam("year2") String year2, @QueryParam("month2") String month2,
			@QueryParam("day2") String day2) {

		if (isNotEmpty(year1) && isNotEmpty(month1) && isNotEmpty(day1) && isNotEmpty(year2) && isNotEmpty(month2)
				&& isNotEmpty(day2)) {
			String[] args = new String[] { "-2", year1, month1, day1, year2, month2, day2 };
			return CommandLineParser.executeCommandLineTask(Parser.getInstance(args));
		}
		return new Scanner(ClientQuestion.class.getResourceAsStream("page2.html"), "UTF-8").useDelimiter("\\A").next();
	}

	@GET
	@Path("/3")
	public String getMostUnstableCurrencySinceDate(@QueryParam("year") String year, @QueryParam("month") String month,
			@QueryParam("day") String day) {

		if (isNotEmpty(year) && isNotEmpty(month) && isNotEmpty(day)) {
			String[] args = new String[] { "-3", year, month, day };
			return CommandLineParser.executeCommandLineTask(Parser.getInstance(args));
		}
		return new Scanner(ClientQuestion.class.getResourceAsStream("page3.html"), "UTF-8").useDelimiter("\\A").next();
	}

	@GET
	@Path("/4")
	public String cheapestCurrency(@QueryParam("year") String year, @QueryParam("month") String month,
			@QueryParam("day") String day) {

		if (isNotEmpty(year) && isNotEmpty(month) && isNotEmpty(day)) {
			String[] args = new String[] { "-4", year, month, day };
			return CommandLineParser.executeCommandLineTask(Parser.getInstance(args));

		}
		return new Scanner(ClientQuestion.class.getResourceAsStream("page4.html"), "UTF-8").useDelimiter("\\A").next();
	}

	@GET
	@Path("/5")
	public String sortCurrencySpread(@QueryParam("year") String year, @QueryParam("month") String month,
			@QueryParam("day") String day, @QueryParam("n") int number) {

		if (isNotEmpty(year) && isNotEmpty(month) && isNotEmpty(day) && number > 0) {
			String[] args = new String[] { "-5", year, month, day, Integer.toString(number) };
			return CommandLineParser.executeCommandLineTask(Parser.getInstance(args));

		}
		return new Scanner(ClientQuestion.class.getResourceAsStream("page5.html"), "UTF-8").useDelimiter("\\A").next();
	}

	@GET
	@Path("/6")
	public String extremaDates(@QueryParam("code") String code) {

		if (isNotEmpty(code)) {
			String[] args = new String[] { "-6", code };
			return CommandLineParser.executeCommandLineTask(Parser.getInstance(args));

		}
		return new Scanner(ClientQuestion.class.getResourceAsStream("page6.html"), "UTF-8").useDelimiter("\\A").next();
	}

	public boolean isNotEmpty(String string) {
		return (string != null) && (!string.isEmpty());
	}

}
