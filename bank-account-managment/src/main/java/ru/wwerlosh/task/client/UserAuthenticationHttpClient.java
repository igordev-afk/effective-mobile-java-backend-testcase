package ru.wwerlosh.task.client;

import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationHttpClient {

    private final CloseableHttpClient httpClient;
    private final String EXTERNAL_ENDPOINT;

    public UserAuthenticationHttpClient(@Value("${server.external-jwt-validation-endpoint}") String EXTERNAL_ENDPOINT) {
        this.httpClient = HttpClients.createDefault();
        this.EXTERNAL_ENDPOINT = EXTERNAL_ENDPOINT;
    }

    public boolean isValidJWT(String jwt, Long userId) {

        HttpGet request = new HttpGet(EXTERNAL_ENDPOINT);
        request.setHeader("Authorization", jwt);
        request.setHeader("userId", String.valueOf(userId));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == 200;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
