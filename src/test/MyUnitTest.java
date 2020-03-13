package test;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.Exchange;

public class MyUnitTest {

    @Test
    public void test0() {	
        assertEquals("190.85", Exchange.goldRate("2019-10-10"));
    }
    
    @Test
    public void test1() {	
    

        assertEquals("3.9226", Exchange.currencyRate("USD","2019-10-10"));
    }
    
    @Test
    public void test2() {	
  
        assertEquals("185.38619047619045", Exchange.averageGoldRate("2019-10-10","2019-11-10"));
    }
    
    @Test
    public void test3() {	
   
        assertEquals("rubel rosyjski",Exchange.getMostUnstableCurrencySinceDate("2019-10-10"));
    }
    
    @Test
    public void test4() {	

        assertEquals("forint (Węgry)", Exchange.lowestCurrencyRate("2019-10-10"));
    }
    
    @Test
    public void test5() {	

		String result = "2019-10-10 List of currencies sorted by spread\n" + 
				"forint (Węgry) 2.580000000000013E-4\n" + 
				"jen (Japonia) 7.320000000000035E-4\n";
        assertEquals(result, Exchange.sortCurrencySpread("2019-10-10", 2));
    }
    
    @Test
    public void test6() {	

		String result = "MAX: 4.2628 2002-02-07\n" + 
				"MIN: 2.022 2008-07-21";
        assertEquals(result, Exchange.extremaDates("USD"));
    }
}