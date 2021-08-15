package bepicky.bot.client.service;

import bepicky.common.domain.request.NotifyMessageRequest;
import bepicky.common.domain.request.NotifyNewsRequest;

public interface INotificationService {

    void newsNoteNotification(NotifyNewsRequest request);

    void messageNotification(NotifyMessageRequest request);

}
