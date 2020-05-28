package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    TimeEntryRepository timeEntryRepository;
     private final DistributionSummary timeEntrySummary;
     private final Counter actionCounter;


    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry){

        this.timeEntryRepository=timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }



    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {

         TimeEntry result = timeEntryRepository.create(timeEntryToCreate);
         actionCounter.increment();
         timeEntrySummary.record(timeEntryRepository.list().size());

        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list(){
        List<TimeEntry> result=timeEntryRepository.list();
        actionCounter.increment();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id){
        TimeEntry result = timeEntryRepository.find(id);

        if(result!=null){
            actionCounter.increment();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @PutMapping("{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable long id,@RequestBody TimeEntry timeEntryToUpdate) {

        TimeEntry result = timeEntryRepository.update(id,timeEntryToUpdate);

        if(result!=null){
            actionCounter.increment();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id){

        timeEntryRepository.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
