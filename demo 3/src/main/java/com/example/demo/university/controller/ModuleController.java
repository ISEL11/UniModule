package com.example.demo.university.controller;

import com.example.demo.university.model.Module;
import com.example.demo.university.service.ModuleService;
import com.example.demo.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/universities/{uniId}/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private PagedResourcesAssembler<Module> assembler;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Module>>> getAllModulesFromUniversity(@PathVariable Long uniId, Pageable pageable) {
        Page<Module> modules = moduleService.findAllByUniversityId(uniId, pageable);

        PagedModel<EntityModel<Module>> pagedModel = assembler.toModel(modules, module -> {
            EntityModel<Module> entityModel = EntityModel.of(module,
                    linkTo(methodOn(ModuleController.class).getModuleById(uniId, module.getId())).withSelfRel(),
                    linkTo(methodOn(ModuleController.class).getAllModulesFromUniversity(uniId, pageable)).withRel("modules"));
            return entityModel;
        });

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LINK, linkTo(methodOn(ModuleController.class).getAllModulesFromUniversity(uniId, pageable)).withSelfRel().toUri().toString());

        if (modules.hasNext()) {
            Pageable nextPageable = pageable.next();
            pagedModel.add(linkTo(methodOn(ModuleController.class)
                    .getAllModulesFromUniversity(uniId, nextPageable)).withRel("next").expand(nextPageable.getPageNumber(), nextPageable.getPageSize()));
        }
        if (modules.hasPrevious()) {
            Pageable previousPageable = pageable.previousOrFirst();
            pagedModel.add(linkTo(methodOn(ModuleController.class)
                    .getAllModulesFromUniversity(uniId, previousPageable)).withRel("prev").expand(previousPageable.getPageNumber(), previousPageable.getPageSize()));
        }

        return ResponseEntity.ok().headers(headers).body(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> getModuleById(@PathVariable Long uniId, @PathVariable Long id) {
        Optional<Module> moduleOptional = moduleService.findByIdAndUniversityId(id, uniId);
        if (moduleOptional.isPresent()) {
            Module module = moduleOptional.get();
            EntityModel<Module> entityModel = EntityModel.of(module,
                    linkTo(methodOn(ModuleController.class).getModuleById(uniId, id)).withSelfRel(),
                    linkTo(methodOn(ModuleController.class).getAllModulesFromUniversity(uniId, Pageable.unpaged())).withRel("modules"));

            return ResponseEntity.ok(entityModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EntityModel<Module>> createModule(@PathVariable Long uniId, @RequestBody Module module) {
        module.setUniversity(universityService.findById(uniId).orElseThrow(() -> new IllegalArgumentException("Invalid university ID")));
        Module createdModule = moduleService.save(module);

        EntityModel<Module> entityModel = EntityModel.of(createdModule,
                linkTo(methodOn(ModuleController.class).getModuleById(uniId, createdModule.getId())).withSelfRel(),
                linkTo(methodOn(ModuleController.class).getAllModulesFromUniversity(uniId, Pageable.unpaged())).withRel("modules"));

        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> updateModule(@PathVariable Long uniId, @PathVariable Long id, @RequestBody Module moduleDetails) {
        Optional<Module> moduleOptional = moduleService.findByIdAndUniversityId(uniId, id);
        if (moduleOptional.isPresent()) {
            Module module = moduleOptional.get();
            module.setName(moduleDetails.getName());
            module.setUniversity(universityService.findById(uniId).orElseThrow(() -> new IllegalArgumentException("Invalid university ID")));

            Module updatedModule = moduleService.save(module);

            EntityModel<Module> entityModel = EntityModel.of(updatedModule,
                    linkTo(methodOn(ModuleController.class).getModuleById(uniId, updatedModule.getId())).withSelfRel(),
                    linkTo(methodOn(ModuleController.class).getAllModulesFromUniversity(uniId, Pageable.unpaged())).withRel("modules"));

            return ResponseEntity.ok(entityModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long uniId, @PathVariable Long id) {
        boolean isDeleted = moduleService.deleteModule(uniId, id);

        if (!isDeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
