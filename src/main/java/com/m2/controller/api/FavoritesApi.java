package com.m2.controller.api;

import com.m2.model.Advertisement;
import com.m2.model.User;
import com.m2.repository.AdvertisementRepository;
import com.m2.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@AllArgsConstructor
public class FavoritesApi {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    @PostMapping("/add")
    public ResponseEntity<?> toggleFavorite(@RequestParam int userId, @RequestParam int advertisementId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));

        boolean isAdded;
        if (user.getFavorites().contains(advertisement)) {
            user.getFavorites().remove(advertisement);
            isAdded = false;
        } else {
            user.getFavorites().add(advertisement);
            isAdded = true;
        }

        userRepository.save(user);

        return ResponseEntity.ok().body(
                Map.of(
                        "success", true,
                        "isFavorite", isAdded
                )
        );
    }
}
