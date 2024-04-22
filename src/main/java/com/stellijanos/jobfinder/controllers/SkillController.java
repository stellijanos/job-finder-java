package com.stellijanos.jobfinder.controllers;

import com.stellijanos.jobfinder.models.Skill;
import com.stellijanos.jobfinder.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SkillController {

    private final SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }


    @GetMapping("/skills")
    public ResponseEntity<Iterable<Skill>> getAll() {
        return ResponseEntity.ok(this.skillService.getAll());
    }
}

