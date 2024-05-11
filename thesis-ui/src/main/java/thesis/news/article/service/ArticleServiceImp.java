package thesis.news.article.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thesis.news.article.dto.Article;
import thesis.news.http.dto.request.OkHttpRequest;
import thesis.news.http.dto.response.OkHttpResponse;
import thesis.news.http.service.OkHttpService;
import thesis.utils.dto.CommandCommonQuery;

import java.util.List;
import java.util.Set;

@Service
public class ArticleServiceImp implements ArticleService {
    @Value("${thesis.be-api.host}")
    private String thesisApiHost;
    @Value("${thesis.be-api.article-url}")
    private String thesisArticleUrl;
    @Value("${thesis.be-api.article.get}")
    private String thesisArticleGetPath;
    @Autowired
    private OkHttpService okHttpService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Article getArticleById(String id) throws Exception {
        OkHttpResponse response = okHttpService.postRequest(OkHttpRequest.builder()
                .url(thesisApiHost + thesisArticleUrl + thesisArticleGetPath)
                .data(CommandCommonQuery.builder()
                        .articleIds(Set.of(id))
                        .page(1)
                        .size(1)
                        .build())
                .build()).orElseThrow();
        List<Article> articles = objectMapper.convertValue(response.getData(), new TypeReference<>() {
        });
        articles.forEach(article -> {
            article.setContent(article.getContent().replaceAll("\n", "<new_line>"));
        });
        return CollectionUtils.isNotEmpty(articles) ? articles.get(0) : null;
    }

    @Override
    public List<Article> getLatestNews() throws Exception {
        OkHttpResponse response = okHttpService.postRequest(OkHttpRequest.builder()
                .url(thesisApiHost + thesisArticleUrl + thesisArticleGetPath)
                .data(CommandCommonQuery.builder()
                        .isDescCreatedDate(true)
                        .isDescPublicationDate(true)
                        .page(1)
                        .size(10)
                        .build())
                .build()).orElseThrow();
        return objectMapper.convertValue(response.getData(), new TypeReference<>() {
        });
    }
}
