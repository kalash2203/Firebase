package com.example.firebase.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.media.Image
import android.net.Uri
import android.util.Log
import com.example.firebase.ui.LoginActivity
import com.example.firebase.ui.RegisterActivity
import com.example.firebase.model.User
import com.example.firebase.ui.UserProfileActivity
import com.example.firebase.utils.Constants.FIREBASE_PREFERENCES
import com.example.firebase.utils.Constants.LOGGED_IN
import com.example.firebase.utils.Constants.USERS
import com.example.firebase.utils.Constants.getFileExtensions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI

class FireStoreClass {
    val TAG = "FireStoreClass"
    var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    //to upload data on firestore db
    fun registerUser(Activity: RegisterActivity, user: User) {
        fireStore.collection(USERS)
            .document(getCurrentUserId()).set(user, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "doc added")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    // to get the id of current user who is logged in
    fun getCurrentUserId(): String {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var currentId = ""
        if (currentUser != null) {
            currentId = auth.uid.toString()
        }
        return currentId
    }
    //provide user details (we have given while registering user) by fetching from firestore db and storing it in shared preferences

    fun getUserDetails(activity: Activity) {
        fireStore.collection(USERS)
            .document(getCurrentUserId())
            .get()

            //addOnSuccessListener returns DocumentSnapshot and convert that document snapshot into POJO

            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)

                //we hv made object of shared preference and provided it a context()
                val sharedPreferences =
                    activity.getSharedPreferences(FIREBASE_PREFERENCES, Context.MODE_PRIVATE)
                //now making editor of shared preferences helps in putting values in shared preference
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(LOGGED_IN, "${user?.firstName} ${user?.lastName}")

                if (user != null) {
                    LoginActivity().loggedInSuccess(user)
                }
            }
    }

    //uploading image to firebase cloud or firebase storage.
    fun uploadImageToCloud(activity: Activity, uri: Uri, image: String) {
        //image is denoting the random name gvn to image
        val sReference: StorageReference = FirebaseStorage.getInstance().reference
            //renaming the file name using .child
            //.currentTimeMillis is giving the current time in milli seconds when image is uploaded.
            .child(
                image + System.currentTimeMillis() +
                        "." + getFileExtensions(activity, uri)
            )
        //putting file in firebase storage by adding uri of the image.
        //provide uri of the image that is uploaded on the firebase storage.
        sReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->

                Log.e("firebaseImageUrl", taskSnapshot.metadata?.reference?.downloadUrl.toString())
                //download url is the url of the image uploaded on firebase storage
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                    when (activity) {
                        is UserProfileActivity -> activity.imageUploadSucess(uri.toString())
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    fun updateUserProfileData(activity: Activity, userHashmap: HashMap<String, Any>) {
        fireStore.collection(USERS)
            .document(getCurrentUserId())
            .update(userHashmap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> activity.userProfileUpdateSucess()
                }
            }
    }
}


