package com.workerfinder.restapi.model;

import java.io.Serializable;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bookingId, jobId, workerId, customerId, scheduledDate, status;

    public Booking(String bookingId, String jobId, String workerId,
                   String customerId, String scheduledDate) {
        this.bookingId = bookingId; this.jobId = jobId;
        this.workerId = workerId; this.customerId = customerId;
        this.scheduledDate = scheduledDate; this.status = "SCHEDULED";
    }

    public String getBookingId()      { return bookingId; }
    public String getJobId()          { return jobId; }
    public String getWorkerId()       { return workerId; }
    public String getCustomerId()     { return customerId; }
    public String getScheduledDate()  { return scheduledDate; }
    public String getStatus()         { return status; }
    public void setStatus(String s)   { status = s; }
}