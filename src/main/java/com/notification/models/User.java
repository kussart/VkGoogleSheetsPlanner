package com.notification.models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String vkDomain;

    @Column
    private String date;

    @Column
    private String task;

    @Column
    private String lastTaskDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVkDomain() {
        return vkDomain;
    }

    public void setVkDomain(String vkDomain) {
        this.vkDomain = vkDomain;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getLastTaskDate() {
        return lastTaskDate;
    }

    public void setLastTaskDate(String lastTaskDate) {
        this.lastTaskDate = lastTaskDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (vkDomain != null ? !vkDomain.equals(user.vkDomain) : user.vkDomain != null) return false;
        if (date != null ? !date.equals(user.date) : user.date != null) return false;
        return task != null ? task.equals(user.task) : user.task == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (vkDomain != null ? vkDomain.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (task != null ? task.hashCode() : 0);
        return result;
    }
}
