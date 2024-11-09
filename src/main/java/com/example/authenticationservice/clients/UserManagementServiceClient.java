package com.example.authenticationservice.clients;

import com.example.authenticationservice.dtos.UserRequestDto;
import com.example.authenticationservice.dtos.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USERMANAGEMENTSERVICE")
public interface UserManagementServiceClient {

    @PostMapping("/api/createUser")
    UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto);

    @GetMapping("/api/users/{email}/userDetails")
    UserResponseDto getUser(@PathVariable String email);

}
