package org.dice.alk.nlp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@SpringBootTest
public class InputProcessorTest {

	@Autowired
	private InputProcessor proc;

	@Test
	public void testAnnotator() {
		String text = "Oak Hill, West Virginia is Hank Williams' last place.";

		assertThat(this.proc.processTextInput(text), containsInAnyOrder("Hank Williams", "Oak Hill", "West Virginia"));
	}

}
