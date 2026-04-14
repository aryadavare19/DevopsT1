package com.workerfinder.restapi.service;

import com.workerfinder.restapi.model.Worker;
import com.workerfinder.restapi.model.Job;
import com.workerfinder.restapi.model.Booking;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface WorkerService extends Remote {
    List<Worker>  getWorkersBySkillAndArea(String skill, String area) throws RemoteException;
    List<Worker>  getAllWorkers()                                      throws RemoteException;
    Job           postJob(String customerId, String skill,
                          String area, String description)            throws RemoteException;
    List<Job>     getOpenJobs()                                       throws RemoteException;
    Booking       assignJob(String jobId, String workerId,
                            String customerId, String date)           throws RemoteException;
    boolean       completeJob(String bookingId)                       throws RemoteException;
    double        rateWorker(String workerId, double rating)          throws RemoteException;
    List<Booking> getBookingsForCustomer(String customerId)           throws RemoteException;
    List<Booking> getBookingsForWorker(String workerId)               throws RemoteException;
}