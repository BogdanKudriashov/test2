package com.bohdandroid.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class PageFragment extends Fragment {

    interface OnFragmentSendDataListener {
        void onSendData(String data);
    }

    private OnFragmentSendDataListener fragmentSendDataListener;

    private int pageNumber = 0;
    int notificationId = 0;

    private NotificationManager mNotificationManager;
    private Button btnCreateNotification;

    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public PageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context
                .NOTIFICATION_SERVICE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            pageNumber = arguments.getInt("num");
        } else {
            pageNumber = 1;
        }

        displayValues(view, pageNumber);

        btnCreateNotification = view.findViewById(R.id.btn_create_notification);
        btnCreateNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationId = pageNumber + 1;
                mNotificationManager.notify(notificationId, createNotification());
                fragmentSendDataListener.onSendData(String.valueOf(notificationId));
            }
        });

        return view;
    }

    private void displayValues(View view, int pageNumber) {
        TextView pageHeader = view.findViewById(R.id.displayText);
        String header = "Fragment " + (pageNumber + 1);
        pageHeader.setText(header);
    }

    private Notification createNotification() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("page_number", notificationId);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText("Notification " + String.valueOf(pageNumber + 1))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("You create notification")
                .setContentText("Notification " + String.valueOf(pageNumber + 1))
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setAutoCancel(true);

        return notificationBuilder.build();
    }
}