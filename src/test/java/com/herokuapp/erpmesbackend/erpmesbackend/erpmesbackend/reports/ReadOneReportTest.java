package com.herokuapp.erpmesbackend.erpmesbackend.erpmesbackend.reports;

import com.herokuapp.erpmesbackend.erpmesbackend.finance.MonthlyReport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReadOneReportTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void checkIfResponseContainsReportWithGivenId() {
        ResponseEntity<MonthlyReport> forEntity = restTemplate.getForEntity("/reports/{id}",
                MonthlyReport.class, 3);
        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(forEntity.getBody().getId()).isEqualTo(3);
    }
}