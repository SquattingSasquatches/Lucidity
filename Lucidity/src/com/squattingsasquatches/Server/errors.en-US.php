<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 
 $errors['no_user_id_found'] 				= array( 'id' => 1, 'message' => 'User not found.');
 $errors['no_user_id_supplied'] 			= array( 'id' => 2, 'message' => 'Missing parameter: user_id.');
 $errors['no_device_id_found'] 				= array( 'id' => 3, 'message' => 'Device not found.');
 $errors['no_device_id_supplied'] 			= array( 'id' => 4, 'message' => 'Missing parameter: device_id.');
 $errors['no_course_id_found'] 				= array( 'id' => 5, 'message' => 'Course not found.');
 $errors['no_course_id_supplied'] 			= array( 'id' => 6, 'message' => 'Missing parameter: course_id.');
 $errors['user_not_professor_of_course'] 	= array( 'id' => 7, 'message' => 'Permission denied. User is not a professor of specified course.');
 $errors['no_student_course_pair_found']	= array( 'id' => 8, 'message' => 'User has not registered for course.');
?>
