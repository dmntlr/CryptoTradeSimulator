const gameTemplate = document.createElement('template');
 
gameTemplate.innerHTML = `
  <style>
    div {
      padding: 8px;
      width: 200px;
    }

    input {
      width : 100%;
    }
    
    button {
    	width : 94px;
    }
   
    form {
    	border  : 2px solid black;
    }
    
    .right {
    	float: right;
    }
	
  </style>
  <div id='container'>
  <form>
    <label for='crypto'>Cryptocurrency:</label>
    <input type='text' id='crypto' readonly/>
    <label for='price'>Price:</label>
    <input type='text' id='price' readonly></input>
    <label for='amount'>Amount:</label>
    <input type='text' id='amount'></input>
    <button id='buy' onclick='sendTransaction(this.getElementById("amount"))'>Buy</button>
    <button class='right' id='sell'>Sell</button>
  </form>
  <form>
  <label for='balance'>Balance:</label>
  <input type='text' id='balance' readonly>
  <table id='transactions'>
    <tr>
      <th>ID</th>
      <th>B/S</th>
      <th>Amount</th>
    </tr>
  </table>
</form>
</div>
`;
 
class CryptoGame extends HTMLElement {
  constructor() {
    super();
    this._shadowRoot = this.attachShadow({ mode: 'open' });
    this._shadowRoot.appendChild(gameTemplate.content.cloneNode(true));
  }

  set balance(balance) {
	 this.shadowRoot.querySelector('#balance').value = balance + '$';
  }
  
  set crypto(crypto) {
    this.shadowRoot.querySelector('#crypto').value = crypto;
  }

  set price(price) {
    this.shadowRoot.querySelector('#price').value = price;
  }
}
function sendTransaction(amount) {
	var json = JSON.stringify({
		"amount" : amount,
	});
	gameRoomClient.send(json);
}
window.customElements.define('crypto-game', CryptoGame);