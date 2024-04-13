package adapter.Messages;
public class MessagesList {
    private String phone,lastMessage,name,profilePic,chatKey;
    private int unseenMessages;

    public MessagesList(String name,String phone, String lastMessage,  String profilePic, int unseenMessages,String chatKey) {
        this.chatKey = chatKey;
        this.phone = phone;
        this.lastMessage = lastMessage;
        this.name = name;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }
    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public String getPhone() {
        return phone;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getName() {
        return name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setUnseenMessages(int unseenMessages) {
        this.unseenMessages = unseenMessages;
    }
}
