var gameRoomClient;
var serverList = document.createElement('server-list');
var host = document.location.host;
var pathname = document.location.pathname;

document.body.appendChild(serverList);

function connectToGameRoom() {

	gameRoomClient = new WebSocket("ws://" + host + pathname + 'rooms');

	gameRoomClient.onmessage = function(event) {
		console.log("On Message called!");
		var data = JSON.parse(event.data);
		console.log('Data parsed');
		console.log(data);
		switch(data.MESSAGE_TYPE) {
			case 'GAME_MESSAGE':
				console.log(data);
				var cryptoGame = document.getElementById('cryptoGame');
				if(cryptoGame == null) {
					var cryptoGame = document.createElement('crypto-game');
					document.body.appendChild(cryptoGame);
				}
				cryptoGame.setAttribute('id','cryptoGame');
				cryptoGame.crypto = data.cryptoId;
				cryptoGame.price = data.cryptoPrice;
				cryptoGame.balance = data.balance;
				break;
			default:
				console.log('Standard from Game Room Server!');
				serverList.data = data;
				console.log("Updated rooms...")
				break;
		}
	};
}
