package com.example.authenticationservice.clients;

import com.example.authenticationservice.dtos.UserRequestDto;
import com.example.authenticationservice.dtos.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "USERMANAGEMENTSERVICE")
public interface UserManagementServiceClient {

    @PostMapping("/api/createUser")
    UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto);

    @GetMapping("/api/users/{email}/userDetails")
    UserResponseDto getUser(@PathVariable String email);

    @PostMapping("/api/send-welcome-email")
    void sendWelcomeEmail(@RequestBody String email);

    @DeleteMapping("/api/delete/user/{email}")
    void deleteUser(@PathVariable String email);

}
