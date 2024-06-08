<?php
/**
 * Actualizar un producto en la base de datos
 */
 
if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $server = "localhost";
    $user = "root";
    $pass = "";
    $bd = "roger_market_db";

    // Creamos la conexión
    $conexion = mysqli_connect($server, $user, $pass, $bd) 
    or die("Ha sucedido un error inesperado en la conexión de la base de datos");

    // Decodificando formato JSON
    $body = json_decode(file_get_contents('php://input'), true);

    // Sentencia UPDATE
    $sql = "UPDATE products SET " .
           "name = '" . $body['name'] . "', " .
           "amount = '" . $body['amount'] . "', " .
           "price = '" . $body['price'] . "', " .
           "type = '" . $body['type'] . "', " .
           "provider = '" . $body['provider'] . "' " .
           "WHERE id = " . $body['id'];

    if ($conexion->query($sql) === TRUE) {
        $json_string = json_encode(array("estado" => 1, "mensaje" => "Actualización correcta"));
    } else {
        $json_string = json_encode(array("estado" => 2, "mensaje" => "No se pudo actualizar el registro"));
    }

    $conexion->close();
} else {
    $json_string = json_encode(array("estado" => 2, "mensaje" => "No se recibió un comando POST"));
}

echo $json_string;
?>
