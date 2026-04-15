package com.workerfinder.restapi.service;

import com.workerfinder.restapi.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkerServiceImpl {

    private List<Job> jobs = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private List<Worker> workers = new ArrayList<>();

    public WorkerServiceImpl() {

    Worker w1 = new Worker();
    w1.setId("W001");
    w1.setName("Ramesh");
    w1.setSkill("plumber");
    w1.setArea("Pune");
    w1.setAvailable(true);
    w1.setRating(4.5);
    w1.setPhone("9999999999");

    Worker w2 = new Worker();
    w2.setId("W002");
    w2.setName("Suresh");
    w2.setSkill("electrician");
    w2.setArea("Mumbai");
    w2.setAvailable(true);
    w2.setRating(4.2);
    w2.setPhone("8888888888");

    workers.add(w1);
    workers.add(w2);
}

    public List<Worker> getAllWorkers() {
        return workers;
    }

    public List<Worker> findWorkers(String skill, String area) {
        List<Worker> res = new ArrayList<>();
        for (Worker w : workers) {
            if ((skill == null || skill.isEmpty() || w.getSkill().equalsIgnoreCase(skill)) &&
                (area == null || area.isEmpty() || w.getArea().equalsIgnoreCase(area))) {
                res.add(w);
            }
        }
        return res;
    }

    public Job postJob(String customerId, String skill, String area, String description) {
        Job j = new Job();
        j.setJobId("J" + (jobs.size() + 1));
        j.setCustomerId(customerId);
        j.setSkill(skill);
        j.setArea(area);
        j.setDescription(description);
        j.setStatus("OPEN");
        jobs.add(j);
        return j;
    }

    public List<Job> getOpenJobs() {
        List<Job> res = new ArrayList<>();
        for (Job j : jobs) {
            if ("OPEN".equals(j.getStatus())) res.add(j);
        }
        return res;
    }

    public Booking assignJob(String jobId, String workerId, String customerId, String date) {
        Booking b = new Booking();
        b.setBookingId("B" + (bookings.size() + 1));
        b.setJobId(jobId);
        b.setWorkerId(workerId);
        b.setCustomerId(customerId);
        b.setScheduledDate(date);
        b.setStatus("ASSIGNED");

        bookings.add(b);

        for (Job j : jobs) {
            if (j.getJobId().equals(jobId)) {
                j.setStatus("ASSIGNED");
            }
        }

        return b;
    }

    public boolean completeJob(String bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId)) {
                b.setStatus("COMPLETED");
                return true;
            }
        }
        return false;
    }

    public double rateWorker(String workerId, double rating) {
        for (Worker w : workers) {
            if (w.getId().equals(workerId)) {
                double newRating = (w.getRating() + rating) / 2;
                w.setRating(newRating);
                return newRating;
            }
        }
        return 0;
    }

    public List<Booking> getBookingsForCustomer(String customerId) {
        List<Booking> res = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getCustomerId().equals(customerId)) res.add(b);
        }
        return res;
    }

    public List<Booking> getBookingsForWorker(String workerId) {
        List<Booking> res = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getWorkerId().equals(workerId)) res.add(b);
        }
        return res;
    }
}
