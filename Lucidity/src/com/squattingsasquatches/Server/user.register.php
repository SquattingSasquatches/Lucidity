<?php
	
	include 'return.codes.php';
	include 'init.php';
	
	global $db;
	
	extract( $_REQUEST );
	
	// If no name parameter is specified, return with error code.
	
	if( !isset( $name ) || !isset( $device_id ) ) 
	{
		echo MISSING_REQUIRED_PARAMETER;
		return;
	}
	
	if ( !$db->select('user_id','user_devices', 'device_id = ?', array($device_id) )
	{
		echo DATABASE_ERROR;
		return;
	}
	
	$records = $db->fetch_assoc_all();
	
	// UUID exists. Return with error code.
	
	if( !empty( $records ) )
	{
		echo USER_ALREADY_REGISTERED;
		return;
	}
	
	if ( !$db->insert('users', array( 'name' => $name ) ) )
	{

		echo DATABASE_ERROR;
		return;
	}
	
	$user_id = $db->insert_id();
	
	if( !$db->insert('user_devices', array( 'user_id' => $user_id, 'device_id' => $device_id ) ) )
	{	
		echo DATABASE_ERROR;
		return;
	}
	
	$db->close();
	
	echo SUCCESS;
?>