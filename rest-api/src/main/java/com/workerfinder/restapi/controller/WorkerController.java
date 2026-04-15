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

    // No constructor-time RMI connection — look up lazily per request
    // so Spring Boot can start even if the RMI server isn't ready yet.
    private WorkerService getRMI() throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        return (WorkerService) registry.lookup("WorkerService");
    }

    // GET /api/workers?skill=plumber&area=Pune
    @GetMapping("/workers")
    public List<Worker> searchWorkers(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String area) {
        try {
            return getRMI().getWorkersBySkillAndArea(skill, area);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // GET /api/workers/all
    @GetMapping("/workers/all")
    public List<Worker> getAllWorkers() {
        try {
            return getRMI().getAllWorkers();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
