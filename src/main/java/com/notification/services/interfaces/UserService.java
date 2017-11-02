package com.notification.services.interfaces;

import com.notification.models.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    void setAllUsers(List<List<Object>> usersDetails);
    void save(User user);
    List<User> getAll();
    void setTaskDate(String date, int id);
    String getTaskDate(int id);
    void saveNewUserInDb(List userDataList);
    void update(User userInDb, List objects);
}
