<?php
   $con=mysqli_connect("158.69.192.249","root","","sportswhere");

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }
?>