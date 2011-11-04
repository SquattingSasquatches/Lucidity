<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 $this->errors['database_error'] 				= array( 'id' => -1, 'message' => 'Error connecting to remote database.');
 $this->errors['no_user_id_found'] 				= array( 'id' => 1, 'message' => 'User not found.');
 $this->errors['no_user_id_supplied'] 			= array( 'id' => 2, 'message' => 'Missing parameter: user_id.');
 $this->errors['no_device_id_found'] 			= array( 'id' => 3, 'message' => 'Device not found.');
 $this->errors['no_device_id_supplied'] 		= array( 'id' => 4, 'message' => 'Missing parameter: device_id.');
 $this->errors['no_course_id_found'] 			= array( 'id' => 5, 'message' => 'Course not found.');
 $this->errors['no_course_id_supplied'] 		= array( 'id' => 6, 'message' => 'Missing parameter: course_id.');
 $this->errors['user_not_professor_of_course'] 	= array( 'id' => 7, 'message' => 'Permission denied. User is not a professor of specified course.');
 $this->errors['no_student_course_pair_found']	= array( 'id' => 8, 'message' => 'User has not registered for course.');
 $this->errors['no_name_supplied']				= array( 'id' => 9, 'message' => 'Missing parameter: name.');
 $this->errors['student_already_exists']		= array( 'id' => 10, 'message' => 'Student already exists.');
 $this->errors['device_id_already_exists']		= array( 'id' => 11, 'message' => 'Device already registered.');
 $this->errors['course_already_registered']		= array( 'id' => 12, 'message' => 'Course already registered.');
 $this->errors['no_student_id_supplied']		= array( 'id' => 13, 'message' => 'Missing parameter: student_id.');
/*$this->errors['user_not_professor_of_course']	= array( 'id' => 14, 'message' => 'User is not a professor of the course.');
 $this->errors['no_student_course_pair_found']	= array( 'id' => 15, 'message' => 'User is not registered for the course.');*/
 // ^ duplicates
?>
