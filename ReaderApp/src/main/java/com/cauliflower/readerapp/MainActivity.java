package com.cauliflower.readerapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.dropbox.client2.*;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends Activity {

    final private int MENU_LOGIN = 3;
    final private int MENU_LOGOUT = 4;


    //Dropbox Objects
    final static private String APP_KEY = "s3voc9raoqvgdj6";
    final static private String APP_SECRET = "ez81gny641pdtjq";
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI<AndroidAuthSession> mDBApi;

    private static SharedPreferences m_SharedPreferences;
    private static final String PROPERTY_CURRENT_USER = "property_current_user";
    private User m_CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        loadSavedPreferences();
    }

    private void loadSavedPreferences() {
        m_CurrentUser = new Gson().fromJson(m_SharedPreferences.getString(PROPERTY_CURRENT_USER, null), User.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem currentMenuItem;
        if(m_CurrentUser == null)
            currentMenuItem = menu.add(Menu.NONE, MENU_LOGIN, Menu.NONE, "Login");
        else
            currentMenuItem = menu.add(Menu.NONE, MENU_LOGOUT, Menu.NONE, "Logout");
        currentMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
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
            case R.id.action_new_file:
                return true;
            case R.id.action_load_file:
                return true;
            case MENU_LOGIN:
                return true;
            case MENU_LOGOUT:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
