package ru.otus.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static ru.otus.server.utils.WebServerHelper.buildUrl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.ClientDao;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.TemplateProcessor;

@DisplayName("Тест сервера должен ")
class ClientWebServerImplTest {

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String API_USER_URL = "api/user";

    private static final long DEFAULT_USER_ID = 1L;

    private static final Client DEFAULT_USER = new Client(1L, "Крис Гир", new Address(null,"street1"), List.of(new Phone(null,"123-456")), "user1", "11111");

    private static Gson gson;
    private static ClientWebServer webServer;
    private static HttpClient http;

    @BeforeAll
    static void setUp() throws Exception {
        http = HttpClient.newHttpClient();

        TemplateProcessor templateProcessor = mock(TemplateProcessor.class);
        ClientDao clientDao = mock(ClientDao.class);

        given(clientDao.findById(DEFAULT_USER_ID)).willReturn(Optional.of(DEFAULT_USER));

        gson = new GsonBuilder().serializeNulls().create();
        webServer = new ClientWebServerSimple(WEB_SERVER_PORT, gson, templateProcessor);
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        webServer.stop();
    }

    @DisplayName("возвращать корректные данные при запросе пользователя по id если вход выполнен")
    @Test
    void shouldReturnCorrectUserWhenAuthorized() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(buildUrl(WEB_SERVER_URL, API_USER_URL, String.valueOf(DEFAULT_USER_ID))))
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body()).isEqualTo(gson.toJson(DEFAULT_USER));
    }
}
