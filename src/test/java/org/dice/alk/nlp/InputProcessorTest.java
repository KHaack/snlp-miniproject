package org.dice.alk.nlp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.Set;

import org.dice.alk.controller.Config;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {Config.class})
public class InputProcessorTest {

	@Autowired
	private InputProcessor proc;
	
	private final static String WIKIPEDIA_EP = "https://en.wikipedia.org/wiki/";

	@Test
	public void testAnnotator() {
		String text = "Oak Hill, West Virginia is Hank Williams' last place.";
		
		Set<String> res = this.proc.processTextInput(text);

		// sets don't have order anyways, but it works 
		assertThat(res, containsInAnyOrder(
				WIKIPEDIA_EP+"Hank_Williams", 
				WIKIPEDIA_EP+"Oak_Hill,_West_Virginia", 
				WIKIPEDIA_EP+"West_Virginia"));
	}

}
