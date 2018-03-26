package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.activity.Home;
import info.horriblesubs.sher.adapter.ListRecycler;
import info.horriblesubs.sher.model.Item;

@SuppressLint("StaticFieldLeak")
public class FetchListItems extends AsyncTask<String, String, List<Item>> {

    private Context context;
    private TextView textView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean b;
    private String[] strings = {"Initializing App...", "Connecting to Server...",
            "Fetching Latest Releases...", "Verifying Data...", "Parsing Received Data..."};
    private int i = 0;

    public FetchListItems(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
        b = true;
    }

    public FetchListItems(Context context, RecyclerView recyclerView,
                          SwipeRefreshLayout swipeRefreshLayout) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout = swipeRefreshLayout;
        b = false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        publishProgress(strings[i++]);
        if (!b)
            swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onProgressUpdate(String... strings) {
        super.onProgressUpdate(strings);
        if (!b)
            return;
        String s = String.valueOf(textView.getText()).concat("\n").concat(strings[0]);
        textView.setText(s);
    }

    @Override
    protected List<Item> doInBackground(String... strings) {
        publishProgress(this.strings[i++]);
        String s = BuildConfig.HAPI + strings[0];
        try {
            Log.e("LINK_FLIs", s);
            URL url = new URL(s);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                publishProgress(this.strings[i++]);
                httpURLConnection.connect();
                publishProgress(this.strings[i++]);
                BufferedReader bufferedReader = new BufferedReader(new
                        InputStreamReader(httpURLConnection.getInputStream()));
                return new Gson().fromJson(bufferedReader,
                        new TypeToken<List<Item>>() {
                        }.getType());
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(final List<Item> items) {
        super.onPostExecute(items);
        if (items != null)
            if (b) {
                publishProgress(strings[i++]);
                Intent intent = new Intent(context, info.horriblesubs.sher.activity.List.class);
                intent.putExtra("size", items.size());
                intent.putExtra("mode", "current");
                int i = 0;
                for (Item item : items) {
                    intent.putExtra("extra-" + i, item);
                    i++;
                }
                context.startActivity(intent);
                ((AppCompatActivity) context).finish();
            } else {
                final ListRecycler listRecycler = new ListRecycler(context, items);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                recyclerView.setAdapter(listRecycler);
                swipeRefreshLayout.setRefreshing(false);
                if (Home.searchView != null)
                    Home.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            List<Item> items1 = new ArrayList<>();
                            for (Item item : items) {
                                if (item.title.toLowerCase().contains(newText.toLowerCase()))
                                    items1.add(item);
                            }
                            listRecycler.onQueryUpdate(items1);
                            return false;
                        }
                    });
            }
        else
            publishProgress("Error Fetching Data from Server\nTry Again Later...");
    }
}