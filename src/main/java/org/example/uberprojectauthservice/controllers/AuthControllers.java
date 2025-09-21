package org.example.uberprojectauthservice.controllers;

import org.example.uberprojectauthservice.dtos.AuthRequestDto;
import org.example.uberprojectauthservice.dtos.PassengerDto;
import org.example.uberprojectauthservice.dtos.PassengerSignupRequestDto;
import org.example.uberprojectauthservice.services.AuthServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllers {

    private final AuthServices authServices;
    private final AuthenticationManager authenticationManager;

    public AuthControllers(AuthServices authServices, AuthenticationManager authenticationManager) {
        this.authServices = authServices;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping
    public void hanuman(){
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signUp(@RequestBody @Validated PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerDto response =  authServices.signUpPassenger(passengerSignupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto) {
        System.out.println("Request Received  " + authRequestDto.getEmail()  +"  "+ authRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
       if( authentication.isAuthenticated())    {
           return new ResponseEntity<>("Successfully Auth", HttpStatus.OK);
       }

        return new ResponseEntity<>("Auth not successful", HttpStatus.UNAUTHORIZED);
    }
}