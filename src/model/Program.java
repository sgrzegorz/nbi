package model;


/**
 * pobiera z Parsera sformatowane parametry i wywołuje odpowiednie metody
 *@param 
 *startdate "2017-10-10"
 *endate "2017-11-10" 
 *date "2017-10-10"
 *code - "USD" , "CHF"
 *number - liczba danych, które ma się wyświetlić na ekranie
 *ex - parametr funkcji programu (wprowadzany przez: -0 -1,-2,-3,-4,-5,-6,-7) jest on cyfrą od 0 do 7
 */
public class Program {
	public static void main(String []args) {
		Parser p= Parser.getInstance(args);
		
		String startdate=p.getStartdate();
		String enddate=p.getEnddate();
		int number=p.getNumber();
		String date=p.getDate();
		String code=p.getCode();
	
		 
		//System.out.println("[code: "+code+" date: "+date +" startdate: "+startdate+" enddate: "+enddate+" number: "+number+"]");
		try {
		if (p.getEx() =='0') {
			
			System.out.println(Exchange.goldRate(date));
			
		} else if (p.getEx() =='1') {
			System.out.println(Exchange.currencyRate(code,date));

		} else if (p.getEx() =='2') {
			System.out.println(Exchange.averageGoldRate(startdate,enddate));
			
		} else if (p.getEx() =='3') {
			Exchange.biggestAmplitudeChange(date);
			
		} else if (p.getEx() =='4') {
			System.out.println(Exchange.lowestCurrencyRate(date));
			 
		} else if (p.getEx() =='5') {
			
			Exchange.sortCurrencySpread(date, number);
		} else if (p.getEx() =='6') {
			
			Exchange.extremaDates(code);
		} else if (p.getEx() =='7') {
			Exchange.printChart(p.getCode(),p.getStartdate(),p.getEnddate());
			
		} else {
			System.out.println("Error while parsing, incorrect exercise number.");
		}
		}catch(Exception e) {
			System.out.println("Wystąpił nieoczekiwany problem. Program zamknie się");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
