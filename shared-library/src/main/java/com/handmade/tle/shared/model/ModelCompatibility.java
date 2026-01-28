package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "model_compatibility")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelCompatibility {
    @Id
    public String id;

    @Column(name = "prompt_id")
    public String promptId;

    @Column(name = "model_id")
    public String modelId;

    @Enumerated(EnumType.STRING)
    public ModelStatus status;

    @Column(name = "tested_at")
    public Date testedAt;

    @Column(name = "test_pass_rate")
    public double testPassRate;

    public String notes;
}
