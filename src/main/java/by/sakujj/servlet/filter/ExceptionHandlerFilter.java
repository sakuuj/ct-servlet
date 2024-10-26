package by.sakujj.servlet.filter;

import by.sakujj.context.Context;
import by.sakujj.servlet.error.ApiError;
import by.sakujj.http.StatusCodes;
import by.sakujj.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlerFilter extends HttpFilter {

    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        objectMapper = Context.getInstance().getByClass(ObjectMapper.class);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {

            chain.doFilter(req, res);

        } catch (Exception exception) {

            if (res.isCommitted()) {
                throw new RuntimeException(exception);
            }

            JsonUtil.writeResponse(
                    StatusCodes.INTERNAL_SERVER_ERROR,
                    ApiError.builder()
                            .message(exception.getMessage())
                            .build(),
                    res,
                    objectMapper
            );
        }
    }
}
