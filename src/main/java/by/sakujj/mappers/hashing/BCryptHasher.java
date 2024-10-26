package by.sakujj.mappers.hashing;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.mindrot.jbcrypt.BCrypt;

@RequiredArgsConstructor
public class BCryptHasher implements Hasher{

    private static final int LOG_ROUNDS = 10;

    @Named("hash")
    @Override
    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
    }

    @Override
    public boolean verifyHash(String candidatePassword, String storedHash) {
        return BCrypt.checkpw(candidatePassword, storedHash);
    }
}
