package thesis.core.article.model.topic.service;

import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.topic.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    List<Topic> getMany(CommandCommonQuery command);

    Optional<Boolean> addMany(List<Topic> topics);
}
