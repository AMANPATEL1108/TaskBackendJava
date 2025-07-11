package com.example.Task.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "leave_table")
public class LeaveSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String description;



    private Date leavedate;
    private String daytype;
    private String statusofleave;
    private Date requestdate;
    private Date updateDate;
    private String reasonfordeclineleave;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false) // foreign key column
    private User user;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Date getLeavedate() { return leavedate; }
    public void setLeavedate(Date leavedate) { this.leavedate = leavedate; }

    public String getDaytype() { return daytype; }
    public void setDaytype(String daytype) { this.daytype = daytype; }

    public String getStatusofleave() { return statusofleave; }
    public void setStatusofleave(String statusofleave) { this.statusofleave = statusofleave; }

    public Date getRequestdate() { return requestdate; }
    public void setRequestdate(Date requestdate) { this.requestdate = requestdate; }

    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }

    public String getReasonfordeclineleave() { return reasonfordeclineleave; }
    public void setReasonfordeclineleave(String reason) { this.reasonfordeclineleave = reason; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
