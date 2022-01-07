package org.dice.alk.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class WikipediaService {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WikipediaService.class);

    @Value("${wikipedia.timeout}")
    private int timeout;

    private Document getDocument(String url) throws IOException {
        LOGGER.info("fetch wikipedia document from " + url);
        return Jsoup.connect(url).timeout(this.timeout).get();
    }

    /**
     * Get the text content of a wikipedia page
     *
     * @param url
     * @return
     */
    public String fetch(String url) {
        StringBuilder content = new StringBuilder();
        try {
            Document document = getDocument(url);
            Elements elements = document.select("#bodyContent .mw-parser-output p");
            for (Element element:
                    elements) {
                content.append(element.text());
            }
        } catch (IOException | Error e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    /**
     * Get the table about content of a wikipedia page
     *
     * @param url
     * @return
     */
    public Map<String, String> fetchTable(String url) {
        Map<String, String> content = new HashMap<>();
        try {
            Document document = getDocument(url);
            Elements elements = document.select(".infobox.vcard tr");
            for (Element element:
                    elements) {

                Elements th = element.getElementsByTag("th");
                Elements td = element.getElementsByTag("td");

                if(!th.isEmpty() && !td.isEmpty()){
                    content.put(th.text(), td.text());
                }
            }
        } catch (IOException | Error e) {
            e.printStackTrace();
        }

        return content;
    }

}
