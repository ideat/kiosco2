package org.vaadin.example.backend.entity.sec.creditRequest;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Charge {
    private UUID id;

    private String name;

    private Double value;

    private boolean selected;


}
