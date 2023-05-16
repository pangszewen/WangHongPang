package Facebook.Login_Registration;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class AccountManagement {
    Scanner sc = new Scanner(System.in);
    UserBuilder builder = new UserBuilder();
    User user;
    Database database = new Database();

    public AccountManagement(){}

    public void registration(){
        System.out.println("\t\tRegistration Form");

        // Input username
        System.out.print("Username: ");
        String username = sc.nextLine();
        while(!database.verifyUsername(username)){
            System.out.print("Username: ");
            username = sc.nextLine();
        }
        builder.setUsername(username);

        // Input email
        System.out.print("Email: ");
        String email = sc.nextLine();
        while(!database.verifyEmail(email)){
            System.out.print("Email: ");
            email = sc.nextLine();
        }
        builder.setEmail(email);

        // Input phone number
        System.out.print("Phone number: ");
        String phoneNo = sc.nextLine();
        while(!database.verifyPhoneNo(phoneNo)){
            System.out.print("Phone number: ");
            phoneNo = sc.nextLine();
        }
        builder.setPhoneNo(phoneNo);

        // Input password
        System.out.print("Password: ");
        String password = sc.nextLine();
        while(!verifyPassword(password)){
            System.out.print("Password: ");
            password = sc.nextLine();
        }
        // Retype password
        System.out.print("Confirm password: ");
        String retypePassword = sc.nextLine();
        while(!confirmPassword(password, retypePassword)){
            System.out.print("Confirm password: ");
            retypePassword = sc.nextLine();
        }
        builder.setPassword(password);

        // Get account ID
        builder.setAccountID();
        //builder = new UserBuilder(builder.getAccountID(), username, email, phoneNo, password, null);

        // Get user
        user = builder.build();
        
        // Store user info into CSV file
        database.registerUser(user);
    }

    public void login(){
        System.out.print("Enter your email address or phone number: ");
        String emailOrPhoneNo = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        // Check if login successful
        while(!database.isLogin(emailOrPhoneNo, password)){
            System.out.print("Reenter your email address or phone number: ");
            emailOrPhoneNo = sc.nextLine();
            System.out.print("Reenter password: ");
            password = sc.nextLine();
        }
        user = database.getProfile(emailOrPhoneNo);

        // Check if user has setup account
        if(!isSetup(user)){
            User updatedUser = setupAccount(user);
            database.setupProfile(updatedUser);
        }

        System.out.println("Welcome to Facebook!");
    }

    public boolean isSetup(User user){
        if(user.getName()==null)
            return false;
        return true;
    }

    public User setupAccount(User user){
        builder.setAccountID(user.getAccountID());
        builder.setUsername(user.getUsername());
        builder.setEmail(user.getEmail());
        builder.setPhoneNo(user.getPhoneNo());
        builder.setPassword(user.getPassword());
        builder.setRole(user.getRole());

        System.out.println("Please fill in the below information to get started!");

        // Input name
        System.out.println("What is your name?");
        builder.setName(sc.nextLine());

        //Input birthday
        System.out.println("When is your birthday? (format: YYYY-MM-DD)");
        builder.setBirthday(sc.nextLine());
        //Check validation of birthday format
        while(!validateBirthdayFormat(builder.getBirthday())){
            System.out.println("When is your birthday? (format: YYYY-MM-DD)");
            builder.setBirthday(sc.nextLine());
        }
        LocalDate birthday = LocalDate.parse(builder.getBirthday());

        // Calculate age based on birthday
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthday, currentDate);
        builder.setAge(period.getYears());

        // Get address
        System.out.println("Where do you live?");
        builder.setAddress(sc.nextLine());

        // Get gender
        System.out.println("What is your gender? (M-male, F-female, N-not applicable)");
        builder.setGender(sc.next().charAt(0));
        sc.nextLine();

        // Initialize number of friends
        builder.setNoOfFriends(0);

        // Get relationship status
        System.out.println("What is your relationship status?");
        builder.setStatus(sc.nextLine());

        // Get hobbies (ArrayList)
        System.out.println("What are your hobbies?");
        builder.setHobbies(sc.nextLine());
        System.out.print("Do you wish to add more hobbies? (y-yes, n-no)");
        char choice = sc.next().charAt(0);
        sc.nextLine();
        while(choice=='y'){
            builder.setHobbies(sc.nextLine());        
            System.out.print("Do you wish to add more hobbies? (y-yes, n-no)");
            choice = sc.next().charAt(0);
            sc.nextLine();
        }

        // Get job (Stack)
        System.out.println("What is your current job?");
        builder.setJobs(sc.nextLine());     

        // Done setup account
        System.out.println("That's all for the account setup. You are now ready to explore Facebook!");
        return builder.build();
    }

    public boolean verifyPassword(String password){
        // Check password length
        if(password.length()<8){
            System.out.println("Password must be at least 8 characters.");
            return false;
        }

        // Check if password contains space characters
        if(password.contains(" ")){
            System.out.println("Password must not contain any space.");
            return false;
        }

        // Check if password is a strong password
        boolean upper=false, lower=false, digit=false, special=false;
        for(int i=0; i<password.length(); i++){
            char ch = password.charAt(i);
            if((int)ch >= (int)'A' && (int)ch <= (int)'Z')
                upper = true;
            else if((int)ch >= (int)'a' && (int)ch <= (int)'z')
                lower = true;
            else if((int)ch >= (int)'0' && (int)ch <= (int)'9')
                digit = true;
            else if((int)ch>=32&&(int)ch<=47 || (int)ch>=58&&(int)ch<=64 || (int)ch>=91&&(int)ch<=96 || (int)ch>=123&&(int)ch<=126)
                special = true;

        }
        if(upper&&lower&&digit&&special)
            return true;
        else{
            if(!upper)
                System.out.println("Password must contains at least one uppercase letter.");
            if(!lower)
                System.out.println("Password must contains at least one lowercase letter.");
            if(!digit)
                System.out.println("Password must contains at least one digit");
            if(!special)
                System.out.println("Password must contains at least one special character.");
            return false;
        }
    }
    
    public boolean confirmPassword(String p1, String p2){
        if(p1.equals(p2))
            return true;
        else{
            System.out.println("Password is not matched. Please try again.");
            return false;
        }
    }

    public boolean validateBirthdayFormat(String birthday) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        try {
            LocalDate.parse(birthday, formatter);
            return true; // Format is valid
        } catch (DateTimeParseException e) {
            System.out.println("Invalid birthday format");
            return false; // Format is invalid
        }
    }

}
