package com.example.demo.university.repository;

import com.example.demo.university.model.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    Page<University> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
