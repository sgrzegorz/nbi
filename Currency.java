package nbi;

import java.util.HashMap;

/**
 * Obiekt odpowiadający każdej pojawiającej się pierwszy raz walucie w dokumencie xml. Jeśli dokumencie są:
 * "USD", "USD", "USD","CHF", "GBP" to powstaną trzy obiekty currency: code="USD", code="CHF", code="GBP"
 * "GOLD", "CHF" to powstaną dwa obiekty o code="CHF", code="GOLD"
 *  "GOLD" "GOLD" "GOLD" to powstanie jeden obiekt o code="GOLD" - złoto traktujemy jak każdą inną walutę 
 *  Każdy obiekt ma w sobie tablice haszującą <"YYYY-MM-DD",cenaWaluty> 
 *  Robiąc zadanie natrafiłem na kilka cenWaluty: "Mid", "Ask", "Bid" zamiast zrobić trzy osobne tablice haszujące
 *  postanowiłem modyfikować klucz tablicy haszujacej:
 *  <"YYYY-MM-DDMid",cenaWaluty> 
 *  <"YYYY-MM-DDAsk",cenaWaluty> 
 *  <"YYYY-MM-DDBid",cenaWaluty> 
 *  
 *   
 */
public class Currency {
	private String code,name;
	private HashMap <String, String> map = new HashMap <String, String>();
    
	public HashMap<String, String> getMap() {
		return map;
	}

	public Currency(String code,String name) {
		this.code=code;
		this.name=name;
	}

	public void put(String key,String value) {
		map.put(key, value);
	}
	
	public String get(String key) {
		return map.get(key);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Currency other = (Currency) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	
}
