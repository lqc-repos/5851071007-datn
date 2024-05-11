package thesis.news.search.service;

import thesis.news.search.command.CommandSearch;
import thesis.news.search.dto.SearchForm;

import java.util.Optional;

public interface SearchService {
    Optional<SearchForm> search(CommandSearch command) throws Exception;
}
