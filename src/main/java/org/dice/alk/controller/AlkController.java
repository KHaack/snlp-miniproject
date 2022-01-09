package org.dice.alk.controller;

import org.dice.alk.io.IOUtils;
import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.dice.alk.service.StandfortExtractorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
public class AlkController {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AlkController.class);

    @Autowired
    private StandfortExtractorService standfort;

    @RequestMapping(value = "/runStandfort", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE})
    @ResponseBody
    public String runStandfort() {
        String inputFile = "./data/SNLP2020_training_test.tsv";
        Set<Sentence> sentences = IOUtils.readFromFile(inputFile);

        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\">");
        builder.append("</head>");
        builder.append("<body>");

        for (Sentence sentence : sentences) {
            LOGGER.info(sentence.getSentenceText());
            builder.append("<p>");

            try {
                this.standfort.extract(sentence);

                if (sentence.getEntities().size() != 2) {

                    builder.append(sentence.getEntities().size());
                    builder.append(" ");
                    builder.append(sentence.getSentenceText());
                    builder.append("<br />");
                    for (Entity e : sentence.getEntities()) {
                        builder.append(e.getText());
                        builder.append(" => ");
                        builder.append(e.getWikipediaTitle());
                        builder.append("<br/>");
                    }


                }
            } catch (Exception ex) {
                builder.append(sentence.getSentenceText());
                builder.append("<br/>");
                builder.append(ex.getMessage());
            }

            builder.append("</p>");
        }
        builder.append("</body>");
        builder.append("</html>");

        return builder.toString();
    }
}
