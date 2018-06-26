package info.horriblesubs.sher.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.ChangeTransform;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ReleaseRecycler;
import info.horriblesubs.sher.adapter.ScheduleRecycler;
import info.horriblesubs.sher.model.base.ScheduleItem;
import info.horriblesubs.sher.model.response.HomeResponse;

public class HomeFragment2 extends Fragment {

    private static final String ARG_RESPONSE = "RESPONSE-HOME";
    private static final String ARG_NUMBER = "RESPONSE-NUMBER";

    private int type;
    private HomeResponse homeResponse;
    private RecyclerView recyclerView;
    private TextView textView;

    public static HomeFragment2 newInstance(HomeResponse homeResponse, int i) {
        HomeFragment2 fragment = new HomeFragment2();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE, homeResponse);
        args.putInt(ARG_NUMBER, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_2, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        textView = rootView.findViewById(R.id.textView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
            setEnterTransition(new ChangeTransform());
            setExitTransition(new ChangeTransform());
            setReturnTransition(new ChangeTransform());
            setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
        }
        assert getArguments() != null;
        homeResponse = (HomeResponse) getArguments().getSerializable(ARG_RESPONSE);
        type = getArguments().getInt(ARG_NUMBER, 0);
    }

    private List<ScheduleItem> getTodaySchedule() {
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        for (ScheduleItem item : homeResponse.schedule) {
            cal1.setTime(item.getTime());
            if (cal1.get(Calendar.DAY_OF_WEEK) == cal2.get(Calendar.DAY_OF_WEEK) && item.isScheduled)
                scheduleItems.add(item);
        }
        return scheduleItems;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        switch (type) {
            case 0:
                textView.setText(R.string.new_releases);
                recyclerView.setAdapter(new ReleaseRecycler(getContext(), homeResponse.allSubs));
                break;
            case 1:
                textView.setText(R.string.batches);
                recyclerView.setAdapter(new ReleaseRecycler(getContext(), homeResponse.allBatches));
                break;

            case 2:
                textView.setText(R.string.today_s_schedule);
                recyclerView.setAdapter(new ScheduleRecycler(getContext(), getTodaySchedule()));
                break;
        }
    }
}