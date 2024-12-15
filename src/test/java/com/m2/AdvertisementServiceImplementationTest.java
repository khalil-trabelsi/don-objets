package com.m2;

import com.m2.dto.AdvertisementDto;
import com.m2.dto.CategoryDto;
import com.m2.dto.UserDto;
import com.m2.exception.EntityNotFoundException;
import com.m2.model.Advertisement;
import com.m2.model.Category;
import com.m2.model.User;
import com.m2.repository.*;
import com.m2.service.implementation.AdvertisementServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdvertisementServiceImplementationTest {

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private AdvertisementServiceImplementation advertisementService;

    private AdvertisementDto advertisementDto;
    private Advertisement advertisement;
    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);

        category = new Category();
        category.setId(1);
        category.setLabel("Electronics");

        advertisementDto = new AdvertisementDto();
        advertisementDto.setId(1);
        advertisementDto.setTitle("Phone");
        advertisementDto.setDescription("A used phone");
        advertisementDto.setCategory(new CategoryDto(1, "Electronics", "test"));
        advertisementDto.setUser(new UserDto(1, "John Doe", "email@example.com", "password", new Date(), new ArrayList<>()));
        advertisementDto.setLocation("Paris");

        advertisement = AdvertisementDto.toEntity(advertisementDto);
    }

    @Test
    void testCreateAdvertisement() {
        when(userRepository.findById(advertisementDto.getUser().getId())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(advertisementDto.getCategory().getId())).thenReturn(Optional.of(category));
        when(advertisementRepository.save(any(Advertisement.class))).thenReturn(advertisement);

        AdvertisementDto result = advertisementService.createAdvertisement(advertisementDto);

        assertNotNull(result);
        assertEquals("Phone", result.getTitle());
        verify(advertisementRepository, times(1)).save(any(Advertisement.class));
    }

    @Test
    void testCreateAdvertisement_UserNotFound() {
        when(userRepository.findById(advertisementDto.getUser().getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> advertisementService.createAdvertisement(advertisementDto));

        assertEquals("Aucun utilisateur: 1n'a été trouvé dans la BDD", exception.getMessage());
    }

    @Test
    void testGetAdvertisementById() {
        when(advertisementRepository.findById(1)).thenReturn(Optional.of(advertisement));

        AdvertisementDto result = advertisementService.getAdvertisementById(1);

        assertNotNull(result);
        assertEquals("Phone", result.getTitle());
    }

    @Test
    void testGetAdvertisementById_NotFound() {
        when(advertisementRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> advertisementService.getAdvertisementById(1));

        assertEquals("Advertisement not found", exception.getMessage());
    }

    @Test
    void testUpdateAdvertisement() {
        // Mocker la catégorie et l'annonce
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(advertisementRepository.findById(1)).thenReturn(Optional.of(advertisement));
        when(advertisementRepository.save(any(Advertisement.class))).thenReturn(advertisement);

        // Mise à jour du DTO
        advertisementDto.setTitle("Updated Phone");

        // Appel de la méthode
        AdvertisementDto result = advertisementService.updateAdvertisement(1, advertisementDto);

        // Vérifications
        assertAll("Vérifications du résultat de la mise à jour",
                () -> assertNotNull(result),
                () -> assertEquals("Updated Phone", result.getTitle()),
                () -> assertEquals("Electronics", result.getCategory().getLabel()) // Vérification de la catégorie si pertinent
        );

        verify(advertisementRepository, times(1)).save(any(Advertisement.class));
    }

    @Test
    void testUpdateAdvertisement_NotFound() {
        when(advertisementRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> advertisementService.updateAdvertisement(1, advertisementDto));

        assertEquals("Advertisement not found", exception.getMessage());
    }

    @Test
    void testDeleteAdvertisement() {
        when(advertisementRepository.existsById(1)).thenReturn(true);

        advertisementService.deleteAdvertisement(1);

        verify(advertisementRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAdvertisement_NotFound() {
        when(advertisementRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> advertisementService.deleteAdvertisement(1));

        assertEquals("Advertisement not found", exception.getMessage());
    }

    @Test
    void testGetAdvertisementByFilters() {
        String keyword = "Phone";
        String title = "Phone";
        String location = "Paris";
        String objectState = "New";
        String category = "Electronics";
        int page = 0;
        int size = 10;

        when(advertisementRepository.findAdvertisementByFilters(keyword, title, location, objectState, category, PageRequest.of(page, size)))
                .thenReturn(Page.empty());

        Page<AdvertisementDto> result = advertisementService.getAdvertisementByFilters(null, keyword, title, location, objectState, category, page, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
