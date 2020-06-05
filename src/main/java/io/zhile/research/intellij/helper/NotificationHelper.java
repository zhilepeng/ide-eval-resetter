package io.zhile.research.intellij.helper;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class NotificationHelper {
    public static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup(Constants.PLUGIN_ID.getIdString(), NotificationDisplayType.BALLOON, true);

    public static Notification show(@Nullable Project project, String title, String subtitle, String content, NotificationType type) {
        if (title == null) {
            title = Constants.PLUGIN_NAME;
        }

        Notification notification = NOTIFICATION_GROUP.createNotification(title, subtitle, content, type);
        notification.notify(project);

        return notification;
    }

    public static Notification showError(@Nullable Project project, String title, String subtitle, String content) {
        return show(project, title, subtitle, content, NotificationType.ERROR);
    }

    public static Notification showError(@Nullable Project project, String title, String content) {
        return showError(project, title, null, content);
    }

    public static Notification showError(@Nullable Project project, String content) {
        return showError(project, null, null, content);
    }

    public static Notification showWarn(@Nullable Project project, String title, String subtitle, String content) {
        return show(project, title, subtitle, content, NotificationType.WARNING);
    }

    public static Notification showWarn(@Nullable Project project, String title, String content) {
        return showWarn(project, title, null, content);
    }

    public static Notification showWarn(@Nullable Project project, String content) {
        return showWarn(project, null, null, content);
    }

    public static Notification showInfo(@Nullable Project project, String title, String subtitle, String content) {
        return show(project, title, subtitle, content, NotificationType.INFORMATION);
    }

    public static Notification showInfo(@Nullable Project project, String title, String content) {
        return showInfo(project, title, null, content);
    }

    public static Notification showInfo(@Nullable Project project, String content) {
        return showInfo(project, null, null, content);
    }
}
