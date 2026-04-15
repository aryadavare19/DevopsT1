package com.workerfinder.restapi.controller;

import com.workerfinder.restapi.model.Booking;
import com.workerfinder.restapi.model.Job;
import com.workerfinder.restapi.service.WorkerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class JobController {

    @Autowired
    private WorkerServiceImpl service;

    // POST /api/jobs
    @PostMapping("/jobs")
    public Job postJob(@RequestBody Map<String, String> body) {
        return service.postJob(
                body.get("customerId"),
                body.get("skill"),
                body.get("area"),
                body.get("description")
        );
    }

    // GET /api/jobs/open
    @GetMapping("/jobs/open")
    public List<Job> getOpenJobs() {
        return service.getOpenJobs();
    }

    // POST /api/jobs/assign
    @PostMapping("/jobs/assign")
    public Booking assignJob(@RequestBody Map<String, String> body) {
        return service.assignJob(
                body.get("jobId"),
                body.get("workerId"),
                body.get("customerId"),
                body.get("date")
        );
    }

    // POST /api/jobs/complete
    @PostMapping("/jobs/complete")
    public Map<String, Object> completeJob(@RequestBody Map<String, String> body) {
        Map<String, Object> res = new HashMap<>();
        boolean ok = service.completeJob(body.get("bookingId"));

        res.put("success", ok);
        res.put("message", ok ? "Job completed" : "Not found");

        return res;
    }

    // POST /api/jobs/rate
    @PostMapping("/jobs/rate")
    public Map<String, Object> rateWorker(@RequestBody Map<String, String> body) {
        Map<String, Object> res = new HashMap<>();

        double rating = service.rateWorker(
                body.get("workerId"),
                Double.parseDouble(body.get("rating"))
        );

        res.put("success", true);
        res.put("newRating", rating);

        return res;
    }

    // GET /api/jobs/bookings/customer
    @GetMapping("/jobs/bookings/customer")
    public List<Booking> customerBookings(@RequestParam String customerId) {
        return service.getBookingsForCustomer(customerId);
    }

    // GET /api/jobs/bookings/worker
    @GetMapping("/jobs/bookings/worker")
    public List<Booking> workerBookings(@RequestParam String workerId) {
        return service.getBookingsForWorker(workerId);
    }
}
