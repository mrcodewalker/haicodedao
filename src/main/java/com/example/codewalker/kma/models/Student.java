package com.example.codewalker.kma.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Entity
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "students")
//, uniqueConstraints = @UniqueConstraint(columnNames = "student_code")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "student_code", length = 20, unique = true, nullable = false, columnDefinition = "VARCHAR(20) DEFAULT ''")
    private String studentCode;

    @Column(name = "student_name", length = 100, nullable = false)
    private String studentName;

    @Column(name = "student_class", length = 20, columnDefinition = "VARCHAR(20) DEFAULT ''")
    private String studentClass;
}
