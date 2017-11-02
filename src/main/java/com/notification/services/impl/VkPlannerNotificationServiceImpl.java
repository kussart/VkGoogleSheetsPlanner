package com.notification.services.impl;

import com.notification.configs.property.VkProperties;
import com.notification.services.interfaces.VkPlannerNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

//private static final List<Integer> ERROR_CODES_INVALID_TOKEN = Arrays.asList(900, 901, 902, 913, 914);

@Service
public class VkPlannerNotificationServiceImpl implements VkPlannerNotificationService {
    private static final String NOTIFICATION_URL = "https://api.vk.com/method/messages.send?domain={domain}&message={message}&access_token={access_token}&v={v}";
    private final RestTemplate restTemplate;
    private final VkProperties vkProperties;

    @Autowired
    public VkPlannerNotificationServiceImpl(RestTemplate restTemplate, VkProperties vkProperties) {
        this.restTemplate = restTemplate;
        this.vkProperties = vkProperties;
    }

    @Override
    public void sendNotificationToClient(Map<String, String> usersToNotificateAndMessages) {
        Map<String, String> vkUrlVariables = new HashMap<>();
        for (Map.Entry entry : usersToNotificateAndMessages.entrySet()) {
            vkUrlVariables = initVariables(entry, vkUrlVariables);
            try {
                restTemplate.postForEntity(NOTIFICATION_URL, null, String.class, vkUrlVariables);
            } catch (Exception e) {
                System.out.println("Ошибка");
            }
        }
    }

    private Map<String, String> initVariables(Map.Entry entry, Map<String, String> variables) {
        String domain = entry.getKey().toString();
        String message = ("don't forget: " + entry.getValue().toString());
        variables.put("domain", domain);
        variables.put("message", message);
        variables.put("access_token", vkProperties.getAccessToken());
        variables.put("v", vkProperties.getApiVersion());
        return variables;
    }


}
