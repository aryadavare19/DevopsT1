package com.workerfinder.restapi.controller;
import com.workerfinder.restapi.model.Booking;
import com.workerfinder.restapi.model.Job;
import com.workerfinder.restapi.service.WorkerService;
import org.springframework.web.bind.annotation.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class JobController {

    private WorkerService getRMI() throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        return (WorkerService) registry.lookup("WorkerService");
    }

    // POST /api/jobs
    @PostMapping("/jobs")
    public Job postJob(@RequestBody Map<String, String> body) {
        try {
            return getRMI().postJob(
                    body.get("customerId"), body.get("skill"),
                    body.get("area"),       body.get("description")
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to post job: " + e.getMessage());
        }
    }

    // GET /api/jobs/open
    @GetMapping("/jobs/open")
    public List<Job> getOpenJobs() {
        try {
            return getRMI().getOpenJobs();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // POST /api/jobs/assign
    @PostMapping("/jobs/assign")
    public Booking assignJob(@RequestBody Map<String, String> body) {
        try {
            return getRMI().assignJob(
                    body.get("jobId"),      body.get("workerId"),
                    body.get("customerId"), body.get("date")
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign: " + e.getMessage());
        }
    }

    // POST /api/jobs/complete
    @PostMapping("/jobs/complete")
    public Map<String, Object> completeJob(@RequestBody Map<String, String> body) {
        Map<String, Object> res = new HashMap<>();
        try {
            boolean ok = getRMI().completeJob(body.get("bookingId"));
            res.put("success", ok);
            res.put("message", ok ? "Job completed" : "Not found");
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
        }
        return res;
    }

    // POST /api/jobs/rate
    @PostMapping("/jobs/rate")
    public Map<String, Object> rateWorker(@RequestBody Map<String, String> body) {
        Map<String, Object> res = new HashMap<>();
        try {
            double rating = getRMI().rateWorker(
                    body.get("workerId"),
                    Double.parseDouble(body.get("rating"))
            );
            res.put("success", true);
            res.put("newRating", rating);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", e.getMessage());
        }
        return res;
    }

    // GET /api/jobs/bookings/customer?customerId=CUS001
    @GetMapping("/jobs/bookings/customer")
    public List<Booking> customerBookings(@RequestParam String customerId) {
        try {
            return getRMI().getBookingsForCustomer(customerId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // GET /api/jobs/bookings/worker?workerId=W001
    @GetMapping("/jobs/bookings/worker")
    public List<Booking> workerBookings(@RequestParam String workerId) {
        try {
            return getRMI().getBookingsForWorker(workerId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}