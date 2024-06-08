For the authentication using a ip from your pc.
To find out your ip address open a cmd and write 
"ipconfig" and use the Wireless LAN adapter WI-FI
and use the IPv4 adrress and modifiy the next files:

LoginRequest 
private static final String LOGIN_REQUEST_URL="http://*replaces with your ip*/roger_market_db/login_app.php";

RegisterRequest:
private static final String REGISTER_REQUEST_URL="http://*replace with your ip*/roger_market_db/register_app.php";