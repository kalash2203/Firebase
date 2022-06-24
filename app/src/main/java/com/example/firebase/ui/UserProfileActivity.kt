package com.example.firebase.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.firebase.R
import com.example.firebase.firestore.FireStoreClass
import com.example.firebase.model.User
import com.example.firebase.utils.Constants
import com.example.firebase.utils.Constants.FIRST_NAME
import com.example.firebase.utils.Constants.LAST_NAME
import com.example.firebase.utils.Constants.LOGIN_USER_DETAILS
import com.example.firebase.utils.Constants.MALE
import com.example.firebase.utils.Constants.PROFILE_IMAGE_REQUEST_CODE
import com.example.firebase.utils.Constants.READ_STORAGE_REQUEST_CODE
import com.example.firebase.utils.Constants.USER_PROFILE_IMAGE
import com.example.firebase.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException


class UserProfileActivity : AppCompatActivity() {
    lateinit var mUserDetails: User
    val TAG = "UserProfileActivity"
    var mUserProfileImageUrl: String = ""

    //making data type nullable
    var mSelectedImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        val intent = Intent()
        //extracting information from intent first checking if there is some extra field or not which is sended and then pass key
        //in getParcelableExtra to get the value from Intent.
        if (intent.hasExtra(LOGIN_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(LOGIN_USER_DETAILS)!!
        }
        if (mUserDetails.isProfileCompleted != false) {
            tv_title.text = "COMPLETE PROFILE!!"
            //now we are making first name ,last name and email these not editable and setting data from mUserDetails into it.
            et_first_name.isEnabled = false
            et_first_name.setText(mUserDetails.firstName)
            et_last_name.isEnabled = false
            et_last_name.setText(mUserDetails.lastName)
            et_email.isEnabled = false
            et_email.setText(mUserDetails.emailID)
        } else {
            tv_title.text = "EDIT PROFILE!!"
            //now we are making first name ,last name and email these not editable and setting data from mUserDetails into it.
            et_first_name.setText(mUserDetails.firstName)
            et_last_name.setText(mUserDetails.lastName)
            et_email.isEnabled = false
            et_email.setText(mUserDetails.emailID)
            //checking if mob is empty or not
            if (mUserDetails.mobileNumber != "") {
                et_mobile_number.setText(mUserDetails.mobileNumber)
            }
            //checking is gender is male and box is checked
            if (mUserDetails.gender == MALE) {
                rb_male.isChecked = true
            } else {
                rb_female.isChecked = true
            }
        }
        iv_user_photo.setOnClickListener {
            //checking for permission of reading external storage (permission granted or not),if permission granted then call gallery
            // intent,else will ask for the permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageDialog(this)
            } else {
                //Asking for granting permissions
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_REQUEST_CODE
                )
            }
        }
//applying setOnClickListener on save button in userProfileActivity.
        btn_save.setOnClickListener {
            if (validation()) {
                if (mSelectedImageFileUri != null) {
                    FireStoreClass().uploadImageToCloud(
                        this,
                        mSelectedImageFileUri!!,
                        USER_PROFILE_IMAGE
                    )
                } else {
                    updateUserProfileDetails()
                }
            }
        }
    }

    fun validation(): Boolean {

        if (et_mobile_number.text.toString().trim() { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Please Enter a Mobile Number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (et_mobile_number.text.toString().trim().length != 10) {
            Toast.makeText(this, "Please enter a valide Mobile Number", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    //after clicking on allow or deny we add the functionality/body of functn in android in overriding functn  onrequestpermissionresult
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //checking the req code
        if (requestCode == READ_STORAGE_REQUEST_CODE) {
            //got req code,then if permission granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageDialog(this)
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //it provides result for the gallery intent that was provided.
    //when intent picks anything and then come back to the activity then this function is called.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == PROFILE_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        //loading image in the imageview of userprofileactivity using glide
                        mSelectedImageFileUri = data.data
                        GlideLoader(this).loadUserImage(mSelectedImageFileUri!!, iv_user_photo)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this, "Image Loading Failed", Toast.LENGTH_SHORT
                        )
                            .show()

                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.e(TAG, "request cancelled")
        }
    }
//
    fun imageUploadSucess(imageUrl: String) {
        mUserProfileImageUrl = imageUrl
    //attach the url along with other details like name, mobileNo, gender
        //upload details on firestore database including image url
        updateUserProfileDetails()
    }

    fun userProfileUpdateSucess() {
        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT)
            .show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateUserProfileDetails() {
        //CREATE hashmap to put user details
        // then pass the hashmap to firestore
        val userHashMap = HashMap<String, Any>()
        val first_name = et_first_name.text.toString().trim() { it <= ' ' }
        // check if entered first name is not similar to old first name
        // if it is similar then dont upload first name on database
        if (first_name != mUserDetails.firstName) {
            userHashMap[FIRST_NAME] = first_name
        }
        val last_name = et_last_name.text.toString().trim() { it <= ' ' }
        if (last_name != mUserDetails.lastName) {
            userHashMap[LAST_NAME] = last_name
        }
        val mobile_number = et_mobile_number.text.toString().trim() { it <= ' ' }
        val gender = if (rb_male.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if (mUserProfileImageUrl.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageUrl
        }

        if (mobile_number.isNotEmpty()) {
            userHashMap[Constants.MOBILE_NUMBER] = mobile_number
        }
        if (gender.isNotEmpty()) {
            userHashMap[Constants.GENDER] = gender
        }

        //we check if profile is not completed then we will put true in it
        if (mUserDetails.isProfileCompleted == false) {
            userHashMap[Constants.IS_PROFILE_COMPLETED] = true
        }
        //now hashmap is created
        //at last we will pass the hashmap in firestore
        FireStoreClass().updateUserProfileData(this, userHashMap)
    }
}

