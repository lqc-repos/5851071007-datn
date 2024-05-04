package thesis.core.article.model.location.service;

import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.location.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    List<Location> getMany(CommandCommonQuery command);

    Optional<Boolean> addMany(List<Location> locations);
}
