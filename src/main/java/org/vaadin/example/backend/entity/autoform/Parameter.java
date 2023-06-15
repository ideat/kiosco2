package org.vaadin.example.backend.entity.autoform;

import lombok.Data;

@Data
public class Parameter {

    private String id;

    private String name;

    private String category;

    private String value;

    private String state;
}
