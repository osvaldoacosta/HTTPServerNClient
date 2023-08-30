
import java.io.*;
import java.net.*;
import java.util.List;

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

    private static String mensagem = "";

    private static String httpVersion = "";

    public HandlerServerHttp(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("Nova requisicao!!!");

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
            String requestLine = in.readLine();


            if (requestLine == null || requestLine.isBlank()) {
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
            httpVersion = requestParts[2]; // Extraindo a versao http da requisicao do cliente

            if(!(httpVersion.equalsIgnoreCase("HTTP/1.0") || httpVersion.equalsIgnoreCase("HTTP/1.1"))){
                sendResponse(HTTPStatus.VERSION_NOT_SUPPORTED);
                return;
            }

            System.out.println("Metodo: " + requestHttpMethod + "\nURI: " + uri + "\nVersao HTTP: " + httpVersion);

            if (!uri.startsWith("/pessoa")) {
                sendResponse(HTTPStatus.BAD_REQUEST);
                return;
            }
            String pessoaId = uri.substring("/pessoa".length());


            StringBuilder bodyData = new StringBuilder();
            String line;
            StringBuilder headerData = new StringBuilder();

            //Le e armazena os headers
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                headerData.append(line).append("\r\n");
            }

            // Lê e armazena o body da requisicao
            while (in.ready()) {
                bodyData.append((char) in.read());
            }

            System.err.println("Headers do cliente:\n" + headerData);
            System.err.println("Body do cliente:\n" + bodyData);


            switch (requestHttpMethod) {
                case "GET" -> handleGetMethod(pessoaId);
                case "POST" -> handlePostMethod(bodyData);
                case "PUT" -> handlePutMethod(pessoaId, bodyData);
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

        responseBuilder.append(httpVersion).append(" ").append(status.getStatusCode()).append(" ").append(status.getReasonText()).append("\r\n");
        responseBuilder.append("Date: ").append(new java.util.Date()).append("\r\n");
        responseBuilder.append("Server: ServidorHTTP\r\n");

        if (!mensagem.isBlank()  && !status.isError()) {
            responseBuilder.append("Content-Type: application/json\r\n");
            responseBuilder.append("Content-Length: ").append(mensagem.length()).append("\r\n");
        }

        responseBuilder.append("Connection: close\r\n");
        responseBuilder.append("\r\n");

        if (!status.equals(HTTPStatus.NO_CONTENT) && !status.isError()) {
            responseBuilder.append(mensagem);
        }

        out.write(responseBuilder.toString());
        out.flush();

    }


    private void handlePutMethod(String pessoaId, StringBuilder bodyData) throws IOException {
        //Vai editar os dados de uma pessoa (menos o nome, pois eh o identificador).

        try {
            String nome = pessoaId.split("/")[1];

            PersonService.editarPessoa(nome,bodyData);
            sendResponse(HTTPStatus.NO_CONTENT);
        }
        catch (IOException|StringIndexOutOfBoundsException|ArrayIndexOutOfBoundsException e){
            sendResponse(HTTPStatus.BAD_REQUEST);
        }



    }
    private void handlePostMethod(StringBuilder bodyData) throws IOException {
        //Vai criar uma pessoa, se uma pessoa com um mesmo nome ja existir, ele retornara um method not allowed.
        try {

            Person person = new Person(bodyData.toString());
            if (PersonService.personExists(person)) {
                sendResponse(HTTPStatus.CONFLICT);
                return;
            }
            PersonService.salvarPessoa(person);
            mensagem = person.toJSON();
        } catch (IOException|StringIndexOutOfBoundsException|ArrayIndexOutOfBoundsException e) {
            sendResponse(HTTPStatus.BAD_REQUEST);
            return;
        }

        sendResponse(HTTPStatus.CREATED);
    }

    private void handleGetMethod(String pessoaId) throws IOException {

        //Vai Pegar todos as pessoas do banco
        if (pessoaId.length() < 2) {
            List<Person> people = FileDatabase.resgatarTodasPessoas();
            mensagem = PersonService.fromListToJsonArray(people);
            sendResponse(HTTPStatus.OK);
            return;
        }

        //Vai pegar uma pessoa especifica, baseada no nome, se nao achar nenhuma retorna not found
        try {

            String nome = pessoaId.split("/")[1];
            Person person = PersonService.resgatarPessoaPorNome(nome);
            mensagem = person.toJSON();

        } catch (IOException e) {
            sendResponse(HTTPStatus.NOT_FOUND);
            return;
        }

        sendResponse(HTTPStatus.OK);
    }


}



