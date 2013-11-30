package com.cauliflower.readerapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
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
import com.dropbox.client2.session.Session;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends Activity implements MenuFragment.MenuFragmentInterface{

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


    }

    @Override
    public void menuLoadFile() {

    }

    @Override
    public void menuImportFile() {

    }

    @Override
    public void menuDropboxFile() {

    }
}
