package com.app.booking_care.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "healthtest")
@Getter
@Setter
public class HealthTestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String test_name;

    @Column()
    private String description;

    @Column()
    private Long duration;

    @Column()
    private Long status;

}
