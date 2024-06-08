<?php 

$server = "localhost";
$user = "root";
$pass = "";
$bd = "roger_market_db";

//Creamos la conexión
$conexion = mysqli_connect($server, $user, $pass,$bd) 
or die("Ha sucedido un error inexperado en la conexion de la base de datos");

//generamos la consulta
$sql = "SELECT * FROM products";
mysqli_set_charset($conexion, "utf8"); //formato de datos utf8

if(!$result = mysqli_query($conexion, $sql)) die();

$products = array(); //creamos un array

while($row = mysqli_fetch_array($result)) 
{ 
    $id=$row['id'];
    $name=$row['name'];
    $amount=$row['amount'];
    $price=$row['price'];
    $type=$row['type'];
    $provider=$row['provider'];
    

    $products[] = array('id'=> $id, 'name'=> $name, 'amount'=> $amount,
                        'price'=> $price, 'type'=> $type, 'provider'=> $provider);

}
    
    
//desconectamos la base de datos
$close = mysqli_close($conexion) 
or die("Ha sucedido un error inexperado en la desconexion de la base de datos");
  

//Creamos el JSON
$json_string = "{products:" . json_encode($products) . "}";
echo $json_string;

//Si queremos crear un archivo json, sería de esta forma:
/*
$file = 'clientes.json';
file_put_contents($file, $json_string);
*/
    

?>
