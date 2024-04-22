package com.stellijanos.jobfinder.services;

import com.stellijanos.jobfinder.models.Company;
import com.stellijanos.jobfinder.repositories.CompanyRepository;
import com.stellijanos.jobfinder.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, JobRepository jobRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
    }


    public Iterable<Company> getAll() {
        return this.companyRepository.findAll();
    }
}
