package com.example.bookservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    @SequenceGenerator(name = "book_id_seq", sequenceName = "book_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    private Long id;


    @Column(name = "ISBN")
    private String ISBN;

    @Column(name="name")
    private String name;

    @Column(name = "genre")
    private String genre;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "author")
    private String author;
}
