package org.dice.alk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AlkController {

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String upload() {
        return "upload";
    }

    @RequestMapping(value = {"", "/", "/factCheck"}, method = RequestMethod.GET)
    public String factCheck() {
        return "factCheck";
    }
}
