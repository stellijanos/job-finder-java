package com.stellijanos.jobfinder.controllers;

import com.stellijanos.jobfinder.models.Company;
import com.stellijanos.jobfinder.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
