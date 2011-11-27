<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 	
 	
 	include('init.php');
	
	global $db;
	
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
 	
 	if( !isset( $lecture_id ) )
 	{
 		$response->addError('no_lecture_id_supplied', true);
 	}
 	
 	if( !isset( $lecture_name ) )
 	{
 		$response->addError('no_lecture_name_supplied', true);
 	}
 	
 	
 	
 	// Check to see if device is linked to a professor.
 	$db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = ?', array('device_id' => $device_id, 'course_id' => $course_id ));
 	
	if( !$db->found_rows )
	{
		// No professor id found.
 		$response->addError('user_not_professor_of_course', true);
	}
	
	
	$db->update('lectures', array('course_id' => $course_id, 'lecture_name' => $lecture_name ), 'lecture_id = ?', array( $lecture_id ) );
	
	
	$db->show_debug_console();
	
	$db->close();
	
	$response->send();
?>
