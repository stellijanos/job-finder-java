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

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public Company updateCompany(Long id, Company companyDetails) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        company.setName(companyDetails.getName());
        company.setEmail(companyDetails.getEmail());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
