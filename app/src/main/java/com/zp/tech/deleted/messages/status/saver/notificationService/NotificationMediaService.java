package com.zp.tech.deleted.messages.status.saver.notificationService;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;

import com.zp.tech.deleted.messages.status.saver.BaseApplication;
import com.zp.tech.deleted.messages.status.saver.R;
import com.zp.tech.deleted.messages.status.saver.database.Messages;
import com.zp.tech.deleted.messages.status.saver.database.Users;
import com.zp.tech.deleted.messages.status.saver.database.repository.MyRepository;
import com.zp.tech.deleted.messages.status.saver.ui.activities.MainActivity;

import java.text.SimpleDateFormat;

public class NotificationMediaService extends NotificationListenerService {

    public static final String OBSERVER_INTENT = "com.zp.observer.start";
    public static final String WHTSBUSINESS = "com.whatsapp.w4b";
    public static final String WHTAPP = "com.whatsapp";
    private MyRepository chatRepository;


    @Override
    public void onCreate() {
        super.onCreate();
        chatRepository = ((BaseApplication) getApplication()).getRepository();
    }


    public void onListenerConnected() {
        try {
            getActiveNotifications();
        } catch (SecurityException exp) {
            exp.printStackTrace();
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        try {
            try {
                if (sbn.getPackageName().equals(WHTAPP) || sbn.getPackageName().equals(WHTSBUSINESS)) {
                    Bundle bundle = sbn.getNotification().extras;
                    if (!bundle.getBoolean("android.isGroupConversation", false)) {
                        String title = bundle.getString("android.title");
                        String text = bundle.getString("android.text");
                        long row_id = bundle.getLong("last_row_id");
                        String time = getTime(sbn.getPostTime());
                        String date = getDate(sbn.getPostTime());
                        if (row_id != 0) {

                            if (text != null) {
                                Users users;
                                Messages messages;
                                if (text.equals("This message was deleted")) {
                                    // users = new Users(title, text, time, row_id, sbn.getPackageName());
                                    // messages = new Messages(title, text, time, row_id, sbn.getPackageName(), true);
                                    chatRepository.updateMessages(true, row_id, title);
                                    String message = chatRepository.getDeletedMessage(title, row_id);
                                    if (message != null && !message.isEmpty()) {
                                        showNotification(title, title + " : " + message);
                                    }


                                } else {
                                    users = new Users(title, text, time, date, row_id, sbn.getPackageName(), false);
                                    messages = new Messages(title, text, time, date, row_id, sbn.getPackageName(), false);
                                    chatRepository.insertUser(users);
                                    chatRepository.insertMessages(messages);

                                }

                            }
                        }
                    } else {
                        String conversationTitle = bundle.getString("android.conversationTitle");
                        String title = bundle.getString("android.title");
                        String text = bundle.getString("android.text");
                        String time = getTime(sbn.getPostTime());
                        String date = getDate(sbn.getPostTime());
                        String message = title.substring(title.indexOf(":") + 1, title.length()) + ":" + text;
                        String groupTitle = "";
                        long last_row_id = bundle.getLong("last_row_id");
                        if (last_row_id != 0) {
                            if (conversationTitle.contains("(")) {
                                groupTitle = conversationTitle.substring(0, conversationTitle.indexOf("(") - 1);
                            } else {
                                groupTitle = conversationTitle;
                            }
                            Users users;
                            Messages messages;


                            if (text != null) {
                                if (text.equals("This message was deleted")) {
                                    //users = new Users(groupTitle, message, time, last_row_id, sbn.getPackageName());
                                    // messages = new Messages(groupTitle, message, time, last_row_id, sbn.getPackageName(), true);
                                    chatRepository.updateMessages(true, last_row_id, groupTitle);
                                    String messageDeleted = chatRepository.getDeletedMessage(title, last_row_id);
                                    if (messageDeleted != null && !messageDeleted.isEmpty()) {
                                        showNotification(title, title + " : " + message);
                                    }

                                } else {
                                    users = new Users(groupTitle, message, time, date, last_row_id, sbn.getPackageName(), true);
                                    messages = new Messages(groupTitle, message, time, date, last_row_id, sbn.getPackageName(), false);
                                    chatRepository.insertUser(users);
                                    chatRepository.insertMessages(messages);
                                }
                            }
                        }
                    }
                }

            } catch (StringIndexOutOfBoundsException exp) {
                exp.printStackTrace();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


    private String getTime(Long time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        return simpleDateFormat.format(time);
    }

    private String getDate(Long time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(time);
    }

    private void showNotification(String title, String str) {
        try {
            NotificationCompat.Builder builder;
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel("whatsG", "whatsG", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                notificationManager.createNotificationChannel(notificationChannel);
            }
            builder = new NotificationCompat.Builder(this, "whatsG")
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Message Recovered").setContentText(str)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true);
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentIntent(pendingIntent);
            notificationManager.notify(1, builder.build());
        } catch (Exception str2) {
            str2.printStackTrace();
        }
    }


}
