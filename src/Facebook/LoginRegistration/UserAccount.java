package Facebook.LoginRegistration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public abstract class UserAccount {
    Random rand = new Random();
    public String username, email, password, phoneNo, accountID;

    public UserAccount(){}

    public UserAccount(String username, String email, String phoneNo){
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public UserAccount(String username, String email, String password, String phoneNo){
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        while(true){
            int temp = rand.nextInt(100000);
            try (BufferedReader reader = new BufferedReader(new FileReader("user_data.csv"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] userData = line.split(",");
                    int storedAccountID = Integer.parseInt(userData[0]);
                    // Check if the accountID is repeated
                    if (storedAccountID == temp){
                        continue;
                    }
                }
                this.accountID = Integer.toString(temp);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void setupAccount();
    public abstract UserAccount getUserProfile(String emailOrPhoneNo);
    public abstract void deleteAccount(String accountIDString);
    public abstract void setAdmin(String accountID);
}
