package org.dice.alk.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class RequestService {

    public String fetch(String url) {
        String content = "";
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("#bodyContent .mw-parser-output p");
            for (Element element:
                    elements) {
                content+=element.text();
            }
        } catch (IOException | Error e) {
            e.printStackTrace();
        }

        return content;
    }

}
