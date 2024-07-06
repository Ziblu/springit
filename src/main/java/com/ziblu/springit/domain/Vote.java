package com.ziblu.springit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue
    private Long id;
    private int vote;

    //user
    //link

}
