package io.github.zaur2025.userservicespringswaggerhateoas.controller;

import io.github.zaur2025.userservicespringswaggerhateoas.assembler.UserModelAssembler;
import io.github.zaur2025.userservicespringswaggerhateoas.dto.UserRequestDto;
import io.github.zaur2025.userservicespringswaggerhateoas.dto.UserModel;
import io.github.zaur2025.userservicespringswaggerhateoas.entity.User;
import io.github.zaur2025.userservicespringswaggerhateoas.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Пользователи", description = "Управление пользователями")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @PostMapping
    @Operation(summary = "Создать нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан",
                    content = @Content(schema = @Schema(implementation = UserModel.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные или email уже существует")
    })
    public ResponseEntity<UserModel> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("POST /api/users - создание пользователя");
        User createdUser = userService.createUser(userRequestDto); // Сервис теперь возвращает User, а не DTO
        UserModel userModel = userModelAssembler.toModel(createdUser);

        return ResponseEntity
                .created(userModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(userModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "400", description = "Пользователь не найден")
    })
    public ResponseEntity<UserModel> getUserById(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/users/{} - получение пользователя", id);
        User user = userService.getUserById(id); // Сервис теперь возвращает User
        UserModel userModel = userModelAssembler.toModel(user);
        return ResponseEntity.ok(userModel);
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    public ResponseEntity<CollectionModel<UserModel>> getAllUsers() {
        log.info("GET /api/users - получение всех пользователей");
        List<User> users = userService.getAllUsers(); // Сервис теперь возвращает List<User>
        List<UserModel> userModels = users.stream()
                .map(userModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<UserModel> collectionModel = CollectionModel.of(userModels);
        // Добавляем ссылку на самого себя (коллекцию)
        collectionModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные или пользователь не найден")
    })
    public ResponseEntity<UserModel> updateUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("PUT /api/users/{} - обновление пользователя", id);
        User updatedUser = userService.updateUser(id, userRequestDto); // Сервис возвращает User
        UserModel userModel = userModelAssembler.toModel(updatedUser);
        return ResponseEntity.ok(userModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "400", description = "Пользователь не найден")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/users/{} - удаление пользователя", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}