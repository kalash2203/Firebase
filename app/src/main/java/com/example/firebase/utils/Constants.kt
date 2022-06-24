package com.example.firebase.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS = "users"
    const val FIREBASE_PREFERENCES = "firebase_preferences"
const val LOGGED_IN = "logged_in_user"
    const val  LOGIN_USER_DETAILS = "login_user_details"
    const val MALE="male"
    const val FEMALE="female"
    const val PROFILE_IMAGE_REQUEST_CODE=1
    const val READ_STORAGE_REQUEST_CODE=2

    const val FIRST_NAME="firstName"
    const val LAST_NAME="lastName"
    const val IMAGE="imagePath"
    const val  MOBILE_NUMBER="mobileNumber"
   const val GENDER="gender"
const val IS_PROFILE_COMPLETED="isProfileCompleted"
    const val USER_PROFILE_IMAGE="user_profile_image"

 fun showImageDialog(activity: Activity)
 {
     //intent will pick image from gallery that is external resource.
     val galleryIntent=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
     //FOR STARTING ACTIVITY WE SEND REQ CODE AND UPON RECEIVING RESPONSE WE CHECK THE REQ CODE.
     activity.startActivityForResult(galleryIntent, PROFILE_IMAGE_REQUEST_CODE)
 }
    //for storing data(image) in firebase storage (this function is providing the file extension)
    fun getFileExtensions(activity: Activity,uri: Uri): String? {
       return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri))
    }
}