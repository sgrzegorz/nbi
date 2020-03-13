package resource;

import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


@Path("/question")
public class ClientQuestion {

	@GET
	@Path("/1")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_HTML)
	public String getMessages(@QueryParam("year") int year, @QueryParam("month") int month,@QueryParam("day") int day) {
		System.out.println("hejj");
		if(year >0 && month>0 &&day>0) {
			return " "+year+month+day;
//			return "Hejjl";
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
	
	
	
	public String getDataFromNbi()) {
		
		
		//String text= new Scanner(ClientQuestion.class.getResourceAsStream("selection.html"), "UTF-8").useDelimiter("\\A").next();
		return text;
	}
	
	
	
}
