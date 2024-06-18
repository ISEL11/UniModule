package com.example.demo.university.repository;


import com.example.demo.university.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ModuleRepository extends PagingAndSortingRepository<Module, Long> {

     Optional<Module> findByIdAndUniversityId(Long id, Long universityId);
     List<Module> findByUniversityId(Long universityId);

     Module save(Module module);

     void deleteById(Long id);

     Optional<Module> findById(Long id);
}
