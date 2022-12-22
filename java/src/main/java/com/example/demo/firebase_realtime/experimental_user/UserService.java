package com.example.demo.firebase_realtime.experimental_user;


import com.google.firebase.database.*;
import com.google.firebase.internal.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Service
public class UserService{

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/");

    public void postUser(User user) {
        DatabaseReference usersRef = ref.child("users");

        Map<String, Object> users = new HashMap<>();
        users.put(user.getLogin(), new User(user.getLogin(), user.getEmail()));

        usersRef.updateChildrenAsync(users);
    }


    public void getUser(String login) {
        ArrayList<User> users = new ArrayList<>();
        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getLogin().equals(login)) {
                        System.out.println(user);
                        gettingUser();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error occurred " + error);
            }
        });
        System.out.println(users);
    }


    @Scheduled(fixedDelay = 1000)
    public void gettingUser() {
        System.out.println("gettingUser");
    }
}
