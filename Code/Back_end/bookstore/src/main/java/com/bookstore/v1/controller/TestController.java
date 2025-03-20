package com.bookstore.v1.controller;

import com.bookstore.v1.entity.Authors;
import com.bookstore.v1.entity.Categories;
import com.bookstore.v1.service.TestService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {

    private TestService testService;

    @GetMapping("/test")
    public String test(){
        return "tested";
    }

    @PostMapping("/author")
    public Authors author(@RequestHeader("name") String name){
        return testService.getAuthorName(name);
    }

    @PostMapping("/categories")
    public Categories categories(@RequestHeader("name") String name){
        return testService.getCategoriesName(name);
    }
}
