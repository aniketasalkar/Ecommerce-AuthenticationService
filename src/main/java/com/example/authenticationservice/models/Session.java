package com.example.authenticationservice.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Session extends BaseModel{

    @ManyToOne(cascade = CascadeType.ALL)
    private UserAuth userAuth;

    @Column(nullable = false, length = 512)
    private String accessToken;

    @Column(nullable = false, length = 512)
    private String refreshToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionState sessionState;
}
