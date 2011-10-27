<?php
	
	include('init.php');
	
	global $db;
	
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
	
	$db->select('name','users', 'criteria = ?', array($name) );
	
	
	// If database encounters an error, return with error code 2.
	
	if( !$records = db_fetch_assoc_all() )
	{
		echo '3';
		return;
	}
	
	// Name exists. Return with error code 3.
	
	if( !empty( $records ) )
	{
		echo '4';
		return;
	}
	
	$db->select('device_id','user_devices', 'criteria = ?', array($device_id) );
	
	
	// If database encounters an error, return with error code 4.
	
	if( !$records = db_fetch_assoc_all() )
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
	
	if ( !$db->insert('name', array( 'name' => $name ) ) )
	{
		echo '6';
		return;
	}
	
	$user_id = $db->insert_id();
	
	if( !$db->insert('user_id, device_id', array( 'user_id' => $user_id, 'device_id' => device_id ) ) )
	{	
		echo '7';
		return;
	}
	
	
	$db->show_debug_console();
	
	$db->close();
	
	echo '0';
?>