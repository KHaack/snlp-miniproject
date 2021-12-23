package org.dice.alk.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class RequestServiceTest {

    @Autowired
    private RequestService service;

    @Test
    public void testFetch() {
        String content = service.fetch("https://en.wikipedia.org/wiki/Elon_Musk");
        System.out.println(content);
    }

}
