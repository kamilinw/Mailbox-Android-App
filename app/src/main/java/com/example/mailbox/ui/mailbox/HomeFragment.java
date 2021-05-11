package com.example.mailbox.ui.mailbox;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mailbox.R;
import com.example.mailbox.data.UserDatabase;
import com.example.mailbox.databinding.FragmentHomeBinding;

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

        //String mailboxes[] = {"Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen", "Audi", "Citroen"};

        ArrayList<Long> mailboxes = new ArrayList<>();
        List<Long> mailboxIds = userDatabase.getMailboxIds();
        if (mailboxIds != null){
            mailboxes.addAll(mailboxIds);
        } else {
            mailboxes.add(-1L);
        }
        userDatabase.close();

        ArrayList<String> list = new ArrayList<>();

        for (Long mailboxId: mailboxes) {
            list.add(mailboxId.toString());
        }

        adapter = new ArrayAdapter<String>(getContext(), R.layout.listview_mailbox_layout, list);

        listView.setAdapter(adapter);


        return rootView;
    }
}