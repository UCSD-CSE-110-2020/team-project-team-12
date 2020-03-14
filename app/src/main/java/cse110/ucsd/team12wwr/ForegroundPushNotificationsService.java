package cse110.ucsd.team12wwr;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ForegroundPushNotificationsService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(R.drawable.walk)
                .build();
        //NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        //manager.notify(123, notification);

       // Intent resultIntent = new Intent(this, ProposedWalkScreen.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        //PendingIntent resultPendingIntent =
          //      stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
               // new Intent(getApplicationContext(), ProposedWalkScreen.class), 0);

        //NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123");
        //builder.setContentIntent(contentIntent);

    }
}
