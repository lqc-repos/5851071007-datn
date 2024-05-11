package thesis.news.http.service;

import thesis.news.http.dto.request.OkHttpRequest;
import thesis.news.http.dto.response.OkHttpResponse;

import java.util.Optional;

public interface OkHttpService {
    Optional<OkHttpResponse> postRequest(OkHttpRequest okHttpRequest) throws Exception;
}
