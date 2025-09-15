ALTER TABLE booking_review
    DROP FOREIGN KEY FK_BOOKING_REVIEW_ON_BOOKING;

ALTER TABLE passenger_review
    DROP FOREIGN KEY FK_PASSENGER_REVIEW_ON_ID;

ALTER TABLE passenger
    ADD password VARCHAR(255) NULL;

ALTER TABLE passenger
    ADD phone_number VARCHAR(255) NULL;

ALTER TABLE passenger
    MODIFY password VARCHAR(255) NOT NULL;

ALTER TABLE passenger
    MODIFY phone_number VARCHAR(255) NOT NULL;

DROP TABLE booking_review;

DROP TABLE passenger_review;

ALTER TABLE passenger
    MODIFY name VARCHAR(255) NOT NULL;