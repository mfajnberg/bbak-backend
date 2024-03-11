package de.mfberg.bbak.jobs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobInfo {
     private int totalFireCount;
     private boolean runForever = false;
     private int repeatIntervalMs;
     private long initialOffsetMs;
     private String callbackData = "";
}
