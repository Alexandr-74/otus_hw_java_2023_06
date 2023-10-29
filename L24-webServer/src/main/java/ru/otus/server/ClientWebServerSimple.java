package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.dao.ClientDao;
import ru.otus.db.repository.ClientRepository;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.ClientService;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.ClientApiServlet;
import ru.otus.servlet.ClientServlet;

public class ClientWebServerSimple implements ClientWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    private final ClientService clientService;
    private final ClientRepository clientRepository;

    public ClientWebServerSimple(int port, Gson gson, TemplateProcessor templateProcessor) {
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
        this.clientService = new ClientService();
        this.clientRepository = new ClientRepository();
        clientRepository.setUp();
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/api/clients", "/api/client/*", "/create-client", "/client"));

        server.setHandler(handlers);
        return server;
    }

    @SuppressWarnings({"squid:S1172"})
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[] {START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientServlet(templateProcessor, clientService, clientRepository)), "/client");
        servletContextHandler.addServlet(new ServletHolder(new ClientServlet(templateProcessor, clientService, clientRepository)), "/create-client");
        servletContextHandler.addServlet(new ServletHolder(new ClientApiServlet(gson, clientService, clientRepository)), "/api/client/*");
        servletContextHandler.addServlet(new ServletHolder(new ClientApiServlet(gson, clientService, clientRepository)), "/api/clients/");
        return servletContextHandler;
    }
}
