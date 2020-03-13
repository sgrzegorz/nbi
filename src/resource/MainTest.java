package resource;

import model.Exchange;
import model.Parser;

public class MainTest {
	public static void main(String[] arguments) {
		String [] args= new String[]{"-0","2019","10","10"};
		
		Parser p= Parser.getInstance(args);
		String startdate=p.getStartdate();
		String enddate=p.getEnddate();
		int number=p.getNumber();
		String date=p.getDate();
		String code=p.getCode();
		System.out.println();
		
		System.out.println(Exchange.goldRate(date));

	}
}
