package by.sakujj.mappers;

import by.sakujj.dto.ClientRequest;
import by.sakujj.dto.ClientResponse;
import by.sakujj.entity.Client;
import by.sakujj.mappers.hashing.BCryptHasher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = BCryptHasher.class)
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "notHashedPassword", qualifiedByName = "hash")
    Client fromRequest(ClientRequest request);

    ClientResponse toResponse(Client client);
}
