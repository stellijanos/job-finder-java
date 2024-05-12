package com.stellijanos.jobfinder.controllers;

import com.stellijanos.jobfinder.models.Company;
import com.stellijanos.jobfinder.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }


    @GetMapping("/companies")
    public ResponseEntity<Iterable<Company>> getAll() {
        return ResponseEntity.ok(companyService.getAll());
    }

    @PostMapping("/company")
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company createdCompany = companyService.createCompany(company);
        return ResponseEntity.ok(createdCompany);
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @PutMapping("/company/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company companyDetails) {
        Company updatedCompany = companyService.updateCompany(id, companyDetails);
        return ResponseEntity.ok(updatedCompany);
    }


    @DeleteMapping("/company/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok().build();
    }
}
