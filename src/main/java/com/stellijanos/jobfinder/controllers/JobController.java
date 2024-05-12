package com.stellijanos.jobfinder.controllers;

import com.stellijanos.jobfinder.models.Job;
import com.stellijanos.jobfinder.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/job")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job createdJob = jobService.createJob(job);
        return ResponseEntity.ok(createdJob);
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/job/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        Job updatedJob = jobService.updateJob(id, jobDetails);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/job/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok().build();
    }
}
