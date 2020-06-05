package io.zhile.research.intellij;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ApplicationComponent;
import io.zhile.research.intellij.action.ResetAction;
import io.zhile.research.intellij.helper.Constants;
import io.zhile.research.intellij.helper.NotificationHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

public class MainComponent implements ApplicationComponent {
    private static final long RESET_PERIOD = 2160000000L; // 25 days

    public void initComponent() {
        Preferences prefs = Preferences.userRoot().node(Constants.PLUGIN_NAME);
        long lastResetTime = prefs.getLong(Constants.RESET_TIME_KEY, 0L);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (lastResetTime > 0) {
                    Date date = new Date(lastResetTime);
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    NotificationHelper.showInfo(null, "The last reset time: " + format.format(date));
                }

                new ResetTimerTask(lastResetTime).run();
            }
        }, 3000);
    }

    protected static class ResetTimerTask extends TimerTask {
        private final long lastResetTime;

        public ResetTimerTask(long lastResetTime) {
            this.lastResetTime = lastResetTime;
        }

        @Override
        public void run() {
            if (System.currentTimeMillis() - lastResetTime > RESET_PERIOD) {
                String message = "It has been a long time since the last reset!\nWould you like to reset it again?";
                Notification notification = NotificationHelper.NOTIFICATION_GROUP.createNotification(Constants.PLUGIN_NAME, null, message, NotificationType.INFORMATION);
                notification.addAction(new ResetAction());
                notification.notify(null);
            }

            new Timer().schedule(new ResetTimerTask(lastResetTime), 3600000); // 60 min
        }
    }
}
