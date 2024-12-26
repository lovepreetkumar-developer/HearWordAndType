package com.hear_word_and_type.app.util

import android.graphics.Bitmap
import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.hear_word_and_type.app.R
import java.io.File

class BindingUtils {

    companion object {

        @BindingAdapter(value = ["setBitmap"])
        @JvmStatic
        fun setBitmap(imageView: AppCompatImageView, bitmap: Bitmap?) {
            bitmap?.let {
                imageView.setImageBitmap(bitmap)
            }
        }

        @BindingAdapter(value = ["simpleResource"])
        @JvmStatic
        fun setStepGroupIcon(imageView: AppCompatImageView, simpleResource: Int) {
            if (simpleResource != -1) {
                imageView.setImageResource(simpleResource)
            }
        }

        @BindingAdapter(value = ["setSelectedTrueString"])
        @JvmStatic
        fun setSelectedTrueString(textView: AppCompatTextView, string: String) {
            textView.isSelected = true
        }

                @BindingAdapter(value = ["image_path"], requireAll = false)
        @JvmStatic
        fun setBitmapOnImage(imageView: ImageView?, imagePath: String?) {
            Picasso.get().load(File(imagePath))
                .centerCrop()
                .resize(100, 100)
                .into(imageView)
        }


        @BindingAdapter(value = ["setPaintFlag"], requireAll = false)
        @JvmStatic
        fun setPaintFlag(textView: TextView?, string: String) {

            textView?.let {
                it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }


        @BindingAdapter(value = ["simpleGlideMedia"], requireAll = false)
        @JvmStatic
        fun setSimpleGlideMedia(imageView: ImageView, imagePath: String?) {

            imagePath?.let {
                if (imagePath != "") {
                    Glide.with(imageView.context)
                        .load(imagePath)
                        .placeholder(R.drawable.ic_image_placeholder)
                        .into(imageView)
                }
            }
        }
    }
}