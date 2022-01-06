package org.dice.alk.service;

import org.apache.http.client.utils.URIBuilder;
import org.dice.alk.exception.TagMeException;
import org.dice.alk.model.TagMeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TagMeService {

    private static final String PARAMETER_LANG = "lang";
    private static final String PARAMETER_API_KEY = "gcube-token";
    private static final String PARAMETER_TEXT = "text";
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TagMeService.class);

    @Value("${tagme.url.scheme}")
    private String scheme;
    @Value("${tagme.url.host}")
    private String host;
    @Value("${tagme.url.path}")
    private String path;
    @Value("${tagme.url.apiKey}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public TagMeResult tag(String text) {
        URIBuilder builder = new URIBuilder();

        try {
            builder.setScheme(this.scheme);
            builder.setHost(this.host);
            builder.setPath(this.path);
            builder.addParameter(PARAMETER_LANG, "en");
            builder.addParameter(PARAMETER_API_KEY, this.apiKey);
            builder.addParameter(PARAMETER_TEXT, text);

            LOGGER.info("rest call for '{}'", builder);

            TagMeResult result = this.restTemplate.getForObject(builder.build(), TagMeResult.class);
            result.pruneAnnotations();

            return result;
        } catch (Exception ex) {
            throw new TagMeException("unable to get response from " + builder, ex);
        }
    }
}
