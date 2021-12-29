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
    	String inputFile = "/media/ltphen/Ulife/project/school/snlp/snlp-miniproject/data/SNLP2020_training_test.tsv";
    	String outputFile = "/media/ltphen/Ulife/project/school/snlp/snlp-miniproject/data/SNLP2020_training_result.tsv";
    	this.service.factCheck(inputFile, outputFile);

    }

}
