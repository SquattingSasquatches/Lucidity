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
 	
 	if( !isset( $quiz_id ) )
 	{
 		$response->add('no_quiz_id_supplied', true);
 	}
 	
 	if( !isset( $quiz_name ) )
 	{
 		$response->add('no_quiz_name_supplied', true);
 	}
 	
 	if( !isset( $quiz_duration ) )
 	{
 		$response->add('no_quiz_duration_supplied', true);
 	}
 	
 	
 	
 	
 	// Check to see if device is linked to a professor.
 	$db->query('SELECT * FROM `user_devices` AS ud, `quizzes` AS q, `lectures` AS l, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = l.course_id AND l.id = q.lecture_id AND q.id = ?', array('device_id' => $device_id, 'quiz_id' => $quiz_id ));
 	
	if( !$db->found_rows )
	{
		// User not professor of lecture/course/quiz.
 		$response->add('user_not_professor_of_quiz', true);
	}
	
	
	$db->update('quizzes', array(	'lecture_id' => $lecture_id, 
									'quiz_name' => $quiz_name,
									'quiz_duration' => $quiz_duration  ), 
				'quiz_id = ?', array( $quiz_id ) );
	
	
	$db->show_debug_console();
	
	$db->close();
	
	$response->send('success');
?>
s