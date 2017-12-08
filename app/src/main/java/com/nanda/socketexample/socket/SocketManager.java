package com.nanda.socketexample.socket;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.nanda.socketexample.app.AppConstants;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketManager {

    private static SocketManager socketManager;

    private Socket mSocket;
    private SocketManagerListener mListener;

    public static SocketManager getInstance() {
        if (socketManager == null) {
            socketManager = new SocketManager();
        }
        return socketManager;
    }

    public void setListener(SocketManagerListener listener) {
        mListener = listener;
    }

    /**
     * connect to socket
     */
    public void connectToSocket(Context context) {
        Log.e("LOG", "Connecting to socket");
        if (mSocket != null) {
            mSocket.close();
            mSocket.disconnect();
            mSocket = null;
        }
        try {

            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            mSocket = IO.socket(AppConstants.SOCKET_URL, opts);
            mSocket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
            socketFailedDialog(e.getMessage());
            return;
        }

        if (mListener != null) mListener.onLoginWithSocket();

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (mListener != null) mListener.onConnect();
            }
        });

        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String message = args[0].toString();
                socketFailedDialog(message);
            }
        });

        mSocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String message = args[0].toString();
                socketFailedDialog(message);
            }
        });

        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String message = args[0].toString();
                socketFailedDialog(message);
            }
        });

//        mSocket.on(Const.EmitKeyWord.NEW_USER, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                if (mListener != null) mListener.onNewUser(args);
//            }
//        });
//
//        mSocket.on(Const.EmitKeyWord.USER_LEFT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                String userLeft = args[0].toString();
//                Gson gson = new Gson();
//                try {
//                    User user = gson.fromJson(userLeft, User.class);
//                    if (mListener != null) mListener.onUserLeft(user);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        mSocket.on(Const.EmitKeyWord.SEND_TYPING, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                String sendTyping = args[0].toString();
//                Gson gson = new Gson();
//                try {
//                    SendTyping typing = gson.fromJson(sendTyping, SendTyping.class);
//                    if (mListener != null) mListener.onTyping(typing);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        mSocket.on(Const.EmitKeyWord.NEW_MESSAGE, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                LogCS.w("LOG", "MESSAGE RECEIVED");
//                String newMessage = args[0].toString();
//                Gson gson = new Gson();
//                try {
//                    Message message = gson.fromJson(newMessage, Message.class);
//                    if (mListener != null) mListener.onMessageReceived(message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        mSocket.on(Const.EmitKeyWord.MESSAGE_UPDATED, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                String newMessage = args[0].toString();
//                Gson gson = new Gson();
//                try {
//                    List<Message> messages = gson.fromJson(newMessage, new TypeToken<List<Message>>() {
//                    }.getType());
//                    if (mListener != null) mListener.onMessagesUpdated(messages);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        mSocket.on(Const.EmitKeyWord.SOCKET_ERROR, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                String response = args[0].toString();
//                Gson gson = new Gson();
//                try {
//                    BaseModel responseModel = gson.fromJson(response, BaseModel.class);
//                    if (mListener != null) mListener.onSocketError(responseModel.code);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    protected void socketFailedDialog(String message) {
        if (mListener != null)
            mListener.onSocketFailed(!TextUtils.isEmpty(message) ? message : "Socket Connection Error!");
    }

    public void fetchUserList() {
        mSocket.on(AppConstants.USER_LIST, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String userList = args[0].toString();
//                Gson gson = new Gson();
                try {
//                    User user = gson.fromJson(userLeft, User.class);
                    if (mListener != null) mListener.onUserList(userList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * emit message to socket
     *
     * @param emitType   type of emit message
     * @param jsonObject data for send to server
     */
    public void emitMessage(String emitType, JSONObject jsonObject) {
        if (mSocket != null) mSocket.emit(emitType, jsonObject);
    }

    /**
     * emit message to socket
     *
     * @param emitType type of emit message
     * @param object   data for send to server
     */
    public void emitMessage(String emitType, String object) {
        if (mSocket != null) mSocket.emit(emitType, object);
    }

    /**
     * close socket and disconnect to socket and set socket and listener to null
     */
    public void closeAndDisconnectSocket() {
        if (mSocket != null) {
            Log.e("LOG", "Closing socket");
            mSocket.close();
            mSocket.disconnect();
            mSocket = null;
            mListener = null;
        }
    }

    /**
     * reconnect to socket if socket is null or disconnected
     */
    public void tryToReconnect(Context context) {
        Log.e("LOG", "Check for socket reconnect");
        if (mSocket != null) {
            if (mSocket.connected()) {
            } else {
                connectToSocket(context);
            }
        } else {
            connectToSocket(context);
        }
    }

    /**
     * check if socket is connected
     */
    public boolean isSocketConnect() {
        if (mSocket == null) return false;
        return mSocket.connected();
    }

}
