package com.workerfinder.restapi.controller;

import com.workerfinder.restapi.model.Worker;
import com.workerfinder.restapi.service.WorkerService;
import org.springframework.web.bind.annotation.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class WorkerController {

    private final WorkerService rmi;

    public WorkerController() throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        this.rmi = (WorkerService) registry.lookup("WorkerService");
    }

    // GET /api/workers?skill=plumber&area=Pune
    @GetMapping("/workers")
    public List<Worker> searchWorkers(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String area) {
        try {
            return rmi.getWorkersBySkillAndArea(skill, area);  // ← was getRMI().
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // GET /api/workers/all
    @GetMapping("/workers/all")
    public List<Worker> getAllWorkers() {
        try {
            return rmi.getAllWorkers();  // ← was getRMI().
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}