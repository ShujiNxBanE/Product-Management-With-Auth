<?php
/**
 * Insertar un nuevo cliente en la base de datos
 */
 
if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$server = "localhost";
	$user = "root";
	$pass = "";
	$bd = "roger_market_db";

	//Creamos la conexión
	$conexion = mysqli_connect($server, $user, $pass,$bd) 
	or die("Ha sucedido un error inexperado en la conexion de la base de datos");

    // Decodificando formato Json
    $body = json_decode(file_get_contents('php://input'), true);
    
    // Sentencia INSERT
    $sql = "INSERT INTO products ( " .
           "name, " . 
           "amount, " .
           "price, " .
           "type, " .
           "provider) " .
           " VALUES(" .
           "'". $body['name']    . "' ," .
           "'". $body['amount']      . "' ," .
           "'". $body['price']    . "' ," .
           "'". $body['type']     . "' ," .
           "'". $body['provider'] . "'" .
           ")";
	if($conexion->query($sql) === TRUE)
		$json_string = json_encode(array("estado" => 1,"mensaje" => "Creacion correcta"));
	else
		$json_string = json_encode(array("estado" => 2,"mensaje" => "No se creo el registro"));
		
	$conexion->close();
}
else
	$json_string = json_encode(array("estado" => 2,"mensaje" => "No se recibio un comando post"));
echo $json_string;
?>
