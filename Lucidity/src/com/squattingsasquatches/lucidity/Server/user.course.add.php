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
 	
 	if( !isset( $user_id ) )
 	{
 		$response->add('no_user_id_supplied', true);
 	}
 	
 	if( !isset( $course_ids ) )
 	{
 		$response->add('no_course_id_supplied', true);
 	}
 	
 	$course_ids = explode(',', $course_ids);
 	
 	
 	$db->select('course_id', 'student_courses', 'student_id = ? AND course_id IN ?', array($user_id, $db->implode($course_ids)));
 	
 	// Remove the courses they've already registered for
 	
 	if( $db->found_rows )
	{
		$found = $db->fetch_assoc_all();
		
		foreach ($found as $course_id_to_remove) {
			foreach ($course_ids as $i => $id) {
				if ($id == $course_id_to_remove)
					unset($course_ids[$i]);
			}
		}
		
		// If user is already registered for all the courses they're trying to register for
		if (count($courseIds) == 0)
			$response->add('course_already_registered', true);
	}
	
	
	// They haven't registered for the courses, so register them.
	
	foreach ($course_ids as $course_id)
		$db->insert('student_courses', array('student_id' => $user_id, 'course_id' => $course_id, 'verified' => '0') );
	
	$db->close();
	
	$response->send('success');
	
?>
