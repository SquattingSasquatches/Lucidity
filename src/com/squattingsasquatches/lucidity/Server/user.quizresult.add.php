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
 
include('class.controller.php');
class AddQuizResult extends Controller
{
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params[$param_names[0]] ));
	 	
	 	if( !$records = $db->fetch_assoc_all() ) return false;
	 	return true;
	}
	function submittedWithinTimeLimit( $param_names )
	{
		global $config;
		$this->db->select('duration, start_time', 'quizzes', 'quiz_id = ?', array($this->params['quiz_id']));
		$row = $this->db->fetch_assoc();
		return ( time() < $row['start_time'] + $row['duration'] + $config['QUIZ_RESULTS_SEND_WINDOW'] );
	}
	function isStudentOfSection( $param_names )
	{
		$this->db->query(	'SELECT * ' .
							'FROM `quizzes` AS q, ' .
							'`lectures` AS l, ' .
							'`sections` AS s, ' .
							'`user_devices` AS ud, ' .
							'`users` AS u, ' .
							'`student_courses` AS sc, ' .
							'WHERE u.user_id = ud.user_id ' .
							'AND ud.device_id = ? ' .
							'AND l.lecture_id = q.lecture_id ' .
							'AND sc.student_id = u.user_id ' .
							'AND sc.section_id = l.section_id ' .
							'AND l.section_id = s.id ' .
							'AND q.id = ?',
							array('device_id' => $this->params['device_id'], 'quiz_id' => $this->params['quiz_id'] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	
	protected function onShowForm(){}
 	protected function onValid(){
 		
 		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array( $this->params['device_id'] ));
 		
 		$row = $this->db->fetch_assoc();
 		// Save quiz results without grade (Done because of foreign key constraints)
		$this->db->insert('quiz_results', 
	 				array( 	'student_id' 			=> $row['user_id'],
	 						'quiz_id' 				=> $this->params['quiz_id'],
	 						'grade'					=> 0
							
							) 
					);
		
		$quiz_result_id = $this->db->insert_id();
 		
 		// Retrieve the number of questions in the quiz. # of question_results is unreliable.
		$this->db->select('id', 'questions', 'quiz_id = ?', false, false, array($this->params['quiz_id'] ));
		
		$total = $db->found_rows;
		
		$correct = 0;
		foreach( $this->params['question_results'] as $question_result )
		{
			
			$question_result['quiz_result_id'] = $quiz_result_id;
			
			// Save question results.	
			$this->db->insert('question_results', $question_result);
			
			// Compare answers to the correct answers.
			$this->db->select('correct_answer_id', 'questions', 'question_id = ?', false, false, array( $question_result['question_id'] ) );
			
			$row = $this->db->fetch_assoc();
			
			if( $row['correct_answer_id'] == $question_result['selected_answer_id'])
				$correct++;
			
			$total++;
		}
		if ( $total > 0 )
			$grade = $correct/$total;
		else
			$grade = 0;
		
		$this->db->update('quiz_results', array( 'grade' => $grade ), 'id = ?', array($quiz_result_id));
		
		
 	}
 	protected function onInvalid(){
 	}
}


/* Main */

$controller = new AddQuizResult();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isParamSet', 'no_quiz_id_supplied', true );
$controller->addValidation( 'question_results', 'isParamSet', 'no_question_results_supplied', true );


//$controller->addValidation( 'device_id', 'userExists', 'no_user_id_found', true );
//$controller->addValidation( array( 'device_id', 'section_id' ), 'isStudentOfSection', 'user_not_student_of_section', true );
//$controller->addValidation( 'quiz_id', 'submittedWithinTimeLimit', 'quiz_time_limit_exceeded', true );

$controller->execute();

 	
 	
 	
 	
 	
 	
	
?>
