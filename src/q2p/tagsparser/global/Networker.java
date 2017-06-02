package q2p.tagsparser.global;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;

public class Networker {
	private static final Proxy torProxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9150));
	public static final String receive(final String url, final boolean useTor) throws IOException {
		return receive(url, useTor, null);
	}
	
	public static final String receive(final String url, final boolean useTor, final LinkedList<String> headers) throws IOException {
		final URLConnection connection;
		if(useTor)
			connection = new URL(url).openConnection(torProxy);
		else
			connection = new URL(url).openConnection();
		
		if(headers != null) {
			Iterator<String> i = headers.iterator();
			while(i.hasNext())
				connection.setRequestProperty(i.next(), i.next());
		}
		
		connection.connect();
		final StringBuilder builder = new StringBuilder();
		final BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String response;
		while((response = br.readLine()) != null)
			builder.append(response);
		
		return builder.toString();
	}
}