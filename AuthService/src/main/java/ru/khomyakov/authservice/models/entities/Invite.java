package ru.khomyakov.authservice.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "invites")
public class Invite {

    @Id
    private UUID userId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expirationDateTime;
}
