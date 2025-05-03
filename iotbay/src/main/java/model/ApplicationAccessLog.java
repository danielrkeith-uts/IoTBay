package model;

import java.io.Serializable;
import java.util.Date;

import model.Enums.ApplicationAction;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ApplicationAccessLog applicationAccessLog = (ApplicationAccessLog) obj;

        return this.applicationAction == applicationAccessLog.applicationAction && this.dateTime.equals(applicationAccessLog.dateTime);
    }
}
