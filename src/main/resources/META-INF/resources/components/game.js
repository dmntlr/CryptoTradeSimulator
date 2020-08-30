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
    
	.confirmed {
		color : green;
	}
	
	.unconfirmed {
		color : red;	
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
    <button type='button' id='buy'>Buy</button>
    <button type='button' class='right' id='sell'>Sell</button>
  </form>
  <form>
  <label for='status'>Status:</label>
  <input type='text' id='status' readonly/>
  <label for='round'>Round:</label>
  <input type='text' id='round' readonly/>
  <label for='balance'>Balance:</label>
  <input type='text' id='balance' readonly/>
  <label for='amountBalance'>Amount:</label>
  <input type='text' id='amountBalance' readonly/>
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

    this.buyButton = this.shadowRoot.getElementById('buy');
    this.sellButton = this.shadowRoot.getElementById('sell');
    this.amount = this.shadowRoot.getElementById('amount');
    this.buy = this.buy.bind(this);
    this.sell = this.sell.bind(this);

  }

  set balance(balance) {
    this.shadowRoot.querySelector('#balance').value = balance + '$';
  }

  set crypto(crypto) {
    this.shadowRoot.querySelector('#crypto').value = crypto;
  }
  set amountBalance(amount) {
    this.shadowRoot.querySelector('#amountBalance').value = amount;
  }

  set price(price) {
    this.shadowRoot.querySelector('#price').value = price;
  }

 set round(round) {
    this.shadowRoot.querySelector('#round').value = round;
}

 set status(status) {
	var statusField = this.shadowRoot.querySelector('#status');
	if(status == true) {
		  statusField.value = 'Waiting for next Round.'
		  statusField.className = 'confirmed';
	} else if (status == false) {
		statusField.value = 'No transaction yet.'
		statusField.className = 'unconfirmed';
	}
  
}
	

  connectedCallback() {
    this.buyButton.addEventListener('click', this.buy);
    this.sellButton.addEventListener('click',this.sell);
  }

  buy() {
    this.sendTransaction(this.amount.value);
  }


  sell() {
    this.sendTransaction(-this.amount.value);
  }

  sendTransaction(amount) {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", "Bearer " + sessionStorage.getItem('jwt'));

    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
    };

    fetch("http://localhost:8080/transaction/" + amount, requestOptions)
      .then(result => result.json())
		.then(data =>  {
			this.balance = data.balance;
			this.amountBalance = data.amount;
			this.status = data.transacted;
			console.log(data);
		})
      .catch(error =>  {
		console.log('error', error)
	});
  }

}
window.customElements.define('crypto-game', CryptoGame);