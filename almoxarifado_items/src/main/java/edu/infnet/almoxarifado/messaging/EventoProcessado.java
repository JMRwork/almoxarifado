package edu.infnet.almoxarifado.messaging;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "processed_events")
@NoArgsConstructor
@AllArgsConstructor
public class EventoProcessado {
    @Id
    private UUID eventId;
}