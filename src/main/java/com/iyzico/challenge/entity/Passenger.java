package com.iyzico.challenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private String email;
    private int age;

}
