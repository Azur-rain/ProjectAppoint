package com.example.projectappoint;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseService {

    private static final String TAG = "FirebaseService";
    private static final String COLLECTION_USERS = "patient"; // Your collection name

    private static FirebaseService instance;
    private final FirebaseFirestore db;

    private FirebaseService() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FirebaseService getInstance() {
        if (instance == null) {
            instance = new FirebaseService();
        }
        return instance;
    }

    public interface UserAddCallback {
        void onSuccess(String documentId);
        void onFailure(Exception e);
    }

    public interface LoginCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public void addUser(UserData userData, final UserAddCallback callback) {
        db.collection(COLLECTION_USERS)
                .add(userData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "User added with ID: " + documentReference.getId());
                        if (callback != null) {
                            callback.onSuccess(documentReference.getId());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding user", e);
                        if (callback != null) {
                            callback.onFailure(e);
                        }
                    }
                });
    }

    public void loginUser(String email, String password, final LoginCallback callback) {
        db.collection(COLLECTION_USERS)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            callback.onFailure(new Exception("User not found"));
                            return;
                        }

                        UserData user = queryDocumentSnapshots.getDocuments().get(0).toObject(UserData.class);
                        if (user != null && user.getPassword().equals(SecurityUtils.hashPassword(password))) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(new Exception("Invalid password"));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }
                });
    }
}