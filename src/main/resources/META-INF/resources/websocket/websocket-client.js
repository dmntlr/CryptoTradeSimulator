var gameRoomClient;
var serverList = document.createElement('server-list');
var host = document.location.host;
var pathname = document.location.pathname;
var cryptoGame;
document.body.appendChild(serverList);

function connectToGameRoom() {

	gameRoomClient = new WebSocket("ws://" + host + pathname + 'rooms');

	gameRoomClient.onmessage = function (event) {
		console.log("On Message called!");
		var data = JSON.parse(event.data);
		console.log('Data parsed')
		console.log(data);
		switch (data.MESSAGE_TYPE) {
			case 'GAME_MESSAGE':
				sessionStorage.setItem('jwt', data.token)
				data = parseJwt(data.token);
				console.log(data);
				if (document.getElementById('cryptoGame') == null) {
					cryptoGame = document.createElement('crypto-game');
					document.body.appendChild(cryptoGame);
				}
				cryptoGame.setAttribute('id', 'cryptoGame');
				cryptoGame.crypto = data.crypto;
				cryptoGame.price = data.price;
				cryptoGame.balance = data.balance;
				cryptoGame.amountBalance = data.amount;
				cryptoGame.status = false;
				cryptoGame.round = data.round + '/' + data.maxRounds;
				break;
			case 'ENDGAME_MESSAGE':
				if (data.winner == true) {
					alert('You won the game congratulations!');
				} else if (data.winner == false) {
					alert('Sorry you lost. Better luck next time.')
				}
				cryptoGame.parentNode.removeChild(cryptoGame);
				break;
			default:
				console.log('Standard from Game Room Server!');
				serverList.data = data;
				console.log("Updated rooms...")
				break;
		}
	};
}

function parseJwt(token) {
	var base64Url = token.split('.')[1];
	var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
	var jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
		return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
	}).join(''));

	return JSON.parse(jsonPayload);
};

