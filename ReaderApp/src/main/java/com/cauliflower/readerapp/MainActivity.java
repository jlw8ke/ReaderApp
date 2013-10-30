package com.cauliflower.readerapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cauliflower.readerapp.asynctasks.GetUsersTask;
import com.cauliflower.readerapp.asynctasks.HttpUtils;
import com.cauliflower.readerapp.asynctasks.UsersTaskInterface;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements UsersTaskInterface{
        private TextView textView;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            textView = (TextView) rootView.findViewById(R.id.requestId);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            new GetUsersTask(this).execute("http://plato.cs.virginia.edu/~cs4720f13cauliflower/TextToSpeech/developers/users");
        }

        @Override
        public void onUsersAdded(ArrayList<User> userList) {

        }

        @Override
        public void onUsersReceived(ArrayList<User> userList) {
            String users= "";
            for(User user: userList) {
                users += user.toString();
            }
            textView.append(users);
        }
    }

}
