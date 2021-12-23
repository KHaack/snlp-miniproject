package org.dice.alk.controller;

import org.dice.alk.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@Validated
public class AlkApiController {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AlkApiController.class);
    @Autowired
    private RequestService service;


    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public boolean ping() {
        LOGGER.info("ping called");
        return true;
    }

    @RequestMapping(value = "/fecthTest", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String fectch() {
        String content = service.fetch("https://en.wikipedia.org/wiki/Elon_Musk");
        return content;
    }
}
