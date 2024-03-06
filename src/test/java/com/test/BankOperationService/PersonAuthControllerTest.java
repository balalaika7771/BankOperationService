package com.test.BankOperationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.BankOperationService.controllers.personAuth.AuthenticationResponse;
import com.test.BankOperationService.controllers.personAuth.PersonAuthController;
import com.test.BankOperationService.controllers.personAuth.CreateUserRequest;
import com.test.BankOperationService.controllers.transfer.TransferRequest;
import com.test.BankOperationService.model.Account.AccountResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_Successfully() throws Exception {
        List<String> keys = new ArrayList<>(10);
        for(Integer i = 0; i < 10;i++){
            // Создаем объект запроса для регистрации пользователя
            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setFirstName("John " + i.toString());
            createUserRequest.setLastName("Doe "+ i.toString());
            createUserRequest.setDateOfBirth("02.03." + String.valueOf(i + 2000));
            createUserRequest.setPhone("1234567890" + i.toString());
            createUserRequest.setPassword("password" + i.toString());
            createUserRequest.setEmail("john"+ i.toString() +".doe"+ i.toString() +"@example.com");
            createUserRequest.setInitialDeposit(1000 * i);

            // Преобразуем объект запроса в JSON
            String requestBody = objectMapper.writeValueAsString(createUserRequest);

            // Выполняем POST запрос на регистрацию пользователя
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andReturn();

            // Преобразуем ответ в объект класса AuthenticationResponse
            String responseBody = mvcResult.getResponse().getContentAsString();
            AuthenticationResponse authenticationResponse = objectMapper.readValue(responseBody, AuthenticationResponse.class);
            keys.add(authenticationResponse.getAccessToken());
        }
        List<AccountResponse[]> accountResponses = new ArrayList<>(10);
        for(Integer i = 0; i < 10;i++) {
            // Создаем заголовок с авторизационным токеном
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + keys.get(i));

            // Выполняем GET запрос на получение счетов пользователя с установленным заголовком
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .headers(headers))
                    .andExpect(status().isOk())
                    .andReturn();

            // Преобразуем ответ в объект класса AccountResponse
            String responseBody = mvcResult.getResponse().getContentAsString();
            accountResponses.add(objectMapper.readValue(responseBody, AccountResponse[].class));
        }

        for(Integer i = 0; i < 10;i++) {
            assertEquals(String.valueOf(1000*i),String.valueOf((int) Arrays.stream(accountResponses.get(i)).toList().get(0).getInitialDeposit()),false);
        }
        Long numberBox = Arrays.stream(accountResponses.get(0)).toList().get(0).getNumber();
        for(Integer i = 1; i < 10;i++) {
            // Создаем заголовок с авторизационным токеном
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + keys.get(i));
            // Создаем тело запроса на перевод
            TransferRequest transferRequest = TransferRequest.builder()
                    .Amount(BigDecimal.valueOf(Arrays.stream(accountResponses.get(i)).toList().get(0).getCurrentBalance() / 2))
                    .FromAccountId(Arrays.stream(accountResponses.get(i)).toList().get(0).getNumber())
                    .ToAccountId(numberBox)
                    .build();
            // Выполняем POST запрос на перевод с установленным заголовком
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                            .content(objectMapper.writeValueAsString(transferRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .headers(headers))
                    .andExpect(status().isOk())
                    .andReturn();
        }

        accountResponses = new ArrayList<>(10);
        for(Integer i = 0; i < 10;i++) {
            // Создаем заголовок с авторизационным токеном
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + keys.get(i));

            // Выполняем GET запрос на получение счетов пользователя с установленным заголовком
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .headers(headers))
                    .andExpect(status().isOk())
                    .andReturn();

            // Преобразуем ответ в объект класса AccountResponse
            String responseBody = mvcResult.getResponse().getContentAsString();
            accountResponses.add(objectMapper.readValue(responseBody, AccountResponse[].class));
        }
        for(Integer i = 1; i < 10;i++) {
            assertEquals(String.valueOf((1000*i)/2),String.valueOf((int) Arrays.stream(accountResponses.get(i)).toList().get(0).getCurrentBalance()),false);
        }
        Integer ballans = 0;
        for (Integer i = 1; i < 10;i++){
            ballans +=(int)((1000*i)/2);
        }
        assertEquals(String.valueOf(ballans),String.valueOf((int) Arrays.stream(accountResponses.get(0)).toList().get(0).getCurrentBalance()),false);


    }
}