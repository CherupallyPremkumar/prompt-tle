package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "test_cases")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {
    @Id
    public String id;

    @Column(name = "prompt_id")
    public String promptId;

    @Column(name = "input_values", columnDefinition = "TEXT")
    public String inputValues;

    @Column(name = "expected_output", columnDefinition = "TEXT")
    public String expectedOutput;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "test_case_id")
    public List<TestRun> testRuns = new ArrayList<>();
}
