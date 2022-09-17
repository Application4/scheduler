package com.javatechie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class JobService {

    @Autowired
    private EmailService service;

    // @Scheduled(fixedRate = 2000)
    //@Scheduled(cron = "*/5 * * * * *") //run scheduler in every 5 second
    //@Scheduled(cron = "0 0/2 * * * *") //run scheduler in every 10 min //cron_interval
    @Scheduled(cron = "${cron_interval}")
    public void runJob() {

        // service.sendEmailWithDynamicData();
        System.out.println(
                "Cron job Scheduler: Job running at - "
                        + new SimpleDateFormat(
                        "dd-MM-yyyy HH:mm:ss").format(new Date()));

    }
}
