package servers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    String server_URL;
    String api_Token;

    public KVTaskClient(String server_URL) {
        this.server_URL = server_URL;
        api_Token = register();

    }

    public String load(String key) {
        URI uri = URI.create(server_URL + "/load/" + key + "?API_TOKEN=" + api_Token + "/");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола HTTP
                .header("Accept", "application/json") // указываем заголовок Accept
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient client = HttpClient.newHttpClient();
        // отправляем запрос и получаем ответ от сервера

        HttpResponse<String> response = null;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на этапе загрузки " + e.getMessage());
        }
        return response.body();
    }

    public void put(String key, String json) {
        URI uri = URI.create(server_URL + "/save/" + key + "?API_TOKEN=" + api_Token + "/");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(json))    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола HTTP
                .header("Accept", "application/json") // указываем заголовок Accept
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient client = HttpClient.newHttpClient();
        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = null;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на этапе сохранения " + e.getMessage());
        }
    }

    private String register() {
        URI uri = URI.create(server_URL + "/register");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола HTTP
                .header("Accept", "application/json") // указываем заголовок Accept
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient client = HttpClient.newHttpClient();
        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = null;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка на этапе регистрации " + e.getMessage());
            return "";
        }
        return response.body();
    }
}
