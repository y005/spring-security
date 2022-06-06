package com.example.springsecurity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Table(name = "permissions")
public class Permission {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;
}
