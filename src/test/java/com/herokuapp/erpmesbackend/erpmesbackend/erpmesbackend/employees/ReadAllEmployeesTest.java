package com.herokuapp.erpmesbackend.erpmesbackend.erpmesbackend.employees;

import com.herokuapp.erpmesbackend.erpmesbackend.staff.employees.Employee;
import com.herokuapp.erpmesbackend.erpmesbackend.staff.employees.EmployeeFactory;
import com.herokuapp.erpmesbackend.erpmesbackend.staff.employees.EmployeeRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReadAllEmployeesTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private EmployeeFactory employeeFactory;
    private List<EmployeeRequest> employeeRequests;

    @Before
    public void init() {
        employeeFactory = new EmployeeFactory();
        employeeRequests = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            employeeRequests.add(employeeFactory.generateEmployeeRequest());
        }

        employeeRequests.forEach(request -> restTemplate.postForEntity("/employees",
                request, Employee.class));
    }

    @Test
    public void checkIfResponseContainsAllEmployees() {
        ResponseEntity<Employee[]> forEntity = restTemplate
                .getForEntity("/employees", Employee[].class);

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Employee> employees = Arrays.asList(forEntity.getBody());
        for(Employee employee : employees) {
            assertTrue(employeeRequests.stream()
                    .anyMatch(request -> request.extractUser().checkIfDataEquals(employee)));
        }
    }

    @After
    public void clean() {
        for(int i = 0; i < 10; i++) {
            restTemplate.delete("/employees/{id}", i+1);
        }
    }
}