<?php
	
	include 'return.codes.php';
	include 'init.php';
	
	global $db;
	
	extract( $_REQUEST );
	
	// If no device_id parameter is specified, return with error code.
	
	if( !isset( $device_id ) ) 
	{
		echo MISSING_REQUIRED_PARAMETER;
		return;
	}
	
	$db->select('user_id','user_devices', 'device_id = ?', array($device_id) );
	
	$records = $db->fetch_assoc_all();
	
	// UUID not in DB
	
	if( empty( $records ) )
	{
		echo USER_NOT_REGISTERED;
		return;
	}
	
	$db->close();
	
	echo SUCCESS;
?>