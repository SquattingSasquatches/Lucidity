<?php
/*
 * Created on Nov 4, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 
  
 	include('init.php');
	
	global $db;
	//$db->debug = true;
	extract( $_REQUEST );
 
 	
 	/*
 	 * 		Parameter checks.
 	 */
 	
 	if( !isset( $device_id ) )
 	{
 		$response->addError('no_device_id_supplied', true);
 	}
 	
 	if( !isset( $course_id ) )
 	{
 		$response->addError('no_course_id_supplied', true);
 	}
 	
 	if( !isset( $course_department_prefix ) )
 	{
 		$response->addError('no_course_department_prefix_supplied');
 	}
 	
 	if( !isset( $course_number ) )
 	{
 		$response->addError('no_course_number_supplied');
 	}
 	
 	$db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = ?', array('device_id' => $device_id, 'course_id' => $course_id ));
 	
	if( !$db->found_rows )
	{
		// No professor id found.
		$db->show_debug_console();
 		$response->addError('user_not_professor_of_course', true);
	}
 	
	
	
	
	$db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $device_id ));
 	
 	if( !$db->found_rows )
	{
		$response->addError('no_user_id_found', true);
 	}
	
	$records = $db->fetch_assoc_all();
	
	$db->show_debug_console();
	
	$db->close();
	
	$response->send('success');
	
?>
