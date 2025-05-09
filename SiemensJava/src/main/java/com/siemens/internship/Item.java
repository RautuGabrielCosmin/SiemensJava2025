package com.siemens.internship;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotBlank(message = "The email is required!")
    private Long id;
    @NotBlank(message = "The name is required!")
    private String name;
    @NotBlank(message = "The description is required!")
    private String description;
    @NotBlank(message = "The status is required!")
    private String status;

    // Add email regex validation TODO:marked
    @NotBlank(message = "The email is required!")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
             message= "The email must follow the pattern from local@domain.tld")
    private String email;
}