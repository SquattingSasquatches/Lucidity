<?php
	
	include('init.php');
	
	global $db;
	ini_set('error_reporting', 1);
	error_reporting(E_ALL);
	
	$db->debug = true;
	$db->halt_on_errors = true;
	
	print_r( $db );
	extract( $_REQUEST );
	
	
 	/*
 	 * 		Parameter checks.
 	 */
	
	if( !isset( $name ) ) 
	{
		$error->add('no_name_supplied', true);
	}
	
	if( !isset( $device_id ) ) 
	{
		$error->add('no_device_id_supplied', true);
	}
	
	
	$db->select('name','users', 'name = ?', false, false, array($name) );
	
	if( $db->found_rows )
	{
		$error->add('student_already_exists', true);
	}
	
	$db->show_debug_console();
	
	
	$db->select('user_id','user_devices', 'device_id = ?', false, false, array($device_id) );
	
	if( $db->found_rows )
	{
		$error->add('device_id_already_exists', true);
	}
	
	$db->show_debug_console();
	
	$db->insert('users', array( 'name' => $name ));
	
	$user_id = $db->insert_id();
	
	$db->insert('user_devices', array( 'user_id' => $user_id, 'device_id' => $device_id ) );
	
	$db->show_debug_console();
	
	$db->close();
	
?>