package com.example.demo.university.controller;

import com.example.demo.university.model.University;
import com.example.demo.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("api/universities")
public class UniversityController {

    @Autowired
    private UniversityService universityService;
    private final PagedResourcesAssembler<University> assembler;

    public UniversityController(UniversityService universityService, PagedResourcesAssembler<University> assembler) {
        this.universityService=universityService;
        this.assembler = assembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<University>> createUniversity(@RequestBody University university) {
        University savedUniversity = universityService.save(university);

        EntityModel<University> entityModel = EntityModel.of(savedUniversity);


        entityModel.add(linkTo(methodOn(UniversityController.class).getUniversityById(savedUniversity.getId())).withSelfRel());
       // entityModel.add(linkTo(methodOn(UniversityController.class).getAllUniversities(Pageable.unpaged())).withRel("universities"));
        //entityModel.add(linkTo(methodOn(ModuleController.class).getAllModulesFromUniversity(savedUniversity.getId(),Pageable)).withRel("Module"));

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(methodOn(UniversityController.class).getUniversityById(savedUniversity.getId())).toUri());
        headers.add("Link", linkTo(methodOn(UniversityController.class).getAllUniversities(null)).withRel("universities").toUri().toString());

        return ResponseEntity.created(headers.getLocation()).headers(headers).body(entityModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<University>> getUniversityById(@PathVariable Long id) {
        Optional<University> university = universityService.findById(id);
        if (university.isPresent()) {
            EntityModel<University> entityModel = EntityModel.of(university.get());
            entityModel.add(linkTo(methodOn(UniversityController.class).getUniversityById(id)).withSelfRel());
           // entityModel.add(linkTo(methodOn(UniModule.class).getAllModulesFromUniversity(id)).withRel("Module"));

           // entityModel.add(linkTo(methodOn(UniversityController.class).getAllUniversities(null, null)).withRel("universities"));
           // entityModel.add(linkTo(methodOn(UniversityController.class).updateUniversity(id, null)).withRel("update"));
           // entityModel.add(linkTo(methodOn(UniversityController.class).deleteUniversity(id)).withRel("delete"));

            HttpHeaders headers = new HttpHeaders();
            Pageable pageable = PageRequest.of(0,10);
           // headers.add("Link", linkTo(methodOn(UniversityController.class).getUniversityById(id)).withSelfRel().toUri().toString());
            headers.add("Link", linkTo(methodOn(UniversityController.class).getAllUniversities(pageable)).withRel("universities").toUri().toString());
            headers.add("Link", linkTo(methodOn(UniversityController.class).updateUniversity(id, null)).withRel("update").toUri().toString());
            headers.add("Link", linkTo(methodOn(UniversityController.class).deleteUniversity(id)).withRel("delete").toUri().toString());

            return ResponseEntity.ok().headers(headers).body(entityModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<PagedModel<EntityModel<University>>> getAllUniversities(@PageableDefault(size = 10) Pageable pageable) {
        Page<University> universities = universityService.findAll(pageable);

        PagedModel<EntityModel<University>> pagedModel = assembler.toModel(universities, university -> {
            EntityModel<University> entityModel = EntityModel.of(university,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class).getUniversityById(university.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ModuleController.class).getAllModulesFromUniversity(university.getId(),pageable)).withRel("modules"));
            return entityModel;
        });
        pagedModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class).getAllUniversities(pageable)).withSelfRel());

        if (universities.hasNext()) {
            Pageable nextPageable = pageable.next();
            pagedModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class)
                    .getAllUniversities(nextPageable)).withRel("next").expand(nextPageable.getPageNumber(), nextPageable.getPageSize()));
        }
        if (universities.hasPrevious()) {
            Pageable previousPageable = pageable.previousOrFirst();
            pagedModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class)
                    .getAllUniversities(previousPageable)).withRel("prev").expand(previousPageable.getPageNumber(), previousPageable.getPageSize()));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class).getUniversityById(null)).withRel("getSingleById").toUri().toString() + " getUniversity");
        headers.add("Link", WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class).createUniversity(null)).withRel("Post").toUri().toString() + " Post");

        return ResponseEntity.ok().headers(headers).body(pagedModel);

       // return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<University>>> searchUniversitiesByName(@RequestParam String name, @PageableDefault(size = 10) Pageable pageable, PagedResourcesAssembler<University> assembler) {
        Page<University> universities = universityService.findByName(name, pageable);

        PagedModel<EntityModel<University>> pagedModel = assembler.toModel(universities, university -> {
            EntityModel<University> entityModel = EntityModel.of(university,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class).getUniversityById(university.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ModuleController.class).getAllModulesFromUniversity(university.getId(),pageable)).withRel("modules"));
            return entityModel;
        });

        pagedModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class).searchUniversitiesByName(name, pageable, assembler)).withSelfRel());

        if (universities.hasNext()) {
            Pageable nextPageable = pageable.next();
            pagedModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class)
                    .searchUniversitiesByName(name, nextPageable, assembler)).withRel("next").expand(nextPageable.getPageNumber(), nextPageable.getPageSize()));
        }
        if (universities.hasPrevious()) {
            Pageable previousPageable = pageable.previousOrFirst();
            pagedModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class)
                    .searchUniversitiesByName(name, previousPageable, assembler)).withRel("prev").expand(previousPageable.getPageNumber(), previousPageable.getPageSize()));
        }

        return ResponseEntity.ok(pagedModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<University>> updateUniversity(@PathVariable Long id, @RequestBody University universityDetails) {
        Optional<University> optionalUniversity = universityService.findById(id);
        if (!optionalUniversity.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        University university = optionalUniversity.get();
        university.setName(universityDetails.getName());
        university.setCountry(universityDetails.getCountry());
        university.setDepartment(universityDetails.getDepartment());

        university.setContactPerson(universityDetails.getContactPerson());
        university.setOutgoingStudents(universityDetails.getOutgoingStudents());
        university.setIncomingStudents(universityDetails.getIncomingStudents());
        university.setSpringSemesterStart(universityDetails.getSpringSemesterStart());
        university.setAutumnSemesterStart(universityDetails.getAutumnSemesterStart());

        University updatedUniversity = universityService.save(university);

        Pageable pageable = PageRequest.of(0,10);

        EntityModel<University> entityModel = EntityModel.of(updatedUniversity);
        entityModel.add(linkTo(methodOn(UniversityController.class).getUniversityById(id)).withSelfRel());
        entityModel.add(linkTo(methodOn(UniversityController.class).getAllUniversities(pageable)).withRel("universities"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", linkTo(methodOn(UniversityController.class).getUniversityById(id)).withSelfRel().toUri().toString());
        headers.add("Link", linkTo(methodOn(UniversityController.class).getAllUniversities(pageable)).withRel("universities").toUri().toString());

        return ResponseEntity.ok().headers(headers).body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        Optional<University> optionalUniversity = universityService.findById(id);
        if (!optionalUniversity.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        universityService.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        Pageable pageable = PageRequest.of(0,10);
        headers.add("Link", linkTo(methodOn(UniversityController.class).getAllUniversities(pageable)).withRel("universities").toUri().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
