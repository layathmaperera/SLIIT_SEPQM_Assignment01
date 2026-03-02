package com.sliit.se3010_testing.bdd;

import com.sliit.se3010_testing.model.Student;
import com.sliit.se3010_testing.repository.StudentRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private ResponseEntity<Student> response;

    @Given("the system has no students")
    public void the_system_has_no_students() {
        studentRepository.deleteAll();
    }

    @When("I create a student with name {string} and email {string}")
    public void i_create_a_student_with_name_and_email(String name, String email) {
        Student newStudent = Student.builder()
                .name(name)
                .email(email)
                .age(20)
                .course("SE3050")
                .build();
        
        response = restTemplate.postForEntity("/api/students", newStudent, Student.class);
    }

    @When("I request a student with ID {int}")
    public void i_request_a_student_with_id(int id) {
        response = restTemplate.getForEntity("/api/students/" + id, Student.class);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int statusCode) {
        assertThat(response.getStatusCode().value()).isEqualTo(statusCode);
    }

    @Then("the returned student should have the name {string}")
    public void the_returned_student_should_have_the_name(String name) {
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(name);
    }
}
