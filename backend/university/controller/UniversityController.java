package com.example.demo.university.controller;

import com.example.demo.university.model.University;
import com.example.demo.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
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
@RequestMapping("/api/universities")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @PostMapping
    public ResponseEntity<EntityModel<University>> createUniversity(@RequestBody University university) {
        University savedUniversity = universityService.save(university);

        EntityModel<University> entityModel = EntityModel.of(savedUniversity);
        entityModel.add(linkTo(methodOn(UniversityController.class).getUniversityById(savedUniversity.getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(UniversityController.class).getAllUniversities(null, null)).withRel("universities"));

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(methodOn(UniversityController.class).getUniversityById(savedUniversity.getId())).toUri());
        headers.add("Link", linkTo(methodOn(UniversityController.class).getAllUniversities(null, null)).withRel("universities").toUri().toString());

        return ResponseEntity.created(headers.getLocation()).headers(headers).body(entityModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<University>> getUniversityById(@PathVariable Long id) {
        Optional<University> university = universityService.findById(id);
        if (university.isPresent()) {
            EntityModel<University> entityModel = EntityModel.of(university.get());
            entityModel.add(linkTo(methodOn(UniversityController.class).getUniversityById(id)).withSelfRel());
            entityModel.add(linkTo(methodOn(UniversityController.class).getAllUniversities(null, null)).withRel("universities"));
            entityModel.add(linkTo(methodOn(UniversityController.class).updateUniversity(id, null)).withRel("update"));
            entityModel.add(linkTo(methodOn(UniversityController.class).deleteUniversity(id)).withRel("delete"));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Link", linkTo(methodOn(UniversityController.class).getUniversityById(id)).withSelfRel().toUri().toString());
            headers.add("Link", linkTo(methodOn(UniversityController.class).getAllUniversities(null, null)).withRel("universities").toUri().toString());
            headers.add("Link", linkTo(methodOn(UniversityController.class).updateUniversity(id, null)).withRel("update").toUri().toString());
            headers.add("Link", linkTo(methodOn(UniversityController.class).deleteUniversity(id)).withRel("delete").toUri().toString());

            return ResponseEntity.ok().headers(headers).body(entityModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<University>>> getAllUniversities(
            @RequestParam(required = false) String name, Pageable pageable) {
        Page<University> universities;
        if (name != null) {
            universities = universityService.findByName(name, pageable);
        } else {
            universities = universityService.findAll(pageable);
        }

        List<EntityModel<University>> universityResources = universities.stream()
                .map(university -> EntityModel.of(university,
                        linkTo(methodOn(UniversityController.class).getUniversityById(university.getId())).withSelfRel()))
                .collect(Collectors.toList());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", linkTo(methodOn(UniversityController.class).getAllUniversities(null, pageable)).withSelfRel().toUri().toString());

        return ResponseEntity.ok().headers(headers).body(universityResources);
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

        EntityModel<University> entityModel = EntityModel.of(updatedUniversity);
        entityModel.add(linkTo(methodOn(UniversityController.class).getUniversityById(id)).withSelfRel());
        entityModel.add(linkTo(methodOn(UniversityController.class).getAllUniversities(null, null)).withRel("universities"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", linkTo(methodOn(UniversityController.class).getUniversityById(id)).withSelfRel().toUri().toString());
        headers.add("Link", linkTo(methodOn(UniversityController.class).getAllUniversities(null, null)).withRel("universities").toUri().toString());

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
        headers.add("Link", linkTo(methodOn(UniversityController.class).getAllUniversities(null, null)).withRel("universities").toUri().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
