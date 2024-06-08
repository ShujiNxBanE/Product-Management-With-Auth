<?php
/**
 * Eliminar un producto de la base de datos
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

    // Sentencia DELETE
    $sql = "DELETE FROM products WHERE id = " . $body['id'];

    if ($conexion->query($sql) === TRUE) {
        $json_string = json_encode(array("estado" => 1, "mensaje" => "Eliminación correcta"));
    } else {
        $json_string = json_encode(array("estado" => 2, "mensaje" => "No se pudo eliminar el registro"));
    }

    $conexion->close();
} else {
    $json_string = json_encode(array("estado" => 2, "mensaje" => "No se recibió un comando POST"));
}

echo $json_string;
?>
