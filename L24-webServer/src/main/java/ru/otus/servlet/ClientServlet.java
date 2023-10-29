package ru.otus.servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import ru.otus.dao.ClientDao;
import ru.otus.db.repository.ClientRepository;
import ru.otus.services.ClientService;
import ru.otus.services.TemplateProcessor;

@SuppressWarnings({"squid:S1948"})
public class ClientServlet extends HttpServlet {
    private static final String USERS_PAGE_TEMPLATE = "client.html";

    private final TemplateProcessor templateProcessor;
    private final ClientService clientService;

    public ClientServlet(TemplateProcessor templateProcessor, ClientService clientService, ClientRepository clientRepository) {
        this.templateProcessor = templateProcessor;
        this.clientService = clientService;
        this.clientService.setClientRepository(clientRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<String> body = req.getReader().lines().toList();
        Map<String, String> requesrHashMap = new HashMap<>();
        for (String str : body) {

            System.out.println("input str " +str);
            String[] parsedArgs = str.split("&");
            List<String> parsed = Arrays.stream(parsedArgs)
                    .filter(s->s.matches(".+=.+")).
                    flatMap(s -> Arrays.stream(s.split("=")))
                    .toList();

           for (int i = 0; i < parsed.size()-1; i+=2) {
               String val = parsed.get(i+1);

               if (!val.matches("\\d+")) {
                   val = URLDecoder.decode(val, StandardCharsets.UTF_8);
               }
               requesrHashMap.put(parsed.get(i),val);
           }
        }

        clientService.createClient(requesrHashMap);


        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

}
