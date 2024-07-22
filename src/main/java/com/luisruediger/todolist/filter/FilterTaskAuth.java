package com.luisruediger.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.filter.OncePerRequestFilter;

import com.luisruediger.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  //é usado para executar alguma ação. Ex.: verificar se a senha está cadastrada no BD e liberar o login do usuário
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
        var servletPath = request.getServletPath();

        if(servletPath.equals("/tasks/")) {
          //pegar a auth (login e senha):
          //Retorna a request com credentials
          var authorization = request.getHeader("Authorization");
          //Remover o "Basic" que vem no inicio das credentials
          var authEncoded = authorization.substring("Basic".length()).trim();
          //Faz o decode de Base64 para array de byte
          byte[] authDecode = Base64.getDecoder().decode(authEncoded);
          //Transforma os byte para String
          var authString = new String(authDecode);
          //faz o split removendo o ":" e coloca o valor nas posições do array, retornando um array com duas posições (user e password)
          String[] credentials = authString.split(":");
          String username = credentials[0];
          String password = credentials[1];

          //validar usuario
            var user = this.userRepository.findByUsername(username);

            if(user == null) {
              response.sendError(401);
            }else {
              //validar senha
              var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
              if(passwordVerify.verified) {  
                request.setAttribute("idUser", user.getId());          
                filterChain.doFilter(request, response);
              }else {
                response.sendError(401);
              }
            }
        }else {
          filterChain.doFilter(request, response);
        }

        
      }
}
