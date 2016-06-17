package com.scholars.doctor.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by I311636 on 6/8/2016.
 */
public class UserManager {

    private static DatabaseReference myRef=FirebaseDatabase.getInstance().getReference() ;
    // User user;

    public interface UserCallBacks{
        public void onSuccess(User user);


    }

    public static void createUser(String username, String name, String address, String password, String role, final UserCallBacks callback){
        final User user = new User(username, name, password, address, role, null);
        Query userQuery = myRef.child("users").child(username);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                User v = snapshot.getValue(User.class);
                if(v == null){
                    myRef.child("users").child(user.getUsername()).setValue(user);
                    callback.onSuccess(user);
                }
                else{
                    callback.onSuccess(v);
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public static void getUser(String userId, final UserCallBacks callback){
        Query userQuery = myRef.child("users").child(userId);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                User v = snapshot.getValue(User.class);
                callback.onSuccess(v);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public static void modifyUser(String userId,User user, final UserCallBacks callback){
        myRef.child("users").child(user.getUsername()).setValue(user);
        callback.onSuccess(user);
    }



    public static void modifyUserColumn(String userId, final String columnName, final String columnValue){
        myRef.child("users").child(userId).child(columnName).setValue(columnValue);
    }




}
