package com.sliit.se3010_testing;

import com.sliit.se3010_testing.controller.StudentController;
import com.sliit.se3010_testing.model.Student;
import com.sliit.se3010_testing.repository.StudentRepository;
import com.sliit.se3010_testing.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Member3MockingTest {

    // Feature 3: Mock the StudentRepository using Mockito
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    // Feature 3: Mocking service for controller isolation
    @Mock
    private StudentService mockedStudentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testServiceWithMockedRepository() {
        // Arrange: Stubbing repository response
        Student mockStudent = new Student(1L, "Eve", "eve@test.com", 22, "SE3050");
        when(studentRepository.save(any(Student.class))).thenReturn(mockStudent);

        // Act
        Student saved = studentService.createStudent(mockStudent);

        // Assert
        assertThat(saved.getName()).isEqualTo("Eve");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testControllerInIsolation_WithoutHittingDB() {
        // Feature 3: Test controller in isolation without hitting real DB (Service layer is stubbed)
        Student mockStudent = new Student(2L, "Frank", "frank@test.com", 25, "SE3060");
        
        // Stub service layer responses
        when(mockedStudentService.getStudentById(2L)).thenReturn(Optional.of(mockStudent));

        // Act
        ResponseEntity<Student> response = studentController.getStudentById(2L);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Frank");
        
        // Verify service was called exactly once
        verify(mockedStudentService, times(1)).getStudentById(2L);
    }
}
