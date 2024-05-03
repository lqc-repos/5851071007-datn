package thesis.core.search_engine.service;

import thesis.core.search_engine.command.CommandSearchArticle;
import thesis.core.search_engine.dto.SearchEngineResult;

import java.util.Optional;

public interface SearchEngineService {
    Optional<SearchEngineResult> searchArticle(CommandSearchArticle command) throws Exception;
}
