package com.notification.services.impl;

import com.notification.configs.property.UserProperties;
import com.notification.models.User;
import com.notification.repository.UserRepository;
import com.notification.services.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserProperties userProperties;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserProperties userProperties) {
        this.userRepository = userRepository;
        this.userProperties = userProperties;
    }

    @Override
    public void setAllUsers(List<List<Object>> usersDetails) {
        for (int i = 1; i < usersDetails.size() ; i++) {
            User userInDb = userRepository.findOne((long) i);
            List userDataList = usersDetails.get(i);
            if(userInDb != null && userInDb.getVkDomain().equals(usersDetails.get(i).get(userProperties.getDomainColumnNum()))){
                update(userInDb, userDataList);
            } else {
                try {
                    saveNewUserInDb(userDataList);
                } catch (IndexOutOfBoundsException e){
                    logger.error("Заполните все столбцы пользователя!" + e.getMessage());
                }
            }
        }
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void setTaskDate(String date, int id) {
        User user = userRepository.findOne((long) id);
        user.setLastTaskDate(date);
        save(user);
    }

    @Override
    public String getTaskDate(int id) {
        User user = userRepository.findOne((long) id);
        return user.getLastTaskDate();
    }

    @Override
    public void saveNewUserInDb(List userDetailsToCreate) throws IndexOutOfBoundsException {
        User user = new User();
        user.setVkDomain((String) userDetailsToCreate.get(userProperties.getDomainColumnNum()));
        user.setDate((String) userDetailsToCreate.get(userProperties.getDateColumnNum()));
        user.setTask((String) userDetailsToCreate.get(userProperties.getTaskColumnNum()));
        user.setName((String) userDetailsToCreate.get(userProperties.getNameColumnNum()));
        save(user);
    }

    @Override
    public void update(User userInDb, List userDetailsToUpdate) {
        if(!userInDb.getDate().equals(userDetailsToUpdate.get(userProperties.getDateColumnNum()))) {
            userInDb.setLastTaskDate(null);
        }
        userInDb.setDate((String) userDetailsToUpdate.get(userProperties.getDateColumnNum()));
        userInDb.setTask((String) userDetailsToUpdate.get(userProperties.getTaskColumnNum()));
        userInDb.setName((String) userDetailsToUpdate.get(userProperties.getNameColumnNum()));
        save(userInDb);
    }
}
