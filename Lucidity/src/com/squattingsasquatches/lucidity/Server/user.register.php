<?php
	
	include('init.php');
	
	global $db;
	
	extract( $_REQUEST );
	
	
 	/*
 	 * 		Parameter checks.
 	 */
	
	if( !isset( $name ) ) 
	{
		$response->add('no_name_supplied', true);
	}
	
	if( !isset( $device_id ) ) 
	{
		$response->add('no_device_id_supplied', true);
	}
	
	
	/*$db->select('name','users', 'name = ?', false, false, array($name) );
	
	if( $db->found_rows )
	{
		$response->add('student_already_exists', true);
	}*/
	
	
	$db->select('user_id','user_devices', 'device_id = ?', false, false, array($device_id) );
	
	if( $db->found_rows )
	{
		$response->add('device_id_already_exists', true);
	}
	
	$db->insert('users', array( 'name' => $name ));
	
	$user_id = $db->insert_id();
	
	$db->insert('user_devices', array( 'user_id' => $user_id, 'device_id' => $device_id ) );
	
	$db->close();
	
	$response->send('success');	
?>