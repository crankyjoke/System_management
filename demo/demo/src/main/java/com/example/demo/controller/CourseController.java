package com.example.demo.controller;
import com.example.demo.Service.OrganizationTableService;
import com.example.demo.model.Load;  // example model if you want
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.functions.CourseCopier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CourseController {

    CourseController(){

    }
    @PostMapping("/copycourses")
    public void ConvertCourses(@RequestBody String rawdata){
        CourseCopier courseCopier = new CourseCopier(rawdata);
        courseCopier.convert();
    }

}
