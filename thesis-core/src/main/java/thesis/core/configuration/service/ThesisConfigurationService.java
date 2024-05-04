package thesis.core.configuration.service;

import thesis.core.configuration.ThesisConfiguration;

import java.util.Optional;

public interface ThesisConfigurationService {
    Optional<ThesisConfiguration> getByName(String name);
}
