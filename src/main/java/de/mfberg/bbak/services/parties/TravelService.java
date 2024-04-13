package de.mfberg.bbak.services.parties;

import de.mfberg.bbak.exceptions.JobSchedulingException;
import de.mfberg.bbak.jobs.TravelJobInfo;
import de.mfberg.bbak.services.common.QuartzService;
import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TravelService {
    private static final Logger LOG = LoggerFactory.getLogger(TravelService.class);
    private final QuartzService quartzService;

    public void schedule(final Class jobClass, final TravelJobInfo travelJobInfo) {
        try {
            quartzService.getScheduler().scheduleJob(
                    quartzService.buildJobDetail(jobClass, travelJobInfo),
                    quartzService.buildTrigger(jobClass, travelJobInfo));
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            throw new JobSchedulingException(jobClass.getSimpleName());
        }
    }

    public String makeLabel(TravelJobInfo info) {
        return info.getPartyId() + "=>" +
                ((Long) info.getPath().getFirst().getQ()) + ":" +
                ((Long) info.getPath().getFirst().getR());
    }
}
