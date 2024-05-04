package thesis.core.configuration.service;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.configuration.ThesisConfiguration;
import thesis.core.configuration.repository.ThesisConfigurationRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ThesisConfigurationServiceImp implements ThesisConfigurationService {

    @Autowired
    private ThesisConfigurationRepository thesisConfigurationRepository;

    @Override
    public Optional<ThesisConfiguration> getByName(String name) {
        if (StringUtils.isBlank(name))
            return Optional.empty();
        Map<String, Object> query = new HashMap<>();
        query.put("name", name);
        return this.thesisConfigurationRepository.findOne(query, new Document());
    }
}
