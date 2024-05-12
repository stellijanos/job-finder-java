package com.stellijanos.jobfinder.services;

import com.stellijanos.jobfinder.models.User;
import com.stellijanos.jobfinder.repositories.ApplicationRepository;
import com.stellijanos.jobfinder.repositories.JobRepository;
import com.stellijanos.jobfinder.repositories.SkillRepository;
import com.stellijanos.jobfinder.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Transactional
public class UserService {



    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public UserService(UserRepository userRepository, ApplicationRepository applicationRepository, JobRepository jobRepository, SkillRepository skillRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    public Iterable<User> getAll() {
        return this.userRepository.findAll();
    }


    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstname(userDetails.getFirstname());
        user.setLastname(userDetails.getLastname());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
