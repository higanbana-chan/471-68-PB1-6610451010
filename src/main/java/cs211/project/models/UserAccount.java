package cs211.project.models;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.commons.lang3.StringUtils;

public class UserAccount {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String accountType;
    private String lastLoggedIn;
    private String registeredDate;
    private String imgPath;

    // creating a new account object for writing data to csv
    public UserAccount(String username, String password, String email, String first_name, String last_name) {
        this(username, password, email, first_name, last_name, null, null);
    }

    // constructor chaining
    public UserAccount(String username, String password, String email, String first_name, String last_name, String accountType, String imgPath) {
        this.username = username.toLowerCase();
        setPassword(password);
        this.email = email.toLowerCase();
        setAccountName(first_name, last_name);
        this.accountType = "normal";
        setRegisteredDate();
        setImgPath(imgPath);
    }

    // for creating an object to readData off of
    public UserAccount(String username, String password, String email, String first_name, String last_name, String accountType, String registeredDate, String lastLoggedIn, String imgPath) {
        this.username = username;
        this.password = password;
        this.email = email;
        setAccountName(first_name, last_name);
        this.accountType = accountType;
        this.registeredDate = registeredDate;
        this.lastLoggedIn = lastLoggedIn;
        this.imgPath = imgPath;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }
    public String getLastLoggedIn() {
        return lastLoggedIn;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setPassword(String password) {
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public void setEmail(String new_email) {
        boolean check = new_email.matches("[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?");
        if (check) {
            this.email = new_email;
        }
    }

    public void setAccountName(String first_name, String last_name) {
        this.firstName = StringUtils.capitalize(first_name);
        this.lastName = StringUtils.capitalize(last_name);
    }

    public void setLastLoggedIn() {
        Date date = new Date();
        Instant instant = date.toInstant();
        LocalDate currentDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        this.lastLoggedIn = formattedDate;
    }

    private void setRegisteredDate() {
        Date date = new Date();
        Instant instant = date.toInstant();
        LocalDate currentDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        this.registeredDate = formattedDate;
    }

    public void setImgPath(String fileName) {
        if (fileName == null) {
            this.imgPath = "/cs211/project/views/img/default_user_profile.png";
        } else {
            String imgFolder = "data/user_img/";
            this.imgPath = imgFolder + fileName;
        }
    }

    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        return result.verified;
    }
}
