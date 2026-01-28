package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import java.util.Map;

@Entity
@Table(name = "variable")
public class Variable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @Column(name = "prompt_id")
    public String promptId;

    public String name;

    @Enumerated(EnumType.STRING)
    public VarType type;

    public String description;
    public boolean required;

    @Column(name = "default_value")
    public String defaultValue; // Storing as JSON string for simplicity or use a converter

    public String example; // Storing as JSON string
}
