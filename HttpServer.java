
import java.io.*;
import java.net.*;

public class HttpServer {
  public static void main(String[] args) {
    int port = 8080;
    try {
      final ServerSocket serverSocket = new ServerSocket(port);
      System.err.println("Servidor iniciado na porta : " + port);

      while (true) {
        final Socket clientSocket = serverSocket.accept();
        Thread thread = new Thread(new HandlerServerHttp(clientSocket));
        thread.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

class HandlerServerHttp implements Runnable {
  private final Socket clientSocket;

  private static String mensagem = "felipe";

  public HandlerServerHttp(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void run() {
    System.out.println("Nova requisicao!!!");

    try (
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
      String requestLine = in.readLine();


      if (requestLine == null) {
        sendResponse(HTTPStatus.BAD_REQUEST);
        return;
      }
      System.out.println(requestLine);
      String[] requestParts = requestLine.split(" ");

      if (requestParts.length < 3) {
        sendResponse(HTTPStatus.BAD_REQUEST);
        return;
      }

      String requestHttpMethod = requestParts[0]; // Extraindo o metodo http
      String uri = requestParts[1]; // Extraindo a URI
      String httpVersion = requestParts[2]; // Extraindo a versao http da requisicao do cliente

      System.out.println("Metodo: " + requestHttpMethod + "\nURI: " + uri + "\nVersao HTTP: " + httpVersion);

      if(!uri.equals("/pessoa")){
        sendResponse(HTTPStatus.NOT_FOUND);
        return;
      }

      StringBuilder bodyData = new StringBuilder();
      String line;
      StringBuilder headerData= new StringBuilder();

      //Le e armazena os headers
      while ((line = in.readLine()) != null && !line.isEmpty()) {
        headerData.append(line).append("\r\n");
      }

      // Lê e armazena o body da requisicao
      while (in.ready()) {
        bodyData.append((char) in.read());
      }

      System.err.println("Headers:\n" + headerData);
      System.err.println("Body:\n" + bodyData);

      switch (requestHttpMethod){
        case "GET" -> sendResponse(HTTPStatus.OK);
        case "POST" -> {
          if(mensagem.isEmpty()){
            mensagem = "teste";
            sendResponse(HTTPStatus.CREATED);
          }
          else sendResponse(HTTPStatus.METHOD_NOT_ALLOWED);
        }
        case "PUT" -> {
          mensagem = "testePut";

          sendResponse(HTTPStatus.NO_CONTENT);
        }
        default -> sendResponse(HTTPStatus.METHOD_NOT_ALLOWED);
      }



    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void sendResponse(HTTPStatus status) throws IOException {

    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    StringBuilder responseBuilder = new StringBuilder();

    responseBuilder.append("HTTP/1.1 " + status.getStatusCode() + " " + status.getReasonText() + "\r\n");
    responseBuilder.append("Date: " + new java.util.Date() + "\r\n");
    responseBuilder.append("Host : localhost\r\n");
    responseBuilder.append("Content-Type: application/json\r\n");
    responseBuilder.append("Connection: close\r\n"); // HTTP 1.1
    responseBuilder.append("\r\n");
    if(!status.equals(HTTPStatus.NO_CONTENT) && !status.isError())
      responseBuilder.append("{ \""+mensagem+"\" : \"Admirador secreto de tarciana!\"}");


    out.write(responseBuilder.toString());
    out.flush();
  }
}



