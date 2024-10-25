package com.example.codewalker.kma.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "department")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Department {

    @Id
    @Column(name = "department_code", length = 20)
    private String departmentCode;

    @Column(name = "department_name", length = 255)
    private String departmentName;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "id_faculty", nullable = false)
    private Integer facultyId;
}
