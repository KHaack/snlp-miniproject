package org.dice.alk.service;

import org.apache.http.client.utils.URIBuilder;
import org.dice.alk.exception.WordNetException;
import org.dice.alk.model.WordNetRequest;
import org.dice.alk.model.WordNetResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class WordNetService {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WordNetService.class);

    @Value("${wordnet.url.scheme}")
    private String scheme;
    @Value("${wordnet.url.host}")
    private String host;
    @Value("${wordnet.url.port}")
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    private URI buildURI(WordNetRequest request, String word) {
        URIBuilder builder = new URIBuilder();

        builder.setScheme(this.scheme);
        builder.setHost(this.host);
        builder.setPort(this.port);
        builder.setPath(request.getPath() + word);

        try {
            return builder.build();
        } catch (URISyntaxException ex) {
            throw new WordNetException("unable to build uri for " + request + " and " + word, ex);
        }
    }

    public WordNetResult[] get(WordNetRequest request, String word) {
        URI uri = buildURI(request, word);
        LOGGER.info("rest call for '{}'", uri.toString());

        try {
            WordNetResult[] result = this.restTemplate.getForObject(uri, WordNetResult[].class);
            return result;
        } catch (Exception ex) {
            throw new WordNetException("unable to get response from " + uri, ex);
        }
    }

}
