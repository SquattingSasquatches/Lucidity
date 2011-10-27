<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id, course_id, student_id
 * 
 */
 	
 	include('init.php');
	
	global $db;
	$db->debug = true;
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
	
	if( !isset( $student_id ) )
 	{
 		$error->add('no_student_id_supplied', true);
 	}
 	
 	
 	
 	
	
	$db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = ?', array('device_id' => $device_id, 'course_id' => $course_id ));
 	
	if( $db->found_rows )
	{
		// No professor id found.
 		$error->add('user_not_professor_of_course', true);
	}
	
	$db->update('student_courses', array('verified' => '1'), 'course_id = ? AND student_id = ?', array($course_id, $student_id) );
	
	if( !$db->affected_rows )
	{
		// No course id found.
		$db->show_debug_console();
 		$error->add('no_student_course_pair_found', true);
	}
	
	$db->show_debug_console();
	
	$db->close();
?>
