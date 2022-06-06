package com.example.springsecurity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "groups_permission")
public class GroupPermission {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(optional = false)
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
