HTTPStatus -> É o enum contendo os tipos de status http que usaremos, com seus codigos e suas mensagens.
HTTPServer -> É o server http propriamente dito
HTTPClient -> É o client que poderemos fazer requisições http nele, chamando assim o server, se assemelha ao telnet, porém recomendo ficar no postman para testar
->
Essa parte está tratando possiveis erros na requisiçao do client, e printado a primera linha da requisição que é no formato (METODO URI VERSAO_HTTP)

```
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
```

Aqui está retornando com not found se o URI(endpoint) não for válido

```
if(!uri.equals("/pessoa")){
sendResponse(HTTPStatus.NOT_FOUND);
return;
}
```
Aqui nós estamos separando o body do header
```
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

      System.out.println("Headers:\n" + headerData);
      System.err.println("Body:\n" + bodyData);
```

Esse método ainda requer construçao, porém ele é o responsável em criar o corpo da resposta http para o client, caso o status fosse no content, geraria um erro, portanto o if
```
private void sendResponse(HTTPStatus status) throws IOException {

    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    StringBuilder jsonResponse = new StringBuilder();

    jsonResponse.append("HTTP/1.1 " + status.getStatusCode() + " " + status.getReasonText() + "\r\n");
    jsonResponse.append("Date: " + new java.util.Date() + "\r\n");
    jsonResponse.append("Host : localhost\r\n");
    jsonResponse.append("Content-Type: application/json\r\n");
    jsonResponse.append("Connection: keep-alive\r\n"); // HTTP 1.1
    jsonResponse.append("\r\n");
    if(!status.equals(HTTPStatus.NO_CONTENT))
      jsonResponse.append("{ \""+mensagem+"\" : \"Admirador secreto de tarciana!\"}");


    out.write(jsonResponse.toString());
    out.flush();
}
}
```
