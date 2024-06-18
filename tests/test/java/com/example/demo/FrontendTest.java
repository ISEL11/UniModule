package com.example.demo;

//

import com.example.demo.controller.Frontend;
import com.example.demo.university.model.Module;
import com.example.demo.university.model.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.PagedModel;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FrontendTest {

    private Frontend frontend;

    @BeforeEach
    public void setUp() {
        frontend = new Frontend("http://localhost:8080");
    }

    @Test
    public void testCreateAndGetUniversity() {
        University university = new University();
        university.setName("Test University");
        university.setCountry("Test Country");
        university.setDepartment("Test Department");

        University createdUniversity = frontend.createUniversity(university);
        assertNotNull(createdUniversity);
        assertEquals("Test University", createdUniversity.getName());

        University fetchedUniversity = frontend.getUniversity(createdUniversity.getId());
        assertNotNull(fetchedUniversity);
        assertEquals("Test University", fetchedUniversity.getName());
    }

    @Test
    public void testGetAllUniversities() {
        PagedModel<University> universities = frontend.getAllUniversities();
        assertNotNull(universities);
    }

    @Test
    public void testUpdateUniversity() {
        University university = new University();
        university.setName("Old Name");
        university = frontend.createUniversity(university);

        university.setName("New Name");
        University updatedUniversity = frontend.updateUniversity(university.getId(), university);
        assertEquals("New Name", updatedUniversity.getName());
    }

    @Test
    public void testDeleteUniversity() {
        University university = new University();
        university.setName("To Be Deleted");
        university = frontend.createUniversity(university);

        frontend.deleteUniversity(university.getId());
        assertNull(frontend.getUniversity(university.getId()));
    }

    @Test
    public void testCreateAndGetModule() {
        University university = new University();
        university.setName("Module University");
        university = frontend.createUniversity(university);

        Module module = new Module();
        module.setName("Test Module");
        module.setCreditPoints(10);

        Module createdModule = frontend.createModule(university.getId(), module);
        assertNotNull(createdModule);
        assertEquals("Test Module", createdModule.getName());

        Module fetchedModule = frontend.getModule(university.getId(), createdModule.getId());
        assertNotNull(fetchedModule);
        assertEquals("Test Module", fetchedModule.getName());
    }

    @Test
    public void testGetAllModules() {
        University university = new University();
        university.setName("Modules University");
        university = frontend.createUniversity(university);

        PagedModel<Module> modules = frontend.getAllModules(university.getId());
        assertNotNull(modules);
    }

    @Test
    public void testUpdateModule() {
        University university = new University();
        university.setName("Module Update University");
        university = frontend.createUniversity(university);

        Module module = new Module();
        module.setName("Old Module Name");
        module = frontend.createModule(university.getId(), module);

        module.setName("New Module Name");
        Module updatedModule = frontend.updateModule(university.getId(), module.getId(), module);
        assertEquals("New Module Name", updatedModule.getName());
    }

    @Test
    public void testDeleteModule() {
        University university = new University();
        university.setName("Module Delete University");
        university = frontend.createUniversity(university);

        Module module = new Module();
        module.setName("To Be Deleted");
        module = frontend.createModule(university.getId(), module);

        frontend.deleteModule(university.getId(), module.getId());
        assertNull(frontend.getModule(university.getId(), module.getId()));
    }
}
