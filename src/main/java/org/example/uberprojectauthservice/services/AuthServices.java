package org.example.uberprojectauthservice.services;

import org.example.uberprojectauthservice.dtos.AuthRequestDto;
import org.example.uberprojectauthservice.dtos.PassengerDto;
import org.example.uberprojectauthservice.dtos.PassengerSignupRequestDto;
import org.example.uberprojectauthservice.model.Passenger;
import org.example.uberprojectauthservice.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServices{

    private final PassengerRepository passengerRepository;
    private final BCryptPasswordEncoder  bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtServices jwtServices;



    @Autowired
    public AuthServices(PassengerRepository passengerRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtServices jwtServices) {
        this.passengerRepository = passengerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtServices = jwtServices;
    }

    public PassengerDto signUpPassenger(PassengerSignupRequestDto passengerSignupRequestDto) {
        Passenger passenger = Passenger.builder()
                .name(passengerSignupRequestDto.getName())
                .email(passengerSignupRequestDto.getEmail())
                .phoneNumber(passengerSignupRequestDto.getPhoneNumber())
                .password(bCryptPasswordEncoder.encode(passengerSignupRequestDto.getPassword())) //need to encrypt the password
                .build();
        Passenger newPassenger = passengerRepository.save(passenger);
        return PassengerDto.from(newPassenger);
    }

    public Map<String, Object> signIn(AuthRequestDto authRequestDto) {
        try {
            // Authenticate credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword())
            );

            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Invalid credentials");
            }

            // Fetch passenger details from DB
            Passenger passenger = passengerRepository.findPassengerByEmail(authRequestDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Generate JWT token
            String token = jwtServices.generateToken(authRequestDto.getEmail());


            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                    "id", passenger.getId(),
                    "email", passenger.getEmail()
            ));



            return response;

        } catch (BadCredentialsException e) {
            throw e; // controller will catch and return 401
        } catch (UsernameNotFoundException e) {
            throw e; // controller will catch and return 404
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

}
