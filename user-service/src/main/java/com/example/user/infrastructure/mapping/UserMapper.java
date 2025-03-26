package com.example.user.infrastructure.mapping;

import com.example.user.domain.model.User;
import com.example.user.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a User (Domain Model) to a UserEntity (Database Entity).
     * The method ignores mapping userId, createdAt and updateAt fields as they are JPA managed.
     * <p>
     *
     * @param domain the User domain model
     * @return the corresponding UserEntity
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toEntity(User domain);

    /**
     * Converts a UserEntity (Database Entity) to a User (Domain Model).
     * The method ignores mapping userId, createdAt and updateAt fields as they are JPA managed. <br>
     * The method is intended to be used when updating an existing entity.
     * <p>
     *
     * @param domain the User domain model from the which the data has to be mapped
     * @param entity the UserEntity to which the data has to be mapped
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void toEntity(User domain, @MappingTarget UserEntity entity);

    /**
     * Converts a UserEntity (Database Entity) to a User (Domain Model).
     * <p>
     *
     * @param entity the UserEntity from which the data has to be mapped.
     * @return the corresponding User (Domain Model).
     */
    User toDomain(UserEntity entity);

}