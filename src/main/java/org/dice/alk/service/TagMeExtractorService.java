package org.dice.alk.service;

import org.apache.http.client.utils.URIBuilder;
import org.dice.alk.exception.TagMeException;
import org.dice.alk.model.Entity;
import org.dice.alk.model.TagMeResult;
import org.dice.alk.model.TagMeSpot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

@Service
public class TagMeExtractorService {

    private static final String PARAMETER_LANG = "lang";
    private static final String PARAMETER_API_KEY = "gcube-token";
    private static final String PARAMETER_TEXT = "text";

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TagMeExtractorService.class);

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

    @Async
    public Future<Set<Entity>> extractAsync(String text) {
        TagMeResult result = this.extract(text);

        Set<Entity> entities = new HashSet<>();
        for (TagMeSpot spot : result.getAnnotations()) {
            Entity entity = new Entity();
            entity.setWikipediaTitle(spot.getTitle());
            entity.setText(spot.getSpot());
            entities.add(entity);
        }

        return new AsyncResult<>(entities);
    }

    public TagMeResult extract(String text) {
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

            HashSet<TagMeSpot> filtered = new HashSet<>();
            for (TagMeSpot spot : result.getAnnotations()) {
                if (null != spot.getTitle()) {
                    spot.setTitle(spot.getTitle().replace(' ', '_'));
                    filtered.add(spot);
                }
            }

            result.setAnnotations(filtered);
            return result;
        } catch (Exception ex) {
            throw new TagMeException("unable to get response from " + builder, ex);
        }
    }
}