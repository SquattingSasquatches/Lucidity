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
 		$response->add('no_device_id_supplied', true);
 	}
 	
 	if( !isset( $lecture_id ) )
 	{
 		$response->add('no_lecture_id_supplied', true);
 	}
 	
 	if( !isset( $quiz_name ) )
 	{
 		$response->add('no_quiz_name_supplied', true);
 	}
 	
 	if( !isset( $quiz_start_time ) )
 	{
 		$response->add('no_quiz_duration_supplied', true);
 	}
 	
 	
 	
 	
 	// Check to see if device is linked to a professor.
 	$db->query('SELECT * FROM `user_devices` AS ud, `lectures` AS l, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = l.course_id AND l.id = ?', array('device_id' => $device_id, 'lecture_id' => $lecture_id ));
 	
	if( !$db->found_rows )
	{
		// User not professor of lecture/course.
 		$response->add('user_not_professor_of_course', true);
	}
	
	
	$db->insert('quizzes', array(	'lecture_id' => $lecture_id, 
									'quiz_name' => $quiz_name, 
									'quiz_duration' => $quiz_duration ) );
	
	
	$db->show_debug_console();
	
	$db->close();
	
	$response->send('success');
?>
