package com.m2.service.implementation;

import com.m2.dto.AdvertisementDto;
import com.m2.dto.CategoryDto;
import com.m2.dto.UserDto;
import com.m2.exception.EntityNotFoundException;
import com.m2.model.Advertisement;
import com.m2.model.Category;
import com.m2.model.User;
import com.m2.repository.AdvertisementRepository;
import com.m2.repository.CategoryRepository;
import com.m2.repository.UserRepository;
import com.m2.service.AdvertisementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdvertisementServiceImplementation implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public AdvertisementServiceImplementation(AdvertisementRepository advertisementRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.advertisementRepository = advertisementRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public AdvertisementDto save(AdvertisementDto advertisementDto) {
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
        advertisement.setCategory(category.get());

        return AdvertisementDto.fromEntity(advertisementRepository.save(advertisement));
    }

    @Override
    public List<AdvertisementDto> findAll() {
        List<Advertisement> advertisements = advertisementRepository.findAll();
        return advertisements.stream()
                .map(AdvertisementDto::fromEntity)
                .toList();
    }

    @Override
    public AdvertisementDto findById(int id) {
        log.info("Fetching advertisement by id: {}", id);
        return advertisementRepository.findById(id)
                .map(AdvertisementDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found"));
    }

    @Override
    public AdvertisementDto update(Integer id, AdvertisementDto advertisementDto) {
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
    public void delete(int id) {
        if (advertisementRepository.existsById(id)) {
            advertisementRepository.deleteById(id);
        } else {
            log.warn("Advertisement with ID {} not found.", id);
            throw new EntityNotFoundException("Advertisement not found");
        }
    }
}
