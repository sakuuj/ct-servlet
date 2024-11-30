package by.sakujj.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@UtilityClass
public class JsonUtil {

    public <T> void writeResponse(int statusCode, T object, HttpServletResponse resp, ObjectMapper objectMapper) throws IOException {
        resp.setStatus(statusCode);
        String responseBody = objectMapper.writeValueAsString(object);

        PrintWriter writer = resp.getWriter();
        writer.write(responseBody);
        writer.flush();
    }

    public String readRequest(HttpServletRequest req) throws IOException {

        String contentLengthParam = req.getHeader("Content-Length");
        int contentLength = Integer.parseInt(contentLengthParam);

        if (contentLength > 10000 || contentLength < 0) {
            throw new IllegalStateException("Incorrect content length value");
        }

        String contentTypeParam = req.getHeader("Content-Type");
        if (!"application/json".equals(contentTypeParam)) {
            throw new IllegalStateException("Content type should be 'application/json'");
        }

        byte[] body = new byte[contentLength];
        var inputStream = req.getInputStream();

        int totalBytesRead = 0;
        while (totalBytesRead != contentLength) {

            int bytesRead = inputStream.read(body, totalBytesRead, contentLength);
            if (bytesRead == -1) {
                break;
            }
            totalBytesRead += bytesRead;
        }

        if (totalBytesRead != contentLength) {
            throw new IllegalStateException("Incorrect content length value");
        }

        return new String(body);
    }
}
