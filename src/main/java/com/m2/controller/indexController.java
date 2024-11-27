package com.m2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {

    @GetMapping(value="/")
    String index() {
        return "redirect:html/home";
    }

    @GetMapping(value = "/html/home")
    String Index() {
        return "home";
    }
}
