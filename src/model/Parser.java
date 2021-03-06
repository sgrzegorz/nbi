package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.IncorrectArgumentsException;

/**
 * parsuje argumenty linii komend :
 * 
 * -----> Wypisuje, dla podanego dnia, obowiązującą cenę złota: ("-0 {year}
 * {month} {day} ");
 * 
 * -----> Wypisuje, dla podanego dnia, obowiązującą cenę podanej waluty: ("-1
 * {code} {year} {month} {day}");
 * 
 * -----> Średnia cena złota za podany okres:" -2 {year1} {month1} {day1}
 * {year2} {month2} {day2}
 * 
 * -----> Waluta, której kurs, począwszy od podanego dnia, uległ największym
 * wahaniom: -3 {year} {month} {day}
 * 
 * -----> Waluta, której kurs kupna był najmniejszy w podanym dniu:" -4 {year}
 * {month} {day}"
 * 
 * -----> Wypisuje N walut, posortowanych (rosnąco) względem różnicy pomiędzy
 * ceną sprzedaży a ceną kupna, w podanym dniu:" ("-5 {year} {month} {day} {N}
 * ");
 * 
 * -----> Dla podanej waluty wypisuje informację kiedy dana waluta była
 * najtańsza, a kiedy najdroższa: -6 {code}
 * 
 * -----> Wykres zmian ceny podanej waluty w układzie tygodniowym:" -7 {code}
 * {year1} {month1} {week1} {year2} {month2} {week2}
 * 
 * 
 *
 *
 * Zapisuje tak sparsowane dane do wartości: number -ile linijek zadania 5 ma
 * sie wyświetlić date -"YYYY-MM-DD" code -kod waluty "USD" "CHF" lub "GOLD"
 * startdate "YYYY-MM-DD" -poczatek okresu w metodach przesuwamy tak początek i
 * koniec okresu żeby nie był sobotą lub niedzielą enddate "YYYY-MM-DD" - koniec
 * okresu Dane te są pobierane przez metodę main(args[]) w klasie Program.
 * 
 */
public class Parser {
	private char ex = 'z';
	private int number = -1;
	private String date = null;
	private String code = null;
	private String startdate = null;
	private String enddate = null;

	private int currentYear = Calendar.getInstance().get(Calendar.YEAR);

	public Parser(String[] args) {

			// manage and remove "-1" , "-2", "-3"
			if (args[0].length() != 2)
				throw new IncorrectArgumentsException("Incorrect input");
			if (args[0].charAt(0) != '-')
				throw new IncorrectArgumentsException("Incorrect input");
			this.ex = args[0].charAt(1);

			String t[] = new String[args.length - 1];
			for (int i = 1; i < args.length; i++) {
				t[i - 1] = args[i];
			}

			String arguments = String.join(" ", t);

			if (ex == '0') {// {year} {month} {day}

				goldRate0(arguments);
			} else if (ex == '1') {// {code} {year} {month} {day}
				currencyRate1(arguments);

			} else if (ex == '2') {// {year1} {month1} {day1} {year2} {month2} {day2}

				averageGoldRate2(arguments);

			} else if (ex == '3' || ex == '4') {// {year} {month} {day}
				parseYMD(arguments);

			} else if (ex == '5') {// {year} {month} {day} {N}
				sortCurrencySpread5(arguments);

			} else if (ex == '6') {// {code}
				extremaDates6(arguments);

			} else if (ex == '7') {// {code} {year1} {month1} {week1} {year2} {month2} {week2}
				printChart7(arguments);

			} else {
				throw new IncorrectArgumentsException("Incorrenct arguments");
			}

	}

	private void goldRate0(String arguments) {

		String tmp = "[\\s,]*([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]*";
		Pattern pattern = Pattern.compile(tmp);
		Matcher matcher = pattern.matcher(arguments);

		if (matcher.matches()) {

			int iyear = Integer.parseInt(matcher.group(1));
			int imonth = Integer.parseInt(matcher.group(2));
			int iday = Integer.parseInt(matcher.group(3));
			this.code = "GOLD";
			if (iyear > currentYear || iyear < 2013)
				throw new IncorrectArgumentsException("Date out of range");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, iday);
			cal.set(Calendar.MONTH, imonth - 1);
			cal.set(Calendar.YEAR, iyear);
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
				throw new IncorrectArgumentsException("You selected: SUNDAY or SATURDAY. <no data evailable>");

			this.date = sdf.format(cal.getTime());
		} else {
			throw new IncorrectArgumentsException("Incorrenct arguments");
		}
	}

	private void currencyRate1(String arguments) {
		String tmp = "[\\s,]*([^\\s,]+)[\\s,]*([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]*";
		Pattern pattern = Pattern.compile(tmp);
		Matcher matcher = pattern.matcher(arguments);

		if (matcher.matches()) {

			String code = matcher.group(1).toUpperCase();
			manageCode(code);

			int iyear = Integer.parseInt(matcher.group(2));
			int imonth = Integer.parseInt(matcher.group(3));
			int iday = Integer.parseInt(matcher.group(4));

			if ((iyear > currentYear || iyear < 2002))
				throw new IncorrectArgumentsException("Date out of range");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, iday);
			cal.set(Calendar.MONTH, imonth - 1);
			cal.set(Calendar.YEAR, iyear);
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
				throw new IncorrectArgumentsException("You selected: SUNDAY or SATURDAY. <no data evailable>");

			this.date = sdf.format(cal.getTime());
		} else {
			throw new IncorrectArgumentsException("Incorrenct arguments");
		}
	}

	private void averageGoldRate2(String arguments) {
		String tmp = "[\\s,]*([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]*";
		Pattern pattern = Pattern.compile(tmp);
		Matcher matcher = pattern.matcher(arguments);

		if (matcher.matches()) {

			this.code = "GOLD";
			int iyear1 = Integer.parseInt(matcher.group(1));
			int imonth1 = Integer.parseInt(matcher.group(2));
			int iday1 = Integer.parseInt(matcher.group(3));
			int iyear2 = Integer.parseInt(matcher.group(4));
			int imonth2 = Integer.parseInt(matcher.group(5));
			int iday2 = Integer.parseInt(matcher.group(6));

			// warunki zadania
			if (this.code.equals("GOLD") && (iyear1 > currentYear || iyear1 < 2013))
				throw new IncorrectArgumentsException("Date out of range");
			if (this.code.equals("GOLD") && (iyear2 > currentYear || iyear2 < 2013))
				throw new IncorrectArgumentsException("Date out of range");
			if ((iyear1 > currentYear || iyear1 < 2002))
				throw new IncorrectArgumentsException("Date out of range");
			if (iyear2 > currentYear || iyear2 < 2002)
				throw new IncorrectArgumentsException("Date out of range");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, iday1);
			cal.set(Calendar.MONTH, imonth1 - 1); // month is 0 based
			cal.set(Calendar.YEAR, iyear1);
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
				cal.set(Calendar.DAY_OF_MONTH, iday1 + 1);
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
				cal.set(Calendar.DAY_OF_MONTH, iday1 + 2);

			Calendar cal1 = Calendar.getInstance();
			cal1.set(Calendar.DAY_OF_MONTH, iday2);
			cal1.set(Calendar.MONTH, imonth2 - 1);
			cal1.set(Calendar.YEAR, iyear2);
			if (cal1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
				cal1.set(Calendar.DAY_OF_MONTH, iday1 - 2);
			if (cal1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
				cal1.set(Calendar.DAY_OF_MONTH, iday1 - 1);

			this.startdate = sdf.format(cal.getTime());
			this.enddate = sdf.format(cal1.getTime());
		} else {
			throw new IncorrectArgumentsException("Incorrenct arguments");
		}
	}


	private void parseYMD(String arguments) {
		String tmp = "[\\s,]*([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]*";
		Pattern pattern = Pattern.compile(tmp);
		Matcher matcher = pattern.matcher(arguments);

		if (matcher.matches()) {

			int iyear = Integer.parseInt(matcher.group(1));
			int imonth = Integer.parseInt(matcher.group(2));
			int iday = Integer.parseInt(matcher.group(3));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, iday);
			cal.set(Calendar.MONTH, imonth - 1);
			cal.set(Calendar.YEAR, iyear);
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
				throw new IncorrectArgumentsException("You selected: SUNDAY or SATURDAY. <no data evailable>");
			this.date = sdf.format(cal.getTime());

		} else {
			throw new IncorrectArgumentsException("Incorrenct arguments");
		}
	}

	private void sortCurrencySpread5(String arguments) {

		String tmp = "[\\s,]*([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]*";
		Pattern pattern = Pattern.compile(tmp);
		Matcher matcher = pattern.matcher(arguments);

		if (matcher.matches()) {

			int iyear = Integer.parseInt(matcher.group(1));
			int imonth = Integer.parseInt(matcher.group(2));
			int iday = Integer.parseInt(matcher.group(3));
			this.number = Integer.parseInt(matcher.group(4));

			if ((iyear > currentYear || iyear < 2002))
				throw new IncorrectArgumentsException("Date out of range");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, iday);
			cal.set(Calendar.MONTH, imonth - 1);
			cal.set(Calendar.YEAR, iyear);
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
				throw new IncorrectArgumentsException("You selected: SUNDAY or SATURDAY. <no data evailable>");
			this.date = sdf.format(cal.getTime());

		} else {
			throw new IncorrectArgumentsException("Incorrenct arguments");
		}
	}

	private void extremaDates6(String arguments) {

		String tmp = "[\\s,]*([^\\s,]+)[\\s,]*";
		Pattern pattern = Pattern.compile(tmp);
		Matcher matcher = pattern.matcher(arguments);

		if (matcher.matches()) {

			String code = (matcher.group(1));

			manageCode(code);
		} else {
			throw new IncorrectArgumentsException("Incorrenct arguments");
		}
	}

	private void printChart7(String arguments) {
		String tmp = "[\\s,]*([^\\s,]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]+([\\d]+)[\\s,]*";
		Pattern pattern = Pattern.compile(tmp);
		Matcher matcher = pattern.matcher(arguments);

		if (matcher.matches()) {
			String code = matcher.group(1);
			manageCode(code);
			int iyear1 = Integer.parseInt(matcher.group(2));
			int imonth1 = Integer.parseInt(matcher.group(3));
			int iweek1 = Integer.parseInt(matcher.group(4));
			int iyear2 = Integer.parseInt(matcher.group(5));
			int imonth2 = Integer.parseInt(matcher.group(6));
			int iweek2 = Integer.parseInt(matcher.group(7));

			// warunki zadania
			if ((iyear1 > currentYear || iyear1 < 2002))
				throw new IncorrectArgumentsException("Date out of range");
			if (iyear2 > currentYear || iyear2 < 2002)
				throw new IncorrectArgumentsException("Date out of range");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cal = Calendar.getInstance();
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			cal.setMinimalDaysInFirstWeek(1);
			cal.set(Calendar.YEAR, iyear1);
			cal.set(Calendar.MONTH, imonth1 - 1); // month is 0 based
			cal.set(Calendar.WEEK_OF_MONTH, iweek1);

			Calendar cal1 = Calendar.getInstance();
			cal1.setMinimalDaysInFirstWeek(1);
			cal1.setFirstDayOfWeek(Calendar.MONDAY);
			cal1.set(Calendar.WEEK_OF_MONTH, iweek2);
			cal1.set(Calendar.MONTH, imonth2 - 1);
			cal1.set(Calendar.YEAR, iyear2);

			if (cal.compareTo(cal1) > 0) {
				Calendar tmp1 = cal1;
				cal1 = cal;
				cal = tmp1;
			}

			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			cal1.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

			this.startdate = sdf.format(cal.getTime());
			this.enddate = sdf.format(cal1.getTime());
		} else {
			throw new IncorrectArgumentsException("Incorrenct arguments");
		}
	}



	private void manageCode(String code) {
		code = code.toUpperCase();
		final String[] CODES = { "THB", "USD", "AUD", "HKD", "CAD", "NZD", "SGD", "EUR", "HUF", "CHF", "GBP", "UAH",
				"JPY", "CZK", "DKK", "ISK", "NOK", "SEK", "HRK", "RON", "BGN", "TRY", "ILS", "CLP", "PHP", "MXN", "ZAR",
				"BRL", "MYR", "RUB", "IDR", "INR", "KRW", "CNY", "XDR" };
		boolean flag = true;
		for (String s : CODES) {
			if (s.equals(code))
				flag = false;
		}
		if (flag)
			throw new IncorrectArgumentsException("Innapropriate code");
		this.code = code;

	}

	public int getNumber() {
		return number;
	}

	public String getDate() {
		return date;
	}

	public String getCode() {
		return code;
	}

	public String getStartdate() {
		return startdate;
	}

	public String getEnddate() {
		return enddate;
	}
	
	public char getEx() {
		return ex;
	}

}
