package com.stellijanos.jobfinder.services;

import com.stellijanos.jobfinder.models.Skill;
import com.stellijanos.jobfinder.repositories.JobRepository;
import com.stellijanos.jobfinder.repositories.SkillRepository;
import com.stellijanos.jobfinder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public Iterable<Skill> getAll() {
        return this.skillRepository.findAll();
    }
}
