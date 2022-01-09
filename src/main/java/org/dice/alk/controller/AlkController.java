package org.dice.alk.controller;

import org.dice.alk.io.IOUtils;
import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.dice.alk.service.NerService;
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
    private NerService nerService;

    @RequestMapping(value = "/runNer", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE})
    @ResponseBody
    public String runNer() {
        String inputFile = "./data/SNLP2020_training_test.tsv";
        Set<Sentence> sentences = IOUtils.readFromFile(inputFile);

        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\">");
        builder.append("</head>");
        builder.append("<body>");

        int countNot2 = 0;
        int count = 0;
        for (Sentence sentence : sentences) {
            builder.append("<p>");

            try {
                Set<Entity> entities = this.nerService.extract(sentence.getSentenceText());

                if (entities.size() != 2) {

                    builder.append(entities.size());
                    builder.append(" ");
                    builder.append(sentence.getSentenceText());
                    builder.append("<br />");
                    for (Entity e : entities) {
                        builder.append(e.getText());
                        builder.append(" => ");
                        builder.append(e.getWikipediaTitle());
                        builder.append("<br/>");
                    }

                    countNot2++;
                }

                count++;

                if (count % 10 == 0) {
                    LOGGER.info("{}/{}", count, sentences.size());
                }
            } catch (Exception ex) {
                builder.append(sentence.getSentenceText());
                builder.append("<br/>");
                builder.append(ex.getMessage());
            }

            builder.append("</p>");
        }

        builder.append("<h1>");
        builder.append(countNot2);
        builder.append("</h1>");

        builder.append("</body>");
        builder.append("</html>");

        return builder.toString();
    }
}
