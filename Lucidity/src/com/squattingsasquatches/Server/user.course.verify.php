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
 	
	if( !isset( $course_id ) )
 	{
 		// No course id supplied.
 		echo json_encode($errors['no_course_id_supplied']);
 		return;
 	}
	
	if( !isset( $student_id ) )
 	{
 		// No student id supplied.
 		echo json_encode($errors['no_student_id_supplied']);
 		return;
 	}
	
	if( !$db->query('SELECT FROM `user_devices` AS ud, `professors` AS p, `courses` AS c WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = c.professor_id AND c.course_id = ?', array('device_id' => $device_id, 'course_id' => $course_id )) )
 	{
 		// No professor id found.
 		echo json_encode($errors['user_not_professor_of_course']);
		return; 		
 	}
 	
	$records = $db_fetch_assoc_all();
	
	if( !$db->update('student_courses', array('verified' => true), 'course_id = ? AND student_id = ?', array($course_id, $student_id) ))
	{
		// No course id found.
 		echo json_encode($errors['no_student_course_pair_found']);
		return;
	}
?>
