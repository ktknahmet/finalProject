package com.ktknahmet.final_project.utils.workManager

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.utils.Constant.CHANNEL_ID
import com.ktknahmet.final_project.utils.Constant.CHANNEL_NAME
import com.ktknahmet.final_project.utils.Constant.NOTIF_ID

class NotificationService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val nofification= NotificationCompat.Builder(context!!,CHANNEL_ID)
            .setSmallIcon(R.drawable.iv_notification)
            .setContentText(CHANNEL_NAME)
            .setContentText("ahmet")
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(NOTIF_ID,nofification)
    }
}