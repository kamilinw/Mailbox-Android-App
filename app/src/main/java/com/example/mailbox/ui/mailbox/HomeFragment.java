package com.example.mailbox.ui.mailbox;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
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
import com.example.mailbox.util.UserUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

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

        return rootView;
    }
}