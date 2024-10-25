package com.example.codewalker.kma.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "subject")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Subjects {

    @Id
    @Column(name = "subject_code", length = 225)
    private String subjectCode;

    @Column(name = "subject_name", length = 255, nullable = false)
    private String subjectName;

    @ManyToOne
    @JoinColumn(name = "department_code", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "subject_leader")
    private Staff subjectLeader;
}
