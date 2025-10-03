package org.example.uberprojectauthservice.dtos;

import lombok.*;
import org.example.uberprojectentityservice.model.Passenger;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {
    private String id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Date createdAt;
    public static PassengerDto from(Passenger p) {
        PassengerDto passengerDto = PassengerDto.builder()
                .id(String.valueOf(p.getId()))
                .createdAt(p.getCreatedAt())
                .name(p.getName())
                .email(p.getEmail())
                .phoneNumber(p.getPhoneNumber())
                .password(p.getPassword())
                .build();
        return passengerDto;

    }
}
