package com.stellijanos.jobfinder.services;

import com.stellijanos.jobfinder.models.Job;
import com.stellijanos.jobfinder.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public JobService(JobRepository jobRepository, CompanyRepository companyRepository, CategoryRepository categoryRepository, SkillRepository skillRepository, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
        this.applicationRepository = applicationRepository;
    }


    public Iterable<Job> getAll() {

        Iterable<Job> jobs =  this.jobRepository.findAll();

        jobs.forEach(job-> job.setApplications(new HashSet<>()));
        return jobs;
    }


    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public Job updateJob(Long id, Job jobDetails) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setSalary(jobDetails.getSalary());
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

}
