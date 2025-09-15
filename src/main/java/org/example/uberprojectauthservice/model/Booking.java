package org.example.uberprojectauthservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = {"driver", "passenger"})
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends BaseModel{

    @ManyToOne
    private Driver driver;

    @ManyToOne
    private Passenger passenger;

    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    private Long totalDistance;
}