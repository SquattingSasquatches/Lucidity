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
 	
 	
 	
 	
	
	$db->query('SELECT FROM `user_devices` AS ud, `professors` AS p, `courses` AS c WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = c.professor_id AND c.course_id = ?', array('device_id' => $device_id, 'course_id' => $course_id ));
 	
	if( !$records = $db_fetch_assoc_all() )
	{
		// No professor id found.
 		$error->add('user_not_professor_of_course', true);
	}
	
	$db->update('student_courses', array('verified' => true), 'course_id = ? AND student_id = ?', array($course_id, $student_id) );
	
	if( !$db->affected_rows )
	{
		// No course id found.
 		$error->add('no_student_course_pair_found', true);
	}
	
	echo '0';
?>
