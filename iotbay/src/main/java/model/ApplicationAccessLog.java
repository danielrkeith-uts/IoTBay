package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import model.Enums.ApplicationAction;

public class ApplicationAccessLog implements Serializable {
    private int accessLogId;      // now non-final so we can set it after insert
    private int userId;           // ditto
    private final Date dateTime;
    private final ApplicationAction applicationAction;

    /** Full-arg ctor for loading from DB */
    public ApplicationAccessLog(int accessLogId,
                                int userId,
                                Date dateTime,
                                ApplicationAction applicationAction) {
        this.accessLogId       = accessLogId;
        this.userId            = userId;
        this.dateTime          = dateTime;
        this.applicationAction = applicationAction;
    }

    /** Two-arg ctor for new logs prior to persistence */
    public ApplicationAccessLog(ApplicationAction applicationAction, Date dateTime) {
        this.accessLogId       = -1;
        this.userId            = -1;
        this.dateTime          = dateTime;
        this.applicationAction = applicationAction;
    }

    // setters so DB manager can fill in generated keys
    public void setAccessLogId(int id) { this.accessLogId = id; }
    public void setUserId(int userId)   { this.userId       = userId; }

    public int getAccessLogId()            { return accessLogId; }
    public int getUserId()                 { return userId; }
    public Date getDateTime()              { return dateTime; }
    public ApplicationAction getApplicationAction() { return applicationAction; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationAccessLog)) return false;
        ApplicationAccessLog that = (ApplicationAccessLog) o;
        // tests only check action + timestamp
        return applicationAction == that.applicationAction
            && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationAction, dateTime);
    }
}


