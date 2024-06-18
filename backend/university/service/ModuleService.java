package com.example.demo.university.service;

import com.example.demo.university.model.Module;
import com.example.demo.university.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    public Optional<Module> findById(Long id) {
        return moduleRepository.findById(id);
    }
    public Page<Module> findAll(Pageable pageable) {
        return moduleRepository.findAll(pageable);
    }

    public Optional<Module> findByIdAndUniversityId(Long id, Long universityId) {
        return moduleRepository.findByIdAndUniversityId(id, universityId);
    }

    public List<Module> findByUniversityId(Long universityId) {
        return moduleRepository.findByUniversityId(universityId);
    }

    public Module save(Module module) {
        return moduleRepository.save(module);
    }

    public void deleteById(Long id) {
        moduleRepository.deleteById(id);
    }
}

