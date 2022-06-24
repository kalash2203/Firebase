package com.example.firebase.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.firebase.R
import java.io.IOException

class GlideLoader(val context: Context) {
   fun loadUserImage( image:Any,imageView: ImageView)
   {
      try{
      Glide.with(context)
//inside .load we pass uri or url of the image
         .load(image)
         .centerCrop()
//used for setting dummy or default image
      .placeholder(R.drawable.profile)
//in .into we pass the id of the imageview
      .into(imageView);

   }
      catch (e:IOException)
      {
         e.printStackTrace()

      }      }
}