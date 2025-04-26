package model;

import java.io.Serializable;
import java.util.Date;

import model.enums.ApplicationAction;

public class ApplicationAccessLog implements Serializable {
    private ApplicationAction applicationAction;
    private Date dateTime;

    public ApplicationAccessLog(ApplicationAction applicationAction, Date dateTime) {
        this.applicationAction = applicationAction;
        this.dateTime = dateTime;
    }

    public ApplicationAction getApplicationAction() {
        return applicationAction;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
