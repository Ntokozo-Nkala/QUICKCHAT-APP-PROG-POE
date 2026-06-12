/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.nkala.ntokozo.prog.part2;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Message {
    String id;
    int count;
    String recipient;
    String sender;
    String content;
    String hash;
   
private static List<Message> sentMessagesArray = new ArrayList<>(); 
private static int totalMessagesSent = 0; 
private static Scanner scanner = new Scanner(System.in); 

private static List<String> disregardedMessagesArray = new ArrayList<>(); 
private static List<String> storedMessagesArray = new ArrayList<>();
private static List<String> recipientArray = new ArrayList<>();
private static List<String> senderPhoneArray = new ArrayList<>(); 
private static List<String> messageHashArray = new ArrayList<>(); 
private static List<String> messageIdArray = new ArrayList<>();
public static String loggedInSenderPhone = "";
 
    public Message(String id, int count, String recipient, String content, String hash){
        this.id = id;
        this.count = count;
        this.recipient = recipient;
        this.content = content;
        this.hash = hash;
        
    }  
    public Message(){
        this.id = "0000000000";
        this.count = 0;
    }
    
       
    //shows the menu
    public static void show(){
        System.out.println("\n=========QuickChat App========");
        boolean running = true;
        
        while(running){
            System.out.println("\n1. Send Message");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");
            System.out.println("Choose an option: ");
            String choice = scanner.nextLine().trim();
           
            switch(choice){
                case "1":
                    sendMessages();
                    break;
                case "2": 
                    System.out.println("Coming soon");
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                    case "4":
                    storedMessagesMenu();
                    break;
                default:
                    System.out.println("Error: Invalid option. Please enter 1, 2, 3 or 4");
            }
        }
    }
    
    // asks user how many messages they want to send
    // loops and builds each one
    private static void sendMessages(){
        System.out.println("\nHow many messages do you want to send?");
        String input = scanner.nextLine().trim();
        
        int count;
        try{
            count = Integer.parseInt(input);
            if(count <= 0){
                System.out.println("Error: Please enter a number greater than zero");
            }
        }
        catch(NumberFormatException e){
            System.out.println("Error: Invalid input. Please enter a number");
            return;
        }

        for(int i = 1; i <= count; i++){ 
            System.out.println("\n---Message " + i + " of " + count + "---"); 
            Message msg = messageValidation(i);
            msg.sentMessage();
        }  
        System.out.println("\nTotal messages sent: " + returnTotalMessages()); 
    } 
   
    // Ensures that characters are 10 or less. 
    public boolean checkMessageID(){
       return this.id != null && this.id.length() <= 10;
    }
      
    //method ensures that number is no more than 10 characters
    // ensures that the number contains an international code
    public String checkRecipientCell(){ 
        if (this.recipient == null || this.recipient.isEmpty()) { 
            return "Cell phone number cannot be empty"; 
        }
        if (!this.recipient.contains("+") && this.recipient.length() <= 10) {
            return "Cell phone number incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        return "Cell phone number successfully captured.";
    } 
    
    //ensures that the users message is not more than 250 characters
    public String checkMessageLength(String text){
        if (text == null || text.isEmpty()) {
            return "Message cannot be empty.";
        }
        if (text.length() > 250) {
            int excess = text.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
        return "Message ready to send.";
    }
    
    //method creates the message hash and returns it
    //contains message ID, colon and the number of messages
    public String createMessageHash(){ 
        String[] words = this.content.trim().split("\\s+"); 
        String firstWord = words[0]; 
        String lastWord = words[words.length - 1]; 
        String generatedHash =(this.id.substring(0, 2) + ":" + this.count + ":" + firstWord + lastWord).toUpperCase(); 
        this.hash = generatedHash; 
        return generatedHash; 
    } 
    
    //method gives a send, delete or store option to the user
    public String sendMessage(String recipientPhone, String messageContent, String action) {
    this.recipient = recipientPhone;
    this.content = messageContent;
    createMessageHash();

    if (action.equalsIgnoreCase("Send") || action.equalsIgnoreCase("Send Message")) 
    {
        return "Message successfully sent.";
    } 
    else if (action.equalsIgnoreCase("Discard") || action.equalsIgnoreCase("Disregard Message")) {
        return "Message disregarded";
    } 
    else if (action.equalsIgnoreCase("Store") || action.equalsIgnoreCase("Store Message")) {
        storedMessagesArray.add(this.content);
        return "Message is successfully stored";
    }
    return "Invalid action";
}

// Displays the menu 
public String sentMessage() { 
    System.out.println("\nChoose an action for this message:"); 
    System.out.println("1. Send Message"); 
    System.out.println("2. Disregard Message"); 
    System.out.println("3. Store Message to send later"); 
    System.out.print("Your choice: "); 
    String choice = scanner.nextLine().trim(); 
   
    switch(choice) { 
        case "1": 
            sentMessagesArray.add(this);
            senderPhoneArray.add(loggedInSenderPhone);
            recipientArray.add(this.recipient);
            messageIdArray.add(this.id);
            messageHashArray.add(this.hash);
            storedMessagesArray.add(this.content);
            totalMessagesSent++; 
            System.out.println("\nMessage Details:"); 
            System.out.println("Message ID: " + this.id); 
            System.out.println("Message Hash: " + this.hash); 
            System.out.println("Recipient: " + this.recipient); 
            System.out.println("Message: " + this.content);
            System.out.println("Message successfully sent.");
            return sendMessage(this.recipient, this.content, "Send");
            
        case "2": 
            System.out.println("Press 0 to delete the message"); 
            disregardedMessagesArray.add(this.content);
            String confirm = scanner.nextLine().trim(); 
            if (confirm.equals("0")) { 
                System.out.println("Message deleted."); 
                return sendMessage(this.recipient, this.content, "Discard");
            } else { 
                return "Message disregard cancelled"; 
            } 

        case "3": 
            sentMessagesArray.add(this);
            storedMessagesArray.add(this.content);
            recipientArray.add(this.recipient);
            senderPhoneArray.add(loggedInSenderPhone);
            messageIdArray.add(this.id);
            messageHashArray.add(this.hash);
            System.out.println("Message successfully stored"); 
            return sendMessage(this.recipient, this.content, "Store"); 
        default: 
            return "Invalid choice message disregarded"; 
    } 
}
    //method returns all of the messages sent
    public static String printMessages(){ 
        if(sentMessagesArray.isEmpty()){ 
            return "No messages have been sent yet."; 
        } 
        StringBuilder sb = new StringBuilder(); 
        for (int i = 0; i < sentMessagesArray.size(); i++) {
            Message m = sentMessagesArray.get(i);
            sb.append("Message ID: " + m.id + "\n");
            sb.append("Message Hash: " + m.hash + "\n");
            sb.append("Recipient: " + m.recipient + "\n");
            sb.append("Message: " + m.content + "\n");
            sb.append("------------------------\n");
        } 
        return sb.toString(); 
    } 
   
    //method returns the total messages sent
    public static int returnTotalMessages(){ 
        return totalMessagesSent; 
    } 
 
    //method is the json that stores all of the messages 
    public void storeMessage(){ 
        StringBuilder sb = new StringBuilder("[\n"); 
        for (int i = 0; i <sentMessagesArray.size(); i++){ 
            Message m = sentMessagesArray.get(i); 
            sb.append("{\n"); 
            sb.append("\"message_id\": \"" + m.id + "\",\n"); 
            sb.append("\"recipient\": \"" + m.recipient + "\",\n"); 
            sb.append("\"count\":\"" + m.count + "\",\n"); 
            sb.append("\"content\": \"" + m.content.replace("\"", "\\\"") + "\",\n"); 
            sb.append("\"hash\": \"" + m.hash + "\"\n"); 
            sb.append("  }"); 
            if (i < sentMessagesArray.size() - 1){
                sb.append(",");
            } 
            sb.append("\n"); 
        } 
        sb.append("]"); 
 
        try(FileWriter file = new FileWriter("stored_messages.json")){ 
            file.write(sb.toString()); 
            file.close(); 
        } catch(IOException e){ 
            System.out.println("Error: Could not save JSON: " + e.getMessage()); 
        } 
    }
    
    //handles the sending of the messages and displays the appropriate message
    public static Message messageValidation(int messageNumber){ 
        String recipient; 
        while(true){ 
            System.out.print("Enter recipient phone number: "); 
            recipient = scanner.nextLine().trim(); 
            Message temp = new Message("0000000000", messageNumber, recipient, "test",""); 
            String cellCheck = temp.checkRecipientCell(); 
            if (cellCheck.equals("Cell phone number successfully captured.")) { 
                break; 
            }
            System.out.println("Error: " + cellCheck);
        }
        
        String messageText; 
        while(true){ 
            System.out.println("Enter your message(max 250 characters): "); 
            messageText = scanner.nextLine(); 
            
            if(messageText.isEmpty()){ 
                System.out.println("Error: Message cannot be empty. Please try again."); 
                continue; 
            } 
            if (messageText.length() > 250){ 
                System.out.println("Error: Please enter a message of less than 250 characters"); 
                continue; 
            }
            else{
                break; 
            }
        } 
        
    String msgID = String.format("%010d", new Random().nextInt(1_000_000_000)); 
    Message msg = new Message(msgID, messageNumber, recipient, messageText, ""); 
    msg.createMessageHash(); 

    if (!msg.checkMessageID()){ 
        msgID = String.format("%010d", new Random().nextInt(1_000_000_000)); 
        msg.id = msgID; 
    }
    return msg;
    }

    public String getMessageID() {
        return "Message ID generated: " + this.id;
    }

    public String getMessageHash() {
        return "Message ID generated: " + this.hash;
    }
    
    private static void storedMessagesMenu(){ 
        while (true) { 
            System.out.println("\n===== STORED MESSAGES ====="); 
            System.out.println("a. Display Sender and Recipient"); 
            System.out.println("b. Display Longest Message"); 
            System.out.println("c. Search by Message ID"); 
            System.out.println("d. Search by Recipient"); 
            System.out.println("e. Delete Message by Hash"); 
            System.out.println("f. Message Report");
            System.out.println("g. Return to main menu");
            System.out.print("Choose option: "); 
            String choice = scanner.nextLine().trim().toLowerCase(); 
 
            switch(choice){ 
                case "a": 
                  System.out.println();
                 displaySenderAndRecipient();
            break;

            case "b":
                System.out.println();
                showLongestMessage();
            break;

            case "c":
            System.out.print("Enter Message ID: ");
            String id = scanner.nextLine();
            System.out.println();
            searchByMessageID(id);
            break;

            case "d":
            System.out.print("Enter Recipient: ");
            String recipient = scanner.nextLine();
            System.out.println();
            searchByRecipient(recipient);
            break;

            case "e":
            System.out.print("Enter Message Hash: ");
            String hash = scanner.nextLine();
            System.out.println();
            deleteMessageByHash(hash);
            break;

            case "f":
            System.out.println();
            generateReport();
            break;
            
            case "g":
                return;
        default:
            System.out.println("Invalid option.");
    }
   }
}   
    public static void searchByMessageID(String id){
    for(int i = 0; i < messageIdArray.size(); i++){
        if(messageIdArray.get(i).equals(id)){
            System.out.println("Sender: " + senderPhoneArray.get(i));
            System.out.println("Recipient: " + recipientArray.get(i));
            System.out.println("Message: " + storedMessagesArray.get(i));
            System.out.println("Message Hash: " + messageHashArray.get(i));
            return;
        }
    }
    System.out.println("Message not found.");
}
 
    public static void deleteMessageByHash(String hash) {
    for (int i = 0; i < sentMessagesArray.size(); i++) { 
            Message m = sentMessagesArray.get(i); 
            if (messageHashArray.get(i).equalsIgnoreCase(hash)){ 
                messageHashArray.remove(i); 
                messageIdArray.remove(i);
                recipientArray.remove(i);
                senderPhoneArray.remove(i);
                storedMessagesArray.remove(i);
                System.out.println("Message deleted successfully."); 
                return; 
            } 
        } 
        System.out.println("Message hash not found."); 
    }
    
    public static void showLongestMessage() {
    if (sentMessagesArray.isEmpty()) { 
            System.out.println("No messages stored yet."); 
            return; 
        } 
        int longestIndex = 0;
        for (int i = 1; i < sentMessagesArray.size(); i++) { 
            if (storedMessagesArray.get(i).length() > storedMessagesArray.get(longestIndex).length()){ 
                longestIndex = i; 
            } 
        } 
        Message longest = sentMessagesArray.get(longestIndex);
        System.out.println("Sender: " + senderPhoneArray.get(longestIndex));
         System.out.println("Recipient: " + longest.recipient);
        System.out.println("Message ID: " + longest.id);
        System.out.println("Message Hash: " + longest.hash);
        System.out.println("Longest Message: " + longest.content);
    }
    
 
    public static void displaySenderAndRecipient(){
     if(senderPhoneArray.isEmpty()){
        System.out.println("No messages found.");
        return;
        }
     for(int i = 0; i < senderPhoneArray.size(); i++){
         System.out.println("Sender: " + senderPhoneArray.get(i));
         System.out.println("Recipient: " + recipientArray.get(i));
     }
}
    
    public static void searchByRecipient(String recipient) {
        boolean found = false;
        for(int i = 0; i < recipientArray.size(); i++) {
            if(recipientArray.get(i).equals(recipient)) {
                System.out.println("Message: " + messageIdArray.get(i));
                System.out.println("Message ID: " + storedMessagesArray.get(i));
                System.out.println("Message Hash: " + messageHashArray.get(i));
                found = true;
            }
        }
        if(!found) {
            System.out.println("No messages found for this recipient.");
        }
    }
   
    public static void generateReport() {
    if(messageIdArray.isEmpty()){
    System.out.println("No messages found");
    return;
    }
        for(int i = 0; i < messageIdArray.size(); i++){
        System.out.println("Sender: " + senderPhoneArray.get(i));
        System.out.println("Recipient: " + recipientArray.get(i));
        System.out.println("Message ID: " + messageIdArray.get(i));
        System.out.println("Hash: " + messageHashArray.get(i));
        System.out.println("Message: " + sentMessagesArray.get(i));
    }
}
}