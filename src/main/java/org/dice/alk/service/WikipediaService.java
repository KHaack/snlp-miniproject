package org.dice.alk.service;

import org.dice.alk.exception.WikipediaException;
import org.dice.alk.model.Entity;
import org.dice.alk.model.WikipediaDocument;
import org.dice.alk.model.WikipediaParagraph;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WikipediaService {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WikipediaService.class);

    @Value("${wikipedia.endpoint}")
    private String wikipediaEndpoint;

    @Value("${wikipedia.timeout}")
    private int timeout;

    private Document getDocument(String url) throws IOException {
        LOGGER.info("fetch wikipedia document from " + url);
        return Jsoup.connect(url).timeout(this.timeout).get();
    }

    /**
     * Get the text content of a wikipedia page
     *
     * @param entity
     * @return
     */
    public WikipediaDocument fetch(Entity entity) {
        try {
            Document document = getDocument(this.wikipediaEndpoint + entity.getWikipediaTitle());
            WikipediaDocument wiki = new WikipediaDocument(entity);

            // infobox
            Elements elements = document.select(".infobox.vcard tr");
            WikipediaParagraph paragraph = new WikipediaParagraph();
            paragraph.setText("");
            for (Element element : elements) {
                Elements th = element.getElementsByTag("th");
                Elements td = element.getElementsByTag("td");

                if (!th.isEmpty() && !td.isEmpty()) {
                    paragraph.setText(paragraph.getText() + th.text() + " " + td.text() + ". ");
                }

                // links
                Elements as = element.select("a");
                for (Element a : as) {
                    String link = a.attr("href");
                    if (null != link && link.contains("/wiki/")) {
                        paragraph.getUrls().add(link.replace("/wiki/", ""));
                    }
                }
            }
            wiki.getParagraphs().add(paragraph);

            // paragraphs
            elements = document.select("#bodyContent .mw-parser-output p");
            for (Element element : elements) {
                paragraph = new WikipediaParagraph();
                paragraph.setText(element.text());

                // links
                Elements as = element.select("a");
                for (Element a : as) {
                    String link = a.attr("href");
                    if (null != link && link.contains("/wiki/")) {
                        paragraph.getUrls().add(link.replace("/wiki/", ""));
                    }
                }

                wiki.getParagraphs().add(paragraph);
            }

            return wiki;
        } catch (Exception ex) {
            throw new WikipediaException("unable to fetch from wikipedia.", ex);
        }
    }
}
