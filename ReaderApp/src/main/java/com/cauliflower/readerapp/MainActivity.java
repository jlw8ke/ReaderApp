package com.cauliflower.readerapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cauliflower.readerapp.asynctasks.SearchTaskInterface;
import com.cauliflower.readerapp.asynctasks.UsersTaskInterface;
import com.cauliflower.readerapp.dialogs.TestDialogFragment;
import com.cauliflower.readerapp.objects.AppFile;
import com.cauliflower.readerapp.objects.User;
import com.dropbox.client2.session.Session;

import java.util.ArrayList;

public class MainActivity extends Activity {

    final static private String APP_KEY = "s3voc9raoqvgdj6";
    final static private String APP_SECRET = "ez81gny641pdtjq";
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;

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
            //case R.id.a:
            //    return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements UsersTaskInterface, SearchTaskInterface{
        private TextView textView;
        private Button testButton;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            textView = (TextView) rootView.findViewById(R.id.requestId);
            testButton = (Button) rootView.findViewById(R.id.testRequest);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            testButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment test = TestDialogFragment.newInstance();
                    test.setTargetFragment(PlaceholderFragment.this, 0);
                    test.show(getFragmentManager(), "user_dialog");
                }
            });
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
            textView.setText(users);
        }

        @Override
        public void onSearchReceived(ArrayList<AppFile> files) {
            String searchResults = "";
            for(AppFile file : files) {
                searchResults += file.toString();
            }
            textView.setText(searchResults);
        }
    }

}
