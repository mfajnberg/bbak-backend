package de.mfberg.bbak.quartz.jobs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelJobInfo {
     private int totalFireCount;
     private boolean runForever = false;
     private int repeatIntervalMs;
     private long initialOffsetMs;
     private String callbackData = "";
}
