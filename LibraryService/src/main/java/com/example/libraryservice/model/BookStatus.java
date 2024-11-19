package com.example.libraryservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookStatus {

    @Id
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "borrowed_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime borrowedAt;

    @Column(name = "due_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;

    @PrePersist
    private void init() {
        this.borrowedAt = LocalDateTime.now();
        this.dueDate = LocalDateTime.now().plusDays(7);
    }

}
