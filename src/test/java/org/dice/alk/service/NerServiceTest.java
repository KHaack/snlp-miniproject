package org.dice.alk.service;

import org.dice.alk.model.Entity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class NerServiceTest {

    @Autowired
    private NerService nerService;

    @Test
    public void extract1Test() {
        String text = "Metta World Peace's team is Los Angeles Lakers.";

        Set<Entity> result = this.nerService.extract(text);

        assertThat(result, hasItem(hasProperty("text", containsString("Metta World Peace"))));
        assertThat(result, hasItem(hasProperty("wikipediaTitle", equalTo("Metta_World_Peace"))));

        assertThat(result, hasItem(hasProperty("text", containsString("Los Angeles Lakers"))));
        assertThat(result, hasItem(hasProperty("wikipediaTitle", equalTo("Los_Angeles_Lakers"))));
    }

    @Test
    public void extract2Test() {
        String text = "IBM's subordinate is Rational Software.";

        Set<Entity> result = this.nerService.extract(text);

        assertThat(result, hasItem(hasProperty("text", containsString("IBM"))));
        assertThat(result, hasItem(hasProperty("wikipediaTitle", equalTo("IBM"))));

        assertThat(result, hasItem(hasProperty("text", containsString("Rational Software"))));
        assertThat(result, hasItem(hasProperty("wikipediaTitle", equalTo("Rational_Software"))));
    }

    @Test
    public void extract3Test() {
        String text = "Frank Herbert is An Unusual Angle's author.";

        Set<Entity> result = this.nerService.extract(text);

        assertThat(result, hasItem(hasProperty("text", equalTo("Frank Herbert"))));
        assertThat(result, hasItem(hasProperty("wikipediaTitle", equalTo("Frank_Herbert"))));

        assertThat(result, hasItem(hasProperty("text", containsString("Unusual Angle"))));
        assertThat(result, hasItem(hasProperty("wikipediaTitle", equalTo("Unusual_Angle"))));
    }

    @Test
    public void mixEntities1Test() {
        Set<Entity> setA = new HashSet<>();
        Entity entity = new Entity();
        entity.setText("Sevier County, Tennessee");
        entity.setWikipediaTitle("Sevier_County,_Tennessee");
        setA.add(entity);

        Set<Entity> setB = new HashSet<>();
        entity = new Entity();
        entity.setText("Tennessee");
        entity.setWikipediaTitle("Tennessee");
        setB.add(entity);

        Set<Entity> result = NerService.mixEntities(setA, setB);
        assertEquals(1, result.size());

        assertThat(result, hasItem(hasProperty("text", equalTo("Sevier County, Tennessee"))));
    }

    @Test
    public void mixEntities2Test() {
        Set<Entity> setA = new HashSet<>();
        Entity entity = new Entity();
        entity.setText("Coldwater, Michigan");
        entity.setWikipediaTitle("Coldwater,_Michigan");
        setA.add(entity);

        Set<Entity> setB = new HashSet<>();
        entity = new Entity();
        entity.setText("Michigan");
        entity.setWikipediaTitle("Michigan");
        setB.add(entity);

        Set<Entity> result = NerService.mixEntities(setA, setB);
        assertEquals(1, result.size());

        assertThat(result, hasItem(hasProperty("text", equalTo("Coldwater, Michigan"))));
    }

    @Test
    public void mixEntities3Test() {
        Set<Entity> setA = new HashSet<>();
        Entity entity = new Entity();
        entity.setText("Frank Herbert");
        entity.setWikipediaTitle("Frank_Herbert");
        setA.add(entity);

        Set<Entity> setB = new HashSet<>();

        Set<Entity> result = NerService.mixEntities(setA, setB);
        assertEquals(1, result.size());

        assertThat(result, hasItem(hasProperty("text", equalTo("Frank Herbert"))));
    }
}