package com.example.demo.university.service;

import com.example.demo.university.model.University;
import com.example.demo.university.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    public University save(University university) {
        return universityRepository.save(university);
    }

    public Optional<University> findById(Long id) {
        return universityRepository.findById(id);
    }

    public Page<University> findAll(Pageable pageable) {
        return universityRepository.findAll(pageable);
    }

    public Page<University> findByName(String name, Pageable pageable) {
        return universityRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public void deleteById(Long id) {
        universityRepository.deleteById(id);
    }
}
