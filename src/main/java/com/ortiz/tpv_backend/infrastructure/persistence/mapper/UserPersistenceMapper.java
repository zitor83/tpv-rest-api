package com.ortiz.tpv_backend.infrastructure.persistence.mapper;

import com.ortiz.tpv_backend.domain.model.User;
import com.ortiz.tpv_backend.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    User toDomain(UserEntity userEntity);

    UserEntity toEntity(User user);
}

