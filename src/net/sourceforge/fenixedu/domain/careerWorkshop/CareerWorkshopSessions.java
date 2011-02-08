package net.sourceforge.fenixedu.domain.careerWorkshop;

import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;

public enum CareerWorkshopSessions {
    
    FEB_26_11(new DateTime(2011,2,26,0,0,0,0)),
    
    MAR_05_11(new DateTime(2011,3,5,0,0,0,0)),
    
    MAR_12_11(new DateTime(2011,3,12,0,0,0,0)),
    
    MAR_19_11(new DateTime(2011,3,19,0,0,0,0)),
    
    MAR_26_11(new DateTime(2011,3,26,0,0,0,0)),
    
    APR_02_11(new DateTime(2011,4,2,0,0,0,0)),
    
    APR_09_11(new DateTime(2011,4,9,0,0,0,0)),
    
    APR_16_11(new DateTime(2011,4,16,0,0,0,0)),
    
    APR_23_11(new DateTime(2011,4,23,0,0,0,0)),
    
    APR_30_11(new DateTime(2011,4,30,0,0,0,0));
    
    private DateTime date;
    
    private CareerWorkshopSessions(DateTime date) {
	this.date = date;
    }
    
    public DateTime getDate() {
	return date;
    }
    
    public String getDescription() {
	return getDate().toString("dd-MM-yyyy");
    }
    
    static public Map<Integer, CareerWorkshopSessions> getEmptyRankings() {
	Map<Integer, CareerWorkshopSessions> rankings = new HashMap<Integer, CareerWorkshopSessions>();
	for(int i=0; i<getTotalOptions(); i++) {
	    rankings.put(i,null);
	}
	return rankings;
    }
    
    static public int getTotalOptions() {
	return CareerWorkshopSessions.values().length;
    }

}
