package com.example.mailbox.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import com.example.mailbox.util.NetworkUtil;

public class RefreshJobService extends JobService {

    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), CommunicationService.class);
        getApplicationContext().startService(service);
        NetworkUtil.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
