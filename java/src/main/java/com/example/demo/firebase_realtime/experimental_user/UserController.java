package com.example.demo.firebase_realtime.experimental_user;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    public UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/users")
    public void postUser(@RequestBody User user) throws Exception{
        try{
            service.postUser(user);
        }
        catch (Exception e) {
            throw new Exception("problem with transfer");
        }
    }

    @GetMapping("/users")
    public void getUser(@RequestParam String login) throws Exception{
        try{
            service.getUser(login);
        }
        catch (Exception e) {
            throw new Exception("problem with transfer");
        }
    }





}
