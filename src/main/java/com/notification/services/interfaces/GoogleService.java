package com.notification.services.interfaces;

import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;

public interface GoogleService {
    void getUsersMessagesForVkPlanner() throws HttpClientErrorException, IOException;

}
