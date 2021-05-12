package com.example.mailbox.ui.mailbox;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mailbox.R;
import com.example.mailbox.data.MailboxDatabase;
import com.example.mailbox.data.UserDatabase;
import com.example.mailbox.databinding.FragmentHomeBinding;
import com.example.mailbox.model.Mailbox;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArrayAdapter<String> adapter ;
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
        ArrayList<String> mailboxNames = new ArrayList<>();
        if (mailboxIds != null){
            for (Long mailboxId: mailboxIds) {
                mailboxNames.add(mailboxDatabase.getMailboxField(MailboxDatabase.COLUMN_NAME_NAME, mailboxId));
            }
        } else {
            mailboxNames.add("Brak skrzynek");
        }

        userDatabase.close();
        mailboxDatabase.close();

        adapter = new ArrayAdapter<String>(getContext(), R.layout.listview_mailbox_layout, mailboxNames);

        listView.setAdapter(adapter);


        return rootView;
    }
}