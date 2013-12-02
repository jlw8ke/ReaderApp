package com.cauliflower.readerapp.asynctasks;

import com.cauliflower.readerapp.objects.User;

import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 10/30/13.
 */
public interface UsersTaskInterface {
    public void onUsersAdded(ArrayList<User> userList);
    public void onUsersReceived(ArrayList<User> userList);
    public void login(User user);
    public void logout();
}
