package de.mfberg.bbak.services.parties;

import de.mfberg.bbak.jobs.TravelData;
import de.mfberg.bbak.services.SchedulerService;
import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TravelService {
    private static final Logger LOG = LoggerFactory.getLogger(TravelService.class);
    private final SchedulerService schedulerService;

    public void schedule(final Class jobClass, final TravelData travelData) {
        try {
            schedulerService.getScheduler().scheduleJob(
                    schedulerService.buildJobDetail(jobClass, travelData),
                    schedulerService.buildTrigger(jobClass, travelData));
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
