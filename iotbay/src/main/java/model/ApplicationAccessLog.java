package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import model.Enums.ApplicationAction;

public class ApplicationAccessLog implements Serializable {
    private int accessLogId;      
    private int userId;           
    private final Date dateTime;
    private final ApplicationAction applicationAction;

    
    public ApplicationAccessLog(int accessLogId,
                                int userId,
                                Date dateTime,
                                ApplicationAction applicationAction) {
        this.accessLogId       = accessLogId;
        this.userId            = userId;
        this.dateTime          = dateTime;
        this.applicationAction = applicationAction;
    }

    
    public ApplicationAccessLog(ApplicationAction applicationAction, Date dateTime) {
        this.accessLogId       = -1;
        this.userId            = -1;
        this.dateTime          = dateTime;
        this.applicationAction = applicationAction;
    }

    
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
        
        return applicationAction == that.applicationAction
            && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationAction, dateTime);
    }
}


