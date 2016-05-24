package com.josie.earthquake.model;

/**
 * Created by Josie on 16/5/24.
 */
public class FilterRule {
    private int id;
    private String rule;
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "FilterRule{" +
                "id=" + id +
                ", rule='" + rule + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
