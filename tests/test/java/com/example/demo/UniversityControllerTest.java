package com.example.demo;

import com.example.demo.university.controller.UniversityController;
import com.example.demo.university.model.University;
import com.example.demo.university.service.UniversityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UniversityController.class)
public class UniversityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UniversityService universityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUniversities() throws Exception {
        University university1 = new University();
        university1.setId(1L);
        university1.setName("University 1");

        University university2 = new University();
        university2.setId(2L);
        university2.setName("University 2");

        given(universityService.findAll(PageRequest.of(0, 10)))
                .willReturn(new PageImpl<>(Arrays.asList(university1, university2)));

        mockMvc.perform(get("/api/universities")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.universityList").exists());
    }

    @Test
    public void testGetUniversityById() throws Exception {
        University university = new University();
        university.setId(1L);
        university.setName("Test University");

        given(universityService.findById(anyLong())).willReturn(Optional.of(university));

        mockMvc.perform(get("/api/universities/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test University"));
    }

    @Test
    public void testCreateUniversity() throws Exception {
        University university = new University();
        university.setId(1L);
        university.setName("New University");

        given(universityService.save(any(University.class))).willReturn(university);

        mockMvc.perform(post("/api/universities")
                        .contentType("application/json")
                        .content("{\"name\": \"New University\"}")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/universities/1"))
                .andExpect(jsonPath("$.name").value("New University"));
    }

    @Test
    public void testUpdateUniversity() throws Exception {
        University university = new University();
        university.setId(1L);
        university.setName("Updated University");

        given(universityService.findById(anyLong())).willReturn(Optional.of(university));
        given(universityService.save(any(University.class))).willReturn(university);

        mockMvc.perform(put("/api/universities/1")
                        .contentType("application/json")
                        .content("{\"name\": \"Updated University\"}")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated University"));
    }

    @Test
    public void testDeleteUniversity() throws Exception {
        University university = new University();
        university.setId(1L);
        university.setName("Test University");

        given(universityService.findById(anyLong())).willReturn(Optional.of(university));

        mockMvc.perform(delete("/api/universities/1"))
                .andExpect(status().isNoContent());
    }
}