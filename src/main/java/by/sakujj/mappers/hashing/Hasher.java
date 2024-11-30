package by.sakujj.mappers.hashing;

import org.mapstruct.Named;

public interface Hasher {

    String hash(String plainText);

    boolean verifyHash(String candidateText, String storedHash);
}
