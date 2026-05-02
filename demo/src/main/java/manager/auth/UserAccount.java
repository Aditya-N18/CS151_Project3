package manager.auth;

import java.util.Objects;

public class UserAccount {

    private final String username;
    private final String password;

    public UserAccount(String username, String password) {
        this.username = Objects.requireNonNull(username, "username");
        this.password = Objects.requireNonNull(password, "password");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String candidate) {
        return password.equals(candidate);
    }

    public String serialize() {
        return username + "|" + password;
    }

    public static UserAccount deserialize(String line) {
        if (line == null) {
            return null;
        }
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        int sep = trimmed.indexOf('|');
        if (sep < 0) {
            return null;
        }
        String u = trimmed.substring(0, sep);
        String p = trimmed.substring(sep + 1);
        if (u.isEmpty()) {
            return null;
        }
        return new UserAccount(u, p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount other)) return false;
        return username.equals(other.username) && password.equals(other.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
