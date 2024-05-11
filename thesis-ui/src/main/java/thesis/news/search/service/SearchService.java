package thesis.news.search.service;

import thesis.news.search.dto.SearchForm;

import java.util.Optional;

public interface SearchService {
    Optional<SearchForm> search(String search, Integer page, Integer size) throws Exception;

    Optional<SearchForm> searchByTopic(String topic, Integer page, Integer size) throws Exception;
}
