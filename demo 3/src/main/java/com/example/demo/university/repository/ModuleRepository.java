package com.example.demo.university.repository;

import com.example.demo.university.model.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

     Optional<Module> findByIdAndUniversityId(Long id, Long universityId);

     Page<Module> findByUniversityId(Long universityId, Pageable pageable);

     @Transactional
     void deleteByIdAndUniversityId(Long moduleId, Long universityId);
}
