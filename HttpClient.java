import java.io.*;
import java.net.*;

public class HttpClient {
    public static void main(String[] args) throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        System.err.print("Insira o host e a porta (ex: localhost 8080)\n");
        String hostPort = consoleReader.readLine();

        String[] hostPortArr = hostPort.split(" ");

        if (hostPortArr.length < 2) {
            throw new IOException("Output invalido de host e/ou porta");
        }

        String host = hostPortArr[0];
        int port = Integer.parseInt(hostPortArr[1]);

        try (Socket clientSocket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            System.err.println("Insira a requisicao\n");
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            while (!(line = consoleReader.readLine()).isEmpty()) {
                requestBuilder.append(line).append("\r\n");
            }

            StringBuilder bodyBuilder = new StringBuilder();


            bodyBuilder.append(consoleReader.readLine().trim());


            requestBuilder.append("Host: ").append(host).append("\r\n");

            if(!bodyBuilder.isEmpty()){

                int contentLength = bodyBuilder.length();

                requestBuilder.append("Content-Type: application/json\r\n");
                requestBuilder.append("Content-Length: ").append(contentLength).append("\r\n");
            }

            requestBuilder.append("Connection: close\r\n");

            String request = requestBuilder +"\r\n" + bodyBuilder;
            System.out.println("ENVIANDO REQUISICAO HTTP...:\n" + request);

            out.write(request);
            out.flush();

            System.out.println("Response do SERVIDOR:");
            while ((line = in.readLine()) != null) {
                System.err.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
