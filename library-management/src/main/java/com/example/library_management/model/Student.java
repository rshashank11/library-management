package com.example.library_management.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

import static com.example.library_management.model.StudentStatus.ACTIVE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @OneToMany(mappedBy = "student") //attribute name of the column in Book class
    @JsonIgnoreProperties("book")
    private List<Book> bookList;

    @OneToMany(mappedBy = "student")
    @JsonIgnoreProperties("transactions")
    private List<Transaction> transactions;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private StudentStatus status = ACTIVE;
}
