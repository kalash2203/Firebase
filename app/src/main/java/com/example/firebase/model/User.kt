package com.example.firebase.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var uID:String="",
  var firstName:String="",
  var lastName:String="",
  var emailID:String="",


):Parcelable
