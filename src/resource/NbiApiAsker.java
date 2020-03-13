package resource;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class NbiApiAsker {
	public static void main(String[]args) {		
		Client client = ClientBuilder.newClient();
		String name = client.target("http://api.nbp.pl/api/cenyzlota/2019-10-10/?format=xml")
		        .request(MediaType.TEXT_PLAIN)
		        .get(String.class);
		
		
//		parse(name, DefaultHandler dh)

		
		System.out.println("NBIAPIaSKER	"+name);
		
				
		

	}
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://api.nbp.pl/api").build();
	}

}
