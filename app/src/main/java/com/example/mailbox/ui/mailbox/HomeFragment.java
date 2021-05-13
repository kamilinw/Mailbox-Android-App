package com.example.mailbox.ui.mailbox;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mailbox.R;
import com.example.mailbox.data.MailboxDatabase;
import com.example.mailbox.data.UserDatabase;
import com.example.mailbox.databinding.FragmentHomeBinding;
import com.example.mailbox.model.Mailbox;
import com.example.mailbox.service.AlarmReceiver;
import com.example.mailbox.service.CommunicationService;
import com.example.mailbox.util.NetworkUtil;
import com.example.mailbox.util.UserUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.primitives.Longs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MailboxListAdapter adapter ;
    View rootView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Inflate the layout for this fragment
        ListView listView = rootView.findViewById(R.id.mailboxesListView);

        // For application bar title change
        setHasOptionsMenu(true);



        UserDatabase userDatabase = UserDatabase.getInstance(getContext());
        MailboxDatabase mailboxDatabase = MailboxDatabase.getInstance(getContext());

        List<Long> mailboxIds = userDatabase.getMailboxIds();

        if (mailboxIds == null){
            mailboxIds = new ArrayList<>();
            mailboxIds.add(-1L);
        }

        userDatabase.close();
        mailboxDatabase.close();

        adapter = new MailboxListAdapter(getContext(), R.layout.listview_mailbox_layout, mailboxIds);

        listView.setAdapter(adapter);

        List<Long> finalMailboxIds = mailboxIds;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // new intent, id as intent param
                Toast.makeText(getContext(), finalMailboxIds.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        MailboxListAdapter finalAdapter = adapter;
        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserUtil.downloadUserData(getContext(),false, finalAdapter);
            }
        });

        /*ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        UserUtil.downloadUserData(getContext(),false, finalAdapter);
                        MailboxDatabase mailboxDatabase1 = MailboxDatabase.getInstance(getContext());

                        Intent service = new Intent(getContext(), CommunicationService.class);
                        long[] ids = Longs.toArray(finalMailboxIds);
                        service.putExtra("ids", ids);
                        getContext().startService(service);
                    }
                }, 0, 10, TimeUnit.SECONDS);



        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), 20*1000,pendingIntent);
        }*/

        Intent serviceIntent = new Intent(getContext(), CommunicationService.class);
        getContext().startService(serviceIntent);
        NetworkUtil.scheduleJob(getContext());
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_home));
        super.onCreateOptionsMenu(menu, inflater);
    }
}