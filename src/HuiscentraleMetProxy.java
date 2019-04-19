import com.onsdomein.proxy.ProxyOnsDomein;
import java.io.IOException;

class HuiscentraleMetProxy {
    private String client_id = "5678";
    private ProxyOnsDomein proxyOnsDomein = new ProxyOnsDomein();

    HuiscentraleMetProxy() {
        // After first boot of app connection is made, then passes on to listeningForMessage
        try {
            proxyOnsDomein.connectClientToServer(client_id);
            listenForMessage();
        } catch (IOException e) {
            System.out.println("HC cannot connect to server: " + e);
        }
    }

    private void listenForMessage() {
        // get messages from server
        while (true) {
            String request;
            try {
                request = proxyOnsDomein.receiveRequest();
            } catch (Exception e) {
                System.out.println("Connection with server lost. " + e);
                break;
            }
            System.out.println("received from server: " + request);
            // what you do from here is up to you.
            sendToArduino(request);
        }
    }

    private void sendToArduino(String message) {
        String reactionFromArduino;
        // You know the protocol so this can't be a surprise.
        String[] messageSplit = message.split(";", 0);
        // only send part 3 and 4 to arduino or handle this in HC code
        //TODO: send via Jserial
        if (messageSplit.length == 3) {
            System.out.println(messageSplit[2] + " send to Arduino");
            //TODO: handle response from Arduino
        reactionFromArduino = "Arduino received your message and says hi!";
//            reactionFromArduino = "setHc;" + client_id + ";" + messageSplit[1] + ";" + messageSplit[2];
        } else {
            reactionFromArduino = "res;FAIL: No message send to Arduino";
        }
            receivedFromArduino(messageSplit[1], reactionFromArduino);

    }

    private void receivedFromArduino(String reactionFor, String reactionFromArduino) {
        try {
            //TODO: make sure you always respond, the server will if HC is offline, GA will wait for a reply
//            proxyOnsDomein.sendRequest(client_id, reactionFor, reactionFromArduino);
            proxyOnsDomein.sendResponse("setHc",client_id, reactionFor, reactionFromArduino);
        } catch (Exception e) {
            System.out.println("HC kan geen contact maken met de server. " + e);
        }
    }

}
