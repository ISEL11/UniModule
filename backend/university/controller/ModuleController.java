package com.example.demo.university.controller;

import com.example.demo.university.model.Module;
import com.example.demo.university.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Module>>> getAllModules(@PageableDefault(size = 10) Pageable pageable, PagedResourcesAssembler<Module> assembler) {
        Page<Module> modules = moduleService.findAll(pageable);
        PagedModel<EntityModel<Module>> pagedModel = assembler.toModel(modules, module -> {
            EntityModel<Module> entityModel = EntityModel.of(module);
            entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getModuleById(module.getId())).withSelfRel());
            return entityModel;
        });

        Link link = WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules(pageable, assembler)).withSelfRel();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LINK, link.toString());

        return ResponseEntity.ok().headers(headers).body(pagedModel);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Module>> createModule(@RequestBody Module module) {
        Module savedModule = moduleService.save(module);
        URI location = URI.create("/api/modules/" + savedModule.getId());

        EntityModel<Module> entityModel = EntityModel.of(savedModule);
        entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getModuleById(savedModule.getId())).withSelfRel());
        entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules(Pageable.unpaged(), null)).withRel("modules"));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, location.toString());

        return ResponseEntity.created(location).headers(headers).body(entityModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> getModuleById(@PathVariable Long id) {
        Optional<Module> moduleOptional = moduleService.findById(id);
        if (moduleOptional.isPresent()) {
            Module module = moduleOptional.get();
            EntityModel<Module> entityModel = EntityModel.of(module);
            entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getModuleById(id)).withSelfRel());
            entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules(Pageable.unpaged(), null)).withRel("modules"));
            entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).updateModule(id, module)).withRel("update"));
            entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).deleteModule(id)).withRel("delete"));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LINK, WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getModuleById(id)).withSelfRel().toUri().toString());
            headers.add(HttpHeaders.LINK, WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules(Pageable.unpaged(), null)).withRel("modules").toUri().toString());
            headers.add(HttpHeaders.LINK, WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).updateModule(id, module)).withRel("update").toUri().toString());
            headers.add(HttpHeaders.LINK, WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).deleteModule(id)).withRel("delete").toUri().toString());

            return ResponseEntity.ok().headers(headers).body(entityModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> updateModule(@PathVariable Long id, @RequestBody Module updatedModule) {
        if (!moduleService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        updatedModule.setId(id);
        Module savedModule = moduleService.save(updatedModule);

        EntityModel<Module> entityModel = EntityModel.of(savedModule);
        entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getModuleById(id)).withSelfRel());
        entityModel.add(WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules(Pageable.unpaged(), null)).withRel("modules"));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LINK, WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getModuleById(id)).withSelfRel().toUri().toString());
        headers.add(HttpHeaders.LINK, WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules(Pageable.unpaged(), null)).withRel("modules").toUri().toString());

        return ResponseEntity.ok().headers(headers).body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        if (!moduleService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        moduleService.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LINK, WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules(Pageable.unpaged(), null)).withRel("modules").toUri().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
