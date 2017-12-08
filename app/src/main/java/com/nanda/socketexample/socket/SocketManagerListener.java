package com.nanda.socketexample.socket;

public interface SocketManagerListener {

    /**
     * when socket connected
     */
    public void onConnect();

    /**
     * socket failed while trying to connect with exception
     */
    public void onSocketFailed(String message);

    /**
     * socket connected, connect to room
     */
    public void onLoginWithSocket();

    /**
     * user left from room
     */
    public void onUserList(String userList);
//
//   /**
//     * user left from room
//     */
//    public void onUserLeft(User user);

//    /**
//     * received typing
//     */
//    public void onTyping(SendTyping typing);

//    /**
//     * received message
//     */
//    public void onMessageReceived(Message message);
//
//    /**
//     * received messages update
//     */
//    public void onMessagesUpdated(List<Message> messages);

    /**
     * receive new user connect to room
     */
    public void onNewUser(Object... args);

    /**
     * receive socket error
     */
    public void onSocketError(int code);

}
