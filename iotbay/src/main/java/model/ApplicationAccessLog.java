package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import model.Enums.ApplicationAction;

public class ApplicationAccessLog implements Serializable {
    private int accessLogId;
    private int userId;
    private ApplicationAction applicationAction;
    private Date dateTime;

    public ApplicationAccessLog(int logId, int userId, Timestamp timestamp, ApplicationAction applicationAction) {
        this.accessLogId = logId;
        this.userId = userId;
        this.dateTime = new Date(timestamp.getTime());
        this.applicationAction = applicationAction;
    }

    public ApplicationAccessLog(ApplicationAction applicationAction, Date dateTime) {
        this.applicationAction = applicationAction;
        this.dateTime = dateTime;
    }

    public int getAccessLogId() {
        return accessLogId;
    }

    public int getUserId() {
        return userId;
    }

    public ApplicationAction getApplicationAction() {
        return applicationAction;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setApplicationAction(ApplicationAction applicationAction) {
        this.applicationAction = applicationAction;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ApplicationAccessLog other = (ApplicationAccessLog) obj;
        return applicationAction == other.applicationAction && dateTime.equals(other.dateTime);
    }

    @Override
    public String toString() {
        return "ApplicationAccessLog{" +
                "logId=" + accessLogId +
                ", userId=" + userId +
                ", applicationAction=" + applicationAction +
                ", dateTime=" + dateTime +
                '}';
    }
}