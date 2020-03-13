package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Giełda implementuje singleton, zawiera arraylistę "list" w której obiektami są obiekty typu Currency
 * UWAGA złoto tez jest typu Currency z tym, że jako kod uznajemy "GOLD"
 * Zawiera zestaw metod implementujące zadania 1 do 7 (wszystkie)
 */ 
public class Exchange {
	private static List <Currency> list=new ArrayList <Currency>();
	private static final double MAX = 999999999;
	private static final double MIN = -999999999;
	private static Exchange firstInstance=null;
	private Exchange() {}
	public static Exchange getInstance() {
		if(firstInstance==null) {
			firstInstance=new Exchange();
		}
		return firstInstance;
	}
	
	/**
	 * dodaj nową Currency do listy
	 *
	 */
	public static boolean addCurrency(Currency c) {
		
		
		for(Currency currency: list) {
			if((currency).equals(c)) return false;
				
		}
		
		list.add(c);
		return true;
		
	}
	/**
	 * @param code "USD"
	 * @return obiekt Currency o kodzie "USD"
	 */
	public static Currency getCurrency(String code) {
		for(Currency c : list) {
		
			if(c.getCode().equals(code)) {
				
				return c;
			}
		}
		return null;
	}
	////////////////////////////////////////////////////////////////////////////ZESTAW METOD///////////////////////////////////////////////////////
	

	 
	
	/** Wypisuje, dla podanego dnia, obowiązującą cenę złota oraz cenę podanej waluty (tabela A) 
	 * @param date
	 * @return w
	 */
	public static String goldRate(String date) {
		System.out.println(date);
		list.clear();
		XMLReader.parse("http://api.nbp.pl/api/cenyzlota/"+date+"/?format=xml");	
	    Currency c=Exchange.getCurrency("GOLD");
	    
	    String tmp=c.get(date);
	    
	    return tmp;
		
	}

	/**
	 *  Wypisuje, dla podanego dnia,obowiązującą cenę podanej waluty (tabela A) 
	 */
	public static String currencyRate(String code, String date) {
		list.clear();
		XMLReader.parse("http://api.nbp.pl/api/exchangerates/rates/a/"+code+"/"+date+"/?format=xml");
		Currency c=Exchange.getCurrency(code);
		
		String tmp=c.get(date+"Mid");
		
		return tmp;
	}
	
	 
	
	/**Oblicza średnią cenę złota za podany okres
	 * @param startdate "YYYY-MM-DD"
	 * @param enddate
	 * @return
	 */
	public static String averageGoldRate(String startdate,String enddate)
	{
		list.clear();
		double average=0;
		int ile=0;
		XMLReader.parse("http://api.nbp.pl/api/cenyzlota/"+startdate+"/"+enddate+"/?format=xml");	
		for (String value : getCurrency("GOLD").getMap().values()) {
		    average+=Double.parseDouble(value);
		    ile++;
		}
		
		
		return Double.toString(average/ile);
	}
	
	
	
//	
	/**Odszukuje walutę (tabela A), której kurs, począwszy od podanego dnia, 
	 * uległ największym wahaniom (max-min)/max (waluta, której różnica między najmniejszą i najwyższą ceną w danym okresie jest największa) 
	 * @param startdate - data począwszy od której liczymy kurs
	 */
	public static String getMostUnstableCurrencySinceDate(String startdate) {
		list.clear();
		//download data for all currencies from statdate to today
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.YEAR,Integer.parseInt(startdate.substring(0,4)));
    	cal.set(Calendar.MONTH,Integer.parseInt(startdate.substring(5,7)) -1); 
    	cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startdate.substring(8)));
    	Calendar today =  Calendar.getInstance();
		while(!(today.get(Calendar.YEAR)==cal.get(Calendar.YEAR) && today.get(Calendar.MONTH)==cal.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH)==cal.get(Calendar.DAY_OF_MONTH))) {
	    	
	    	if(cal.get(Calendar.DAY_OF_WEEK) !=Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) !=Calendar.SUNDAY) {
	    		String date=sdf.format(cal.getTime());
	    		XMLReader.parse("http://api.nbp.pl/api/exchangerates/tables/a/"+date+"/?format=xml");
	    		//System.out.println(sdf.format(cal.getTime()));
	    	}		
	    	cal.add(Calendar.DAY_OF_YEAR, 1);
		}

		
		//The map is sorted according to the natural ordering of its keys
		TreeMap <Double,String> tree=new TreeMap <>();
		
		for(Currency c : list) {
			double min=MAX;
			double max=MIN;
			for(String value :c.getMap().values()) {
				double val =Double.parseDouble(value);
			    if(val>max) max=val;
			    if(val<min) min=val;
			}
			double stability= (max-min)/max;
			tree.put(stability,c.getCode()) ;
		}
		
		return getCurrency(tree.lastEntry().getValue()).getName();
	}
	
	
	
	
	
	/**Odszukuje walutę (tabela C), której kurs kupna był najmniejszy w podanym dniu 
	 * @param date "YYYY-MM-DD"
	 * @return
	 */
	public static String lowestCurrencyRate(String date) {
		list.clear();
		XMLReader.parse("http://api.nbp.pl/api/exchangerates/tables/c/"+date+"/?format=xml");	
		double lowest=1000000000;
		String lowestCode="";
		for(Currency c:list) {

			double rate =Double.parseDouble(c.get(date+"Ask"));
			//System.out.println(rate+c.getCode());
			if(rate<lowest) {
				lowest=rate;
				lowestCode=c.getCode();
			}
		}
		return getCurrency(lowestCode).getName();
	}
	

	
	/**
	 * Wypisuje N walut (tabela C), posortowanych (rosnąco) względem różnicy pomiędzy ceną sprzedaży a ceną kupna, w podanym dniu 
	 * @param date - podany dzień "YYYY-MM-DD"
	 * @param N    - ile ma wypisać
	 */
	public static String sortCurrencySpread(String date,int N) {
		list.clear();
		XMLReader.parse("http://api.nbp.pl/api/exchangerates/tables/c/"+date+"/?format=xml");
		
		class Tmp implements Comparable<Tmp> {
			private Currency c;
			private double d;
			Tmp(Double d,Currency c){
				this.d=(double) d;
				this.c=c;
			}
			public Currency getCurrency() {
				return c;
			}
			public Double getSpread() {
				return d;
			}
			
		    public int compareTo(Tmp y){        
		        return Double.compare(this.d, y.getSpread());
		    }
			
		}
		
		List <Tmp> t  = new ArrayList <Tmp>();
		for(Currency c : list) {
			double bid=Double.parseDouble(c.get(date+"Bid"));
			double ask=Double.parseDouble(c.get(date+"Ask"));
			double spread=ask-bid;
			t.add(new Tmp(spread,c));
		}
		Collections.sort(t);

		String sortedCurriencies =date+ " List of currencies sorted by spread"+"\n";
//		System.out.println(date+ " List of currencies sorted by spread");
		int ile=0;
		for(Tmp e: t) {
			if(ile==N) break;
			sortedCurriencies+=e.getCurrency().getName()+" "+e.getSpread()+"\n";
//			System.out.println(e.getCurrency().getName()+" "+e.getSpread());
			ile++;
		}
		
		return sortedCurriencies;
	}
	 
	
	/**Dla podanej waluty (tabela A) wypisuje informację kiedy dana waluta była najtańsza, a kiedy najdroższa 
	 * @param code 
	 */
	public static String extremaDates(String code) {
		list.clear();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR)-1);
		
		while(today.get(Calendar.DAY_OF_WEEK) ==Calendar.SATURDAY || today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
    		today.set(Calendar.DAY_OF_YEAR,today.get(Calendar.DAY_OF_YEAR)-1);
    		
    	}
		
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.DAY_OF_MONTH, 02);   
    	cal.set(Calendar.MONTH, 1-1);   
    	cal.set(Calendar.YEAR,2002);
    	String startdate=sdf.format(cal.getTime());
		while(today.get(Calendar.YEAR)> cal.get(Calendar.YEAR)) {
			
			while(cal.get(Calendar.DAY_OF_WEEK) ==Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
	    		cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)-1);
	    	}
			String enddate=sdf.format(cal.getTime());
			XMLReader.parse("http://api.nbp.pl/api/exchangerates/rates/a/"+code+"/"+startdate+"/"+enddate+"/?format=xml");
			startdate=enddate;
			cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)+1);
		}
		
			String enddate=sdf.format(today.getTime());
			XMLReader.parse("http://api.nbp.pl/api/exchangerates/rates/a/"+code+"/"+startdate+"/"+enddate+"/?format=xml");
		
		
		Currency c=getCurrency(code.toUpperCase());
	     
		double min=MAX;
		double max=MIN;
		String minKey="";
		String maxKey="";
		for (String key : c.getMap().keySet()) {		
		    double value=Double.parseDouble(c.get(key));
		    
		    if(value>max) {
		    	max=value;
		    	maxKey=key;
		    }
		    if(value<min) {
		    	min=value;
		    	minKey=key;
		    }
		}
		minKey=minKey.substring(0, minKey.length()-3);
		maxKey=maxKey.substring(0, maxKey.length()-3);
		
		
		String returnString = "MAX: "+max+" "+maxKey+"\n"+
							"MIN: "+min+" "+minKey;
							
		return returnString;
		 
	}
	

	 
	
	/**
	 * Rysuje (w trybie tekstowym) wspólny (dla wszystkich tygodni) wykres zmian ceny 
	 * (np. wykres słupkowy, za pomocą różnorodnych znaków ASCII) podanej waluty (tabela A) w układzie tygodniowym, 
	 * tzn. jaka była cena w poniedziałki, wtorki, itd. w pierwszym tygodniu, drugim tygodniu, ... 
	 * 
	 * @param code - symbol "USD" itp. z dużej litery 
	 * @param startdate - początek okresu "2017-10-10" nie moze być sobotą albo niedziela
	 * @param enddate -koniec okresu
	 */
	public static void printChart(String code,String startdate,String enddate) {
		list.clear();
		XMLReader.setKeys(new ArrayList<String>());
		XMLReader.parse("http://api.nbp.pl/api/exchangerates/rates/a/"+code+"/"+startdate+"/"+enddate+"/?format=xml");
		
		Currency c=getCurrency(code.toUpperCase());
		
	    //Get minimal and maximal values in range 
		double min=MAX;
		double max=MIN;

		for (String key : c.getMap().keySet()) {		
		    double value=Double.parseDouble(c.get(key));
		    
		    if(value>max) {
		    	max=value;
		    }
		    if(value<min) {
		    	min=value;
		    }
		}
		
		double N=30;
		double STEP=(max-min)/N;
		
		List <String> keys= new ArrayList <String>(XMLReader.getKeys());
		
		
		String []week= {"Pon","Wt ","Sr ","Czw","Pt "};
		for(int weekday=0;weekday<5;weekday++){
			int ile=0;
			for(int i=0;i<keys.size();i++){
				if(i%5==weekday) {
					ile++;
					System.out.print(week[weekday]+ile+" ");
					double value=Double.parseDouble(c.get(keys.get(i)));
					if(ile<=9) System.out.print(" ");
					for(int j=0;j<N;j++) {
						if(value>=min+STEP*j) System.out.print("\u23f9");
					}
					System.out.print(value+" zl");
					System.out.println();
				}
				
			}
			System.out.println();
		}
		
	}
	
	
	
}
