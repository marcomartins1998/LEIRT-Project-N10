package com.example.nuagemobilealarms.helper

import android.content.Context
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.widget.Toast
import com.example.nuagemobilealarms.AlarmFiltersActivity
import com.example.nuagemobilealarms.DescriptionActivity
import com.example.nuagemobilealarms.NotificationFiltersActivity
import com.example.nuagemobilealarms.SettingsActivity

class AndroidHelper{
    companion object {
        fun toastMessage(context: Context, msg: String?): Unit = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

        fun setupDrawer(context: Context, intent: Intent, navigationView: NavigationView, drawerLayout: DrawerLayout) {
            navigationView.setNavigationItemSelectedListener {
                //it.isChecked = true
                drawerLayout.closeDrawer(navigationView)
                true
            }
            navigationView.menu.getItem(0).setOnMenuItemClickListener {
                if (context.javaClass.simpleName == AlarmFiltersActivity::class.java.simpleName) drawerLayout.closeDrawer(
                    navigationView
                )
                else context.startActivity(Intent(context, AlarmFiltersActivity::class.java).putExtras(intent.extras!!))
                true
            }
            navigationView.menu.getItem(1).setOnMenuItemClickListener {
                if (context.javaClass.simpleName == NotificationFiltersActivity::class.java.simpleName) drawerLayout.closeDrawer(
                    navigationView
                )
                else context.startActivity(
                    Intent(
                        context,
                        NotificationFiltersActivity::class.java
                    ).putExtras(intent.extras!!)
                )
                true
            }
            navigationView.menu.getItem(2).setOnMenuItemClickListener {
                intent.putExtra("parentActivity", context.javaClass.simpleName)
                context.startActivity(Intent(context, SettingsActivity::class.java).putExtras(intent.extras!!))
                true
            }
            navigationView.menu.getItem(3).setOnMenuItemClickListener {
                context.startActivity(Intent(context, DescriptionActivity::class.java).putExtras(intent.extras!!))
                true
            }
        }
    }

}