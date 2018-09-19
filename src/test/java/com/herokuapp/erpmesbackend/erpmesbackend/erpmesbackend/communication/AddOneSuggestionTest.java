package com.herokuapp.erpmesbackend.erpmesbackend.erpmesbackend.communication;

import com.herokuapp.erpmesbackend.erpmesbackend.staff.dto.EmployeeDTO;
import com.herokuapp.erpmesbackend.erpmesbackend.erpmesbackend.FillBaseTemplate;
import com.herokuapp.erpmesbackend.erpmesbackend.communication.dto.SuggestionDTO;
import com.herokuapp.erpmesbackend.erpmesbackend.communication.request.SuggestionRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddOneSuggestionTest extends FillBaseTemplate {

    private SuggestionDTO suggestions;

    @Before
    public void init() {
        super.init();
        String name = suggestionFactory.generateName();
        String description = suggestionFactory.generateDescription();

        EmployeeDTO authorDTO = restTemplate.exchange("/employees/{id}", HttpMethod.GET,
                new HttpEntity<>(null, requestHeaders), EmployeeDTO.class, 1).getBody();
        Long authorId = authorDTO.getId();

        List<EmployeeDTO> recipientDTOs = new ArrayList<>();
        List<Long> recipientIds = new ArrayList<>();

        for (int i = 2; i < 6; i++) {
            EmployeeDTO recipientDTO = restTemplate.exchange("/employees/{id}", HttpMethod.GET,
                    new HttpEntity<>(null, requestHeaders), EmployeeDTO.class, i).getBody();
            recipientIds.add(recipientDTO.getId());
            recipientDTOs.add(recipientDTO);
        }

        suggestionRequest = new SuggestionRequest(name, description, authorId, recipientIds);
        suggestions = new SuggestionDTO(name, description, authorDTO, recipientDTOs);
    }

    @Test
    public void checkIfResponseContainsAddedSuggestion() {
        ResponseEntity<SuggestionDTO> suggestionDTOResponseEntity = restTemplate.postForEntity("/suggestions",
                new HttpEntity<>(suggestionRequest, requestHeaders), SuggestionDTO.class);
        assertThat(suggestionDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        SuggestionDTO body = suggestionDTOResponseEntity.getBody();
        assertTrue(body.checkIfDataEquals(suggestions));
    }
}