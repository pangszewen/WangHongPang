package Facebook.LoginRegistration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class NormalUser extends UserAccount{
    Scanner sc = new Scanner(System.in);
    private String name, address, role, status;
    private char gender;
    private int age, noOfFriends;
    private ArrayList<String> hobbies;
    private Stack<String> jobs;
    private LocalDate birthday;

    public NormalUser(){}

    public NormalUser(String accountID, String phoneNo, String role, String name, String username, LocalDate birthday, int age, String address, char gender, String status, int noOfFriends, ArrayList<String> hobbies, Stack<String> jobs){
        super(username, address, phoneNo);
        this.accountID = accountID;
        this.role = role;
        this.name = name;
        this.username = username;
        this.birthday = birthday;
        this.age = age;
        this.address = address;
        this.gender = gender;
        this.status = status;
        this.noOfFriends = noOfFriends;
        this.hobbies = hobbies;
        this.jobs = jobs;
    }

    public NormalUser(String username, String email, String password, String phoneNo){
        super(username, email, password, phoneNo);
        setupAccount();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_data.csv", true))) {
            // Write the user data in CSV format
            writer.write(String.valueOf(accountID) + "," +
                         email + "," +
                         phoneNo + "," +
                         password + "," +
                         role + "," +
                         name + "," +
                         username + "," +
                         birthday + "," +
                         age + "," +
                         address + "," +
                         gender + "," +
                         status + "," +
                         noOfFriends + "," +
                         hobbies + "," +
                         jobs);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupAccount(){
        role = "user";
        hobbies = new ArrayList<>();
        jobs = new Stack<>();
        System.out.println("Please fill in the below information to get started!");
        System.out.println("What is your name?");
        name = sc.nextLine();
        System.out.println("When is your birthday? (format: YYYY-MM-DD)");
        birthday = LocalDate.parse(sc.next());
        sc.nextLine();
        // Calculate age based on birthday
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthday, currentDate);
        age = period.getYears();
        System.out.println("Where do you live?");
        address = sc.nextLine();
        System.out.println("What is your gender? (M-male, F-female, N-not applicable)");
        gender = sc.next().charAt(0);
        sc.nextLine();
        noOfFriends = 0;
        System.out.println("What is your relationship status?");
        status = sc.nextLine();
        System.out.println("What are your hobbies?");
        this.hobbies.add(sc.nextLine());
        System.out.print("Do you wish to add more hobbies? (y-yes, n-no)");
        char choice = sc.next().charAt(0);
        sc.nextLine();
        while(choice=='y'){
            hobbies.add(sc.nextLine());        // ArrayList
            System.out.print("Do you wish to add more hobbies? (y-yes, n-no)");
            choice = sc.next().charAt(0);
            sc.nextLine();
        }
        System.out.println("What is your current job?");
        jobs.push(sc.next());           // stack
        System.out.println("That's all for the account setup. You are now ready to explore Facebook!");
    }

    public UserAccount getUserProfile(String emailOrPhoneNo){
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String storedUsername = userData[1];
                String storedEmail = userData[2];

                // Check if the entered username/email and password match the stored data
                if (emailOrPhoneNo.equals(storedUsername) || emailOrPhoneNo.equals(storedEmail)){
                    return new NormalUser(storedEmail, emailOrPhoneNo, line, storedUsername, storedUsername, birthday, age, storedEmail, gender, status, noOfFriends, hobbies, jobs); // User found, login successful
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAccount(String accountID){
        System.out.println("Unauthorized Action");
    }

    public void setAdmin(String accountID){
        System.out.println("Unauthorized Action");
    }

}
