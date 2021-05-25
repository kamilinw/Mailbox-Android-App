package com.example.mailbox.ui.mailbox;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mailbox.R;
import com.example.mailbox.data.MailboxDatabase;
import com.example.mailbox.data.UserDatabase;
import com.example.mailbox.databinding.FragmentHomeBinding;
import com.example.mailbox.util.UserUtil;
import com.example.mailbox.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MailboxListAdapter adapter ;
    Timer timer;
    private static final String TAG = "HomeFragment";

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
        ImageButton refreshButton = rootView.findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserUtil.downloadUserData(getContext(),false, finalAdapter, false);
            }
        });

        ImageButton addMailboxButton = rootView.findViewById(R.id.addMailboxButton);
        addMailboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddMailboxActivity.class);
                startActivity(intent);
            }
        });

        //update UI every 5 seconds
        updateListView(5);

        if (getContext()!= null)
            Util.setAlarm(getContext());

        return rootView;
    }

    private void updateListView(int period) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (adapter != null) {
                            Log.i(TAG, "Update UI");
                            //adapter.notify();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }, 0, period * 1000);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_home));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        if (timer != null)
            timer.cancel();
        super.onPause();
    }
}