package com.m2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Controller
public class CategoryController {

    @GetMapping(value = "/html/home")
    String Index() {
        return "home";
    }

}
