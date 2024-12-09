package com.m2.controller;

import com.m2.model.Advertisement;
import com.m2.repository.AdvertisementRepository;
import com.m2.service.AdvertisementService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
@AllArgsConstructor
public class AdvertisementController {

    public final AdvertisementRepository advertisementRepository;
    @GetMapping
    public String get(Model model, @RequestParam(name = "page", defaultValue = "0") int page,@RequestParam(name = "size", defaultValue = "8") int size) {
        Page<Advertisement> advertisementPage =  advertisementRepository.findAll(PageRequest.of(page, size));

        model.addAttribute("advertisements", advertisementPage);
        model.addAttribute("pages", new int[advertisementPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "advertisement";
    }

    @GetMapping("deposer-une-annonce")
    public String createAdvertisement() {
        return "advertisementCreationForm";
    }

}
