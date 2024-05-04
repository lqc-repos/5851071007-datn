package thesis.core.article.model.topic.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.topic.Topic;
import thesis.core.article.model.topic.repository.TopicRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TopicServiceImp implements TopicService {
    @Autowired
    private TopicRepository topicRepository;

    @Override
    public List<Topic> getMany(CommandCommonQuery command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        return topicRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> addMany(List<Topic> topics) {
        return topicRepository.insertMany(topics);
    }
}
