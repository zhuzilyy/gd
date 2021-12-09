package com.gd.form.model;

import java.util.List;

public class TaskCountModel {
    private int approval2;
    private int approval1;
    private int task1;
    private int task2;
    private int overCount;
    private int overFinishCount;
    private int waitCount;
    private List<OverTimeModel> overTaskList;
    private List<OverTimeModel> waitTaskList;
    private List<OverTimeModel> overFinishTaskList;
    public int getOverCount() {
        return overCount;
    }

    public void setOverCount(int overCount) {
        this.overCount = overCount;
    }

    public int getOverFinishCount() {
        return overFinishCount;
    }

    public void setOverFinishCount(int overFinishCount) {
        this.overFinishCount = overFinishCount;
    }

    public int getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(int waitCount) {
        this.waitCount = waitCount;
    }

    public List<OverTimeModel> getOverTaskList() {
        return overTaskList;
    }

    public void setOverTaskList(List<OverTimeModel> overTaskList) {
        this.overTaskList = overTaskList;
    }

    public List<OverTimeModel> getWaitTaskList() {
        return waitTaskList;
    }

    public void setWaitTaskList(List<OverTimeModel> waitTaskList) {
        this.waitTaskList = waitTaskList;
    }

    public List<OverTimeModel> getOverFinishTaskList() {
        return overFinishTaskList;
    }

    public void setOverFinishTaskList(List<OverTimeModel> overFinishTaskList) {
        this.overFinishTaskList = overFinishTaskList;
    }

    public int getApproval2() {
        return approval2;
    }

    public void setApproval2(int approval2) {
        this.approval2 = approval2;
    }

    public int getApproval1() {
        return approval1;
    }

    public void setApproval1(int approval1) {
        this.approval1 = approval1;
    }

    public int getTask1() {
        return task1;
    }

    public void setTask1(int task1) {
        this.task1 = task1;
    }

    public int getTask2() {
        return task2;
    }

    public void setTask2(int task2) {
        this.task2 = task2;
    }

    @Override
    public String toString() {
        return "TaskCountModel{" +
                "approval2=" + approval2 +
                ", approval1=" + approval1 +
                ", task1=" + task1 +
                ", task2=" + task2 +
                ", overCount=" + overCount +
                ", overFinishCount=" + overFinishCount +
                ", waitCount=" + waitCount +
                ", overTaskList=" + overTaskList +
                ", waitTaskList=" + waitTaskList +
                ", overFinishTaskList=" + overFinishTaskList +
                '}';
    }
}
