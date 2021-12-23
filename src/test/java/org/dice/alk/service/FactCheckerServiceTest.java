package org.dice.alk.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class FactCheckerServiceTest {

    @Autowired
    private FactCheckerService service;

    @Test
    public void testFactChecker() {
//    	String inputFile = "C:\\Users\\Nikit\\Downloads\\SNLP2020_training.tsv";
//    	String outputFile = "temp.nt";
//    	
//    	this.service.factCheck(inputFile, outputFile);
    	

    }

}
