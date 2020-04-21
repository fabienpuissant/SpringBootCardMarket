/**
 * Redirect if the user is not connected
 */
if(sessionStorage.getItem("Name") == null){
	window.location.href = "/login.html";
}

$(".cardbutton").on("click", function(){
	console.log('ok'); 
});

$(document).ready(function() {
	
	var cardDisplayed = 10;
	
	/**
	 *  Display homepage and set listeners for market display
	 */
	var type;
	
	$("#market").hide();
	$("#craft").hide();
	
	$('#homeIcon').click(function(){
		$("#market").hide();
		$("#craft").hide();
		$("#home").show();
	});
	
	$('#sellIcon').click(function(){
		type = "Sell";
		displaySellMarket();
	});
	
	$('#buyIcon').click(function(){
		type = "Buy";
		displayBuyMarket();
	});
	
	$("#craft").click(function(){
		displayCraft();
	});
	
	$("#sellMarket").click(function(){
		type = "Sell";
		displaySellMarket();
	});
	
	$("#buyButtonId").click(function(){
		type = "Buy";
		displayBuyMarket();
	});
	
	$("craftIcon").click(function(){
		console.log("ok");
		displayCraft();
	});
	
    //Adding information of the user
	var json = JSON.parse(sessionStorage.getItem("Name"));
	$("#userNameId").text(json.surname);
	
	
	updateMoney();
	
	

/*****************************************************************************************************************************************/

	/**
	 * Toast a success message
	 * @param String the message to toast
	 */
	function toast(msg){
		toastr.options = {
				  "closeButton": false,
				  "debug": false,
				  "newestOnTop": false,
				  "progressBar": false,
				  "positionClass": "toast-top-center",
				  "preventDuplicates": true,
				  "onclick": null,
				  "showDuration": "300",
				  "hideDuration": "500",
				  "timeOut": "1000",
				  "extendedTimeOut": "500",
				  "showEasing": "swing",
				  "hideEasing": "linear",
				  "showMethod": "fadeIn",
				  "hideMethod": "fadeOut"
				}
		toastr["success"](msg, "Success");
	}
	
	/**
	 * Fill the selected image
	 * @param String imgUrl
	 * @param String name
	 * @param String description
	 * @param String attack
	 * @param String defence
	 * @param String price
	 * @param String id
	 */
	function fillImage(imgUrl, name, description, attack, defence, price, id){
		$('#cardImgId').attr("src", imgUrl);
	    //$('#cardDescriptionId').text(description);
	    $('#Name').text(name);
	    $('#cardAttack').text(attack);
	    $('#cardDefence').text(defence);
	    $('#cardPriceId').text(price);
	    $('#cardId').text(id);
	}
	
	/**
	 * Add an image in the market list
	 * @param String imgUrl
	 * @param String name
	 * @param String description
	 * @param String attack
	 * @param String defence
	 * @param String price
	 * @param String id
	 */
	function addImage(imgUrl, name, description, attack, defence, price, id){
		$("#imgrow").append("<tr class = 'cardchild'><td>" +
                "<img  class='ui avatar image' src='"+ imgUrl +"'> <span class = 'Name'>"+ name +" </span>" +
            "</td>" +
            "<td class='Attack'>"+ attack +"</td>"+
            "<td class='Defence'>"+ defence +"</td>"+
            "<td class='Price'>"+ price +"</td>"+
            "<td class='Description' style='display:none !important'>" + description + "</td>" + 
            "<td class='Id' style='display:none !important'>" + id + "</td>" +
            "<td class = 'cardbutton'>"+
                "<div class='ui vertical animated button' tabindex='0'>"+
                    "<div class='hidden content'>"+ type +"</div>"+
                    "<div class='visible content'>"+
                        "<i class='shop icon'></i>"+
                    "</div>"+
                "</div>"+
            "</td></tr>");
	}
	
	/**
	 * Display all the cards in the array
	 * @param String[] array containing id of cards to display
	 */
	function displayCards(cards){
		var i;
		for(i = 0; i < cards.length; i++){
			
			  $.ajax({
				  url:"/CardService/" + cards[i],
				  type:"GET",
				  success: function( data ){
					  var json = JSON.parse(data);
					  refillImage(json.imgUrl, json.name, json.description, json.attack, json.defence, json.price, json.id);
					  addImage(json.imgUrl, json.name, json.description, json.attack, json.defence, json.price, json.id)
				  }
			  });
			  
		  }
	}
	
	/**
	 * Add a focus listener to fill the selected card
	 */
	function addFocusListener(type){
		$("#imgrow").unbind();
		$("#imgrow").on("click", ".cardchild" ,function(event){
			var imgUrl = $(this).find(".image").attr('src');
			var name = $(this).find(".Name").text();
			var description = $(this).find(".Description").text();
			var attack = $(this).find(".Attack").text();
			var defence = $(this).find(".Defence").text();
			var price = $(this).find(".Price").text();
			var id = $(this).find(".Id").text();
		    fillImage(imgUrl, name, description, attack, defence, price, id);
		    
		    if($(event.target).closest("td").attr('class') == "cardbutton"){
		    	$.ajax({
					  url:"/UserService/" + type + "/" + json.name + "/" + id,
					  type:"GET",
					  success: function( data ){
						  if(data == true){
							  //Erase the card in the market
							  $("td").filter(function() {
								    return $(this).text() == id;
								}).closest("tr").attr("style", "display: none !important"); 
							  //Update the current money of the user
							  updateMoney();
							  //Refill image
							  refillImage();
								var msg = ""; 
								if(type == "Sell"){
									msg = "Card sold";
								}
								else {
									msg = "Card purchased";
								}
								
								toast(msg);
						  }
					  }
					
		    	});
		    
		    }
		    
		    
		});
		    
			
		    
	
	}
	
	/**
	 * Add listener to buy or sell a card
	 * @param String Sell or Buy
	 */
	function addClickListener(type){
		//Onclick on the selected image
		$("#OrderSelected").unbind();
		$("#OrderSelected").on("click", function(){
			/* var msg = confirm("Would you really " + type + " the selected card ?");
			
			if(!msg){
				return;
			} */
			
			var id = $("#cardId").text();
			
			$.ajax({
				  url:"/UserService/" + type + "/" + json.name + "/" + id,
				  type:"GET",
				  success: function( data ){
					  if(data == true){
						  //Erase the card in the market
						  $("td").filter(function() {
							    return $(this).text() == id;
							}).closest("tr").attr("style", "display: none !important"); 
						  //Update the current money of the user
						  updateMoney();
						  //Refill image
						  refillImage();
						  
	
						
					  }
				  }
			  });
			
			var msg = ""; 
			if(type == "Sell"){
				msg = "Card sold";
			}
			else {
				msg = "Card purchased";
			}
			
			toast(msg);
			
			
		});
		
	}
	
	/**
	 * Refill the image with the first card visible
	 */
	function refillImage(){
		  var card = $('#imgrow').find('.cardchild:visible').first();
		  var imgUrl = card.find(".image").attr('src');
		  var name = card.find(".Name").text();
		  var description = card.find(".Description").text();
		  var attack = card.find(".Attack").text();
		  var defence = card.find(".Defence").text();
		  var price = card.find(".Price").text();
		  var id = card.find(".Id").text();
		  fillImage(imgUrl, name, description, attack, defence, price, id);
	}
	
	/**
	 * Update the money of the user in the header banner
	 */
	function updateMoney(){
		$.ajax({
			  url:"/UserService/user/money/" + json.name,
			  type:"GET",
			  success: function( data ){
					  $("#money").text(data);
			  }
		  });
		
	}
	
	
	/**
	 * Display the sell market : empty it and refill
	 */	
	function displaySellMarket(){
		
		cardDisplayed = 10;
		
		$("#imgrow").empty();
		$("#MarketType").text(type + " Market");
		$('#marketType')[0].innerText=type;
		
		$("#home").hide();
		$("#market").show();
		
		$.ajax({
			  url:"/UserService/user/cards/" + json.id,
			  type:"GET",
			  success: function( data ){
				  var cards = data.split("/");
				  displayCards(cards);
				  addFocusListener("Sell");
				  addClickListener("Sell");
				 
			  }
			  
		});

	}
	
	
	/**
	 * Display the buy market : empty and refill
	 */
	function displayBuyMarket(){
		
		cardDisplayed = 10;
		
		$("#imgrow").empty();
		$("#MarketType").text(type + " Market");
		$('#marketType')[0].innerText=type;
		$("#home").hide();
		$("#market").show();
		
		
		$.ajax({
			  url:"UserService/user/BuyCards/" + json.id,
			  type:"GET",
			  success: function( data ){
				  cards = data.split('/');
				  displayCards(cards);
				  addFocusListener("Buy");
				  addClickListener("Buy");

				 
			  }
		  });
	}
	
	/**
	 * Display the craft menu
	 */
	function displayCraft(){
		
		$("#market").hide();
		$("#home").hide();
		$("#craft").show();
		
	}
	
	
	
});