package org.dice.alk.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class WikipediaServiceTest {

    @Autowired
    private WikipediaService service;

    @Test
    public void festContentTest() {
        String content = service.fetch("https://en.wikipedia.org/wiki/Elon_Musk");
        assertThat(content, containsString("He is the founder, CEO and Chief Engineer at SpaceX;"));
    }

    @Test
    public void festTableTest() {
        Map<String, String> table = service.fetchTable("https://en.wikipedia.org/wiki/Elon_Musk");
        assertThat(table, hasEntry("Education", "University of Pennsylvania (BS, BA)"));
    }

}
