<?php
/*
 * Created on Nov 21, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id
 * 
 */
 
	include('init.php');
	
	global $db;
 	
 	$db->query('SELECT * FROM `unis`');
 	
 	$records = $db->fetch_assoc_all();

	echo json_encode( $records );
	
	$db->close();
	
?>