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
 	
 	if( !isset( $lecture_id ) )
 	{
 		$response->addError('no_lecture_id_supplied', true);
 	}
 	
 	
 	// Check to see if device is linked to a professor.
 	$db->query('SELECT * FROM `user_devices` AS ud, `lectures` AS l, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = l.course_id AND l.id = ?', array('device_id' => $device_id, 'lecture_id' => $lecture_id ));
 	
	if( !$db->found_rows )
	{
		// User not professor of lecture/course.
 		$response->addError('user_not_professor_of_course', true);
	}
	
	$db->select('*', 'quizzes', 'lecture_id  = ?', false, false, array( $lecture_id ) );
 	
 	$records = $db->fetch_assoc_all();

	$db->close();
	
	echo json_encode( $records );
	
?>
