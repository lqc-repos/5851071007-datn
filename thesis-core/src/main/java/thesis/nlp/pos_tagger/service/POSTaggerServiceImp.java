package thesis.nlp.pos_tagger.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class POSTaggerServiceImp implements POSTaggerService {
    @Override
    public Optional<Boolean> trainModel() {
        return Optional.empty();
    }
}
