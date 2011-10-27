<?php
/*
 * Created on Oct 26, 2011
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
 		echo '1';
 		return;
 	}
 	
 	if( !$db->query('SELECT FROM `users_devices` AS ud, `student_courses` AS sc, WHERE ud.device_id = ? AND ud.user_id = sc.student_id AND ?', array('device_id' => $device_id, 'course_id' => $course_id )) )
 	{
 		echo '2';
		return; 		
 	}
 	
 	$records = $db->fetch_assoc_all();
 	
 	// If they've already added the course, return with error code 3.
 	
 	if( $records )
	{
		echo '3';
		return;
	}
	
	if( !$db->query('SELECT FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $device_id )) )
 	{
 		echo '4';
		return; 		
 	}
 	
	$records = $db_fetch_assoc_all();
 	
	$db->insert('student_courses', array('student_id' => $records['user_id'], 'course_id' => $course_id, 'verified' => '0') );
	
	
?>
