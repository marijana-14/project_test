package marijana.app.project_test.controller;

import marijana.app.project_test.dto.UserDTO;
import marijana.app.project_test.dto.UserWithoutPasswordDTO;
import marijana.app.project_test.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    // Simple method available for both user and admin roles
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> hello()
    {
        return new ResponseEntity<>( "Hello World!", HttpStatus.OK);
    }

    // Returns list of users
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserWithoutPasswordDTO>> listAllUsers()
    {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // Returns user found by id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserWithoutPasswordDTO> findUserById(@PathVariable Long id)
    {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    // Method called to create user
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserWithoutPasswordDTO> createUser(@Valid @RequestBody UserDTO userDTO)
    {
        return new ResponseEntity<>(userService.createNewUser(userDTO), HttpStatus.CREATED);
    }

    // Method called to update user
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserWithoutPasswordDTO> updateUser(@Valid @RequestBody UserDTO userDTO)
    {
        return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
    }


    // Exception handler for non valid request body. Returns error message.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> fieldsWithError = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldWithError = ((FieldError) objectError).getField();
            String errorMsg= objectError.getDefaultMessage();
            fieldsWithError.put(fieldWithError, errorMsg);
        });
        return fieldsWithError;
    }



}
