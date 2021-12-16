package org.dice.alk.nlp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for parsing and processing the input string.
 */
@Component
public class InputProcessor implements ITextProcessor {

	/**
	 * API endpoint
	 */
	private final static String TAGME_ENDPOINT = "https://wat.d4science.org/wat/tag/tag";
	
	/**
	 * English wikipedia articles path
	 */
	private final static String WIKIPEDIA_EP = "https://en.wikipedia.org/wiki/";

	/**
	 * Language identifier for query responses
	 */
	private final static String LANG = "en";

	/**
	 * Authorization tokens
	 */
	private final String TOKEN;

	/**
	 * Reusable http client
	 */
	private final CloseableHttpClient httpClient;

	/**
	 * Arbitrary distance defines two mentions that belong to the same entity
	 */
	private final int arbitraryDistance = 3;

	/**
	 * Constructor.
	 */
	 @Autowired
	public InputProcessor(String token) {
		this.TOKEN = token;
		httpClient = HttpClients.createDefault();
	}

	@Override
	public Set<String> processTextInput(String input) {

		// get result from API and parse it
		JSONObject result = getAnnotatorResult(input);
		JSONArray jsonArray = result.getJSONArray("annotations");
		Set<String> paths = new HashSet<>();

		
		// resolve multiple mentions for one entity
		// if under arbitraryDistance, it's the same entity
//		int oldStart = -1;
//		int oldEnd = -1;
		for (int i = 0; i < jsonArray.length(); i++) {
			String relativePath = jsonArray.getJSONObject(i).getString("title");
			int startMention = jsonArray.getJSONObject(i).getInt("start");
			int endMention = jsonArray.getJSONObject(i).getInt("end");

//			if (startMention < oldEnd + arbitraryDistance) {
//					
//			}
			paths.add(WIKIPEDIA_EP+relativePath);

//			oldStart = startMention;
//			oldEnd = endMention;
		}
		return paths;
	}

	public static void main(String[] args) {
//    processTextInput2("Oak Hill, West Virginia is Hank Williams' last place.");
	}

	/**
	 * Retrieves the annotated entities from WAT/TAGME API
	 * 
	 * @param text
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ParseException
	 */
	private JSONObject getAnnotatorResult(String text) {

		// attach parameters to request
		URIBuilder builder = null;
		try {
			builder = new URIBuilder(TAGME_ENDPOINT);
			builder.setParameter("gcube-token", TOKEN);
			builder.setParameter("lang", LANG);
			builder.setParameter("text", text);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		// create request
		HttpGet get = null;
		try {
			get = new HttpGet(builder.build());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		// run and retrieve response if existing, otherwise null
		JSONObject jsonResponse = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get);) {
			String result = EntityUtils.toString(response.getEntity());
			jsonResponse = new JSONObject(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}

}
