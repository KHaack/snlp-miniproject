package org.dice.alk.service;

import edu.stanford.nlp.ie.util.RelationTriple;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class StanfordExtractorServiceTest {

    @Autowired
    private StanfordExtractorService service;

    @Test
    public void extractTest() {
        String text = "Claude Auchinleck's death place is Marrakesh.";
        List<RelationTriple> result = this.service.extract(text);

        assertThat(result, hasItem(allOf(
                subject(equalTo("Claude Auchinleck")),
                relation(equalTo("have")),
                object(equalTo("death place"))
        )));

        assertThat(result, hasItem(allOf(
                subject(equalTo("Claude Auchinleck 's death place")),
                relation(equalTo("be")),
                object(equalTo("Marrakesh"))
        )));
    }

    private FeatureMatcher<RelationTriple, String> relation(Matcher<String> matcher) {
        return new FeatureMatcher<RelationTriple, String>(matcher, "relation", "relation") {
            @Override
            protected String featureValueOf(RelationTriple actual) {
                return actual.relationLemmaGloss();
            }
        };
    }

    private FeatureMatcher<RelationTriple, String> object(Matcher<String> matcher) {
        return new FeatureMatcher<RelationTriple, String>(matcher, "object", "object") {
            @Override
            protected String featureValueOf(RelationTriple actual) {
                return actual.objectLemmaGloss();
            }
        };
    }

    private FeatureMatcher<RelationTriple, String> subject(Matcher<String> matcher) {
        return new FeatureMatcher<RelationTriple, String>(matcher, "subject", "subject") {
            @Override
            protected String featureValueOf(RelationTriple actual) {
                return actual.subjectLemmaGloss();
            }
        };
    }
}