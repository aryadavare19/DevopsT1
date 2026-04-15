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
        workers.add(new Worker("W001", "Ramesh", "plumber", "Pune", true, 4.5, "9999999999"));
        workers.add(new Worker("W002", "Suresh", "electrician", "Mumbai", true, 4.2, "8888888888"));
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
