package com.example.demo.university.controller;

import com.example.demo.university.model.Module;
import com.example.demo.university.model.University;
import com.example.demo.university.service.ModuleService;
import com.example.demo.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/universities/{universityId}/modules")
public class UniModule {

    @Autowired
    private UniversityService universityService;

    @Autowired
    private ModuleService moduleService;

    @PostMapping
    public ResponseEntity<EntityModel<Module>> addModuleToUniversity(@PathVariable Long universityId, @RequestBody Module module) {
        Optional<University> universityOptional = universityService.findById(universityId);
        if (!universityOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        module.setUniversity(universityOptional.get());
        Module savedModule = moduleService.save(module);

        EntityModel<Module> entityModel = EntityModel.of(savedModule);
        entityModel.add(linkTo(methodOn(UniModule.class).getModuleFromUniversity(universityId, savedModule.getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(UniModule.class).getAllModulesFromUniversity(universityId)).withRel("modules"));

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(methodOn(UniModule.class).getModuleFromUniversity(universityId, savedModule.getId())).toUri());
        headers.add("Link", linkTo(methodOn(UniModule.class).getModuleFromUniversity(universityId, savedModule.getId())).withSelfRel().toUri().toString());
        headers.add("Link", linkTo(methodOn(UniModule.class).getAllModulesFromUniversity(universityId)).withRel("modules").toUri().toString());

        return ResponseEntity.created(headers.getLocation()).headers(headers).body(entityModel);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<EntityModel<Module>> getModuleFromUniversity(@PathVariable Long universityId, @PathVariable Long moduleId) {
        Optional<Module> module = moduleService.findByIdAndUniversityId(moduleId, universityId);
        if (module.isPresent()) {
            EntityModel<Module> entityModel = EntityModel.of(module.get());
            entityModel.add(linkTo(methodOn(UniModule.class).getModuleFromUniversity(universityId, moduleId)).withSelfRel());
            entityModel.add(linkTo(methodOn(UniModule.class).getAllModulesFromUniversity(universityId)).withRel("modules"));
            entityModel.add(linkTo(methodOn(UniModule.class).updateModuleFromUniversity(universityId, moduleId, null)).withRel("update"));
            entityModel.add(linkTo(methodOn(UniModule.class).deleteModuleFromUniversity(universityId, moduleId)).withRel("delete"));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Link", linkTo(methodOn(UniModule.class).getModuleFromUniversity(universityId, moduleId)).withSelfRel().toUri().toString());
            headers.add("Link", linkTo(methodOn(UniModule.class).getAllModulesFromUniversity(universityId)).withRel("modules").toUri().toString());
            headers.add("Link", linkTo(methodOn(UniModule.class).updateModuleFromUniversity(universityId, moduleId, null)).withRel("update").toUri().toString());
            headers.add("Link", linkTo(methodOn(UniModule.class).deleteModuleFromUniversity(universityId, moduleId)).withRel("delete").toUri().toString());

            return ResponseEntity.ok().headers(headers).body(entityModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<Module>>> getAllModulesFromUniversity(@PathVariable Long universityId) {
        List<Module> modules = moduleService.findByUniversityId(universityId);
        List<EntityModel<Module>> moduleResources = modules.stream()
                .map(module -> EntityModel.of(module,
                        linkTo(methodOn(UniModule.class).getModuleFromUniversity(universityId, module.getId())).withSelfRel()))
                .collect(Collectors.toList());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", linkTo(methodOn(UniModule.class).getAllModulesFromUniversity(universityId)).withSelfRel().toUri().toString());

        return ResponseEntity.ok().headers(headers).body(moduleResources);
    }


    @PutMapping("/{moduleId}")
    public ResponseEntity<EntityModel<Module>> updateModuleFromUniversity(@PathVariable Long universityId, @PathVariable Long moduleId, @RequestBody Module moduleDetails) {
        Optional<Module> optionalModule = moduleService.findByIdAndUniversityId(moduleId, universityId);
        if (!optionalModule.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Module module = optionalModule.get();
        module.setName(moduleDetails.getName());
        module.setSemester(moduleDetails.getSemester());
        module.setCreditPoints(moduleDetails.getCreditPoints());
        Module updatedModule = moduleService.save(module);

        EntityModel<Module> entityModel = EntityModel.of(updatedModule);
        entityModel.add(linkTo(methodOn(UniModule.class).getModuleFromUniversity(universityId, moduleId)).withSelfRel());
        entityModel.add(linkTo(methodOn(UniModule.class).getAllModulesFromUniversity(universityId)).withRel("modules"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", linkTo(methodOn(UniModule.class).getModuleFromUniversity(universityId, moduleId)).withSelfRel().toUri().toString());
        headers.add("Link", linkTo(methodOn(UniModule.class).getAllModulesFromUniversity(universityId)).withRel("modules").toUri().toString());

        return ResponseEntity.ok().headers(headers).body(entityModel);
    }


    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Void> deleteModuleFromUniversity(@PathVariable Long universityId, @PathVariable Long moduleId) {
        Optional<Module> optionalModule = moduleService.findByIdAndUniversityId(moduleId, universityId);
        if (!optionalModule.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        moduleService.deleteById(moduleId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", linkTo(methodOn(UniModule.class).getAllModulesFromUniversity(universityId)).withRel("modules").toUri().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
