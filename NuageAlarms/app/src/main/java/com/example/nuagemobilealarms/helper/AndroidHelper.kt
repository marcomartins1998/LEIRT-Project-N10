package com.example.nuagemobilealarms.helper

import android.content.Context
import android.widget.Toast

class AndroidHelper{

    fun toastMessage(context: Context, msg: String): Unit = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

}