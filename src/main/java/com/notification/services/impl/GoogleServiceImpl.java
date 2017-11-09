package com.notification.services.impl;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.notification.configs.property.GoogleProperties;
import com.notification.exceptions.GoogleServiceException;
import com.notification.models.User;
import com.notification.services.interfaces.GoogleService;
import com.notification.services.interfaces.UserService;
import com.notification.services.interfaces.VkPlannerNotificationService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import java.util.*;

/**
 * Для корректной работы - необходимо создать таблицу Google.
 * Настроить параметры доступа - "все, у кого есть ссылка";
 *
 * SpreadSheetID  - для обращения к таблице с целью получить список данных, например:
 * https://docs.google.com/spreadsheets/d/1n3H8D_AZmwt7snEHLWjgxvIU1yxpl2wnQN2lkhHvu6w/edit#gid=0
 * в данном случае spreadsheetId будет:  1n3H8D_AZmwt7snEHLWjgxvIU1yxpl2wnQN2lkhHvu6w
 *
 * Table Name - имя нашего Листа в таблице - указано на закладке внизу листа.
 *
 * API Key - зарегистрировать своё приложение в Google, активировав Google Sheets API
 * и в личном кабинете выбрать получить API ключ.
 *
 * **/

@EnableScheduling
@Service
public class GoogleServiceImpl implements GoogleService {
    private static final String SHEET_GET_URL = "https://sheets.googleapis.com/v4/spreadsheets/{spreadsheetId}/values/{tableName}?majorDimension=ROWS&key={apiKey}";
    private final RestTemplate restTemplate;
    private final GoogleProperties googleProperties;
    private final VkPlannerNotificationService vkPlannerNotificationService;
    private final UserService userService;

    @Autowired
    public GoogleServiceImpl(RestTemplate restTemplate, GoogleProperties googleProperties, VkPlannerNotificationService vkPlannerNotificationService, UserService userService) {
        this.restTemplate = restTemplate;
        this.googleProperties = googleProperties;
        this.vkPlannerNotificationService = vkPlannerNotificationService;
        this.userService = userService;
    }

    @Scheduled(fixedDelay = 10000)
    @Override
    public void getUsersMessagesForVkPlanner() throws IOException, GoogleServiceException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm:ss").withLocale(new Locale("ru"));
        Map<String, String> googleUrlVariables = initVariables();
        ValueRange valueRange;
        try {
            valueRange = restTemplate.getForObject(SHEET_GET_URL, ValueRange.class, googleUrlVariables);
        } catch (HttpClientErrorException e){
            throw new GoogleServiceException("Нельзя обратиться по заданным настройкам таблицы");
        }
        List<List<Object>> userDetails = valueRange.getValues();
        userService.setAllUsers(userDetails);
        List<User> userList = userService.getAll();
        Map<String, String> usersAndNotification = new HashMap<>();
        initTasksForUsersList(userList, usersAndNotification, formatter);
        vkPlannerNotificationService.sendNotificationToClient(initTasksForUsersList(userList, usersAndNotification, formatter));
    }

    private Map<String, String> initTasksForUsersList(List<User> userList, Map<String, String> usersAndNotification, DateTimeFormatter formatter) {
        for (int i = 0; i < userList.size() ; i++) {
            String planDate = userList.get(i).getDate();
            LocalDateTime dateTask = (userService.getTaskDate(i+1) == null ? null: LocalDateTime.parse(userService.getTaskDate(i+1), formatter));
            if (((dateTask == null || dateTask.isBefore(getCurrentTime().minusHours(24))) && (planDate.equals("каждый день")))
                    || ((dateTask == null || dateTask.isBefore(getCurrentTime().minusDays(7))) && (planDate.equals("каждую неделю")))
                    || ((dateTask == null || dateTask.isBefore(getCurrentTime().minusDays(30))) && (planDate.equals("каждый месяц")))
                    || (planDate.equals(getTomorrowDate()) && (dateTask == null || dateTask.isBefore(getCurrentTime().minusHours(12)))))
            {
                userService.setTaskDate(getTaskSendingTime(formatter), i+1);
                usersAndNotification.put(userList.get(i).getVkDomain().substring(15), userList.get(i).getTask());
            }
        }
        return usersAndNotification;
    }

    private Map<String, String> initVariables() {
        Map<String, String> variables = new HashMap<>();
        variables.put("spreadsheetId", googleProperties.getSpreadsheetId());
        variables.put("tableName", googleProperties.getTableName());
        variables.put("apiKey", googleProperties.getApiKey());
        return variables;
    }

    private String getTomorrowDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy");
        return LocalDate.now()
                .plusDays(1)
                .format(formatter);
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    private String getTaskSendingTime(DateTimeFormatter formatter) {
        return LocalDateTime.now().format(formatter);
    }
}
