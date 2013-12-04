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
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cauliflower.readerapp.asynctasks.UsersTaskInterface;
import com.cauliflower.readerapp.constants.BundleConstants;
import com.cauliflower.readerapp.constants.ServerConstants;
import com.cauliflower.readerapp.dialogs.LoginDialogFragment;
import com.cauliflower.readerapp.dialogs.RegisterDialogFragment;
import com.cauliflower.readerapp.dropbox.DropboxUtils;
import com.cauliflower.readerapp.objects.AppFile;
import com.cauliflower.readerapp.objects.User;
import com.dropbox.client2.*;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.dropboxchooser.DbxChooser;
import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity implements MenuFragment.MenuFragmentInterface,
        LocalFileFragment.FileInterface, UsersTaskInterface, DropboxFileFragment.DropboxFileInterface {

    private static final int NEW_FILE_REQUEST_CODE = 6384;
    private static final int DBX_CHOOSER_REQUEST = 3000;

    final private int MENU_NEW_FILE = 0;
    final private int MENU_LOAD_FILE = 1;
    final private int MENU_IMPORT_FILE = 2;
    final private int MENU_DROPBOX_FILE = 3;

    //Dropbox Objects
    final static private String APP_KEY = "s3voc9raoqvgdj6";
    final static private String APP_SECRET = "ez81gny641pdtjq";
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private AccessTokenPair m_Tokens;
    private DropboxAPI<AndroidAuthSession> mDBApi;

    private static SharedPreferences m_SharedPreferences;
    private static final String PROPERTY_CURRENT_USER = "property_current_user";
    private static final String PROPERTY_CURRENT_FILE = "property_current_file";
    private User m_CurrentUser;
    private AppFile m_CurrentFile;

    private DrawerLayout m_DrawerLayout;
    private ActionBarDrawerToggle m_DrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container_menu, new MenuFragment(), MenuFragment.TAG)
                            // .add(R.id.container_main, new DrawingFragment())
                            // .add(R.id.left_drawer, new DrawerFragment())
                    .commit();
        }
        loadSavedPreferences();
        setupHomeDrawer();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (mDBApi == null) {
            AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
            AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
            mDBApi = new DropboxAPI<AndroidAuthSession>(session);

            DropboxUtils.DropboxAuthenticate(mDBApi, this);
        }
    }


    private void setupHomeDrawer() {
        m_DrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        m_DrawerToggle = new ActionBarDrawerToggle(this, m_DrawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_closed) {
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        m_DrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem login = menu.findItem(R.id.action_login);
        MenuItem logout = menu.findItem(R.id.action_logout);

        if (m_CurrentUser == null) {
            if (login != null)
                login.setVisible(true);
            if (logout != null)
                logout.setVisible(false);
        } else {
            if (login != null)
                login.setVisible(false);
            if (logout != null)
                logout.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.action_settings:
            //    return true;
            case R.id.action_login:
                DialogFragment loginDialog = LoginDialogFragment.newInstance(ServerConstants.SERVER_LOGIN_DEBUG);
                loginDialog.show(getFragmentManager(), LoginDialogFragment.TAG);
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void loadSavedPreferences() {
        m_CurrentUser = new Gson().fromJson(m_SharedPreferences.getString(PROPERTY_CURRENT_USER, null), User.class);
        String path = m_SharedPreferences.getString(PROPERTY_CURRENT_FILE, null);
        if (path != null)
            m_CurrentFile = new AppFile(path, FileParser.parse(path));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDBApi != null) {
            if (mDBApi.getSession().authenticationSuccessful()) {
                try {
                    mDBApi.getSession().finishAuthentication();
                    m_Tokens = mDBApi.getSession().getAccessTokenPair();
                } catch (IllegalStateException e) {
                    Log.i("DbAuthLog", "Error authenticating", e);
                }
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = m_SharedPreferences.edit();
        editor.putString(PROPERTY_CURRENT_USER, new Gson().toJson(m_CurrentUser, User.class));
        if (m_CurrentFile != null)
            editor.putString(PROPERTY_CURRENT_FILE, m_CurrentFile.getAbsolutePath());
        editor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    private void showChooser(int choice) {
        Intent target = FileUtils.createGetContentIntent();
        switch (choice) {
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

                DbxChooser mChooser = new DbxChooser(APP_KEY);
                mChooser.forResultType(DbxChooser.ResultType.DIRECT_LINK)
                        .launch(this, DBX_CHOOSER_REQUEST);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NEW_FILE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final Uri uri = data.getData();
                        try {
                            // Create a file instance from the URI
                            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                            String path = FileUtils.getPath(this, uri);
                            if (path.startsWith("file://"))
                                path = path.substring(6);
                            final File file = new File(path);
                            //Toast.makeText(this, "File Selected: "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                            m_CurrentFile = new AppFile(file.getAbsolutePath(), FileParser.parse(file.getAbsolutePath()));
                            loadPDFIntoWindow();
                        } catch (Exception e) {
                            Log.e("MainActivity", "File select error", e);
                        }
                    }
                }
                break;
            case DBX_CHOOSER_REQUEST:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        DbxChooser.Result result = new DbxChooser.Result(data);
                        Log.d("MainActivity", "Link to selected file: " + result.getLink());

                        Bundle args = new Bundle();
                        args.putString(BundleConstants.CURRENT_FILE_PATH, result.getLink().toString());
                        args.putString(BundleConstants.DROPBOX, result.getName().toString());
                        Fragment fileFragment = new DropboxFileFragment();
                        fileFragment.setArguments(args);

                        getFragmentManager().beginTransaction()
                                .replace(R.id.container_main, fileFragment, DropboxFileFragment.TAG)
                                .commit();
                    }
                }
            default:
                break;
        }
    }

    private void loadPDFIntoWindow() {
        Bundle args = new Bundle();
        args.putString(BundleConstants.CURRENT_FILE_PATH, m_CurrentFile.getAbsolutePath());
        args.putString(BundleConstants.CURRENT_USER, new Gson().toJson(m_CurrentUser));

        Fragment fileFragment = new LocalFileFragment();
        fileFragment.setArguments(args);

        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, fileFragment, LocalFileFragment.TAG)
                .commit();
    }

    /*
    -----------------------------------------------------------------------------------
    UsersTaskInterface
    -----------------------------------------------------------------------------------
    */
    @Override
    public void onUsersAdded(ArrayList<User> userList) {
    }

    @Override
    public void onUsersReceived(ArrayList<User> userList) {
    }

    @Override
    public void login(User user) {
        DialogFragment loginDialog = (DialogFragment) getFragmentManager().findFragmentByTag(LoginDialogFragment.TAG);

        if (user != null) {
            m_CurrentUser = user;
            if (loginDialog != null)
                loginDialog.dismiss();
            Toast.makeText(this, getString(R.string.login_success) + " " + m_CurrentUser.getUsername(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.login_failure), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void logout() {
        m_CurrentUser = null;
    }

    @Override
    public User getCurrentUser() {
        return m_CurrentUser;
    }

    @Override
    public void register(User user, String message) {
        DialogFragment registerDialog = (DialogFragment) getFragmentManager().findFragmentByTag(RegisterDialogFragment.TAG);

        if (user != null) {
            m_CurrentUser = user;
            if (registerDialog != null)
                registerDialog.dismiss();
            Toast.makeText(this, getString(R.string.register_success) + "\n" +
                    getString(R.string.login_success) + " " + m_CurrentUser.getUsername(), Toast.LENGTH_LONG).show();
        } else {
            if (message.contains("Duplicate entry"))
                Toast.makeText(this, getString(R.string.username_failure), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, getString(R.string.register_failure), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public DropboxAPI<AndroidAuthSession> getApiSession() {
        return mDBApi;
    }
}
