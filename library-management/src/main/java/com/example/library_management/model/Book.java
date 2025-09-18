package com.example.library_management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.web.PageableDefault;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    //Book : Author = N : 1
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Genre genre;

    private String name;

    @ManyToOne //Book : Author = Many : One
    @JoinColumn //Make author id as a foreign key
    @JsonIgnoreProperties({"bookList", "createdOn", "updatedOn"})
    private Author author; //Hibernate automatically picks the primary key of the Author table/object.

    @ManyToOne
    @JoinColumn
    @JsonManagedReference
    private Student student;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties("book")
    private List<Transaction> transactions;

}
