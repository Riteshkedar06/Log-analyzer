package com.loganalyzer.platform.project.repository;

import com.loganalyzer.platform.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsRepository extends JpaRepository<Project, Integer> {

}
