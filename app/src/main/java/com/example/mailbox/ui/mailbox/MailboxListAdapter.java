package com.example.mailbox.ui.mailbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mailbox.R;
import com.example.mailbox.model.Mailbox;

import java.util.List;

public class MailboxListAdapter extends ArrayAdapter<Mailbox> {

    private int resourceLayout;
    private Context mContext;

    public MailboxListAdapter(Context context, int resource, List<Mailbox> mailboxes) {
        super(context, resource, mailboxes);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Mailbox mailbox = getItem(position);

        if (mailbox != null) {
            TextView mailboxNameTextView = (TextView) v.findViewById(R.id.mailboxNameTextView);
            TextView batteryTextView = (TextView) v.findViewById(R.id.batteryTextView);
            TextView temperatureTextView = (TextView) v.findViewById(R.id.temperatureTextView);
            TextView pressureTextView = (TextView) v.findViewById(R.id.pressureTextView);
            TextView humidityTextView = (TextView) v.findViewById(R.id.humidityTextView);

            if (mailboxNameTextView != null) {
                mailboxNameTextView.setText(mailbox.getName());
            }
            if (batteryTextView != null) {
                batteryTextView.setText(mailbox.getBattery().toString());
            }

            if (temperatureTextView != null) {
                temperatureTextView.setText(mailbox.getTemperature().toString());
            }
            if (pressureTextView != null) {
                pressureTextView.setText(mailbox.getPressure().toString());
            }
            if (humidityTextView != null) {
                humidityTextView.setText(mailbox.getHumidity().toString());
            }
        }

        return v;
    }
}
