package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.eclipse.jetty.server.Request;
import ru.otus.dao.ClientDao;
import ru.otus.db.repository.ClientRepository;
import ru.otus.model.Client;
import ru.otus.services.ClientService;

@SuppressWarnings({"squid:S1948"})
public class ClientApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;
    private final Gson gson;
    private final ClientService clientService;

    public ClientApiServlet(Gson gson, ClientService clientService, ClientRepository clientRepository) {
        this.gson = gson;
        this.clientService = clientService;
        this.clientService.setClientRepository(clientRepository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (((Request) request).getPathInContext().equals("/api/clients/")) {
            List<Client> clients = clientService.findAll();
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(gson.toJson(clients));
        } else {

            Client client = clientService.findById(extractIdFromRequest(request));

            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(gson.toJson(client));
        }
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }
}
