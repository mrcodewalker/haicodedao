package com.example.codewalker.kma.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "staff")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "staff_code", length = 20, nullable = false, unique = true)
    private String staffCode;

    @Column(name = "staff_name", length = 255, nullable = false)
    private String staffName;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "degree", length = 255)
    private String degree;

    @Column(name = "identity_card_number", length = 12)
    private String identityCardNumber;

    @Column(name = "identity_card_provide_day")
    @Temporal(TemporalType.DATE)
    private Date identityCardProvideDay;

    @Column(name = "identity_card_from", length = 255)
    private String identityCardFrom;

    @Column(name = "identity_card_address", length = 255)
    private String identityCardAddress;

    @Column(name = "current_location", length = 255)
    private String currentLocation;

    @Column(name = "position", length = 255)
    private String position;

    @Column(name = "work_place", length = 20)
    private String workPlace;

    @ManyToOne
    @JoinColumn(name = "department_code")
    private Department department;

    @Column(name = "tax_code", length = 20)
    private String taxCode;

    @Column(name = "account_number", length = 20)
    private String accountNumber;

    @Column(name = "bank_name", length = 255)
    private String bankName;

    @Column(name = "branch", length = 255)
    private String branch;

    @Column(name = "background_file", length = 255)
    private String backgroundFile;

    @Column(name = "main_teaching_subject", length = 255, nullable = false)
    private String mainTeachingSubject;

    @Column(name = "related_subjects", length = 255)
    private String relatedSubjects;

}
