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
                if ((emailOrPhoneNo.equals(storedUsername) || emailOrPhoneNo.equals(storedEmail))
                     && password.equals(storedPassword)) {
                    return true; // User found, login successful
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAdmin(String emailOrPhoneNo){
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String storedUsername = userData[1];
                String storedEmail = userData[2];
                String storedRole = userData[4];

                // Check if the entered username/email is admin or not
                if ((emailOrPhoneNo.equals(storedUsername) || emailOrPhoneNo.equals(storedEmail))
                     && storedRole.equalsIgnoreCase("Admin")) {
                    return true; // User found, login successful
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static UserAccount getProfile(String emailOrPhoneNo, boolean status){
        NormalUser normalUser = new NormalUser();
        AdminUser adminUser = new AdminUser();
        if(status)
            return adminUser.getUserProfile(emailOrPhoneNo);
        else
            return normalUser.getUserProfile(emailOrPhoneNo);
    }

    public static void deleteAccount(String accountID, boolean status){
        NormalUser normalUser = new NormalUser();
        AdminUser adminUser = new AdminUser();
        if(status)
            adminUser.deleteAccount(accountID);
        else
            normalUser.deleteAccount(accountID);
    }
}