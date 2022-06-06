package com.example.springsecurity.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Setter
@Getter
@Table(name = "groups")
public class Group {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "group")
    private List<GroupPermission> permissions = new ArrayList<>();

    public List<GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(gp -> new SimpleGrantedAuthority(gp.getPermission().getName()))
                .collect(toList());
    }
}
