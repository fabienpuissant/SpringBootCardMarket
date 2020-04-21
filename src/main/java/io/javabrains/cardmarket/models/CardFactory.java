package io.javabrains.cardmarket.models;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class CardFactory {
	
	private String[] names;
	
	private String champions = "Aatrox/Ahri/Akali/Alistar/Amumu/Anivia/Annie/Aphelios/Ashe/AurelioSol/Azir/Bard/Blitzcrank/Brand/Braum/Caitlyn/Camille/Cassiopea/Chogath/Corki/Darius/Diana/Draven/DrMundo/Ekko/Elise/Evelynn/Ezreal/Fiddlestick/Fiora/Fizz/Galio/Gangplank/Garen/Gnar/Gragas/Graves/Hecarim/Heimerdinger/Illaoi/imagesLO/Irelia/Ivern/Janna/JarvanIV/Jax/Jayce/Jhin/Jinx/Kaisa/Kalista/Karma/Karthus/Kassadin/Katarina/Kayle/Kayne/Kennen/Khazix/Kindred/Kled/Kogmaw/Leblanc/LeeSin/Leona/Lissandra/Lucian/Lulu/Lux/Malphite/Malzahar/Maokai/MasterYi/MissFortune/Morderkaiser/Morgana/Nami/Nasus/Nautilus/Neeko/Nidalee/Nocturne/Nunu/Olaf/Orianna/Ornn/Pantheon/Poppy/Pyke/Qiyana/Quinn/Rakan/Rammus/Reksai/Renekton/Rengar/Rest-JPA-Mysql-SpringBoot-Sample/Riven/Rumble/Ryze/Sejuani/Senna/Sett/Shaco/Shen/Shyvana/Singed/Sion/Sivir/Skarner/Sona/Soraka/Swain/Sylas/Syndra/TahmKench/Taliyah/Talon/Taric/Teemo/Tresh/Tristana/Trundamere/Trundle/TwistedFate/Twitch/Udyr/Urgot/Varus/Vayne/Veigar/Velkoz/Vi/Viktor/Vladimir/Volibear/Warwick/Wukong/Xayah/Xerath/XinZhao/Yasuo/Yorick/Yuumi/Zac/Zed/Ziggs/Zilean/Zoe/Zyra";

	public CardFactory() {
		
		this.names = this.champions.split("/");
	}
	
	public String[] getnames() {
		return names;
	}


	public void setnames(String[] names) {
		this.names = names;
	}

	public CardEntity createCard() {
		Random rand = new Random();
		String name = this.names[rand.nextInt(this.names.length)];
		String imgUrl = "/images/" + name + ".png";
		String description = "";
		int attack = rand.nextInt(100) + 30;
		int defence = rand.nextInt(100) + 30;
		return new CardEntity(name, imgUrl, description, attack, defence);
		
	}
	
}

