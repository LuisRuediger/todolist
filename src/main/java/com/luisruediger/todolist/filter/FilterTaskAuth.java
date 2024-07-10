package com.luisruediger.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Controller;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FilterTaskAuth extends OncePerRequestFilter {

  //é usado para executar alguma ação. Ex.: verificar se a senha está cadastrada no BD e liberar o login do usuário
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
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
        String user = credentials[0];
        String password = credentials[1];

        //validar usuario

        //validar senha

        filterChain.doFilter(request, response);

  }

  
}
