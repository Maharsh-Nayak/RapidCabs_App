package com.maharsh.rapidcabs;

import java.util.Date;

public class PastRide {
    Date date;
    Integer distance;

    public PastRide(Date d, Integer dist) {
        this.date = d;
        this.distance= dist;
    }
    public Date getD() {
        return date;
    }

    public void setD(Date d) {
        this.date = d;
    }

    public Integer getDist() {
        return distance;
    }

    public void setDist(Integer dist) {
        this.distance = dist;
    }
}
