package bepicky.bot.client.service;

import bepicky.common.domain.request.NewsNotificationRequest;
import bepicky.common.msg.TextMessage;

public interface INotificationService {

    void newsNoteNotification(NewsNotificationRequest request);

    void messageNotification(TextMessage msg);

}
