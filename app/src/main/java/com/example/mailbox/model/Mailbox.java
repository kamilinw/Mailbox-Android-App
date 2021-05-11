package com.example.mailbox.model;

import java.time.LocalDateTime;
import java.util.List;

public class Mailbox {
    private Long mailboxId;
    private boolean newMail;
    private String name;
    private List<String> mailHistory;
    private boolean attemptedDeliveryNoticePresent;
    private Double battery;
    private Double temperature;
    private Double pressure;
    private Double humidity;

    public Mailbox(Long mailboxId,
                   boolean newMail,
                   String name,
                   List<String> mailHistory,
                   boolean attemptedDeliveryNoticePresent,
                   Double battery,
                   Double temperature,
                   Double pressure,
                   Double humidity) {
        this.mailboxId = mailboxId;
        this.newMail = newMail;
        this.name = name;
        this.mailHistory = mailHistory;
        this.attemptedDeliveryNoticePresent = attemptedDeliveryNoticePresent;
        this.battery = battery;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public Long getMailboxId() {
        return mailboxId;
    }

    public void setMailboxId(Long mailboxId) {
        this.mailboxId = mailboxId;
    }

    public boolean isNewMail() {
        return newMail;
    }

    public void setNewMail(boolean newMail) {
        this.newMail = newMail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMailHistory() {
        return mailHistory;
    }

    public void setMailHistory(List<String> mailHistory) {
        this.mailHistory = mailHistory;
    }

    public boolean isAttemptedDeliveryNoticePresent() {
        return attemptedDeliveryNoticePresent;
    }

    public void setAttemptedDeliveryNoticePresent(boolean attemptedDeliveryNoticePresent) {
        this.attemptedDeliveryNoticePresent = attemptedDeliveryNoticePresent;
    }

    public Double getBattery() {
        return battery;
    }

    public void setBattery(Double battery) {
        this.battery = battery;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }
}
