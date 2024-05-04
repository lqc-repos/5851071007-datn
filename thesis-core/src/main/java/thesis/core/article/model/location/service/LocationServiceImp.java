package thesis.core.article.model.location.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.model.location.Location;
import thesis.core.article.model.location.repository.LocationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LocationServiceImp implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<Location> getMany(CommandCommonQuery command) {
        Map<String, Object> sort = new HashMap<>();
        if (command.getIsDescCreatedDate() != null)
            sort.put("createdDate", command.getIsDescCreatedDate() ? -1 : 1);
        return locationRepository.find(new Document(), sort, (command.getPage() - 1) * command.getSize(), command.getSize());
    }

    @Override
    public Optional<Boolean> addMany(List<Location> locations) {
        return locationRepository.insertMany(locations);
    }

}
