package com.example.demo;

import com.example.demo.university.controller.ModuleController;
import com.example.demo.university.model.Module;
import com.example.demo.university.service.ModuleService;
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

@WebMvcTest(ModuleController.class)
public class ModuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModuleService moduleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllModules() throws Exception {
        Module module1 = new Module();
        module1.setId(1L);
        module1.setName("Module 1");

        Module module2 = new Module();
        module2.setId(2L);
        module2.setName("Module 2");

        given(moduleService.findAll(PageRequest.of(0, 10)))
                .willReturn(new PageImpl<>(Arrays.asList(module1, module2)));

        mockMvc.perform(get("/api/modules")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.moduleList").exists());
    }

    @Test
    public void testGetModuleById() throws Exception {
        Module module = new Module();
        module.setId(1L);
        module.setName("Test Module");

        given(moduleService.findById(anyLong())).willReturn(Optional.of(module));

        mockMvc.perform(get("/api/modules/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Module"));
    }

    @Test
    public void testCreateModule() throws Exception {
        Module module = new Module();
        module.setId(1L);
        module.setName("New Module");

        given(moduleService.save(any(Module.class))).willReturn(module);

        mockMvc.perform(post("/api/modules")
                        .contentType("application/json")
                        .content("{\"name\": \"New Module\"}")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/modules/1"))
                .andExpect(jsonPath("$.name").value("New Module"));
    }

    @Test
    public void testUpdateModule() throws Exception {
        Module module = new Module();
        module.setId(1L);
        module.setName("Updated Module");

        given(moduleService.findById(anyLong())).willReturn(Optional.of(module));
        given(moduleService.save(any(Module.class))).willReturn(module);

        mockMvc.perform(put("/api/modules/1")
                        .contentType("application/json")
                        .content("{\"name\": \"Updated Module\"}")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Module"));
    }

    @Test
    public void testDeleteModule() throws Exception {
        Module module = new Module();
        module.setId(1L);
        module.setName("Test Module");

        given(moduleService.findById(anyLong())).willReturn(Optional.of(module));

        mockMvc.perform(delete("/api/modules/1"))
                .andExpect(status().isNoContent());
    }
}
