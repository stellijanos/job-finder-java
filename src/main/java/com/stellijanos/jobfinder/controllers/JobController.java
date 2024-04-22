package com.stellijanos.jobfinder.controllers;

import com.stellijanos.jobfinder.models.Job;
import com.stellijanos.jobfinder.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    private final JobService jobService;


    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }


    @GetMapping("/jobs")
    public ResponseEntity<Iterable<Job>> getAll() {
        return new ResponseEntity<>(this.jobService.getAll(), HttpStatus.OK);
    }

}
