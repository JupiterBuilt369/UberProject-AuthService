package org.example.uberprojectauthservice.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.uberprojectauthservice.dtos.AuthRequestDto;
import org.example.uberprojectauthservice.dtos.PassengerDto;
import org.example.uberprojectauthservice.dtos.PassengerSignupRequestDto;
import org.example.uberprojectauthservice.services.AuthServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllers implements WebMvcConfigurer {


    @Value("${cookie.expiry}")
    private int cookieExpiry;

    private final AuthServices authServices;

    public AuthControllers(AuthServices authServices) {
        this.authServices = authServices;
    }

    @GetMapping
    public void hanuman() {
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signUp(@RequestBody @Validated PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerDto response = authServices.signUpPassenger(passengerSignupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse httpResponse) {
        try {
            Map<String, Object> response = authServices.signIn(authRequestDto);
            String token = response.get("token").toString();

            ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                    .httpOnly(false)
                    .maxAge(cookieExpiry)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .build();

            httpResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Authentication failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/validate")
    public ResponseEntity<?> validate(HttpServletRequest request) {
        for(Cookie cookie:request.getCookies()){
            System.out.println( cookie.getName() + "  " + cookie.getValue());
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}