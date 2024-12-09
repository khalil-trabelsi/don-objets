package com.m2.controller.api;

import com.m2.dto.AdvertisementDto;
import com.m2.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementApi {

    private final AdvertisementService advertisementService;

    @Autowired
    public AdvertisementApi(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }
    @GetMapping
    public List<AdvertisementDto> getAllAdvertisements() {
        return  this.advertisementService.findAll();
    }

    @GetMapping("/{id}")
    public AdvertisementDto getAdvertisementById(@PathVariable Integer id) {
        return advertisementService.findById(id);
    }

    @PostMapping
    public AdvertisementDto save(@RequestBody AdvertisementDto advertisementDto) {
        return advertisementService.save(advertisementDto);
    }

    @PutMapping("/{id}")
    public AdvertisementDto updateAdvertisement(@PathVariable Integer id, @RequestBody AdvertisementDto advertisementDto) {
        return advertisementService.update(id, advertisementDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        advertisementService.delete(id);
    }






}
