package com.workerfinder.restapi.model;

import java.io.Serializable;

 public class Job implements Serializable {
     private static final long serialVersionUID = 1L;
    private String jobId, customerId, skill, area, description, status;

    public Job(String jobId, String customerId, String skill,
               String area, String description) {
        this.jobId = jobId; this.customerId = customerId;
        this.skill = skill; this.area = area;
        this.description = description; this.status = "OPEN";
    }

    public String getJobId()        { return jobId; }
    public String getCustomerId()   { return customerId; }
    public String getSkill()        { return skill; }
    public String getArea()         { return area; }
    public String getDescription()  { return description; }
    public String getStatus()       { return status; }
    public void setStatus(String s) { status = s; }
}