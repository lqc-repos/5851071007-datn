package thesis.core.article.model.author.service;

import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.author.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> getMany(CommandCommonQuery command);

    Optional<Boolean> addMany(List<Author> authors);
}
