package com.workerfinder.restapi.model;

import java.io.Serializable;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bookingId, jobId, workerId, customerId, scheduledDate, status;

    public Booking() {}

    public Booking(String bookingId, String jobId, String workerId,
                   String customerId, String scheduledDate) {
        this.bookingId = bookingId; this.jobId = jobId;
        this.workerId = workerId; this.customerId = customerId;
        this.scheduledDate = scheduledDate; this.status = "SCHEDULED";
    }

    public String getBookingId()          { return bookingId; }
    public String getJobId()              { return jobId; }
    public String getWorkerId()           { return workerId; }
    public String getCustomerId()         { return customerId; }
    public String getScheduledDate()      { return scheduledDate; }
    public String getStatus()             { return status; }
    public void setBookingId(String b)    { this.bookingId = b; }
    public void setJobId(String j)        { this.jobId = j; }
    public void setWorkerId(String w)     { this.workerId = w; }
    public void setCustomerId(String c)   { this.customerId = c; }
    public void setScheduledDate(String d){ this.scheduledDate = d; }
    public void setStatus(String s)       { this.status = s; }
}