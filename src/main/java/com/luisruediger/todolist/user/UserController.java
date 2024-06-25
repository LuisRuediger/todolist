package com.luisruediger.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
  
  // Chamar a interface
  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  // ResponsyEntity usado para retornar status code
  public ResponseEntity create(@RequestBody UserModel userModel) {
    var user = this.userRepository.findByUsername(userModel.getUsername());

    if (user != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já cadastrado");
    }

    var passwordHasred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

    userModel.setPassword(passwordHasred);
    
    var userCreated = this.userRepository.save(userModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
  }
}
