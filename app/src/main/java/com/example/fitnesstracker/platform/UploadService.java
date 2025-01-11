package com.example.fitnesstracker.platform;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.presentation.MainActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class UploadService extends Service {
    private CompositeDisposable disposables;
    private static final @NonNull String channelId = "PhotoUploadChannel";
    private static final int notificationId = 1;

    @Nullable
    @Override
    public final IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        disposables = new CompositeDisposable();
        createNotificationChannel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            startForeground(notificationId, buildNotification("Загрузка фото..."));
            final var uploading = upload(intent)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (v) -> stopService(true),
                            (e) -> {
                                updateNotification("Ошибка загрузки");
                                stopService(false);
                            }
                    );

            disposables.add(uploading);
        }

        return START_NOT_STICKY;
    }

    private void stopService(boolean removeNotifications) {
        stopForeground(removeNotifications);
        stopSelf();
    }

    private void createNotificationChannel() {
        final var channel = new NotificationChannel(
                channelId,
                "Photo Upload Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        final var manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    @NonNull
    private Notification buildNotification(String message) {
        final var intent = new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        final var builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_upload)
                .setContentTitle("Загрузка")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE);
        }

        return builder.build();
    }

    private void updateNotification(@NonNull String message) {
        getSystemService(NotificationManager.class)
                .notify(notificationId, buildNotification(message));
    }

    abstract Single<?> upload(@NonNull Intent intent);
}
