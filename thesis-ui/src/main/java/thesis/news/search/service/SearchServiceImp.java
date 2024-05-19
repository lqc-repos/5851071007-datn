package thesis.news.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thesis.news.http.dto.request.OkHttpRequest;
import thesis.news.http.dto.response.OkHttpResponse;
import thesis.news.http.service.OkHttpService;
import thesis.news.search.command.CommandSearch;
import thesis.news.search.dto.SearchForm;
import thesis.utils.dto.CommandCommonQuery;

import java.util.Optional;

@Service
public class SearchServiceImp implements SearchService {
    @Value("${thesis.be-api.host}")
    private String thesisApiHost;
    @Value("${thesis.be-api.article-url}")
    private String thesisArticleUrl;
    @Value("${thesis.be-api.article.search}")
    private String thesisArticleSearchPath;
    @Value("${thesis.be-api.article.search-by-topic}")
    private String thesisArticleSearchByTopicPath;
    @Autowired
    private OkHttpService okHttpService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<SearchForm> search(CommandSearch command) throws Exception {
        OkHttpResponse response = okHttpService.postRequest(OkHttpRequest.builder()
                .url(thesisApiHost + thesisArticleUrl + thesisArticleSearchPath)
                .data(CommandCommonQuery.builder()
                        .search(command.getSearch())
                        .page(command.getPage() == null ? 1 : command.getPage())
                        .size(command.getSize() == null ? 10 : command.getSize())
                        .isCustomTag(command.getIsCustomTag())
                        .build())
                .build()).orElseThrow();
        SearchForm searchForm = objectMapper.convertValue(response.getData(), SearchForm.class);
        if (searchForm == null)
            searchForm = SearchForm.builder()
                    .search(command.getSearch())
                    .build();
        return Optional.of(searchForm);
    }

    @Override
    public Optional<SearchForm> searchByTopic(String topic, Integer page, Integer size) throws Exception {
        OkHttpResponse response = okHttpService.postRequest(OkHttpRequest.builder()
                .url(thesisApiHost + thesisArticleUrl + thesisArticleSearchByTopicPath)
                .data(CommandCommonQuery.builder()
                        .topic(topic)
                        .page(page == null ? 1 : page)
                        .size(size == null ? 10 : size)
                        .build())
                .build()).orElseThrow();
        SearchForm searchForm = objectMapper.convertValue(response.getData(), SearchForm.class);
        if (searchForm == null)
            searchForm = SearchForm.builder()
                    .topic(topic)
                    .build();
        return Optional.of(searchForm);
    }

}
