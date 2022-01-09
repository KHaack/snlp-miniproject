package org.dice.alk.service;

import org.dice.alk.model.Entity;
import org.dice.alk.model.WikipediaDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class WikipediaServiceTest {

    @Autowired
    private WikipediaService service;

    @Test
    public void festContentTest() {
        Entity entity = new Entity();
        entity.setWikipediaTitle("Elon_Musk");

        WikipediaDocument document = service.fetch(entity);
        assertThat(document.getParagraphs(), hasItem(containsString("Elon Reeve Musk FRS")));
        assertThat(document.getParagraphs(), hasItem(containsString("In 2002, Musk founded SpaceX")));
        assertThat(document.getParagraphs(), hasItem(containsString("Elon Reeve Musk was born on")));
    }

    @Test
    public void festTableTest() {
        Map<String, String> table = service.fetchTable("https://en.wikipedia.org/wiki/Elon_Musk");
        assertThat(table, hasEntry("Education", "University of Pennsylvania (BS, BA)"));
    }

}
