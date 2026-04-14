package com.workerfinder.restapi.service;

import com.workerfinder.restapi.model.Worker;
import com.workerfinder.restapi.model.Job;
import com.workerfinder.restapi.model.Booking;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerServiceImp1 extends UnicastRemoteObject implements WorkerService {

    private final Map<String, Worker>  workers  = new HashMap<>();
    private final Map<String, Job>     jobs     = new HashMap<>();
    private final Map<String, Booking> bookings = new HashMap<>();
    private final AtomicInteger jobSeq = new AtomicInteger(1);
    private final AtomicInteger bkSeq  = new AtomicInteger(1);

    public WorkerServiceImp1() throws RemoteException {
        super();
        addWorker("W001", "Ramesh Kumar",  "plumber",     "Pune",   "9876543210");
        addWorker("W002", "Suresh Patil",  "electrician", "Mumbai", "9876543211");
        addWorker("W003", "Mahesh Singh",  "plumber",     "Mumbai", "9876543212");
        addWorker("W004", "Ganesh Rao",    "carpenter",   "Pune",   "9876543213");
        addWorker("W005", "Dinesh Verma",  "electrician", "Pune",   "9876543214");
        addWorker("W006", "Rajesh Sharma", "painter",     "Mumbai", "9876543215");
    }

    private void addWorker(String id, String name, String skill,
                           String area, String phone) {
        workers.put(id, new Worker(id, name, skill, area, phone));
    }

    @Override
    public List<Worker> getWorkersBySkillAndArea(String skill, String area)
            throws RemoteException {
        List<Worker> result = new ArrayList<>();
        for (Worker w : workers.values()) {
            boolean skillMatch = skill == null || skill.isEmpty()
                    || w.getSkill().equalsIgnoreCase(skill);
            boolean areaMatch  = area == null  || area.isEmpty()
                    || w.getArea().equalsIgnoreCase(area);
            if (skillMatch && areaMatch && w.isAvailable()) result.add(w);
        }
        result.sort(Comparator.comparingDouble(Worker::getRating).reversed());
        return result;
    }

    @Override
    public List<Worker> getAllWorkers() throws RemoteException {
        return new ArrayList<>(workers.values());
    }

    @Override
    public Job postJob(String customerId, String skill,
                       String area, String description) throws RemoteException {
        String jobId = "JOB" + jobSeq.getAndIncrement();
        Job job = new Job(jobId, customerId, skill, area, description);
        jobs.put(jobId, job);
        System.out.println("Job posted: " + jobId);
        return job;
    }

    @Override
    public List<Job> getOpenJobs() throws RemoteException {
        List<Job> open = new ArrayList<>();
        for (Job j : jobs.values())
            if ("OPEN".equals(j.getStatus())) open.add(j);
        return open;
    }

    @Override
    public Booking assignJob(String jobId, String workerId,
                             String customerId, String date) throws RemoteException {
        Job job = jobs.get(jobId);
        Worker worker = workers.get(workerId);
        if (job == null)    throw new RemoteException("Job not found: " + jobId);
        if (worker == null) throw new RemoteException("Worker not found: " + workerId);
        if (!worker.isAvailable()) throw new RemoteException("Worker not available");

        String bookingId = "BK" + bkSeq.getAndIncrement();
        Booking booking  = new Booking(bookingId, jobId, workerId, customerId, date);
        bookings.put(bookingId, booking);
        job.setStatus("ASSIGNED");
        worker.setAvailable(false);
        System.out.println("Booking created: " + bookingId);
        return booking;
    }

    @Override
    public boolean completeJob(String bookingId) throws RemoteException {
        Booking booking = bookings.get(bookingId);
        if (booking == null) return false;
        booking.setStatus("COMPLETED");
        Job job = jobs.get(booking.getJobId());
        Worker worker = workers.get(booking.getWorkerId());
        if (job != null)    job.setStatus("COMPLETED");
        if (worker != null) worker.setAvailable(true);
        return true;
    }

    @Override
    public double rateWorker(String workerId, double newRating) throws RemoteException {
        Worker worker = workers.get(workerId);
        if (worker == null) throw new RemoteException("Worker not found");
        double updated = (worker.getRating() * 0.8) + (newRating * 0.2);
        worker.setRating(Math.round(updated * 10.0) / 10.0);
        return worker.getRating();
    }

    @Override
    public List<Booking> getBookingsForCustomer(String customerId) throws RemoteException {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings.values())
            if (b.getCustomerId().equals(customerId)) result.add(b);
        return result;
    }

    @Override
    public List<Booking> getBookingsForWorker(String workerId) throws RemoteException {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings.values())
            if (b.getWorkerId().equals(workerId)) result.add(b);
        return result;
    }
}