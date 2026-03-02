package com.sliit.se3010_testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sliit.se3010_testing.model.Student;
import com.sliit.se3010_testing.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class Member1AssertionsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        studentRepository.deleteAll();
    }

    @Test
    void testCreateStudent_AssertStatus201AndBody() throws Exception {
        Student student = Student.builder()
                .name("Alice")
                .email("alice@test.com")
                .age(22)
                .course("SE3010")
                .build();

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                // Assert Status Code 201
                .andExpect(status().isCreated())
                // Assert Body Fields match
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@test.com"));
    }

    @Test
    void testGetStudent_AssertStatus200() throws Exception {
        Student savedStudent = studentRepository.save(
                Student.builder().name("Bob").email("bob@test.com").age(23).course("SE3020").build()
        );

        mockMvc.perform(get("/api/students/{id}", savedStudent.getId()))
                // Assert Status Code 200
                .andExpect(status().isOk())
                // Assert Body Fields
                .andExpect(jsonPath("$.name").value("Bob"));
    }

    @Test
    void testGetStudent_NegativeCase_StudentNotFound404() throws Exception {
        mockMvc.perform(get("/api/students/{id}", 999L))
                // Assert Status Code 404 Not Found for missing student
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateStudent_NegativeCase_BadRequest400() throws Exception {
        // Sending invalid/empty payload should theoretically return 400 (if constraints exist).
        // Since we don't have constraints, sending malformed JSON to trigger 400 Bad Request
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                // Assert Status Code 400
                .andExpect(status().isBadRequest());
    }
}
