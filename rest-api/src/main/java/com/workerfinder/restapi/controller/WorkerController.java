package com.workerfinder.restapi.controller;

import com.workerfinder.restapi.model.Worker;
import com.workerfinder.restapi.service.WorkerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/workers")
public class WorkerController {

    @Autowired
    private WorkerServiceImpl service;

    // GET /api/workers?skill=plumber&area=Pune
    @GetMapping
    public List<Worker> searchWorkers(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String area) {

        return service.findWorkers(skill, area);
    }

    // GET /api/workers/all
    @GetMapping("/all")
    public List<Worker> getAllWorkers() {
        return service.getAllWorkers();
    }
}
