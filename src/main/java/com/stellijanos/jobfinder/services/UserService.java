package com.stellijanos.jobfinder.services;

import com.stellijanos.jobfinder.models.User;
import com.stellijanos.jobfinder.repositories.ApplicationRepository;
import com.stellijanos.jobfinder.repositories.JobRepository;
import com.stellijanos.jobfinder.repositories.SkillRepository;
import com.stellijanos.jobfinder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public UserService(UserRepository userRepository, ApplicationRepository applicationRepository, JobRepository jobRepository, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }


    public Iterable<User> getAll() {
        return this.userRepository.findAll();
    }
}
