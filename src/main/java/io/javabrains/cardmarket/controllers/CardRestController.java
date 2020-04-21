package io.javabrains.cardmarket.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.cardmarket.models.CardEntity;
import io.javabrains.cardmarket.models.UserEntity;
import io.javabrains.cardmarket.utils.Tools;


@RestController
public class CardRestController {
	
	@Autowired
	private CardService cardService;
	
	@GetMapping("CardService/card/{Userid}")
	public String getcard(@PathVariable String Userid) throws IOException {
		String userCards = this.getUserCards(Userid); 
		return userCards;

	}
	
	@GetMapping("CardService/card/number")
	public long getNumber() {
		return cardService.getNumber();
	}
	
	@GetMapping("CardService/{id}")
	public String getCardFeatures(@PathVariable String id) {
		CardEntity card = cardService.getCardById(id);
		return Tools.toJsonString(card);
	}
	
	@GetMapping("init")
	public void getcard() {
		List<CardEntity> cardlist = this.generateCard();
		for (int i = 0; i < cardlist.size(); i++) {
			CardEntity card = cardlist.get(i);
			cardService.addCard(card);
		}
		
	}
	
	@PostMapping(value="CardService/addCard", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addCard(@RequestBody CardEntity card) {
		cardService.addCard(card);
	}
	
	private String getUserCards(String id) throws IOException {
		URL obj = new URL("http://localhost:8080/UserService/user/cards/"+id);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                 while ((inputLine = in.readLine()) != null) {
                   response.append(inputLine);
                 } in .close();
        return response.toString();
	}
	

	private List<CardEntity> generateCard(){
		List<CardEntity> cardlist = new ArrayList<CardEntity>();
		CardEntity card;
		card = new CardEntity("Tayloy", "http://fairedesgifs.free.fr/da/sh/batman/batman%20(24).gif", "Description", 15, 20);
		cardlist.add(card);
		card = new CardEntity("Supej", "http://fairedesgifs.free.fr/da/sh/hulk/hulk%20(3).gif", "Description", 25, 20);
		cardlist.add(card);
		card = new CardEntity("Juye", "http://fairedesgifs.free.fr/da/sh/deadpool/dp%20(18).gif", "Description", 35, 40);
		cardlist.add(card);
		card = new CardEntity("Protec", "http://fairedesgifs.free.fr/da/sh/superman/superman%20(5).gif", "Description", 55, 20);
		cardlist.add(card);
		card = new CardEntity("Crewis", "http://fairedesgifs.free.fr/dragon/dragon-(16).gif", "Description", 15, 30);
		cardlist.add(card);
		card = new CardEntity("Cranit", "http://fairedesgifs.free.fr/dragon/dragon-(2).gif", "Description", 90, 30);
		cardlist.add(card);
		for (int i = 0; i < 20; i++) {
			card = new CardEntity("Tyro" + String.valueOf(i), "http://fairedesgifs.free.fr/dragon/dragon-("+String.valueOf(i)+").gif", "Description", 70, 30);
			cardlist.add(card);
		}

		return cardlist;
	}

}

