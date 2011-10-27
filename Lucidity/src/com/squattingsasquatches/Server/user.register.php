<?php
	
	include('init.php');
	
	global $db;
	$db->debug = true;
	extract( $_REQUEST );
	
	// If no name parameter is specified, return with error code 1.
	
	if( !isset( $name ) ) 
	{
		echo '1';
		return;
	}
	
	if( !isset( $device_id ) ) 
	{
		echo '2';
		return;
	}
	
	$db->select('name','users', 'name = ?', array($name) );
	
	
	// If database encounters an error, return with error code 3.
	
	if( !$records = $db->fetch_assoc_all() )
	{
		echo '3';
		return;
	}
	
	// Name exists. Return with error code 4.
	
	if( !empty( $records ) )
	{
		echo '4';
		return;
	}
	
	$db->select('user_id','user_devices', 'device_id = ?', array($device_id) );
	
	
	// If database encounters an error, return with error code 4.
	
	if( !$records = $db->fetch_assoc_all() )
	{
		echo '4';
		return;
	}
	
	// UUID exists. Return with error code 5.
	
	if( !empty( $records ) )
	{
		echo '5';
		return;
	}
	
	if ( !$db->insert('users', array( 'name' => $name ) ) )
	{
		echo '6';
		return;
	}
	
	$user_id = $db->insert_id();
	
	if( !$db->insert('user_devices', array( 'user_id' => $user_id, 'device_id' => device_id ) ) )
	{	
		echo '7';
		return;
	}
	
	
	$db->show_debug_console();
	
	$db->close();
	
	echo '0';
?>