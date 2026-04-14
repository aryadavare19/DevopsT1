package com.workerfinder.restapi;

import com.workerfinder.restapi.model.Worker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WorkerModelTest {

    @Test
    public void testConstructor() {
        Worker w = new Worker("W001", "Test User", "Electrician", "CityArea", "9999999999");

        assertEquals("W001", w.getId());
        assertEquals("Test User", w.getName());
        assertEquals("Electrician", w.getSkill());
        assertEquals("CityArea", w.getArea());
        assertEquals("9999999999", w.getPhone());

        assertEquals(4.0, w.getRating());
        assertTrue(w.isAvailable());
    }

    @Test
    public void testSettersAndGetters() {
        Worker w = new Worker();

        w.setId("W002");
        w.setName("Sample Worker");
        w.setSkill("Plumber");
        w.setArea("TestLocation");
        w.setPhone("8888888888");
        w.setRating(4.5);
        w.setAvailable(false);

        assertEquals("W002", w.getId());
        assertEquals("Sample Worker", w.getName());
        assertEquals("Plumber", w.getSkill());
        assertEquals("TestLocation", w.getArea());
        assertEquals("8888888888", w.getPhone());
        assertEquals(4.5, w.getRating());
        assertFalse(w.isAvailable());
    }
}