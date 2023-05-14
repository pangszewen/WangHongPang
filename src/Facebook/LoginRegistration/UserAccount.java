package Facebook.LoginRegistration;

import java.io.*;
import java.util.*;

public abstract class UserAccount {
    Random rand = new Random();
    public String username, email, password, phoneNo, accountID;

    public UserAccount() {}

    public UserAccount(String username, String email, String phoneNo) {
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public UserAccount(String username, String email, String password, String phoneNo) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        generateAccountID();
    }

    private void generateAccountID() {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.csv"))) {
            Set<String> accountIDs = new HashSet<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String storedAccountID = userData[0];
                accountIDs.add(storedAccountID);
            }

            int temp;
            do {
                temp = rand.nextInt(100000);
            } while (accountIDs.contains(String.valueOf(temp)));

            this.accountID = String.valueOf(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void setupAccount();
    public abstract UserAccount getUserProfile(String emailOrPhoneNo);
    public abstract void deleteAccount(String accountIDString);
    public abstract void setAdmin(String accountID);
}
