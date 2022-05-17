package com.example.diaryapp2.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @Column(name = "id",nullable = false)
    @SequenceGenerator(
            name ="role_id_sequence",
            sequenceName =  "role_id_sequence"
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "role_id_sequence")

    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public Role(RoleType roletype) {
        this.roleType = roletype;
    }
}
