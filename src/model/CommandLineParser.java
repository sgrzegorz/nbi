package model;

import exception.IncorrectArgumentsException;

/**
 * pobiera z Parsera sformatowane parametry i wywołuje odpowiednie metody
 * 
 * @param startdate "2017-10-10" endate "2017-11-10" date "2017-10-10" code -
 *                  "USD" , "CHF" number - liczba danych, które ma się
 *                  wyświetlić na ekranie ex - parametr funkcji programu
 *                  (wprowadzany przez: -0 -1,-2,-3,-4,-5,-6,-7) jest on cyfrą
 *                  od 0 do 7
 */
public class CommandLineParser {

	public static String executeCommandLineTask(Parser p) {
		String startdate = p.getStartdate();
		String enddate = p.getEnddate();
		int number = p.getNumber();
		String date = p.getDate();
		String code = p.getCode();

		String result = "error_occured";

		if (p.getEx() == '0') {

			result = Exchange.goldRate(date);

		} else if (p.getEx() == '1') {
			result = Exchange.currencyRate(code, date);

		} else if (p.getEx() == '2') {
			result = Exchange.averageGoldRate(startdate, enddate);

		} else if (p.getEx() == '3') {
			result = Exchange.getMostUnstableCurrencySinceDate(date);

		} else if (p.getEx() == '4') {
			result = Exchange.cheapestCurrency(date);

		} else if (p.getEx() == '5') {

			result = Exchange.sortCurrencySpread(date, number);
		} else if (p.getEx() == '6') {

			result = Exchange.extremaDates(code);
		} else if (p.getEx() == '7') {
			Exchange.printChart(p.getCode(), p.getStartdate(), p.getEnddate());
			result = "chart was printed";
		} else {
			throw new IncorrectArgumentsException("Error while parsing, incorrect exercise number.");
		}

		return result;
	}

	public static void main(String[] args) {
		try {
			Parser p = new Parser(args);
			System.out.println(executeCommandLineTask(p));

		}catch(IncorrectArgumentsException e) {
			printCommandLineInformation();
		}

	}
	
	
	private static void printCommandLineInformation() {
		System.out.println("------------------------------------------------------------");
		System.out.println("Correct ways:");

		System.out.println("----->   Wypisuje, dla podanego dnia, obowiązującą cenę złota:");
		System.out.println("-0 {year} {month} {day}  ");

		System.out.println("----->   Wypisuje, dla podanego dnia, obowiązującą cenę podanej waluty:");
		System.out.println("-1 {code} {year} {month} {day}");

		System.out.println("----->   Średnia cena złota za podany okres:");
		System.out.println("-2 {year1} {month1} {day1} {year2} {month2} {day2}");

		System.out.println(
				"----->   Waluta, której kurs, począwszy od podanego dnia, był najmniej stabilny: (maxPrice-minPrice)/maxPrice w danym okresie");
		System.out.println("-3 {year} {month} {day}");

		System.out.println("----->   Waluta, której kurs kupna był najmniejszy w podanym dniu:");
		System.out.println("-4 {year} {month} {day}");

		System.out.println(
				"----->   Wypisuje N walut, posortowanych (rosnąco) względem różnicy pomiędzy ceną sprzedaży a ceną kupna, w podanym dniu:");
		System.out.println("-5 {year} {month} {day} {N} ");

		System.out.println(
				"----->   Dla podanej waluty wypisuje informację kiedy dana waluta była najtańsza, a kiedy najdroższa:");
		System.out.println("-6 {code}");

		System.out.println("----->   Wykres zmian ceny podanej waluty w układzie tygodniowym:");
		System.out.println("-7 {code} {year1} {month1} {week1} {year2} {month2} {week2} ");

	}
}
