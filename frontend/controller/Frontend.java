package com.example.demo.controller;

import com.example.demo.university.model.Module;
import com.example.demo.university.model.University;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Frontend {

    private final String baseUrl;
    private final RestTemplate restTemplate;
    private final Traverson traverson;

    public Frontend(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
        this.traverson = new Traverson(URI.create(baseUrl), MediaTypes.HAL_JSON);
    }

    public University createUniversity(University university) {
        RequestEntity<University> request = RequestEntity.post(URI.create(baseUrl + "/api/universities"))
                .body(university);
        ResponseEntity<University> response = restTemplate.exchange(request, University.class);
        return response.getBody();
    }

    public University getUniversity(Long id) {
        String url = baseUrl + "/api/universities/" + id;
        return restTemplate.getForObject(url, University.class);
    }

    public PagedModel<University> getAllUniversities() {
        ParameterizedTypeReference<PagedModel<University>> typeRef = new ParameterizedTypeReference<>() {};
        return traverson.follow("universities").toObject(typeRef);
    }

    public University updateUniversity(Long id, University university) {
        String url = baseUrl + "/api/universities/" + id;
        restTemplate.put(url, university);
        return getUniversity(id);
    }

    public void deleteUniversity(Long id) {
        String url = baseUrl + "/api/universities/" + id;
        restTemplate.delete(url);
    }

    public Module createModule(Long universityId, Module module) {
        String url = baseUrl + "/api/universities/" + universityId + "/modules";
        ResponseEntity<Module> response = restTemplate.postForEntity(url, module, Module.class);
        return response.getBody();
    }

    public Module getModule(Long universityId, Long moduleId) {
        String url = baseUrl + "/api/universities/" + universityId + "/modules/" + moduleId;
        return restTemplate.getForObject(url, Module.class);
    }

    public PagedModel<Module> getAllModules(Long universityId) {
        ParameterizedTypeReference<PagedModel<Module>> typeRef = new ParameterizedTypeReference<>() {};
        return traverson.follow("modules").withTemplateParameters(Map.of("universityId", universityId.toString())).toObject(typeRef);
    }

    public Module updateModule(Long universityId, Long moduleId, Module module) {
        String url = baseUrl + "/api/universities/" + universityId + "/modules/" + moduleId;
        restTemplate.put(url, module);
        return getModule(universityId, moduleId);
    }

    public void deleteModule(Long universityId, Long moduleId) {
        String url = baseUrl + "/api/universities/" + universityId + "/modules/" + moduleId;
        restTemplate.delete(url);
    }
}
