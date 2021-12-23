package org.dice.alk.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class InputProcessorServiceTest {

    private final static String WIKIPEDIA_EP = "https://en.wikipedia.org/wiki/";
    @Autowired
    private InputProcessorService service;

    @Test
    public void testAnnotator() {
        String text = "Oak Hill, West Virginia is Hank Williams' last place.";

        Set<String> res = this.service.processTextInput(text);

        // sets don't have order anyways, but it works
        assertThat(res, containsInAnyOrder(
                WIKIPEDIA_EP + "Hank_Williams",
                WIKIPEDIA_EP + "Oak_Hill,_West_Virginia",
                WIKIPEDIA_EP + "West_Virginia"));
    }

}
