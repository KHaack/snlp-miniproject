package org.dice.alk.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.dice.alk.model.Sentence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class InputProcessorServiceTest {

	private final static String WIKIPEDIA_EP = "https://en.wikipedia.org/wiki/";
	@Autowired
	private InputProcessorService service;

	@Test
	public void testWikification() {
		String text = "Oak Hill, West Virginia is Hank Williams' last place.";

		List<String> res = this.service.getAllWikipediaPaths(text);

		assertThat(res, containsInAnyOrder(WIKIPEDIA_EP + "Hank_Williams", WIKIPEDIA_EP + "Oak_Hill,_West_Virginia",
				WIKIPEDIA_EP + "West_Virginia"));
	}

	
	@Test
	public void testAnnotator() {
		// TODO just use parameterized runs
		String text = "Queluz, Portugal is Carlota Joaquina of Spain's last place.";
		Sentence sentence = new Sentence(0, text);
		this.service.processTextInput(sentence);
		String pred = sentence.getPredicate();
		System.out.println(pred);
		assertThat(pred, is("last place"));
	}

}
