package com.m2;

import com.m2.model.Advertisement;
import com.m2.model.Notification;
import com.m2.model.Search;
import com.m2.model.User;
import com.m2.repository.NotificationRepository;
import com.m2.repository.SearchRepository;
import com.m2.service.implementation.NotificationServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceImplementationTest {

    @InjectMocks
    private NotificationServiceImplementation notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SearchRepository searchRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSearch_CreatesNotification() {
        // Mock user
        User user = new User();
        user.setId(1);

        // Mock search
        Search search = new Search();
        search.setId(1);
        search.setUser(user);
        search.setTitle("example criteria");

        // Mock advertisement matching the search
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1);
        advertisement.setTitle("Matching Advertisement");

        // Mock notification
        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setSearch(search);
        notification.setAdvertisement(advertisement);

        when(searchRepository.save(search)).thenReturn(search);

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Search savedSearch = searchRepository.save(search);

        Notification createdNotification = notificationRepository.save(notification);

        assertNotNull(savedSearch);
        assertEquals(search.getTitle(), savedSearch.getTitle());

        assertNotNull(createdNotification);
        assertEquals(user.getId(), createdNotification.getUserId());
        assertEquals(search, createdNotification.getSearch());
        assertEquals(advertisement, createdNotification.getAdvertisement());

        verify(searchRepository, times(1)).save(search);
        verify(notificationRepository, times(1)).save(notification);
    }

    // Get all notifications by userId
    @Test
    public void testGetAllNotificationsByUserId_Success() {
        int userId = 1;
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notificationRepository.findAllByUserId(userId)).thenReturn(notifications);

        List<Notification> result = notificationService.getAllNotificationsByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findAllByUserId(userId);
    }

    // When there is no notifications for the userId
    @Test
    public void testGetAllNotificationsByUserId_NoNotifications() {
        int userId = 1;
        when(notificationRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        List<Notification> result = notificationService.getAllNotificationsByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationRepository, times(1)).findAllByUserId(userId);
    }

    //Get all search by userId
    @Test
    public void testGetAllSearchByUserId_Success() {
        int userId = 1;
        List<Search> searches = Arrays.asList(new Search(), new Search());
        when(searchRepository.findAllByUserId(userId)).thenReturn(searches);

        List<Search> result = notificationService.getAllSearchByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(searchRepository, times(1)).findAllByUserId(userId);
    }

    // When there is no search for the userId
    @Test
    public void testGetAllSearchByUserId_NoSearches() {
        int userId = 1;
        when(searchRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        List<Search> result = notificationService.getAllSearchByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(searchRepository, times(1)).findAllByUserId(userId);
    }

    // Delete Search with notifications associated
    @Test
    public void testDeleteSearch_WithNotifications() {
        int searchId = 1;
        Search search = new Search();
        search.setId(searchId);
        search.setUser(new User());
        Notification notification1 = new Notification();
        notification1.setUserId(1);
        Notification notification2 = new Notification();
        notification2.setUserId(2);

        search.setNotifications(Arrays.asList(notification1, notification2));

        when(searchRepository.findById(searchId)).thenReturn(Optional.of(search));

        notificationService.deleteSearch(searchId);

        verify(notificationRepository, times(1)).deleteAll(search.getNotifications());
        verify(searchRepository, times(1)).deleteById(searchId);
    }

    @Test
    public void testDeleteSearch_SearchNotFound() {
        int searchId = 1;
        when(searchRepository.findById(searchId)).thenReturn(Optional.empty());

        notificationService.deleteSearch(searchId);

        verify(notificationRepository, never()).deleteAll(any());
        verify(searchRepository, never()).deleteById(anyInt());
    }


}
