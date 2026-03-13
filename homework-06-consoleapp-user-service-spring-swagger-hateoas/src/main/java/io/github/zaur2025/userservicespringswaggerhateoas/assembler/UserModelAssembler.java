package io.github.zaur2025.userservicespringswaggerhateoas.assembler;

import io.github.zaur2025.userservicespringswaggerhateoas.controller.UserController;
import io.github.zaur2025.userservicespringswaggerhateoas.dto.UserModel;
import io.github.zaur2025.userservicespringswaggerhateoas.entity.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(User user) {
        UserModel userModel = instantiateModel(user);

        userModel.setId(user.getId());
        userModel.setName(user.getName());
        userModel.setEmail(user.getEmail());
        userModel.setAge(user.getAge());
        userModel.setCreatedAt(user.getCreatedAt());

        // Добавляем ссылки (HATEOAS)
        userModel.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
        userModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));

        // Ссылка на удаление (для примера)
        userModel.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete"));

        return userModel;
    }
}