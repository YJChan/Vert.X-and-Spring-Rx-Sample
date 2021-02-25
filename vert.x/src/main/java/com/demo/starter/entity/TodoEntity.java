package com.demo.starter.entity;

public class TodoEntity {
    public int id;
    public String name;
    public String desc;
    public boolean done;

    public TodoEntity() {}

    public TodoEntity(int id, String name, String desc, boolean done) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
