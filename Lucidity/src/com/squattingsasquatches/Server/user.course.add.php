<?php
/*
 * Created on Oct 26, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id, course_id
 * 
 */
 
 	include('init.php');
	
	global $db;
	
	extract( $_REQUEST );
 
 	
 	
 	/*
 	 * 		Parameter checks.
 	 */
 	
 	if( !isset( $device_id ) )
 	{
 		$error->add('no_device_id_supplied', true);
 	}
 	
 	if( !isset( $course_id ) )
 	{
 		$error->add('no_course_id_supplied', true);
 	}
 	
 	
 	
 	
 	
 	
 	
 	$db->query('SELECT * FROM `users_devices` AS ud, `student_courses` AS sc, WHERE ud.device_id = ? AND ud.user_id = sc.student_id AND ?', array('device_id' => $device_id, 'course_id' => $course_id ));
 	
 	
 	
 	// If they've already added the course, return with error code 3.
 	
 	if( $records = $db->fetch_assoc_all() )
	{
		$error->add('course_already_registered', true);
	}
	
	
	// They haven't registered for the course. Fetch user id, and register them.
	
	$db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $device_id ));
 	
 	if( !$records = $db->fetch_assoc_all() )
	{
		$error->add('no_user_id_found', true);
	}
	
	$db->insert('student_courses', array('student_id' => $records['user_id'], 'course_id' => $course_id, 'verified' => '0') );
	
	
?>
