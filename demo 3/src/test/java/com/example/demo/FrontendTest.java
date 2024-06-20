package com.example.demo;

import com.example.demo.Frontend.Frontend;
import com.example.demo.university.model.University;
import com.example.demo.university.service.UniversityService;
import com.example.demo.university.model.Module;
import com.example.demo.university.service.ModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FrontendTest {

    @Mock
    private UniversityService universityService;

    @Mock
    private ModuleService moduleService;

    @InjectMocks
    private Frontend frontend;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUniversity() {
        University university = new University();
        when(universityService.save(any(University.class))).thenReturn(university);

        frontend.createUniversityTemplate("Test University", "USA");

        verify(universityService, times(1)).save(any(University.class));
    }

    @Test
    void testGetUniversityById() {
        University university = new University();
        when(universityService.findById(anyLong())).thenReturn(Optional.of(university));

        frontend.getUniversityByIdTemplate(1L);

        verify(universityService, times(1)).findById(anyLong());
    }

    @Test
    void testGetAllUniversities() {
        Page<University> universities = mock(Page.class);
        when(universityService.findAll(any(Pageable.class))).thenReturn(universities);

        frontend.getAllUniversitiesTemplate();

        verify(universityService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testUpdateUniversity() {
        University university = new University();
        when(universityService.findById(anyLong())).thenReturn(Optional.of(university));
        when(universityService.save(any(University.class))).thenReturn(university);

        frontend.updateUniversityTemplate(1L, "New Name");

        verify(universityService, times(1)).findById(anyLong());
        verify(universityService, times(1)).save(any(University.class));
    }

    @Test
    void testDeleteUniversity() {
        doNothing().when(universityService).deleteById(anyLong());

        frontend.deleteUniversityTemplate(1L);

        verify(universityService, times(1)).deleteById(anyLong());
    }

    @Test
    void testCreateModule() {
        Module module = new Module();
        University university = new University();
        when(universityService.findById(anyLong())).thenReturn(Optional.of(university));
        when(moduleService.save(any(Module.class))).thenReturn(module);

        frontend.createModuleTemplate(1L, "Test Module");

        verify(moduleService, times(1)).save(any(Module.class));
    }

    @Test
    void testGetModuleById() {
        Module module = new Module();
        when(moduleService.findByIdAndUniversityId(anyLong(), anyLong())).thenReturn(Optional.of(module));

        frontend.getModuleByIdTemplate(1L, 1L);

        verify(moduleService, times(1)).findByIdAndUniversityId(anyLong(), anyLong());
    }

    @Test
    void testGetAllModules() {
        Page<Module> modules = mock(Page.class);
        when(moduleService.findAllByUniversityId(anyLong(), any(Pageable.class))).thenReturn(modules);

        frontend.getAllModulesTemplate(1L);

        verify(moduleService, times(1)).findAllByUniversityId(anyLong(), any(Pageable.class));
    }

    @Test
    void testUpdateModule() {
        Module module = new Module();
        University university = new University();
        when(moduleService.findByIdAndUniversityId(anyLong(), anyLong())).thenReturn(Optional.of(module));
        when(universityService.findById(anyLong())).thenReturn(Optional.of(university));
        when(moduleService.save(any(Module.class))).thenReturn(module);

        frontend.updateModuleTemplate(1L, 1L, "New Name");

        verify(moduleService, times(1)).findByIdAndUniversityId(anyLong(), anyLong());
        verify(moduleService, times(1)).save(any(Module.class));
    }

    @Test
    void testDeleteModule() {
        when(moduleService.deleteModule(anyLong(), anyLong())).thenReturn(true);

        frontend.deleteModuleTemplate(1L, 1L);

        verify(moduleService, times(1)).deleteModule(anyLong(), anyLong());
    }
}
