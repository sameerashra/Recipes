package com.example.recipes.utils

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation

@BindingAdapter("formatText")
fun formatText(textView: TextView, text: String){
    // Format text to show html tags like <b> tag
    textView.text = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
        Html.fromHtml(
            text,
            Html.FROM_HTML_MODE_LEGACY
        )
    } else {
        Html.fromHtml(text)
    }
}

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String){
    // Load image using Coil library
    imageView.load(url){
        crossfade(true)
        transformations(RoundedCornersTransformation(10f))
    }
}