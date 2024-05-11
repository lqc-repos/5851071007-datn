package thesis.news.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.news.http.dto.request.OkHttpRequest;
import thesis.news.http.dto.response.OkHttpResponse;
import thesis.news.http.service.OkHttpService;
import thesis.news.search.command.CommandSearch;
import thesis.news.search.dto.SearchForm;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SearchServiceImp implements SearchService {
    @Autowired
    private OkHttpService okHttpService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<SearchForm> search(CommandSearch command) throws Exception {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("search", command.getSearch());
        requestData.put("page", 1);
        requestData.put("size", 10);
        OkHttpResponse response = okHttpService.postRequest(OkHttpRequest.builder()
                .url("http://localhost:8080/article/search")
                .data(requestData)
                .build()).orElseThrow();
        SearchForm searchForm = objectMapper.convertValue(response.getData(), SearchForm.class);
        if (searchForm == null)
            searchForm = SearchForm.builder()
                    .search(command.getSearch())
                    .build();
        return Optional.of(searchForm);
    }
}
