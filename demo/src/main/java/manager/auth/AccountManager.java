package manager.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import utils.CipherUtil;
import utils.Constants;
import utils.FileUtil;

//handle user accounts, login/signup logic
//simple cipher Base64(XOR("username|password"))

public class AccountManager {

    private final String filePath;
    private final Map<String, UserAccount> accounts = new LinkedHashMap<>();
    private boolean loaded = false; //only load accounts from disk once, then keep in memory

    public AccountManager() {
        this(Constants.USER_ACCOUNTS_FILE);
    }

    public AccountManager(String filePath) {
        this.filePath = filePath;
    }

    public void loadAccounts() {
        accounts.clear();
        for (String line : FileUtil.readLines(filePath)) {
            if (line == null || line.isBlank()) {
                continue;
            }
            String plain;
            try {
                plain = CipherUtil.decrypt(line.trim());
            } catch (IllegalArgumentException ex) {
                continue;
            }
            UserAccount acc = UserAccount.deserialize(plain);
            if (acc != null) {
                accounts.put(acc.getUsername(), acc);
            }
        }
        loaded = true;
    }

    public void saveAccounts() {
        List<String> lines = new ArrayList<>(accounts.size());
        for (UserAccount acc : accounts.values()) {
            lines.add(CipherUtil.encrypt(acc.serialize()));
        }
        try {
            FileUtil.writeLines(filePath, lines);
        } catch (IOException e) {
            System.err.println("AccountManager: failed to save accounts: " + e.getMessage());
        }
    }

    public boolean userExists(String username) {
        ensureLoaded();
        return username != null && accounts.containsKey(username);
    }

    public boolean createAccount(String username, String password) {
        ensureLoaded();
        if (username == null || username.isBlank() || password == null) {
            return false;
        }
        if (username.contains("|") || password.contains("|")) {
            return false;
        }
        if (accounts.containsKey(username)) {
            return false;
        }
        accounts.put(username, new UserAccount(username, password));
        saveAccounts();
        return true;
    }

    public boolean login(String username, String password) {
        ensureLoaded();
        UserAccount acc = accounts.get(username);
        return acc != null && acc.checkPassword(password);
    }

    public List<String> listUsernames() {
        ensureLoaded();
        return Collections.unmodifiableList(new ArrayList<>(accounts.keySet()));
    }

    private void ensureLoaded() {
        if (!loaded) {
            loadAccounts();
        }
    }
}
