package fr.rob.core.auth.jwt;

public interface JWTDecodedResultInterface {

    Object get(String key);

    Object get(String key, Class<?> type);

}
