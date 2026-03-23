package com.konopka.UserService.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;
    @Column(unique = true, nullable = false, name = "auth_id")
    private int authId;
    private String firstName;
    private String lastName;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Invitation> sentInvitations = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Invitation> receivedInvitations = new ArrayList<>();


    public void addFriend(User user) {
        if (!friends.contains(user)) {
            friends.add(user);
            if (!user.getFriends().contains(this)) {
                user.addFriend(this);
            }
        }
    }


}
