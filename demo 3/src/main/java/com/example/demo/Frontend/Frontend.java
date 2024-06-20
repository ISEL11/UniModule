package com.example.demo.Frontend;

import com.example.demo.university.model.University;
import com.example.demo.university.service.UniversityService;
import com.example.demo.university.model.Module;
import com.example.demo.university.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Frontend {


    @Autowired
    private UniversityService universityService;

    @Autowired
    private ModuleService moduleService;

    public void createUniversityTemplate(String name, String country) {
        University university = new University();
        university.setName(name);
        university.setCountry(country);
        universityService.save(university);
        System.out.println("University created");
    }

    public void getUniversityByIdTemplate(Long id) {
        Optional<University> university = universityService.findById(id);
        if (university.isPresent()) {
            System.out.println(university.get());
        } else {
            System.out.println("University not found");
        }
    }

    public void getAllUniversitiesTemplate() {
        Pageable pageable = PageRequest.of(0, 10);
        universityService.findAll(pageable).forEach(System.out::println);
    }

    public void updateUniversityTemplate(Long id, String newName) {
        Optional<University> universityOptional = universityService.findById(id);
        if (universityOptional.isPresent()) {
            University university = universityOptional.get();
            university.setName(newName);
            universityService.save(university);
            System.out.println("University updated");
        } else {
            System.out.println("University not found");
        }
    }

    public void deleteUniversityTemplate(Long id) {
        universityService.deleteById(id);
        System.out.println("University deleted");
    }

    public void createModuleTemplate(Long uniId, String moduleName) {
        Module module = new Module();
        module.setName(moduleName);
        module.setUniversity(universityService.findById(uniId).orElseThrow(() -> new IllegalArgumentException("Invalid university ID")));
        moduleService.save(module);
        System.out.println("Module created");
    }

    public void getModuleByIdTemplate(Long uniId, Long id) {
        Optional<Module> module = moduleService.findByIdAndUniversityId(id, uniId);
        if (module.isPresent()) {
            System.out.println(module.get());
        } else {
            System.out.println("Module not found");
        }
    }

    public void getAllModulesTemplate(Long uniId) {
        Pageable pageable = PageRequest.of(0, 10);
        moduleService.findAllByUniversityId(uniId, pageable).forEach(System.out::println);
    }

    public void updateModuleTemplate(Long uniId, Long id, String newName) {
        Optional<Module> moduleOptional = moduleService.findByIdAndUniversityId(uniId, id);
        if (moduleOptional.isPresent()) {
            Module module = moduleOptional.get();
            module.setName(newName);
            module.setUniversity(universityService.findById(uniId).orElseThrow(() -> new IllegalArgumentException("Invalid university ID")));
            moduleService.save(module);
            System.out.println("Module updated");
        } else {
            System.out.println("Module not found");
        }
    }

    public void deleteModuleTemplate(Long uniId, Long id) {
        moduleService.deleteModule(uniId, id);
        System.out.println("Module deleted");
    }
}
