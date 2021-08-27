package bepicky.bot.client.service;

import bepicky.common.domain.request.NotifyMessageRequest;
import bepicky.common.domain.request.NewsNotificationRequest;

public interface INotificationService {

    void newsNoteNotification(NewsNotificationRequest request);

    void messageNotification(NotifyMessageRequest request);

}
