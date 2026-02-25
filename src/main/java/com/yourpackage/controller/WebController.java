package com.yourpackage.controller; // Make sure this matches your actual package!

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // This catches anyone visiting the main URL (/) and explicitly hands them index.html
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    // Optional: If you have separate HTML files for login/signup, you can explicitly route them too!
    @GetMapping("/login")
    public String login() {
        return "forward:/login.html"; // Only add this if you actually have a login.html file
    }
}