package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{
    HashMap<Long,TimeEntry>  hm=new HashMap<Long,TimeEntry>();
    public long curr_id=0;

    public TimeEntry create(TimeEntry timeEntry){
        timeEntry.setId(++curr_id);
        hm.put(curr_id,timeEntry);
        return hm.get(curr_id);
    }

    public TimeEntry find(long id) {
        if(hm.containsKey(id)){
            return hm.get(id);
        }
        else{
            return null;
        }
    }

    public List<TimeEntry> list() {
        List<TimeEntry> list = new ArrayList<TimeEntry>(hm.values());
        return list;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        if(hm.containsKey(id)){
            timeEntry.setId(id);
            hm.replace(id,timeEntry);
            return hm.get(id);
        }
        else{
            return null;
        }
    }

    public void delete(long id) {
        hm.remove(id);
    }
}
