package com.psthiago.place_service.domain;

import com.github.slugify.Slugify;
import com.psthiago.place_service.api.PlaceRequest;
import com.psthiago.place_service.util.QueryBuilder;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PlaceService {
    private PlaceRepository placeRepository;
    private Slugify slg;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        this.slg = Slugify.builder().build();
    }

    public Mono<Place> create(PlaceRequest placeRequest) {
        var place = new Place(
                null,
                placeRequest.name(),
                slg.slugify(placeRequest.name()),
                placeRequest.city(),
                placeRequest.state(),
                null,
                null);
        return placeRepository.save(place);
    }

    public Mono<Place> get(Long id) {
        return placeRepository.findById(id);
    }

    public Flux<Place> getByName(String name) {
        var place = new Place(null, name, null, null, null, null, null);
        Example<Place> query = QueryBuilder.makeQuery(place);
        return placeRepository.findAll(query, Sort.by("name").ascending());
    }

    public Mono<Place> edit(Long id, PlaceRequest placeRequest) {
        return placeRepository.findById(id)
                .map(place -> PlaceMapper.updatePlaceFromDTO(placeRequest, place))
                .map(place -> place.withSlug(slg.slugify(place.name())))
                .flatMap(placeRepository::save);
    }


}
