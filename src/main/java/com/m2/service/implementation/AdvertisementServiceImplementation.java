package com.m2.service.implementation;

import com.m2.dto.AdvertisementDto;
import com.m2.dto.UserDto;
import com.m2.exception.EntityNotFoundException;
import com.m2.model.Advertisement;
import com.m2.model.Category;
import com.m2.model.Search;
import com.m2.model.User;
import com.m2.repository.AdvertisementRepository;
import com.m2.repository.CategoryRepository;
import com.m2.repository.SearchRepository;
import com.m2.repository.UserRepository;
import com.m2.service.AdvertisementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AdvertisementServiceImplementation implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final SearchRepository searchRepository;
    public AdvertisementServiceImplementation(
            AdvertisementRepository advertisementRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            SearchRepository searchRepository
    ) {
        this.advertisementRepository = advertisementRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.searchRepository = searchRepository;
    }


    @Override
    public AdvertisementDto createAdvertisement(AdvertisementDto advertisementDto) {
        log.info("Saving new advertisement: {}", advertisementDto);

        if (advertisementDto == null) {
            throw new IllegalArgumentException("AdvertisementDto cannot be null");
        }

        Optional<User> user = userRepository.findById(advertisementDto.getUser().getId());
        if (user.isEmpty()) {
            log.warn("User with ID {} was not found in the DB", advertisementDto.getUser().getId());
            throw new EntityNotFoundException("Aucun utilisateur: " + advertisementDto.getUser().getId() + "n'a été trouvé dans la BDD");
        }

        Optional<Category> category = this.categoryRepository.findById(advertisementDto.getCategory().getId());
        if (category.isEmpty()) {
            log.warn("Category with ID {} was not found in the DB", advertisementDto.getCategory().getId());
            throw new EntityNotFoundException("Aucune catégorie " + advertisementDto.getCategory().getId() + "n'a été trouvée dans la BDD");
        }

        advertisementDto.setPublicationDate(new Date());
        Advertisement advertisement = AdvertisementDto.toEntity(advertisementDto);
        advertisement.setUser(user.get());

        return AdvertisementDto.fromEntity(advertisementRepository.save(advertisement));
    }

    @Override
    public List<AdvertisementDto> getAllAdvertisements() {
        List<Advertisement> advertisements = advertisementRepository.findAll();
        return advertisements.stream()
                .map(AdvertisementDto::fromEntity)
                .toList();
    }

    @Override
    public AdvertisementDto getAdvertisementById(int id) {
        log.info("Fetching advertisement by id: {}", id);
        return advertisementRepository.findById(id)
                .map(AdvertisementDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found"));
    }

    @Override
    public AdvertisementDto updateAdvertisement(Integer id, AdvertisementDto advertisementDto) {
        if (advertisementDto == null) {
            throw new IllegalArgumentException("AdvertisementDto cannot be null");
        }
        Advertisement existingAdvertisement = advertisementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Advertisement not found"));

        if (advertisementDto.getTitle() != null) {
            existingAdvertisement.setTitle(advertisementDto.getTitle());
        }
        if (advertisementDto.getDescription() != null) {
            existingAdvertisement.setDescription(advertisementDto.getDescription());
        }
        if (advertisementDto.getPublicationDate() != null) {
            existingAdvertisement.setPublicationDate(advertisementDto.getPublicationDate());
        }
        if (advertisementDto.getLocation() != null) {
            existingAdvertisement.setLocation(advertisementDto.getLocation());
        }
        if (advertisementDto.getDeliveryOption() != null) {
            existingAdvertisement.setDeliveryOption(advertisementDto.getDeliveryOption());
        }
        if (advertisementDto.getObjectState() != null) {
            existingAdvertisement.setObjectState(advertisementDto.getObjectState());
        }
        if (advertisementDto.getCategory() != null) {
            Category existingCategory = this.categoryRepository.findById(advertisementDto.getCategory().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Aucune catégorie " + advertisementDto.getCategory().getId() + "n'a été trouvée dans la BDD"));
            existingAdvertisement.setCategory(existingCategory);
        }
        if (advertisementDto.getUser() != null) {
            existingAdvertisement.setUser(UserDto.toEntity(advertisementDto.getUser()));
        }

        return AdvertisementDto.fromEntity(advertisementRepository.save(existingAdvertisement));

    }

    @Override
    public void deleteAdvertisement(int id) {
        if (advertisementRepository.existsById(id)) {
            advertisementRepository.deleteById(id);
        } else {
            log.warn("Advertisement with ID {} not found.", id);
            throw new EntityNotFoundException("Advertisement not found");
        }
    }

    @Override
    public Page<Advertisement> getAdvertisementByFilters(
            User user,
            String keyword,
            String title,
            String location,
            String objectState,
            String category,
            int page,
            int size) {
        if (keyword == null && title == null && location == null && objectState == null) {
            return advertisementRepository.findAll(PageRequest.of(page, size));
        }
        if (user != null) {
            Search search = Search.builder()
                    .keywords(keyword)
                    .location(location)
                    .categoryName(category)
                    .objectState(objectState)
                    .title(title)
                    .user(user)
                    .build();

            searchRepository.save(search);
            log.info("Save search for user with id "+user.getId());
        }

        return advertisementRepository.
                findAdvertisementByFilters(keyword, title, location, objectState, category,PageRequest.of(page, size));
    }


}
