package com.example.nuagemobilealarms.helper

import android.content.Context
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.widget.Toast

class AndroidHelper{
    companion object {
        fun toastMessage(context: Context, msg: String?): Unit = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

        fun setupDrawer(context: Context, navigationView: NavigationView, drawerLayout: DrawerLayout) {
            navigationView.setNavigationItemSelectedListener {
                //it.isChecked = true
                drawerLayout.closeDrawer(navigationView)
                true
            }

            navigationView.menu.getItem(0).setOnMenuItemClickListener {

                true
            }
        }
    }

}