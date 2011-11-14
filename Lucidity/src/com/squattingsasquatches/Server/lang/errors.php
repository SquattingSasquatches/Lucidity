<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */



 
 
 /* Notices */
 
 $this->messages['success'] 						= array( 'type' => 'notice', 'id' => 0, 'message' => 'Success.');
 
 
 
 
 
 
 
 
 /* Errors */
 
 $this->messages['database_error'] 					= array( 'type' => 'error', 'id' => -1, 'message' => 'Error connecting to remote database.');
 $this->messages['no_user_id_found'] 				= array( 'type' => 'error', 'id' => 1, 'message' => 'User not found.');
 $this->messages['no_user_id_supplied'] 			= array( 'type' => 'error', 'id' => 2, 'message' => 'Missing parameter: user_id.');
 $this->messages['no_device_id_found'] 				= array( 'type' => 'error', 'id' => 3, 'message' => 'Device not found.');
 $this->messages['no_device_id_supplied'] 			= array( 'type' => 'error', 'id' => 4, 'message' => 'Missing parameter: device_id.');
 $this->messages['no_course_id_found'] 				= array( 'type' => 'error', 'id' => 5, 'message' => 'Course not found.');
 $this->messages['no_course_id_supplied'] 			= array( 'type' => 'error', 'id' => 6, 'message' => 'Missing parameter: course_id.');
 $this->messages['user_not_professor_of_course'] 	= array( 'type' => 'error', 'id' => 7, 'message' => 'Permission denied. User is not a professor of specified course.');
 $this->messages['no_student_course_pair_found']	= array( 'type' => 'error', 'id' => 8, 'message' => 'User has not registered for course.');
 $this->messages['no_name_supplied']				= array( 'type' => 'error', 'id' => 9, 'message' => 'Missing parameter: name.');
 $this->messages['student_already_exists']			= array( 'type' => 'error', 'id' => 10, 'message' => 'Student already exists.');
 $this->messages['device_id_already_exists']		= array( 'type' => 'error', 'id' => 11, 'message' => 'Device already registered.');
 $this->messages['course_already_registered']		= array( 'type' => 'error', 'id' => 12, 'message' => 'Course already registered.');
 $this->messages['no_student_id_supplied']			= array( 'type' => 'error', 'id' => 13, 'message' => 'Missing parameter: student_id.');

?>
