package Facebook;

import java.util.Scanner;

import Facebook.Login_Registration.AccountManagement;

public class test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountManagement manager = new AccountManagement();

        System.out.println("\tFacebook");
        System.out.println("------------------------------");
        System.out.println("1 - Register");
        System.out.println("2 - Login");
        int choice = sc.nextInt();
        switch(choice){
            case 1 -> manager.registration();
            case 2 -> manager.login();
        }
    }
}
2
