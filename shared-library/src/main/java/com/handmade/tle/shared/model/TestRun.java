package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "test_runs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestRun {
    @Id
    public String id;

    @Column(name = "test_case_id")
    public String testCaseId;

    @Column(name = "model_id")
    public String modelId;

    public Date timestamp;

    @Column(columnDefinition = "TEXT")
    public String output;

    public boolean passed;

    @Column(name = "execution_time_ms")
    public int executionTimeMs;

    @Column(name = "tokens_used")
    public int tokensUsed;
}
