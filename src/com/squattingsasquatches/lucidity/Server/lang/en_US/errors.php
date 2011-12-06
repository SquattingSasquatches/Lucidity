<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 
 
 
/* Notices */
$this->messages['success'] 							= new Message( MessageType::Notice, 0, 'Success.');

/* Errors */
$this->messages['database_error'] 					= new Message( MessageType::Error, -1, 'Error connecting to remote database.');
$this->messages['no_user_id_found'] 				= new Message( MessageType::Error, 1, 'User not found.');
$this->messages['no_user_id_supplied'] 				= new Message( MessageType::Error, 2, 'Missing parameter: user_id.');
$this->messages['no_device_id_found'] 				= new Message( MessageType::Error, 3, 'Device not found.');
$this->messages['no_device_id_supplied'] 			= new Message( MessageType::Error, 4, 'Missing parameter: device_id.');
$this->messages['no_course_id_found'] 				= new Message( MessageType::Error, 5, 'Course not found.');
$this->messages['no_course_id_supplied'] 			= new Message( MessageType::Error, 6, 'Missing parameter: course_id.');
$this->messages['user_not_professor_of_course'] 	= new Message( MessageType::Error, 7, 'Permission denied. User is not a professor of specified course.');
$this->messages['no_student_course_pair_found']		= new Message( MessageType::Error, 8, 'User has not registered for course.');
$this->messages['no_name_supplied']					= new Message( MessageType::Error, 9, 'Missing parameter: name.');
$this->messages['student_already_exists']			= new Message( MessageType::Error, 10, 'Student already exists.');
$this->messages['device_id_already_exists']			= new Message( MessageType::Error, 11, 'Device already registered.');
$this->messages['course_already_registered']		= new Message( MessageType::Error, 12, 'Course already registered.');
$this->messages['no_student_id_supplied']			= new Message( MessageType::Error, 13, 'Missing parameter: student_id.');
$this->messages['no_lecture_name_supplied']			= new Message( MessageType::Error, 14, 'Missing parameter: lecture_name.');
$this->messages['no_c2dm_id_supplied']				= new Message( MessageType::Error, 15, 'Missing parameter: c2dm_id.');
$this->messages['no_uni_id_supplied']				= new Message( MessageType::Error, 16, 'Missing parameter: uni_id.');
$this->messages['user_not_professor_of_lecture']	= new Message( MessageType::Error, 17, 'Permission denied: User is not a professor of the specified lecture.');
$this->messages['user_not_professor_of_quiz']		= new Message( MessageType::Error, 18, 'Permission denied: User is not a professor of the specified quiz.');
$this->messages['no_quiz_id_supplied']				= new Message( MessageType::Error, 19, 'Missing parameter: quiz_id');
$this->messages['no_quiz_name_supplied']			= new Message( MessageType::Error, 20, 'Missing parameter: quiz_name');
$this->messages['no_quiz_duration_supplied']		= new Message( MessageType::Error, 21, 'Missing parameter: quiz_duration');
$this->messages['quiz_not_found']					= new Message( MessageType::Error, 22, 'Selected quiz does not exist.');
$this->messages['no_lecture_id_supplied']			= new Message( MessageType::Error, 23, 'Missing parameter: lecture_id');

$this->messages['no_question_id_supplied']			= new Message( MessageType::Error, 24, 'Missing parameter: question_id');
$this->messages['no_answer_id_supplied']			= new Message( MessageType::Error, 25, 'Missing parameter: answer_id');
$this->messages['no_quiz_result_id_supplied']		= new Message( MessageType::Error, 26, 'Missing parameter: quiz_result_id');

$this->messages['user_not_professor_of_question']	= new Message( MessageType::Error, 27, 'Permission denied: User is not a professor of the specified question.');
$this->messages['user_not_professor_of_answer']		= new Message( MessageType::Error, 28, 'Permission denied: User is not a professor of the specified answer.');

$this->messages['question_not_found']				= new Message( MessageType::Error, 29, 'Selected question does not exist.');
$this->messages['answer_not_found']					= new Message( MessageType::Error, 30, 'Selected answer does not exist.');
$this->messages['quiz_result_not_found']			= new Message( MessageType::Error, 31, 'Selected quiz result does not exist.');
$this->messages['no_section_id_supplied']			= new Message( MessageType::Error, 32, 'Missing parameter: section_id');

?>
