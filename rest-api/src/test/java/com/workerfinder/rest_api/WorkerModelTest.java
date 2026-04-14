package com.workerfinder.rest_api;

import com.workerfinder.restapi.model.Worker;
import com.workerfinder.restapi.model.Job;
import com.workerfinder.restapi.model.Booking;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Worker Finder model classes.
 * These tests run without needing the RMI server.
 *
 * Person 2 owns this file.
 */
class WorkerModelTest {

    // ─── Worker Tests ────────────────────────────────────────────

    @Test
    void workerConstructor_setsAllFieldsCorrectly() {
        Worker worker = new Worker("W001", "Ramesh Kumar", "Plumber", "Pune", "9876543210");

        assertEquals("W001",        worker.getId());
        assertEquals("Ramesh Kumar",worker.getName());
        assertEquals("Plumber",     worker.getSkill());
        assertEquals("Pune",        worker.getArea());
        assertEquals("9876543210",  worker.getPhone());
    }

    @Test
    void workerDefaultRating_isFourPointZero() {
        Worker worker = new Worker("W002", "Suresh", "Electrician", "Mumbai", "9001234567");
        assertEquals(4.0, worker.getRating(), 0.001);
    }

    @Test
    void workerDefaultAvailability_isTrue() {
        Worker worker = new Worker("W003", "Anita", "Carpenter", "Nagpur", "9112233445");
        assertTrue(worker.isAvailable());
    }

    @Test
    void workerSetters_updateFieldsCorrectly() {
        Worker worker = new Worker();
        worker.setId("W004");
        worker.setName("Priya");
        worker.setSkill("Painter");
        worker.setArea("Nashik");
        worker.setPhone("9988776655");
        worker.setRating(4.5);
        worker.setAvailable(false);

        assertEquals("W004",    worker.getId());
        assertEquals("Priya",   worker.getName());
        assertEquals("Painter", worker.getSkill());
        assertEquals("Nashik",  worker.getArea());
        assertEquals(4.5,       worker.getRating(), 0.001);
        assertFalse(worker.isAvailable());
    }

    @Test
    void workerRating_canBeUpdated() {
        Worker worker = new Worker("W005", "Kiran", "Welder", "Aurangabad", "9123456789");
        worker.setRating(3.8);
        assertEquals(3.8, worker.getRating(), 0.001);
    }

    // ─── Job Tests ───────────────────────────────────────────────

    @Test
    void jobConstructor_setsAllFieldsCorrectly() {
        Job job = new Job("J001", "CUS001", "Plumber", "Pune", "Fix leaking pipe");

        assertEquals("J001",             job.getJobId());
        assertEquals("CUS001",           job.getCustomerId());
        assertEquals("Plumber",          job.getSkill());
        assertEquals("Pune",             job.getArea());
        assertEquals("Fix leaking pipe", job.getDescription());
    }

    @Test
    void jobDefaultStatus_isOPEN() {
        Job job = new Job("J002", "CUS002", "Electrician", "Mumbai", "Install switch");
        assertEquals("OPEN", job.getStatus());
    }

    @Test
    void jobStatus_canBeChanged() {
        Job job = new Job("J003", "CUS003", "Carpenter", "Nashik", "Build shelf");
        job.setStatus("ASSIGNED");
        assertEquals("ASSIGNED", job.getStatus());
    }

    @Test
    void jobSetters_updateFieldsCorrectly() {
        Job job = new Job();
        job.setJobId("J004");
        job.setCustomerId("CUS004");
        job.setSkill("Painter");
        job.setArea("Nagpur");
        job.setDescription("Paint walls");
        job.setStatus("COMPLETED");

        assertEquals("J004",        job.getJobId());
        assertEquals("Painter",     job.getSkill());
        assertEquals("COMPLETED",   job.getStatus());
    }

    // ─── Booking Tests ───────────────────────────────────────────

    @Test
    void bookingConstructor_setsAllFieldsCorrectly() {
        Booking booking = new Booking("B001", "J001", "W001", "CUS001", "2026-05-01");

        assertEquals("B001",       booking.getBookingId());
        assertEquals("J001",       booking.getJobId());
        assertEquals("W001",       booking.getWorkerId());
        assertEquals("CUS001",     booking.getCustomerId());
        assertEquals("2026-05-01", booking.getScheduledDate());
    }

    @Test
    void bookingDefaultStatus_isSCHEDULED() {
        Booking booking = new Booking("B002", "J002", "W002", "CUS002", "2026-05-10");
        assertEquals("SCHEDULED", booking.getStatus());
    }

    @Test
    void bookingStatus_canBeUpdatedToCompleted() {
        Booking booking = new Booking("B003", "J003", "W003", "CUS003", "2026-05-15");
        booking.setStatus("COMPLETED");
        assertEquals("COMPLETED", booking.getStatus());
    }

    @Test
    void bookingSetters_updateFieldsCorrectly() {
        Booking booking = new Booking();
        booking.setBookingId("B004");
        booking.setJobId("J004");
        booking.setWorkerId("W004");
        booking.setCustomerId("CUS004");
        booking.setScheduledDate("2026-06-01");
        booking.setStatus("CANCELLED");

        assertEquals("B004",       booking.getBookingId());
        assertEquals("CANCELLED",  booking.getStatus());
        assertEquals("2026-06-01", booking.getScheduledDate());
    }
}
