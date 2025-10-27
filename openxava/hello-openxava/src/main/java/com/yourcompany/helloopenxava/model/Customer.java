package com.yourcompany.helloopenxava.model;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;

/**
 * Customer entity - hello world example for OpenXava tutorial.
 *
 * This demonstrates the minimal code needed for a full CRUD application:
 * - @Entity marks it as persistent
 * - @Getter @Setter from Lombok generate accessors
 * - @Id marks the primary key
 * - @Column(length=X) sets UI and DB constraints
 * - @Required makes fields mandatory
 */

@Entity @Getter @Setter
public class Customer {

    @Id
    @Column(length=10)
    @Required
    private int number;

    @Column(length=50)
    @Required
    private String name;

}
