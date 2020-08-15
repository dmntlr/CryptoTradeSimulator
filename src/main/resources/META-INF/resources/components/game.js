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
    <button type='button' id='buy'>Buy</button>
    <button type='button' class='right' id='sell'>Sell</button>
  </form>
  <form>
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
      .then(response => response.text())
      .then(result => this.balance = result)
      .catch(error => console.log('error', error));
  }

}
window.customElements.define('crypto-game', CryptoGame);