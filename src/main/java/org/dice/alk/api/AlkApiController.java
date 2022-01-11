package org.dice.alk.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.dice.alk.model.Sentence;
import org.dice.alk.service.FactCheckerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/")
@Validated
public class AlkApiController {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AlkApiController.class);

    @Autowired
    private FactCheckerService factCheckerService;

    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public boolean ping() {
        LOGGER.info("ping called");
        return true;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(HttpServletResponse response, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=result.ttl");

        this.factCheckerService.factCheck(new InputStreamReader(multipartFile.getInputStream()), response.getWriter());
    }

    @RequestMapping(value = "/factCheck", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String factCheck(@RequestParam(name = "sentence") String text) throws JsonProcessingException {
        Sentence sentence = new Sentence(null, text);
        double score = this.factCheckerService.factCheck(sentence);
        sentence.setScore(score);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(sentence);
    }
}
