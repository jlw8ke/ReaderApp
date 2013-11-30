package com.cauliflower.readerapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cauliflower.readerapp.asynctasks.SearchTaskInterface;
import com.cauliflower.readerapp.asynctasks.UsersTaskInterface;
import com.cauliflower.readerapp.dialogs.TestDialogFragment;
import com.cauliflower.readerapp.objects.AppFile;
import com.cauliflower.readerapp.objects.User;
import com.dropbox.client2.*;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.Session;
import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity implements MenuFragment.MenuFragmentInterface{

    final private int MENU_NEW_FILE = 0;
    final private int MENU_LOAD_FILE = 1;
    final private int MENU_IMPORT_FILE = 2;
    final private int MENU_DROPBOX_FILE = 3;
    final private int MENU_LOGIN = 4;
    final private int MENU_LOGOUT = 5;

    //Dropbox Objects
    final static private String APP_KEY = "s3voc9raoqvgdj6";
    final static private String APP_SECRET = "ez81gny641pdtjq";
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI<AndroidAuthSession> mDBApi;

    private static SharedPreferences m_SharedPreferences;
    private static final String PROPERTY_CURRENT_USER = "property_current_user";
    private User m_CurrentUser;

    private DrawerLayout m_DrawerLayout;
    private ActionBarDrawerToggle m_DrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container_menu, new MenuFragment())
                    .add(R.id.left_drawer, new DrawerFragment())
                    .commit();
        }
        loadSavedPreferences();
        setupHomeDrawer();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void loadSavedPreferences() {
        m_CurrentUser = new Gson().fromJson(m_SharedPreferences.getString(PROPERTY_CURRENT_USER, null), User.class);
    }

    private void setupHomeDrawer(){
        m_DrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        m_DrawerToggle =  new ActionBarDrawerToggle(this, m_DrawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        m_DrawerLayout.setDrawerListener(m_DrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        m_DrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        m_DrawerToggle.onConfigurationChanged(newConfig);
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
        switch (item.getItemId()) {
            case R.id.action_settings:
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

    /*
    -----------------------------------------------------------------------------------
    Menu Fragment Interface
    -----------------------------------------------------------------------------------
    */
    @Override
    public void menuNewFile() {
        showChooser(MENU_NEW_FILE);
    }

    @Override
    public void menuLoadFile() {
        showChooser(MENU_LOAD_FILE);
    }

    @Override
    public void menuImportFile() {
        showChooser(MENU_IMPORT_FILE);
    }

    @Override
    public void menuDropboxFile() {
        showChooser(MENU_DROPBOX_FILE);
    }

    private static final int NEW_FILE_REQUEST_CODE = 6384;
    private void showChooser(int choice){
        Intent target = FileUtils.createGetContentIntent();
        switch(choice) {
            case MENU_NEW_FILE:
                Intent intent = Intent.createChooser(target, getString(R.string.home_new_file));
                try {
                    startActivityForResult(intent, NEW_FILE_REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case MENU_LOAD_FILE:
                break;
            case MENU_IMPORT_FILE:
                break;
            case MENU_DROPBOX_FILE:
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case NEW_FILE_REQUEST_CODE:
                if(resultCode == RESULT_OK) {
                    if(data != null) {
                        final Uri uri = data.getData();
                        try {
                            // Create a file instance from the URI
                            final File file = FileUtils.getFile(uri);
                            Toast.makeText(this, "File Selected: "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("MainActivity", "File select error", e);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
