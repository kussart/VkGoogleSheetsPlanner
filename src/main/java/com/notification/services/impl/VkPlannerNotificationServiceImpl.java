package com.notification.services.impl;

import com.notification.configs.property.VkProperties;
import com.notification.services.interfaces.VkPlannerNotificationService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class VkPlannerNotificationServiceImpl implements VkPlannerNotificationService {
    private static final String NOTIFICATION_URL = "https://api.vk.com/method/messages.send?domain={domain}&message={message}&access_token={access_token}&v={v}";
    private static final List<Integer> ERROR_CODES_INVALID_TOKEN = Arrays.asList(900, 901, 902, 913, 914);
    private final Logger logger = LoggerFactory.getLogger(VkPlannerNotificationService.class);
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
            ResponseEntity<String> response = restTemplate.postForEntity(NOTIFICATION_URL, null, String.class, vkUrlVariables);
            try {
                checkForInvalidToken(response);
            } catch (JSONException e) {
                logger.error("Ошибка в получении содержимого из response" + e.getMessage());
            }
        }
    }

    private void checkForInvalidToken(ResponseEntity<String> response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response.getBody());
        if (jsonObject.has("error")) {
            int code = jsonObject.getJSONObject("error").getInt("error_code");
            if (ERROR_CODES_INVALID_TOKEN.contains(code)) {
                switch (code){
                    case 900: logger.error("Нельзя отправлять сообщение пользователю из черного списка \n");
                    break;
                    case 901: logger.error("Нельзя первым писать пользователю от имени сообщества \n");
                    break;
                    case 902: logger.error("Нельзя отправлять сообщения этому пользователю в связи с настройками приватности \n");
                    break;
                    case 913: logger.error("Слишком много пересланных сообщений \n");
                    break;
                    case 914: logger.error("Сообщение слишком длинное\n");
                    break;
                }
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
