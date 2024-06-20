package com.example.demo.university.service;

import com.example.demo.university.model.Module;
import com.example.demo.university.repository.ModuleRepository;
import com.example.demo.university.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;
    private UniversityRepository universityRepository;

    public ModuleService(ModuleRepository moduleRepository, UniversityRepository universityRepository) {
        this.moduleRepository = moduleRepository;
        this.universityRepository = universityRepository;
    }

    public Optional<Module> findByIdAndUniversityId(Long id, Long universityId) {
        return moduleRepository.findByIdAndUniversityId(id, universityId);
    }

    public Page<Module> findAllByUniversityId(Long universityId, Pageable pageable) {
        return moduleRepository.findByUniversityId(universityId, pageable);
    }

    public Module save(Module module) {
        return moduleRepository.save(module);
    }

    @Transactional
    public boolean deleteModule(Long universityId, Long moduleId) {
        Optional<Module> module = moduleRepository.findByIdAndUniversityId(moduleId, universityId);
        if (!module.isPresent()) {
            return false;
        }
        moduleRepository.deleteByIdAndUniversityId(moduleId, universityId);
        return true;
    }
}
