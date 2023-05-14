package Facebook;

import java.util.Scanner;

import Facebook.LoginRegistration.UserAccount;
import Facebook.LoginRegistration.UserAccountFactory;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserAccount user;
        UserAccount viewingUser;
        String email, username, password, phoneNo;
        boolean status;

        System.out.println("Welcome to Facebook");
        System.out.println("1 - Login");
        System.out.println("2 - Register");
        int choice = sc.nextInt();
        switch(choice){
            case 1: System.out.println("Enter your email or phone number:");
                    String emailOrPhoneNo = sc.next();
                    System.out.println("Enter your password:");
                    password = sc.next();
                    status = UserAccountFactory.loginAccount(emailOrPhoneNo, password);
                    if(status)
                        System.out.println("Successfully login");
                    else
                        System.out.println("Incorrect email/phoneNo or incorrect password");
                    boolean isAdmin = UserAccountFactory.isAdmin(emailOrPhoneNo);
                    user = UserAccountFactory.getProfile(emailOrPhoneNo, isAdmin);
                    break;
            case 2: System.out.println("Registration form");
                    System.out.print("Username:");
                    sc.nextLine();
                    username = sc.nextLine();
            	    System.out.print("Email:");
     
                    email = sc.nextLine();
                    System.out.print("Phone no:");

                    phoneNo = sc.nextLine();
                    System.out.println("Your password should contains at least 1 uppercase, 1 lowercase, 1 number and 1 special character.");
                    System.out.println("Password:");
          
                    password = sc.nextLine();
                    status = strongPassword(password);
                    while(!status){
                        System.out.println("Your password is not strong enough, please use a stronger password.");
                        System.out.println("Password:");
  
                        password = sc.nextLine();
                        status = strongPassword(password);
                    }
                    System.out.println("Confirm password:");
 
                    String confirmPassword = sc.nextLine();
                    status = checkPassword(password, confirmPassword);
                    while(!status){
                        System.out.println("Your password does not match, please confirm your password.");
                        System.out.println("Confirm password:");

                        confirmPassword = sc.nextLine();
                        status = checkPassword(password, confirmPassword);
                    }
                    user = UserAccountFactory.createAccount(username, email, password, phoneNo, "Normal");                    
        }
    }

    public static boolean checkPassword(String p1, String p2){
        if(p1.equals(p2))
            return true;
        else 
            return false;
    }
    
    public static boolean strongPassword(String p){
        boolean strong = false;
        if(p.length()>=8){
            strong = true;
        }
        boolean upper=false, lower=false, digit=false, non=false;
        for(int i=0; i<p.length(); i++){
            char ch = p.charAt(i);
            if((int)ch >= (int)'A' && (int)ch <= (int)'Z')
                upper = true;
            else if((int)ch >= (int)'a' && (int)ch <= (int)'z')
                lower = true;
            else if((int)ch >= (int)'0' && (int)ch <= (int)'9')
                digit = true;
            else
                if((int)ch != (int)' ')
                    non = true;
        }
        if(strong&&upper&&lower&&digit&&non)
            return true;
        else
            return false;
    }
}
