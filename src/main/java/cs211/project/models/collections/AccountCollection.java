package cs211.project.models.collections;

import cs211.project.models.UserAccount;

import java.util.ArrayList;

public class AccountCollection {
    private ArrayList<UserAccount> user_accounts;

    public AccountCollection() {
        user_accounts = new ArrayList<>();
    }

    public void addAccount(UserAccount account) {
        user_accounts.add(account);
    }

    public UserAccount findAccount(String username) {
        for (UserAccount account : user_accounts) {
            if (account.getUsername().equals(username)) {
                return account;
            }
        }
        return null;
    }

    public UserAccount findEmail(String email) {
        for (UserAccount account : user_accounts) {
            if(account.getEmail().equals(email)) {
                return account;
            }
        }
        return null;
    }

    public void changePassword(String username, String password) {
        UserAccount found = findAccount(username);
        if (found != null) {
            found.setPassword(password);
        }
    }

    public void changeEmail(String username, String new_email) {
        UserAccount found = findAccount(username);
        UserAccount foundEmail = findEmail(new_email);
        if (found != null && foundEmail == null) {
            found.setEmail(new_email);
        }
    }

    public void changeProfileImage(String username, String fileName) {
        UserAccount found = findAccount(username);
        if (found != null) {
            found.setImgPath(fileName);
        }
    }

    public void changeAccountName(String username, String firstName, String lastName) {
        UserAccount found = findAccount(username);
        if (found != null) {
            found.setAccountName(firstName, lastName);
        }
    }

    public UserAccount login(String username, String password) {
        UserAccount found = findAccount(username);
        if (found != null && found.validatePassword(password)) {
            found.setLastLoggedIn();
            return found;
        }
        return null;
    }

    public ArrayList<UserAccount> getUserAccounts() {
        return user_accounts;
    }
}
