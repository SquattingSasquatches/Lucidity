<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 
  
 	include('init.php');
	
	global $db;
	
	extract( $_REQUEST );
 
 	// Device id not supplied? Get outta here.
 	
 	if( !isset( $device_id ) )
 	{
 		// No device id supplied.
 		echo json_encode($errors['no_device_id_supplied']);
 		return;
 	}
 	
	
	if( !$db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $device_id )) )
 	{
 		// No user id found.
 		echo json_encode($errors['no_user_id_found']);
		return; 		
 	}
 	
	$records = $db_fetch_assoc_all();
 	
	$db->delete('student_courses', 'course_id = ? AND student_id = ?', array('course_id' => $course_id, 'student_id' => $records['user_id'] ) );
	
	echo '0';
	
?>
