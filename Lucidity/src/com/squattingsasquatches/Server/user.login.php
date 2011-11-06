<?php
	include 'init.php';
	
	global $db;
	
	extract( $_REQUEST );
	
	// If no device_id parameter is specified, return with error code.
	
	if( !isset( $device_id ) ) 
	{
		$error->add('no_device_id_supplied', true);
	}
	
	$result = $db->select('user_id','user_devices', 'device_id = ?', '', '', array($device_id) );
	
	if( $result === false )
	{
		$error->add('database_error', true);
	}
	
	$records = $db->fetch_assoc_all();
	
	// UUID not in DB
	
	if( empty( $records ) )
	{
		$error->add('no_user_id_found', true);
	}
	
	$db->close();
	
	$error->add('success');
?>