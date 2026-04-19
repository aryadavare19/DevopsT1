package com.workerfinder.rest_api;

import com.workerfinder.restapi.model.Worker;
import com.workerfinder.restapi.model.Job;
import com.workerfinder.restapi.model.Booking;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WorkerModelTest {

    @Test
    void worker_idIsSetCorrectly() {
        Worker w = new Worker("W001", "Ramesh", "Plumber", "Pune", "9000000000");
        assertEquals("W001", w.getId());
    }

    @Test
    void worker_nameIsSetCorrectly() {
        Worker w = new Worker("W001", "Ramesh", "Plumber", "Pune", "9000000000");
        assertEquals("Ramesh", w.getName());
    }

    @Test
    void worker_skillIsSetCorrectly() {
        Worker w = new Worker("W001", "Ramesh", "Plumber", "Pune", "9000000000");
        assertEquals("Plumber", w.getSkill());
    }

    @Test
    void worker_areaIsSetCorrectly() {
        Worker w = new Worker("W001", "Ramesh", "Plumber", "Pune", "9000000000");
        assertEquals("Pune", w.getArea());
    }

    @Test
    void job_defaultStatusIsOpen() {
        Job j = new Job("J001", "CUS001", "Plumber", "Pune", "Fix pipe");
        assertEquals("OPEN", j.getStatus());
    }

    @Test
    void job_setStatusUpdatesCorrectly() {
        Job j = new Job("J001", "CUS001", "Plumber", "Pune", "Fix pipe");
        j.setStatus("ASSIGNED");
        assertEquals("ASSIGNED", j.getStatus());
    }

    @Test
    void job_customerIdIsSetCorrectly() {
        Job j = new Job("J001", "CUS001", "Plumber", "Pune", "Fix pipe");
        assertEquals("CUS001", j.getCustomerId());
    }

    @Test
    void job_descriptionIsSetCorrectly() {
        Job j = new Job("J001", "CUS001", "Plumber", "Pune", "Fix pipe");
        assertEquals("Fix pipe", j.getDescription());
    }

    @Test
    void booking_workerIdIsSetCorrectly() {
        Booking b = new Booking("B001", "J001", "W001");
        assertEquals("W001", b.getWorkerId());
    }

    @Test
    void booking_jobIdIsSetCorrectly() {
        Booking b = new Booking("B001", "J001", "W001");
        assertEquals("J001", b.getJobId());
    }

    @Test
    void booking_idIsSetCorrectly() {
        Booking b = new Booking("B001", "J001", "W001");
        assertEquals("B001", b.getId());
    }

    @Test
    void worker_ratingDefaultIsZero() {
        Worker w = new Worker("W001", "Ramesh", "Plumber", "Pune", "9000000000");
        assertEquals(0.0, w.getRating(), 0.001);
    }
}
