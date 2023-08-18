import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class App {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 1024);

        server.createContext("/pessoa", new StatusHandler());

        server.start();
    }

    public static class StatusHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {

            String metodo = exchange.getRequestMethod();


            System.out.println(metodo);

//            exchange.getResponseHeaders().set("Custom-Header-1", "A~HA");
            if(isMethodValid(metodo)){

                //TODO: TRATAR E DIRECIONAR OS OUTROS METODOS
                sendRequest(exchange,"atamaluca", MetodosHTTP.GET);
            }
            else sendRequest(exchange,405);

            exchange.close();


        }

        private static void sendRequest(HttpExchange exchange, String response, MetodosHTTP method) throws IOException {
            String responseMsg = response + "\n";
            exchange.sendResponseHeaders(method.getCode(), responseMsg.length());
            OutputStream os = exchange.getResponseBody();
            os.write(responseMsg.getBytes());
            os.flush();
            os.close();
        }

        private static void sendRequest(HttpExchange exchange, int code) throws IOException {
            exchange.sendResponseHeaders(code, "\n".length());
            OutputStream os = exchange.getResponseBody();
            os.write("\n".getBytes());
            os.flush();
            os.close();
        }

        private boolean isMethodValid(String method){
            for (MetodosHTTP metodoValido : MetodosHTTP.values()){
                if(method.equalsIgnoreCase(metodoValido.toString())){
                    return true;
                }

            }
            return false;
        }

    }


}

