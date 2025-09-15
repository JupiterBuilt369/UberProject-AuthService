package org.example.uberprojectauthservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Driver extends BaseModel{

    private String name;


    @Column(nullable = false, unique = true)
    private String licenseNumber;



    @OneToMany(mappedBy = "driver" , fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private final List<Booking> bookings = new ArrayList<>();
}
