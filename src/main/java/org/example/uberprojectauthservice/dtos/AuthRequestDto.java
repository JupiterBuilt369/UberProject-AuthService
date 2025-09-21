package org.example.uberprojectauthservice.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDto {

    private String email;
    private String password;
}
