package com.m2.controller.api;

import com.m2.dto.AdvertisementDto;
import com.m2.model.User;
import com.m2.service.AdvertisementService;
import com.m2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementApi {

    private final AdvertisementService advertisementService;
    private final UserService userService;

    @Autowired
    public AdvertisementApi(AdvertisementService advertisementService, UserService userService) {
        this.advertisementService = advertisementService;
        this.userService = userService;
    }
    @GetMapping
    public List<AdvertisementDto> getAllAdvertisements() {
        return  this.advertisementService.getAllAdvertisements();
    }

    @GetMapping("/filter")
    public Page<AdvertisementDto> getAdvertisementsByFilters(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "objectState", required = false) String objectState,
            @RequestParam(name = "category", required = false) String category,
            Authentication authentication
            ) {
        User user = null;
        if (authentication != null && authentication.isAuthenticated()) {
            user = userService.loadUserByUsername(authentication.getName());
        }
        return advertisementService.getAdvertisementByFilters(user,keyword, title, location, objectState,category, page, size);
    }

    @GetMapping("/{id}")
    public AdvertisementDto getAdvertisementById(@PathVariable Integer id) {
        return advertisementService.getAdvertisementById(id);
    }

    @PostMapping
    public AdvertisementDto save(@RequestBody AdvertisementDto advertisementDto) {
        return advertisementService.createAdvertisement(advertisementDto);
    }

    @PutMapping("/{id}")
    public AdvertisementDto updateAdvertisement(@PathVariable Integer id, @RequestBody AdvertisementDto advertisementDto) {
        return advertisementService.updateAdvertisement(id, advertisementDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        advertisementService.deleteAdvertisement(id);
    }






}
