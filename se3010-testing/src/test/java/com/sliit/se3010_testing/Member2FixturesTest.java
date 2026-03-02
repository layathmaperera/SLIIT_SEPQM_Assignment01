package com.sliit.se3010_testing;

import com.sliit.se3010_testing.model.Student;
import com.sliit.se3010_testing.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class Member2FixturesTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent;

    // Feature 2: @BeforeEach to insert test data before every test
    @BeforeEach
    void setUpTestData() {
        System.out.println("Running @BeforeEach: Setting up test data...");
        studentRepository.deleteAll();
        testStudent = createReusableStudentData("Charlie", "charlie@test.com", 21, "SE3010");
        studentRepository.save(testStudent);
    }

    // Feature 2: @AfterEach to clean up after every test
    @AfterEach
    void cleanUpTestData() {
        System.out.println("Running @AfterEach: Cleaning up test data...");
        studentRepository.deleteAll();
    }

    // Feature 2: Reusable test data setup methods
    private Student createReusableStudentData(String name, String email, int age, String course) {
        return Student.builder()
                .name(name)
                .email(email)
                .age(age)
                .course(course)
                .build();
    }

    @Test
    void testFixtureDataIsPresent() {
        // Given that setUpTestData() ran, the DB should have 1 record
        List<Student> students = studentRepository.findAll();
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getName()).isEqualTo("Charlie");
    }

    @Test
    void testReusableMethodForMultipleStudents() {
        // Using the reusable test data setup method inside tests as well
        Student additionalStudent = createReusableStudentData("Diana", "diana@test.com", 24, "SE3040");
        studentRepository.save(additionalStudent);

        assertThat(studentRepository.findAll()).hasSize(2); // The fixture + this new one
    }
}
