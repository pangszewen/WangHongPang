package Facebook.LoginRegistration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserAccountFactory{
    public static UserAccount createAccount(String username, String email, String password, String phoneNo,String accountType){
        if(accountType.equalsIgnoreCase("Normal"))
            return new NormalUser(username, email, password, phoneNo);
        else if(accountType.equalsIgnoreCase("Admin"))
            return new AdminUser(username, email, password, phoneNo);
        return null;
    }

    public static boolean loginAccount(String emailOrPhoneNo, String password){
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String storedUsername = userData[1];
                String storedEmail = userData[2];
                String storedPassword = userData[3];

                // Check if the entered username/email and password match the stored data
                if ((emailOrPhoneNo.equalsIgnoreCase(storedUsername) || emailOrPhoneNo.equalsIgnoreCase(storedEmail))
                     && password.equals(storedPassword)) {
                    return true; // User found, login successful
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static UserAccount 

    public static boolean deleteAccount(UserAccount user){
        if()
    }
}