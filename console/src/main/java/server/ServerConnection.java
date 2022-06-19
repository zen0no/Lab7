package server;

import exceptions.ServerException;
import net.Request;
import net.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class ServerConnection {
    private static ServerConnection serverConnection;
    private String host;
    private int port;

    private ServerConnection(){
        host =  System.getenv("host");
        port = 65100;
   }

    public static ServerConnection getConnection(){
        if (serverConnection == null){
            serverConnection = new ServerConnection();
        }
        return serverConnection;
    }


    public Response sendRequest(Request request) throws ServerException{
            Socket socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(host, port));
                socket.getOutputStream().write(Request.toJson(request).getBytes(StandardCharsets.UTF_8));
                byte[] bytes = new byte[4100];
                int amount;
                int index;
                int packagesNumber;
                ByteBuffer buffer;
                StringBuilder responseStrings = new StringBuilder();
                do {
                    amount = socket.getInputStream().read(bytes);
                    buffer = ByteBuffer.wrap(bytes);
                    index = buffer.getInt();
                    packagesNumber = buffer.getInt();
                    byte[] responseBytes = new byte[amount - 8];
                    buffer.get(responseBytes);
                    responseStrings.append(new String(responseBytes, StandardCharsets.UTF_8));
                    buffer.clear();

                }
                while (index < packagesNumber);
                Response response = Response.fromJson(responseStrings.toString());
                socket.close();
                if (response.code == Response.StatusCode.ERROR) throw new ServerException((String) response.body);
                return response;
            }
            catch (IOException e){
                throw new ServerException("Can't connect to the server");
            }

            catch (NullPointerException e){
            throw new ServerException(e.getMessage());
        }
    }




}
