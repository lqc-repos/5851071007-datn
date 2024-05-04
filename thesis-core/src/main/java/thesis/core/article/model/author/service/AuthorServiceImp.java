package thesis.core.article.model.author.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.author.Author;
import thesis.core.article.model.author.repository.AuthorRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthorServiceImp implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Author> getMany(CommandCommonQuery command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        return authorRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> addMany(List<Author> authors) {
        return authorRepository.insertMany(authors);
    }
}
