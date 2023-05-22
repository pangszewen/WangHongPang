package Facebook.Friends;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import Facebook.Database;
import Facebook.Login_Registration.User;
import Facebook.Login_Registration.UserBuilder;

public class UsersConnection {
    Scanner sc = new Scanner(System.in);
    User user;
    Database database = new Database();
    UserBuilder builder = new UserBuilder();
    ConnectionGraph<String> graph = new ConnectionGraph<>();

    public UsersConnection(){}

    // Add new user as vertex to graph
    public ConnectionGraph<String> registerGraph(ConnectionGraph<String> graph, String username){
        return graph.registerVertex(graph, username);
    }

    /* WASTED
    // Return updated graph with new edge
    public ConnectionGraph<String> newFriend(User u1, User u2, ConnectionGraph<String> graph){
        graph = graph.addUndirectedEdge(graph, u1.getAccountID(), u1.getAccountID());
        u1.setNoOfFriends(u1.getNoOfFriends()+1);
        u2.setNoOfFriends(u2.getNoOfFriends()+1);
        database.setupProfile(u1);
        database.setupProfile(u2);
        return graph;
    }
    */

    /* Moved to AccountManagement class
    // Display search friend and view account of search friend
    public void searchFriend(){
        String emailOrPhoneNoOrUsernameOrName = sc.nextLine();
        List<String> nameResult = database.ifContains(emailOrPhoneNoOrUsernameOrName);
        List<User> result = new ArrayList<>();
        // Sort name alphabetically
        Collections.sort(nameResult);
        for(String x : nameResult){
            result.add(database.getProfile(x));
        }

        // Display all search result
        int count = 1;
        for(User x : result){
            System.out.println(count + " - " + x.getName());
            count++;
        }

        // Select to view searched account
        int choice = sc.nextInt();
        while(choice>result.size()-1){
            System.out.println("Choice out of bound. Please select again.");
            choice = sc.nextInt();
        }
        if(choice>0)
            manager.viewAccount(result.get(choice-1));
    }
    */

    // Combined this method with searchFriend method
    /*public void selectAccount(List<User> displayedUser){
        int count = 1;
        for(User x : displayedUser){
            System.out.println(count + " - " + x.getName());
            count++;
        }
        int choice = sc.nextInt();
        while(choice>displayedUser.size()-1){
            System.out.println("Choice out of bound. Please select again.");
            choice = sc.nextInt();
        }
        if(choice>0)
            manager.viewAccount(displayedUser.get(choice-1));
    }
    */

    public ArrayList<User> getMutual(User u1, User u2, ConnectionGraph<String> graph){
        ArrayList<User> mutual = new ArrayList<>();
        String v1 = graph.getVertex(graph, u1.getUsername());
        ArrayList<String> n1 = graph.getNeighbours(graph, v1);
        String v2 = graph.getVertex(graph, u2.getUsername());
        ArrayList<String> n2 = graph.getNeighbours(graph, v2);
        for(int i=0; i<n1.size(); i++){
            for(int j=0; j<n2.size(); j++){
                if(n1.get(i).equals(n2.get(j))){
                    mutual.add(database.getProfile(n2.get(j)));
                    continue;
                }
            }
        }
        return mutual;
    }

    public User displayMutual(ArrayList<User> mutual){
        // Display appropriate message if have 0 mutual friend
        if(mutual.size()==0){
            System.out.println("No mutual friend");
            return null;
        }

        for(int i=1; i<=mutual.size(); i++){
            System.out.println(i + " - " + mutual.get(i-1));
        }
        int choice = sc.nextInt();
        return mutual.get(choice-1);
    }

    public int getTotalMutual(User u1, User u2, ConnectionGraph<String> graph){
        ArrayList<User> mutual = getMutual(u1, u2, graph);
        return mutual.size();
    }

    // Return ArrayList of recommended users
    // Not used in test class
    public ArrayList<User> recommendedUser(User u1, ConnectionGraph<String> graph){
        ArrayList<User> recomUser = new ArrayList<>();
        ArrayList<Integer> mutualNo = new ArrayList<>();
        ArrayList<String> v = graph.getAllVertices(graph);
        for(int i=0; i<v.size(); i++){
            // User himself and his friends are not included in recommendation list
            if(!v.get(i).equals(u1.getUsername()) && !graph.hasEdge(graph, u1.getUsername(), v.get(i))){
                User u2 = database.getProfile(v.get(i));
                // Users yet to setup account and users that have already reqeusted to be friend are not included in recommendation list
                if(u2.getName()!=null && !checkRequest(u1, u2)){
                    int totalMutual = getTotalMutual(u1, u2, graph);
                    recomUser.add(u2);
                    mutualNo.add(totalMutual);
                }
            }
        }
        for(int i=1; i<mutualNo.size(); i++){
            for(int j=0; j<i; j++){
                if(mutualNo.get(i).compareTo(mutualNo.get(j))>0){
                    int temp1 = mutualNo.get(i);
                    mutualNo.set(i, mutualNo.get(j));
                    mutualNo.set(j, temp1);
                    User temp2 = recomUser.get(i);
                    recomUser.set(i, recomUser.get(j));
                    recomUser.set(j, temp2);
                }
            }
        }
        return recomUser;
    }
    
    // Use in test class to display recommended users
    public User displayRecommendedUsers(User u1, ConnectionGraph<String> graph){
        ArrayList<User> recomUser = recommendedUser(u1, graph);
        for(int i=1; i<=recomUser.size(); i++){
            System.out.print(i + " - " + recomUser.get(i-1).getName());
            System.out.println(" (" + getTotalMutual(u1, recomUser.get(i-1), graph) + ")");
        }
        int choice = sc.nextInt();
        if(choice>0)
            return recomUser.get(choice-1);
        return null;
    }

    // Add friend by sending reqeust
    public void sendRequest(User sender, User receiver){
        ArrayList<User> requestList = database.getRequestList(receiver);
        requestList.add(sender);
        database.updateRequestList(receiver, requestList);
    }

    public boolean checkRequest(User sender, User receiver){
        ArrayList<User> requestList = database.getRequestList(receiver);
        for(User x : requestList){
            if(x.getUsername().compareTo(sender.getUsername())==0)
                return true;
        }
        return false;
    }

    public void cancelRequest(User sender, User receiver){
        ArrayList<User> requestList = database.getRequestList(receiver);
        ArrayList<String> username = new ArrayList<>();
        for(User x : requestList){
    
            username.add(x.getUsername());
        }
        username.remove(sender.getUsername());
        requestList.clear();
        for(String x : username){
            requestList.add(database.getProfile(x));
        }
        database.updateRequestList(receiver, requestList);
    }

    // Moved to AccountManagement class
    /*
    public void displayRequest(User user, ConnectionGraph<String> graph){
        ArrayList<User> requestList = database.getRequestList(user);
        for(int i=0; i<requestList.size(); i++){
            System.out.println(requestList.get(i).getName());
            System.out.println(getTotalMutual(user, requestList.get(i), graph) + " mutuals");
            int choice = sc.nextInt();
            switch(choice){
                case 0: continue;
                case 1: confirmRequest(user, requestList.get(i), graph);
                        break;
                case 2: cancelRequest(user, requestList.get(i));
                        break;

            }
        }
    }
    */

    public void confirmRequest(User sender, User receiver, ConnectionGraph<String> graph){
        graph.addUndirectedEdge(graph, sender.getUsername(), receiver.getUsername());

        // Increment number of friends
        sender.setNoOfFriends(sender.getNoOfFriends()+1);
        receiver.setNoOfFriends(receiver.getNoOfFriends()+1);
        // Update no of friends
        database.setupProfile(sender);
        database.setupProfile(receiver);
    }

    // Display newest friends at top (ArrayList-loop from head of ArrayList)
    public ArrayList<String> displayNewestFriends(User user, ConnectionGraph<String> graph){
        ArrayList<String> friends = graph.getNeighbours(graph, user.getUsername());
        Collections.reverse(friends);
        System.out.println("<" + friends.size() + " friends>");
        for(int i=1; i<=friends.size(); i++){
            User u = database.getProfile(friends.get(i-1));
            System.out.println(i + " - " + u.getName());
            System.out.println("(" + getTotalMutual(user, u, graph) + " mutuals)");
        }
        return friends;
    }

    // Display oldest friends at top (ArrayList-loop from tail of ArrayList)
    public ArrayList<String> displayOldestFriends(User user, ConnectionGraph<String> graph){
        ArrayList<String> friends = graph.getNeighbours(graph, user.getUsername());
        System.out.println("<" + friends.size() + " friends>");
        for(int i=1; i<=friends.size(); i++){
            User u = database.getProfile(friends.get(i-1));
            System.out.println(i + " - " + u.getName());
            System.out.println("(" + getTotalMutual(user, u, graph) + " mutuals)");
        }
        return friends;
    }
}


