package com.m2.service;

import com.m2.dto.AdvertisementDto;

import java.util.List;

public interface AdvertisementService {
    AdvertisementDto save(AdvertisementDto advertisementDto);

    List<AdvertisementDto> findAll();

    AdvertisementDto findById(int id);

    AdvertisementDto update(Integer id,AdvertisementDto advertisementDto);

    void delete(int id);
}
