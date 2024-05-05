package com.example.easynotes.controller;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class IndexController {

    @GetMapping
    public String sayHello() {
        try{
            log.info("/ API hit");
            return "Hello and Welcome to the EasyNotes application. You can create a new Note by making a POST request to /api/notes endpoint.";
        } catch (Exception e ){
            log.error("Error:{}",e);
            return "Contact Administrator";
        }

    }
}
