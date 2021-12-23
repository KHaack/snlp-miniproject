package org.dice.alk.service;

import org.dice.alk.model.TagMeResult;
import org.dice.alk.model.TagMeSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is responsible for parsing and processing the input string.
 */
@Service
public class InputProcessorService {

    /**
     * Arbitrary distance defines two mentions that belong to the same entity
     */
    private final int arbitraryDistance = 3;
    @Value("${wikipedia.endpoint}")
    private String wikipediaEndpoint;
    @Autowired
    private TagMeService tagMeService;

    public Set<String> processTextInput(String input) {

        // get result from API and parse it
        TagMeResult result = this.tagMeService.tag(input);

        // resolve multiple mentions for one entity
        // if under arbitraryDistance, it's the same entity
//		int oldStart = -1;
//		int oldEnd = -1;

        Set<String> paths = new HashSet<>();
        for (TagMeSpot spot : result.getAnnotations()) {
            String relativePath = spot.getTitle();
            int startMention = spot.getStart();
            int endMention = spot.getEnd();

//			if (startMention < oldEnd + arbitraryDistance) {
//					
//			}
            paths.add(this.wikipediaEndpoint + relativePath);

//			oldStart = startMention;
//			oldEnd = endMention;
        }

        return paths;
    }
}
