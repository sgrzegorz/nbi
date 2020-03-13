package model;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX Parser format XML Pobiera BEZPOŚREDNIO ze strony nbi potrzebne dane NIE
 * zapisuje ich do pliku
 * 
 * w metodzie Startelement ustawiamy zmienną boolean bnazwaAkapitu =true
 * odpowiadającą <nazwaAkapitu > <\nazwaAkapitu> dzięki temu w metodzie
 * characters możemy dostać się do zawartości akapitu. Zapisujemy te dane do
 * pamięci. Po wykonaniu zadań na wartości akapitu ustawiamy
 * <bnazwaAkapitu=false
 *
 */
public class XMLReader extends DefaultHandler {

	private boolean bData = false;
	private boolean bCena = false;
	private boolean bCurrency = false;
	private boolean bCode = false;
	private boolean bEffectiveDate = false;
	private boolean bMid = false;
	private boolean bBid = false;
	private boolean bAsk = false;
	private static XMLReader firstInstance = null;

	private XMLReader() {
	}

	public static XMLReader getInstance() {
		if (firstInstance == null) {
			firstInstance = new XMLReader();
		}
		return firstInstance;
	}

	private String key, value, code, name;

	// Arraylista dat "YYYY-MM-DD" zachowują naturalną kolejność od najwcześniejszej
	// do najpóźniejszej
	private static ArrayList<String> keys = new ArrayList<String>();

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// GOLD
		if (qName.equalsIgnoreCase("ArrayOfCenaZlota")) {
			code = "GOLD";
			name = "zloto";

		} else if (qName.equalsIgnoreCase("Data")) {
			bData = true;
		} else if (qName.equalsIgnoreCase("Cena")) {
			;
			bCena = true;
		}
		// Currency
		else if (qName.equalsIgnoreCase("Currency")) {
			bCurrency = true;
		} else if (qName.equalsIgnoreCase("Code")) {
			bCode = true;
		} else if (qName.equalsIgnoreCase("EffectiveDate")) {
			bEffectiveDate = true;
		} else if (qName.equalsIgnoreCase("Mid")) {
			bMid = true;
		} else if (qName.equalsIgnoreCase("Bid")) {
			bBid = true;
		} else if (qName.equalsIgnoreCase("Ask")) {
			bAsk = true;
		}

	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

		// GOLD

		if (bData) {

			key = new String(ch, start, length);

			bData = false;

		} else if (bCena) {

			value = new String(ch, start, length);

			Currency c = Exchange.getCurrency(code);

			if (c == null) {
				c = new Currency(code, name);
				Exchange.addCurrency(c);
			}
			c.put(key, value);

			bCena = false;
		}

		// Currency
		else if (bCurrency) {
			name = new String(ch, start, length);
			bCurrency = false;
		} else if (bCode) {
			code = new String(ch, start, length);
			bCode = false;
		} else if (bEffectiveDate) {
			key = new String(ch, start, length);
			bEffectiveDate = false;
		} else if (bMid) {
			value = new String(ch, start, length);

			Currency c = Exchange.getCurrency(code);

			if (c == null) {

				c = new Currency(code, name);
				Exchange.addCurrency(c);
			}
			keys.add(key + "Mid");
			c.put(key + "Mid", value);

			bMid = false;
		} else if (bBid) {
			value = new String(ch, start, length);
			Currency c = Exchange.getCurrency(code);
			if (c == null) {
				c = new Currency(code, name);
				Exchange.addCurrency(c);
			}

			c.put(key + "Bid", value);

			bBid = false;
		} else if (bAsk) {
			value = new String(ch, start, length);

			Currency c = Exchange.getCurrency(code);

			if (c == null) {
				c = new Currency(code, name);
				Exchange.addCurrency(c);
			}
			c.put(key + "Ask", value);

			bAsk = false;
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

	}

	public void startDocument() throws SAXException {

	}

	public void endDocument() throws SAXException {
	}

	public static void parse(String urlString) {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		for (int i = 0; i < 2; i++) {
			try {
				SAXParser saxParser = factory.newSAXParser();

				Client client = ClientBuilder.newClient();
				String xmlString = client.target(urlString).request(MediaType.TEXT_PLAIN).get(String.class);
				xmlString = xmlString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();

				String fileName = "file.xml";
				System.out.println(xmlString);

				File file = new File(fileName);
				PrintWriter out = new PrintWriter(file);
				out.print(xmlString);
				out.close();

				XMLReader handler = XMLReader.getInstance();

				saxParser.parse(file, handler);

				break;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Natrafiono na problem. Jeżeli program nie wyświetli wyniku w ciągu minuty wyłącz go");
			}

		}

	}

	public static ArrayList<String> getKeys() {
		return keys;
	}

	public static void setKeys(ArrayList<String> keys) {
		XMLReader.keys = keys;
	}

}
