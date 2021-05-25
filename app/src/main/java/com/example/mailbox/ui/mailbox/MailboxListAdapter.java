package com.example.mailbox.ui.mailbox;

import android.content.Context;
import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mailbox.R;
import com.example.mailbox.data.MailboxDatabase;
import com.example.mailbox.model.Mailbox;
import com.example.mailbox.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MailboxListAdapter extends ArrayAdapter<Long> {

    private int resourceLayout;
    private Context mContext;

    public MailboxListAdapter(Context context, int resource, List<Long> mailboxIds) {
        super(context, resource, mailboxIds);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Long id = getItem(position);
        Mailbox mailbox = null;
        if (id > 0){
            MailboxDatabase mailboxDatabase = MailboxDatabase.getInstance(mContext);
            mailbox = mailboxDatabase.getMailboxById(id);
        }



        if (mailbox != null) {
            TextView mailboxNameTextView = (TextView) v.findViewById(R.id.mailboxNameTextView);
            TextView batteryTextView = (TextView) v.findViewById(R.id.batteryTextView);
            TextView temperatureTextView = (TextView) v.findViewById(R.id.temperatureTextView);
            TextView pressureTextView = (TextView) v.findViewById(R.id.pressureTextView);
            TextView humidityTextView = (TextView) v.findViewById(R.id.humidityTextView);
            TextView newMailTextView = (TextView) v.findViewById(R.id.newMailTextView);
            TextView noticeTextView = (TextView) v.findViewById(R.id.noticeTextView);
            TextView newMailDateTextView = (TextView) v.findViewById(R.id.newMailDateTextView);

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
            if (newMailTextView != null) {
                if (mailbox.isNewMail()){
                    newMailTextView.setVisibility(View.VISIBLE);
                    newMailDateTextView.setVisibility(View.VISIBLE);

                    String rawDateString = mailbox.getMailHistory().get(mailbox.getMailHistory().size()-1);
                    String date = Util.formatStringDate(rawDateString);
                    newMailDateTextView.setText(getContext().getString(R.string.recieved, date));
                }
                else{
                    newMailTextView.setVisibility(View.INVISIBLE);
                    newMailDateTextView.setVisibility(View.INVISIBLE);
                }

            }
            if (noticeTextView != null) {
                if (mailbox.isAttemptedDeliveryNoticePresent())
                    noticeTextView.setVisibility(View.VISIBLE);
                else
                    noticeTextView.setVisibility(View.INVISIBLE);
            }
        }

        return v;
    }
}
