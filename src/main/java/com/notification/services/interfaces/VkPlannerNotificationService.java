package com.notification.services.interfaces;

import java.util.Map;

public interface VkPlannerNotificationService {
    void sendNotificationToClient(Map<String, String> usersToNotificateAndMessages);
}
