package com.example.diaryapp2.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Validated
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties("user")
public class Diary {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id", nullable = false)

    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="user_id") //creation of foreign key
    private User user;

    @Size(max=255)
    @Column(nullable = false)
    @NotBlank
    @NotNull
    private String title;

    @CreationTimestamp
    private LocalDateTime creationTime;

//    @Lob //to store an entity that doesn't have a corresponding table in the database/reference
    @OneToMany(mappedBy="diary",
            cascade=CascadeType.ALL,
            fetch=FetchType.LAZY,
            orphanRemoval = true)
    private Set<Entry> entries;


    public Diary(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("id:%d\ttitle:%s", id,title);
    }
}
