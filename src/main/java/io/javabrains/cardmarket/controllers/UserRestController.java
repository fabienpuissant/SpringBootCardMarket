package io.javabrains.cardmarket.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.cardmarket.models.CardEntity;
import io.javabrains.cardmarket.models.CardFactory;
import io.javabrains.cardmarket.models.UserEntity;
import io.javabrains.cardmarket.utils.Tools;

@RestController
public class UserRestController {
	
	private CardFactory cardFactory = new CardFactory();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CardService cardService;
	
	/**
	 * Get user information from name
	 * @param name
	 * @return UserEntity
	 */
	@GetMapping("UserService/user/{name}")
	public UserEntity getUser(@PathVariable String name) {
		return userService.getUserByName(name);
	}
	
	@GetMapping("/UserService/user/money/{name}")
	public String getMoney(@PathVariable String name) {
		return userService.getUserByName(name).getMoney().toString();
	}
	
	/**
	 * Return information of user if name and password match, otherwise return void string
	 * @param name
	 * @param pswd
	 * @return String 
	 */
	@GetMapping("UserService/user/{name}/{pswd}")
	public String checktUser(@PathVariable String name, @PathVariable String pswd){
		UserEntity user = userService.getUserByName(name);
		if(user == null) {
			return "";
		}
		else if(!user.getPassword().contentEquals(pswd)) {
			return "";
		}
		return Tools.toJsonString(user);
	}
	
	/**
	 * Get all cards that user owns
	 * @param id of the user
	 * @return String the cards of the user 
	 */
	@GetMapping("UserService/user/cards/{id}")
	public String getCards(@PathVariable String id) {
		Set<CardEntity> userCards = userService.getUserById(id).getCards();
		String cardsString = "";
		for(CardEntity card: userCards) {
			cardsString += String.valueOf(card.getId()) + "/";
		}
		return this.CardStringConversion(cardsString);
		
	}
	
	/**
	 * Get all cards that user does not have
	 * @param id
	 * @return String all the card that user can buy
	 * @throws IOException
	 */
	@GetMapping("UserService/user/BuyCards/{id}")
	public String getBuyCards(@PathVariable String id) throws IOException {
		Set<CardEntity> userCards = userService.getUserById(id).getCards();
		List<CardEntity> allCards = cardService.getAll();
		String buyCards = "";
		for(CardEntity card: allCards) {
			if(!userCards.contains(card)) {
				buyCards += String.valueOf(card.getId()) + "/";
			}
		}
		return this.CardStringConversion(buyCards);
	}
	
	@GetMapping("UserService/Buy/{name}/{imgId}")
	public boolean buyCard(@PathVariable String name, @PathVariable String imgId) {
		UserEntity user = userService.getUserByName(name);
		CardEntity card = cardService.getCardById(imgId);
		if(user.getMoney() < card.getPrice()) {
			return false;
		}
		user.addCard(card);
		user.setMoney(user.getMoney() - card.getPrice());
		userService.updateUser(user);
		return true;
		
		
	}
	
	/**
	 * Sell the card of the user
	 * @param name
	 * @param imgId
	 * @return boolean
	 */
	@GetMapping("UserService/Sell/{name}/{imgId}")
	public boolean sellCard(@PathVariable String name, @PathVariable String imgId) {
		UserEntity user = userService.getUserByName(name);
		CardEntity card = cardService.getCardById(imgId);
		boolean bool = user.removeCard(card);
		user.setMoney(user.getMoney() + card.getPrice());
		userService.updateUser(user);
		return bool;
	}
	
	
	@GetMapping("UserService/craft/{name}")
	public String craftCard(@PathVariable String name) {
		UserEntity user = userService.getUserByName(name);
		if(user.getMoney() < 100){
			return "";
		}
		else {
			CardEntity card = cardFactory.createCard();
			user.addCard(card);
			cardService.addCard(card);
			if(user.getMoney() < 250) {
				return "";
			}
			user.setMoney(user.getMoney() - 250);
			userService.updateUser(user);
			return Tools.toJsonString(card);
			
		}
	}
	
	
	/**
	 * Add user in the database
	 * @param user
	 * @return boolean true if no errors
	 * @throws IOException
	 */
	@PostMapping(value="UserService/addUser", consumes=MediaType.APPLICATION_JSON_VALUE)
	public boolean addUser(@RequestBody UserEntity user) throws IOException {
		List <CardEntity> cardlist = cardService.getAll();
		Random r = new Random();
		Integer randomInt;
		List<Integer> intlist = new ArrayList<Integer>();
		for(int i = 0; i < 5 ; i++) {
			randomInt = r.nextInt(cardlist.size());
			while(intlist.contains(randomInt)) {
				randomInt = r.nextInt(cardlist.size());
				
			}
			intlist.add(randomInt);
			CardEntity card = cardlist.get(randomInt);
			user.addCard(card);
			
		}
		return userService.addUser(user);
	}

	
	/**
	 * Erase the last character of the string
	 * @param String
	 * @return String
	 */
	private String CardStringConversion(String str) {
	        return str = str.substring(0, str.length() - 1);
	}

}
