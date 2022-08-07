package com.zp.tech.deleted.messages.status.saver.notificationService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import static android.content.Context.NOTIFICATION_SERVICE;

import com.zp.tech.deleted.messages.status.saver.R;
import com.zp.tech.deleted.messages.status.saver.ui.MainActivity;

import org.apache.commons.io.FileUtils;


public class MediaFileObserver extends FileObserver {
    private Context mContext;
    private String mHolderPath;
    private String cachePath = "";
    private String savedPath = "";

    private void copyToCache(File sourceFile, File destinationFile) {
        try {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try {
                bis = new BufferedInputStream(new FileInputStream(sourceFile));
                bos = new BufferedOutputStream(new FileOutputStream(destinationFile, false));

                byte[] buf = new byte[1024];
                bis.read(buf);

                do {
                    bos.write(buf);
                } while (bis.read(buf) != -1);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) bis.close();
                    if (bos != null) bos.close();

                    if (destinationFile.getAbsolutePath().contains(".opus")) {
                        String rename = destinationFile.getAbsolutePath().replace(".opus", ".mp3");
                        destinationFile.renameTo(new File(rename));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void copyFromCache(File sourceFile, File destinationFile) {
        try {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try {
                bis = new BufferedInputStream(new FileInputStream(sourceFile));
                bos = new BufferedOutputStream(new FileOutputStream(destinationFile, false));

                byte[] buf = new byte[1024];
                bis.read(buf);

                do {
                    bos.write(buf);
                } while (bis.read(buf) != -1);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) bis.close();
                    if (bos != null) bos.close();


                    deleteMediaFile(sourceFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }

    MediaFileObserver(String str, Context context) {
        super(str, FileObserver.ALL_EVENTS);
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
            stringBuilder.append("/Whats Download/.cache/");
            this.cachePath = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
            stringBuilder.append("/Whats Download/");
            this.savedPath = stringBuilder.toString();
            this.mHolderPath = str;
            this.mContext = context;

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public void onEvent(int i, final String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            if (i == ACCESS || i == MODIFY || i == OPEN || i == MOVED_TO) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    String stringBuilder1 = MediaFileObserver.this.mHolderPath +
                            str;
                    File file = new File(stringBuilder1);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(MediaFileObserver.this.cachePath);
                    File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Whats Download/.cache/" + str);
                    if (!file1.exists()) {
                        stringBuilder2.append(str);
                        try {
                            copyToCache(file, new File(stringBuilder2.toString()));
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }

                    }
                });
            }
            if (i == DELETE_SELF || i == DELETE) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    String deletedString = str.replace(".opus", ".mp3");
                    showNotification(deletedString);
                    String stringBuilder = MediaFileObserver.this.cachePath +
                            deletedString;
                    File file = new File(stringBuilder);

                    String stringBuilder2 = MediaFileObserver.this.savedPath +
                            deletedString;
                    try {
                        copyFromCache(file, new File(stringBuilder2));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }


                });
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void deleteMediaFile(String str) {
        try {
            File file = new File(str);
            FileUtils.delete(file);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private void showNotification(String str) {
        try {
            NotificationCompat.Builder builder;
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel("deleted", "deleted", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                notificationManager.createNotificationChannel(notificationChannel);
            }
            builder = new NotificationCompat.Builder(mContext, "deleted")
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("File Recovered").setContentText(str)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true);
            Intent intent = new Intent(mContext, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

            builder.setContentIntent(pendingIntent);
            notificationManager.notify(1, builder.build());
        } catch (Exception str2) {
            str2.printStackTrace();
        }
    }

}
