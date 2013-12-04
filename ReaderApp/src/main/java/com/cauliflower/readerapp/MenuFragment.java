package com.cauliflower.readerapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by jlw8k_000 on 11/30/13.
 */
public class MenuFragment extends Fragment {

    public interface MenuFragmentInterface {
        public void menuNewFile();
        public void menuLoadFile();
        public void menuImportFile();
        public void menuDropboxFile();
    }

    ImageButton m_NewFileButton;
    ImageButton m_LoadFileButton;
    ImageButton m_ImportFileButton;
    ImageButton m_DropboxFileButton;
    MenuFragmentInterface m_Interface;

    public static final String TAG = "menu_fragment";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            m_Interface = (MenuFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement MenuFragmentInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        m_NewFileButton = (ImageButton) rootView.findViewById(R.id.menu_new_file);
        m_LoadFileButton = (ImageButton) rootView.findViewById(R.id.menu_load_file);
        m_ImportFileButton = (ImageButton) rootView.findViewById(R.id.menu_import_file);
        m_DropboxFileButton = (ImageButton) rootView.findViewById(R.id.menu_dropbox_file);
        m_LoadFileButton.setVisibility(View.GONE);
        m_ImportFileButton.setVisibility(View.GONE);

        setLongClickListeners();

        m_NewFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Interface.menuNewFile();
            }
        });

        m_LoadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Interface.menuLoadFile();
            }
        });

        m_ImportFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Interface.menuImportFile();
            }
        });

        m_DropboxFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Interface.menuDropboxFile();
            }
        });

        return rootView;
    }

    private void setLongClickListeners() {
        m_NewFileButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                repositionAndShowToast(getString(R.string.home_new_file), view, getActivity());
                return true;
            }
        });

        m_LoadFileButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                repositionAndShowToast(getString(R.string.home_load_file), view, getActivity());
                return true;
            }
        });

        m_ImportFileButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                repositionAndShowToast(getString(R.string.home_import_file), view, getActivity());
                return true;
            }
        });

        m_DropboxFileButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                repositionAndShowToast(getString(R.string.home_dropbox_file), view, getActivity());
                return true;
            }
        });
    }
    private void repositionAndShowToast(String text, View v, Context context) {
        int[] pos = new int[2];
        v.getLocationInWindow(pos);

        Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, pos[0], pos[1]-v.getHeight());
        toast.show();
    }
}
