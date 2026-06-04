package com.porong.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class KakaoApiService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public double[] getCoordinates(String address) {
        try {
            String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8")
                                                       .replace("+", "%20");
            String requestUrl = "https://dapi.kakao.com/v2/local/search/address.json?query=" + encodedAddress;

            RestTemplate restTemplate = new RestTemplate();

            RequestEntity<Void> request =
                RequestEntity
                    .get(java.net.URI.create(requestUrl))
                    .header("Authorization", "KakaoAK " + kakaoApiKey)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode documents = root.get("documents");

            if (documents != null && documents.size() > 0) {
                double latitude = documents.get(0).get("y").asDouble();
                double longitude = documents.get(0).get("x").asDouble();
                return new double[]{latitude, longitude};
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}