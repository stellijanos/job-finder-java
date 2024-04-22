package com.stellijanos.jobfinder.repositories;

import com.stellijanos.jobfinder.models.Skill;
import org.springframework.data.repository.CrudRepository;

public interface SkillRepository extends CrudRepository<Skill, Long> {
}
