package com.m2.controller;

import com.m2.dto.AdvertisementDto;
import com.m2.dto.CategoryDto;
import com.m2.exception.EntityNotFoundException;
import com.m2.model.Advertisement;
import com.m2.model.Category;
import com.m2.model.User;
import com.m2.repository.AdvertisementRepository;
import com.m2.repository.CategoryRepository;
import com.m2.service.AdvertisementService;
import com.m2.service.CategoryService;
import com.m2.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class AdvertisementController {

    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final AdvertisementService advertisementService;

    @GetMapping
    public String get(Model model,
                      Authentication authentication,
                      @RequestParam(name = "page", defaultValue = "0") int page,
                      @RequestParam(name = "size", defaultValue = "8") int size,
                      @RequestParam(name = "title", required = false) String title,
                      @RequestParam(name = "keyword", required = false) String keyword,
                      @RequestParam(name = "location", required = false) String location,
                      @RequestParam(name = "objectState", required = false) String objectState,
                      @RequestParam(name = "category", required = false) String category
    ) {
//        Page<Advertisement> advertisementPage =  advertisementRepository.findAll(PageRequest.of(page, size));
        User user = null;
        if (authentication != null && authentication.isAuthenticated()) {
            log.warn(authentication.getName());
            user = userService.loadUserByUsername(authentication.getName());
        }
        Page<Advertisement> advertisementsPage = advertisementService.getAdvertisementByFilters(user,keyword, title, location, objectState,category, page, size);
        log.info("Search: "+ advertisementsPage);
//        List<String> keywords = new ArrayList<>(Arrays.asList(advertisementPage.split(",")));
        List<CategoryDto> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("advertisements", advertisementsPage);
        model.addAttribute("pages", new int[advertisementsPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("location", location);
        model.addAttribute("objectState", objectState);
        model.addAttribute("title", title);
        model.addAttribute("category", category);
        return "advertisement";
    }

    @GetMapping("deposer-une-annonce")
    public String createAdvertisementForm(Model model) {
        List<CategoryDto> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("advertisement", new Advertisement());
        return "advertisementCreationForm";
    }

    @PostMapping("/createAdvertisement")
    public String createAdvertisement(Model model, @ModelAttribute AdvertisementDto advertisementDto, Authentication authentication) {
        User user = userService.loadUserByUsername(authentication.getName());
        CategoryDto category = categoryService.findById(advertisementDto.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find category with ID: "+advertisementDto.getCategory().getId()));
        Advertisement advertisement = AdvertisementDto.toEntity(advertisementDto);
        advertisement.setUser(user);
        advertisement.setPublicationDate(new Date());
        advertisement.setCategory(CategoryDto.toEntity(category));
        advertisementRepository.save(advertisement);
        model.addAttribute("advertisement", new Advertisement());
        return "redirect:/";
    }



}
