package com.test.BankOperationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.BankOperationService.controllers.personAuth.PersonAuthController;
import com.test.BankOperationService.controllers.personAuth.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PersonAuthController.class)
@AutoConfigureMockMvc
public class PersonAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUserWithValidData() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("1234567890");
        request.setEmail("ivan@example.com");
        request.setInitialDeposit(100.0);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateUserWithMissingFields() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        // Missing phone and email fields
        request.setInitialDeposit(100.0);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("At least one phone number must be provided"));
    }
}
