package by.sakujj.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DAOException extends RuntimeException{
    public DAOException(Throwable cause) {
        super(cause);
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
