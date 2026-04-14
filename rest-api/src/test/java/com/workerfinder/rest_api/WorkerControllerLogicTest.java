package com.workerfinder.rest_api;

import com.workerfinder.restapi.model.Worker;
import com.workerfinder.restapi.model.Job;
import com.workerfinder.restapi.model.Booking;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Unit tests for controller-level logic.
 * Tests use plain Java — no Spring context, no RMI connection needed.
 * This verifies filtering and response-building logic extracted from controllers.
 *
 * Person 2 owns this file.
 */
class WorkerControllerLogicTest {

    // ─── Simulate worker filter logic from WorkerController ──────

    private List<Worker> filterWorkers(List<Worker> all, String skill, String area) {
        List<Worker> result = new ArrayList<>();
        for (Worker w : all) {
            boolean skillMatch = (skill == null || skill.isBlank() || skill.equalsIgnoreCase(w.getSkill()));
            boolean areaMatch  = (area  == null || area.isBlank()  || area.equalsIgnoreCase(w.getArea()));
            if (skillMatch && areaMatch) result.add(w);
        }
        return result;
    }

    private List<Worker> sampleWorkers() {
        List<Worker> workers = new ArrayList<>();
        workers.add(new Worker("W001", "Ramesh", "Plumber",     "Pune",   "900"));
        workers.add(new Worker("W002", "Suresh", "Electrician", "Mumbai", "901"));
        workers.add(new Worker("W003", "Anita",  "Plumber",     "Mumbai", "902"));
        workers.add(new Worker("W004", "Priya",  "Carpenter",   "Pune",   "903"));
        return workers;
    }

    @Test
    void filterBySkill_returnsOnlyMatchingSkill() {
        List<Worker> result = filterWorkers(sampleWorkers(), "Plumber", null);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(w -> w.getSkill().equals("Plumber")));
    }

    @Test
    void filterByArea_returnsOnlyMatchingArea() {
        List<Worker> result = filterWorkers(sampleWorkers(), null, "Pune");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(w -> w.getArea().equals("Pune")));
    }

    @Test
    void filterBySkillAndArea_returnsExactMatch() {
        List<Worker> result = filterWorkers(sampleWorkers(), "Plumber", "Mumbai");
        assertEquals(1, result.size());
        assertEquals("W003", result.get(0).getId());
    }

    @Test
    void filterWithNullParams_returnsAllWorkers() {
        List<Worker> result = filterWorkers(sampleWorkers(), null, null);
        assertEquals(4, result.size());
    }

    @Test
    void filterWithNoMatch_returnsEmptyList() {
        List<Worker> result = filterWorkers(sampleWorkers(), "Welder", "Nagpur");
        assertTrue(result.isEmpty());
    }

    // ─── Simulate open jobs filter logic from JobController ──────

    private List<Job> getOpenJobs(List<Job> all) {
        List<Job> open = new ArrayList<>();
        for (Job j : all) {
            if ("OPEN".equals(j.getStatus())) open.add(j);
        }
        return open;
    }

    @Test
    void getOpenJobs_returnsOnlyOpenJobs() {
        List<Job> jobs = new ArrayList<>();
        Job j1 = new Job("J001", "CUS001", "Plumber",     "Pune",   "Leaking pipe");
        Job j2 = new Job("J002", "CUS002", "Electrician", "Mumbai", "Wiring");
        Job j3 = new Job("J003", "CUS003", "Carpenter",   "Nashik", "Shelf");
        j2.setStatus("ASSIGNED");
        jobs.add(j1); jobs.add(j2); jobs.add(j3);

        List<Job> result = getOpenJobs(jobs);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(j -> "OPEN".equals(j.getStatus())));
    }

    @Test
    void getOpenJobs_whenNoneOpen_returnsEmpty() {
        List<Job> jobs = new ArrayList<>();
        Job j1 = new Job("J001", "CUS001", "Plumber", "Pune", "Fix pipe");
        j1.setStatus("COMPLETED");
        jobs.add(j1);

        assertTrue(getOpenJobs(jobs).isEmpty());
    }

    // ─── Rating calculation logic ─────────────────────────────────

    private double calculateNewRating(double currentRating, double newRating) {
        return (currentRating + newRating) / 2.0;
    }

    @Test
    void ratingCalculation_returnsAverageOfCurrentAndNew() {
        double result = calculateNewRating(4.0, 5.0);
        assertEquals(4.5, result, 0.001);
    }

    @Test
    void ratingCalculation_withPerfectRating_returnsFive() {
        double result = calculateNewRating(5.0, 5.0);
        assertEquals(5.0, result, 0.001);
    }

    @Test
    void ratingCalculation_withLowRating_returnsCorrectAverage() {
        double result = calculateNewRating(4.0, 2.0);
        assertEquals(3.0, result, 0.001);
    }
}
