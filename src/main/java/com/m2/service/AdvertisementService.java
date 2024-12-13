package com.m2.service;

import com.m2.dto.AdvertisementDto;
import com.m2.model.Advertisement;
import com.m2.model.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AdvertisementService {
    AdvertisementDto createAdvertisement(AdvertisementDto advertisementDto);

    List<AdvertisementDto> getAllAdvertisements();

    AdvertisementDto getAdvertisementById(int id);

    AdvertisementDto updateAdvertisement(Integer id,AdvertisementDto advertisementDto);

    void deleteAdvertisement(int id);
    Page<Advertisement> getAdvertisementByFilters(User user, String keyword, String title, String location, String objectState, String category, int page, int size);

    }
