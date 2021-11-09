package org.dice.alk.nlp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InputProcessorTest {
	
	
	@Autowired
	private InputProcessor proc;
	
	@Test
    public void testAnnotator() throws Exception {
		String text = "Oak Hill, West Virginia is Hank Williams' last place.";
	
		// TODO assert something
		proc.processTextInput(text);
	}

}
