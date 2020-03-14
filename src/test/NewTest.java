package test;

import java.util.Calendar;

import org.junit.Test;

import model.CommandLineParser;
import model.Parser;

public class NewTest {

	@Test
	public void test() {
//		ClientQuestion clientQuestion = new ClientQuestion();
//		String f=clientQuestion.goldRate("20222", "222", "344");	

//		String args[]= {"0","2019","10","1099"};
//		CommandLineParser.executeCommandLineTask(new Parser(args));

		String args[] = { "-0", "2019", "10", "1099" };
		CommandLineParser.executeCommandLineTask(new Parser(args));
//		System.out.println(f);
	}

	@Test
	public void test1() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		System.out.println(year);

	}

}