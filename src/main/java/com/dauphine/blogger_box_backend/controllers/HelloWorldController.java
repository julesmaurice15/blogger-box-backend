package com.dauphine.blogger_box_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Tag(
        name = "Hello World API",
        description = "My first hello world endpoint"
)
public class HelloWorldController {
    @GetMapping("hello-world")
    public String helloWorld(){
        return "Hello World";
    }
    @GetMapping("hello")
    public String helloByName(@RequestParam String name){
        return "Hello" + name;
    }
    @GetMapping("hello/{name}")
    @Operation(
            summary = "Hello by Name endpoint",
            description = "Returns 'Hello {name}' by path variable"
    )
    public String hello(
            @Parameter(description = "Name to greet")
            @PathVariable String name
    ){
        return "Hello "+name;
    }
}
