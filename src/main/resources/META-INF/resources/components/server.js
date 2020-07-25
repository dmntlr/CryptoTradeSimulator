const template = document.createElement('template');
template.innerHTML = `
<style>
td {
	border: 3px solid black;
}
</style>
<table id="gameRoomsTable">
	<tbody id ="gameRoomsBody">
		<tr>
			<th>RoomID</th>
			<th>Players</th>
			<th>Button</th>
		</tr>
	</tbody>
</table>

`
class Server extends HTMLElement {
	constructor() {
		super();
		this.attachShadow({ mode: 'open' });
		this.shadowRoot.appendChild(template.content.
			cloneNode(true));
	}
	
	set data(serverData) {
		var table = this.shadowRoot.getElementById('gameRoomsTable');
		table.innerHTML = table.rows[0].innerHTML;
		
		this.setAttribute('data',serverData);
		serverData.forEach(function (room , roomID) {
	    	var playerCount = 0;
			var row = document.createElement('tr');
			var roomIDCell = document.createElement('td');
			roomIDCell.innerText = roomID;
			roomIDCell.id = 'roomID';
			row.appendChild(roomIDCell);
	    	room.users.forEach(function (user, userID) {
	         if(user != null) {
	            playerCount++;
	         }
	      });
	    	var playerCountCell = document.createElement('td');
	    	playerCountCell.innerText = playerCount + '/' + room.users.length;
	    	row.appendChild(playerCountCell);
	    	
	    	var connectButtonCell = document.createElement('td');
	    	var connectButton = document.createElement('button');
	    	connectButton.innerText = 'Connect';
	    	connectButton.setAttribute('onclick',
	    			'connectToRoom(this.parentElement.parentElement)');
	    	connectButtonCell.appendChild(connectButton);
	    	row.appendChild(connectButtonCell);
	    	table.appendChild(row);
	   }); 
		
	}
	
	attributeChangedCallback(name, oldValue, newValue) {
		switch (name) {
		  case 'data':
			this.shadowRoot.querySelector('#playercount').innerText =
			    newValue + '/2';
			break;
		  case 'playercount':
			this.shadowRoot.querySelector('#roomid').innerText =
			    newValue;
			break;
		}
	  }	
}

function connectToRoom(row) {
	var username = document.getElementById("username").value;
	console.log(row);
	var roomID = row.querySelector('#roomID').innerText;
	if(username.length > 0) {
		console.log('connecting to room!' 
			+ roomID);
		sendRoomCoonection(username,roomID);
	} else {
		alert('Bitte Benutzernamen eingeben!');
	}
}

function sendRoomCoonection(username,room) {
	var json = JSON.stringify({
		"username" : username,
		"roomID" : room
	});
	gameRoomClient.send(json);
}

window.customElements.define('server-list', Server);