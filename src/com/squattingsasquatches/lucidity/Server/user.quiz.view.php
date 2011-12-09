<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 include('class.controller.php');

class ViewQuiz extends Controller
{
	
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
	function isWithinTimeLimit( $param_names )
	{
		$this->db->select('duration, start_time', 'quizzes', 'quiz_id = ?', array($this->params['quiz_id']));
		
		$row = $this->db->fetch_assoc();
		
		return ( time() < $row['start_time'] + $row['duration'] && time() > $row['start_time'] );
	}
	protected function onShowForm(){}
 	protected function onValid(){
 		$this->db->select('*', 'quizzes', 'id  = ?', false, false, array( $this->params['quiz_id'] ) );
	 	
	 	$row = $this->db->fetch_assoc();
	 	
	 	$this->db->select('id', 'questions', 'quiz_id  = ?', false, false, array( $this->params['quiz_id'] ) );
	 	
	 	$row['num_of_questions'] = $this->db->found_rows;
	 	
	 	$this->response->addData( $row );
	 	
	}
 	protected function onInvalid(){
 	}
}


/* Main function */

$controller = new ViewQuiz();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isParamSet', 'no_quiz_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isWithinTimeLimit', 'quiz_not_accessible', true );
$controller->addValidation( array( 'device_id', 'section_id' ), 'isStudentOfSection', 'user_not_student_of_section', true );


$controller->execute();



?>
 
