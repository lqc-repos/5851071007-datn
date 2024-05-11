package thesis.news.http.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;
import thesis.news.http.dto.request.OkHttpRequest;
import thesis.news.http.dto.response.OkHttpResponse;

import java.io.IOException;
import java.util.Optional;

@Service
public class OkHttpServiceImp implements OkHttpService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String API_URL = "http://localhost:8080/article/search";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public Optional<OkHttpResponse> postRequest(OkHttpRequest okHttpRequest) throws Exception {
        RequestBody requestBody = RequestBody.create(objectMapper.writeValueAsString(okHttpRequest.getData()),
                MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(okHttpRequest.getUrl())
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return Optional.of(objectMapper.readValue(response.body().string(), OkHttpResponse.class));
            } else {
                throw new IOException("Unexpected response: " + response.code());
            }
        }
    }
}
