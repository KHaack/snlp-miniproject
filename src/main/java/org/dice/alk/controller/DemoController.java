package org.dice.alk.controller;

import org.dice.alk.io.IOUtils;
import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.dice.alk.service.FactCheckerService;
import org.dice.alk.service.NerService;
import org.dice.alk.service.RelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/demo/")
public class DemoController {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Autowired
    private NerService nerService;

    @Autowired
    private RelationService relationService;

    @Autowired
    private FactCheckerService factCheckerService;

    @RequestMapping(value = "/runFileTraining", method = RequestMethod.GET, produces = {MediaType.TEXT_PLAIN_VALUE})
    public void runFileTraining(HttpServletResponse response) throws IOException {
        String inputFile = "src/main/resources/SNLP2020_training.tsv";

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=result.ttl");
        this.factCheckerService.factCheck(new FileReader(inputFile), response.getWriter());
    }

    @RequestMapping(value = "/runFileTest", method = RequestMethod.GET, produces = {MediaType.TEXT_PLAIN_VALUE})
    public void runFileTest(HttpServletResponse response) throws IOException {
        String inputFile = "src/main/resources/SNLP2020_test.tsv";

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=result.ttl");
        this.factCheckerService.factCheck(new FileReader(inputFile), response.getWriter());
    }

    @RequestMapping(value = "/runFactCheck", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE})
    @ResponseBody
    public String runFactCheck() throws FileNotFoundException {
        String inputFile = "src/main/resources/SNLP2020_training.tsv";
        InputStreamReader inputStreamReader = new FileReader(inputFile);
        Set<Sentence> sentences = IOUtils.readFromFile(inputStreamReader);

        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\">");
        builder.append("</head>");
        builder.append("<body>");

        int count = 0;
        int countWrong = 0;
        for (Sentence sentence : sentences) {
            double shouldResult = sentence.getScore();

            builder.append("<p>");

            try {
                double score = this.factCheckerService.factCheck(sentence);
                sentence.setScore(score);

                builder.append(df.format(score));
                builder.append(" (");
                builder.append(df.format(shouldResult));
                builder.append(" ) ");

                double diff = shouldResult - score;
                if (diff > 0.2 || diff < -0.2) {
                    builder.append("!!!");
                    countWrong++;
                }

                builder.append(sentence.getSentenceText());

                LOGGER.info("Score => {} should {}", df.format(score), df.format(shouldResult));

                count++;

                if (count % 100 == 0) {
                    LOGGER.info("{}/{}", count, sentences.size());
                }
            } catch (Exception ex) {
                builder.append(sentence.getSentenceText());
                builder.append("<br/>");
                builder.append(ex.getMessage());
            }

            builder.append("</p>");
        }

        builder.append("<h1>total:");
        builder.append(count);
        builder.append("</h1>");

        builder.append("<h1>wrong:");
        builder.append(countWrong);
        builder.append("</h1>");

        builder.append("</body>");
        builder.append("</html>");

        return builder.toString();
    }

    @RequestMapping(value = "/runNer", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE})
    @ResponseBody
    public String runNer() throws FileNotFoundException {
        String inputFile = "src/main/resources/SNLP2020_training.tsv";
        InputStreamReader inputStreamReader = new FileReader(inputFile);
        Set<Sentence> sentences = IOUtils.readFromFile(inputStreamReader);

        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\">");
        builder.append("</head>");
        builder.append("<body>");

        int countLess2 = 0;
        int countMore2 = 0;
        int count = 0;
        int countRelations = 0;
        Map<String, Integer> countEntities = new HashMap<>();

        for (Sentence sentence : sentences) {
            builder.append("<p>");

            try {
                sentence.setEntities(this.nerService.extractSentence(sentence.getSentenceText()));
                this.relationService.extractRelation(sentence);

                for (Entity e : sentence.getEntities()) {
                    if (!countEntities.containsKey(e.getWikipediaTitle())) {
                        countEntities.put(e.getWikipediaTitle(), 0);
                    }

                    countEntities.put(e.getWikipediaTitle(), countEntities.get(e.getWikipediaTitle()) + 1);
                }

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

                if (sentence.getRelations().size() > 0) {
                    countRelations++;
                } else {
                    builder.append("no relation.<br/>");
                }

                if (sentence.getEntities().size() < 2) {
                    countLess2++;
                } else if (sentence.getEntities().size() > 2) {
                    countMore2++;
                }

                count++;

                if (count % 100 == 0) {
                    LOGGER.info("{}/{}", count, sentences.size());
                }
            } catch (Exception ex) {
                builder.append(sentence.getSentenceText());
                builder.append("<br/>");
                builder.append(ex.getMessage());
            }

            builder.append("</p>");
        }

        builder.append("<h1>count less 2 entities:");
        builder.append(countLess2);
        builder.append("</h1>");

        builder.append("<h1>count more 2 entities:");
        builder.append(countMore2);
        builder.append("</h1>");

        builder.append("<h1>relations:");
        builder.append(countRelations);
        builder.append("</h1>");

        builder.append("<h1>total:");
        builder.append(count);
        builder.append("</h1>");

        builder.append("<hr/>");

        for (String key : countEntities.keySet()) {
            if (countEntities.get(key) > 25) {
                builder.append("<p>");
                builder.append(countEntities.get(key));
                builder.append(" ");
                builder.append(key);
                builder.append("</p>");
            }
        }

        builder.append("</body>");
        builder.append("</html>");

        return builder.toString();
    }
}
