package io.javabrains.cardmarket.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.cardmarket.models.UserEntity;

@RestController
public class IndexController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/addUser")
	public String addUser() {
		/*
		*Affiche la page du formulaire d'inscription
		*/
		return "addUser.html";
	}
	
	@RequestMapping("/login")
	public String login() {
		/*
		 * Affiche la page du formulaire de connexion
		 */
		return "login";
	}
	
	@RequestMapping("/card")
	public String card() {
		/*
		 * Affiche la page d'acceuil lorsque l'utilisateur s'est connecté
		 */
		return "card";
	}
	
	

}
