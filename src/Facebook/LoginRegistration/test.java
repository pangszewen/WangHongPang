package Facebook.LoginRegistration;

public class test{
    public static void main(String[] args) {
        UserAccount user1 = UserAccountFactory.createAccount("pangpangpiu", "eunicepang2003@gmail.com", "Pang16110", "0164468779", "normal");
        //UserAccount user2 = UserAccountFactory.createAccount("Pang Sze Wen", "eunicepang2003@gmail.com", "Pang16110", "0164468779", "admin");
        //UserAccount user3 = UserAccountFactory.createAccount("pangpang", "eunicepang2003@gmail.com", "123456", "016123456", "normal");
        user1.deleteAccount(user1);
    }
}