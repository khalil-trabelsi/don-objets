package com.m2.service.implementation;

import com.m2.dto.AdvertisementDto;
import com.m2.model.Advertisement;
import com.m2.repository.AdvertisementRepository;
import com.m2.service.AdvertisementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdvertisementServiceImplementation implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public AdvertisementServiceImplementation(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }


    @Override
    public AdvertisementDto save(AdvertisementDto advertisementDto) {
        log.info("Saving new advertisement: {}", advertisementDto);
        if (advertisementDto == null) {
            throw new IllegalArgumentException("AdvertisementDto cannot be null");
        }
        return AdvertisementDto.fromEntity(advertisementRepository.save(AdvertisementDto.toEntity(advertisementDto)));
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
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));
    }

    @Override
    public AdvertisementDto update(AdvertisementDto advertisementDto) {
        if (advertisementDto == null) {
            throw new IllegalArgumentException("AdvertisementDto cannot be null");
        }
        Optional<Advertisement> optionalAdvertisement = advertisementRepository.findById(advertisementDto.getId());
        if (optionalAdvertisement.isPresent()) {
            return AdvertisementDto.fromEntity(advertisementRepository.save(AdvertisementDto.toEntity(advertisementDto)));
        } else {
            throw new IllegalArgumentException("Advertisement not found");
        }
    }

    @Override
    public void delete(int id) {
        if (advertisementRepository.existsById(id)) {
            advertisementRepository.deleteById(id);
        } else {
            log.warn("Advertisement with ID {} not found.", id);
            throw new IllegalArgumentException("Advertisement not found");
        }
    }
}
