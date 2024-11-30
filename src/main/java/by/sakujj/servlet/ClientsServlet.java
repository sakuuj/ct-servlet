package by.sakujj.servlet;

import by.sakujj.context.Context;
import by.sakujj.dto.ClientRequest;
import by.sakujj.dto.ClientResponse;
import by.sakujj.http.StatusCodes;
import by.sakujj.services.ClientService;
import by.sakujj.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/clients/*")
public class ClientsServlet extends HttpServlet {

    private ClientService clientService;
    private Validator validator;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        Context context = Context.getInstance();

        clientService = context.getByClass(ClientService.class);
        validator = context.getByClass(Validator.class);
        objectMapper = context.getByClass(ObjectMapper.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {

            handleFindAll(resp);
            return;
        }

        String pathWithoutForwardSlash = pathInfo.substring(1);
        UUID idToFindBy = UUID.fromString(pathWithoutForwardSlash);
        handleFindById(resp, idToFindBy);
    }

    private void handleFindAll(HttpServletResponse resp) throws IOException {
        JsonUtil.writeResponse(StatusCodes.OK, clientService.findAll(), resp, objectMapper);
    }

    private void handleFindById(HttpServletResponse resp, UUID idToFindBy) throws IOException {

        Optional<ClientResponse> optionalClient = clientService.findById(idToFindBy);
        if (optionalClient.isEmpty()) {

            resp.setStatus(StatusCodes.NOT_FOUND);
            resp.getWriter().flush();

        } else {
            JsonUtil.writeResponse(StatusCodes.OK, optionalClient.get(), resp, objectMapper);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();
        if (!(pathInfo == null || "/".equals(pathInfo))) {

            resp.setStatus(StatusCodes.BAD_REQUEST);
            resp.getWriter().flush();
            return;
        }

        String requestBody = JsonUtil.readRequest(req);
        handleSave(resp, requestBody);
    }

    private void handleSave(HttpServletResponse resp, String requestBody) throws IOException {
        ClientRequest clientToSave = objectMapper.readValue(requestBody, ClientRequest.class);

        Set<ConstraintViolation<ClientRequest>> violations = validator.validate(clientToSave);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> "'%s': %s".formatted(v.getPropertyPath(), v.getMessage()))
                    .collect(Collectors.joining());
            JsonUtil.writeResponse(StatusCodes.BAD_REQUEST, errorMsg, resp, objectMapper);
            return;
        }

        UUID saved = clientService.save(clientToSave);

        resp.setHeader("Location", "/clients/" + saved);
        resp.setStatus(StatusCodes.NO_CONTENT);
    }
}
