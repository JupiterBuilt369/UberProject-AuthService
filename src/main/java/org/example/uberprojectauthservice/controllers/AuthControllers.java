package org.example.uberprojectauthservice.controllers;

import org.example.uberprojectauthservice.dtos.PassengerDto;
import org.example.uberprojectauthservice.dtos.PassengerSignupRequestDto;
import org.example.uberprojectauthservice.services.AuthServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllers {

    private final AuthServices authServices;

    public AuthControllers(AuthServices authServices) {
        this.authServices = authServices;
    }

    @GetMapping
    public void hanuman(){
        System.out.println("JAI SHREE RAM,JAI BAJRANGBALI");
    }

    @PostMapping("/signup")
    public ResponseEntity<PassengerDto> signUp(@RequestBody @Validated PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerDto response =  authServices.signUpPassenger(passengerSignupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/signin/passenger")
    public ResponseEntity<?> signIn() {

        return ResponseEntity.ok().body("JaiShreeRam");
    }
}
