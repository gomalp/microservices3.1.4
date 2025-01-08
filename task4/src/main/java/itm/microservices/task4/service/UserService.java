package itm.microservices.task4.service;

import itm.microservices.task4.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final RestTemplate restTemplate;
    private String sessionId;

    private static final String BASE_URL = "http://94.198.50.185:7081/api/users";

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void execute() {
        // 1. Получение всех пользователей
        ResponseEntity<List<User>> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {});
        List<User> users = response.getBody();
        logger.info(" *** users={}",users);
        logger.info(" *** response={}",response);

        // Получение session id
        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (cookies != null && !cookies.isEmpty()) {
            logger.info(" *** cookies={}",cookies);
            sessionId = cookies.get(0).split(";")[0];
            logger.info(" *** sessionId={}",sessionId);
        }

        // 2. Добавление пользователя
        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge((byte) 33);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, sessionId); // Установка session id в заголовок
        //HttpEntity: Это обертка, которая содержит как тело HTTP-запроса (или ответа), так и его заголовки.
        HttpEntity<User> entity = new HttpEntity<>(newUser, headers);

        ResponseEntity<String> postResponse = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, String.class);
        logger.info(" *** ResponseEntity postResponse={}",postResponse);
        String code1 = postResponse.getBody();
        logger.info(" *** 1 part of code={}",code1);

        // 3. Изменение пользователя
        newUser.setName("Thomas");
        newUser.setLastName("Shelby");

        HttpEntity<User> updateEntity = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> putResponse = restTemplate.exchange(BASE_URL, HttpMethod.PUT, updateEntity, String.class);
        logger.info(" *** ResponseEntity putResponse={}",putResponse);
        String code2 = putResponse.getBody();
        logger.info(" *** 1 part of code={}",code2);

        // 4. Удаление пользователя
        String deleteUrl = BASE_URL + "/3"; // Удаление пользователя с id = 3
        ResponseEntity<String> deleteResponse = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        logger.info(" *** ResponseEntity deleteResponse={}",deleteResponse);
        String code3 = deleteResponse.getBody();
        logger.info(" *** 1 part of code={}",code3);

        // Получение итогового кода
        String finalCode = code1 + code2 + code3;
        System.out.println("Итоговый код: " + finalCode);

    }
}
