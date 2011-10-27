<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id, course_id
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
 	
 	
 	
	
	$db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $device_id ));
 	
 	if( !$records = $db_fetch_assoc_all() )
	{
		$error->add('no_user_id_found', true);
 	}
	
 	
	$db->delete('student_courses', 'course_id = ? AND student_id = ?', array('course_id' => $course_id, 'student_id' => $records['user_id'] ) );
	
	echo '0';
	
?>
