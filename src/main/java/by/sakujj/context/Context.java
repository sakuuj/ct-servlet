package by.sakujj.context;


import by.sakujj.dao.ClientDAO;
import by.sakujj.dao.ClientDAOImpl;
import by.sakujj.dao.connection.ConnectionPool;
import by.sakujj.dao.connection.ConnectionPoolImpl;
import by.sakujj.mappers.ClientMapper;
import by.sakujj.mappers.ClientMapperImpl;
import by.sakujj.services.ClientService;
import by.sakujj.services.impl.ClientServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A context to get application classes instances from.
 */
@Slf4j
public class Context {

    private final Map<Class<?>, Object> instanceContainer = new HashMap<>();

    private static final ReentrantLock SINGLETON_LOCK = new ReentrantLock();

    private static final String DEFAULT_PROPERTIES_FILE_NAME = "application.yaml";


    private static Context INSTANCE;

    public static Context getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        try {
            SINGLETON_LOCK.lock();

            if (INSTANCE != null) {
                return INSTANCE;
            }

            INSTANCE = new Context();

        } finally {
            SINGLETON_LOCK.unlock();
        }

        return INSTANCE;
    }


    private Context() {
        initInstanceContainer();
    }

    public void close() {
        instanceContainer
                .entrySet()
                .stream()
                .filter(e -> AutoCloseable.class.isAssignableFrom(e.getKey()))
                .forEach(e -> {
                    try {
                        ((AutoCloseable) e.getValue()).close();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
        instanceContainer.clear();
    }

    public <T> T getByClass(Class<T> clazz) {
        return (T) instanceContainer.get(clazz);
    }

    public <T> void putInstanceOf(Class<T> clazz, T instance) {
        instanceContainer.put(clazz, instance);
    }


    private void initInstanceContainer() {

        putInstanceOf(ObjectMapper.class, new ObjectMapper());
        putInstanceOf(ConnectionPool.class, new ConnectionPoolImpl(DEFAULT_PROPERTIES_FILE_NAME));

        putValidators();
        putDAOs();
        putMappers();
        putServices();
    }

    private void putServices() {
        ClientService clientService = new ClientServiceImpl(
                getByClass(ClientDAO.class),
                getByClass(ClientMapper.class),
                getByClass(ConnectionPool.class)
        );
        putInstanceOf(ClientService.class, clientService);
    }

    private void putMappers() {
        putInstanceOf(ClientMapper.class, new ClientMapperImpl());
    }

    private void putValidators() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        putInstanceOf(Validator.class, validator);
    }


    private void putDAOs() {
        putInstanceOf(ClientDAO.class, new ClientDAOImpl());
    }

}
