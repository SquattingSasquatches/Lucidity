<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 class DeleteAnswer extends Controller
{
	function execute()
	{
	 	$db->delete('answers', 'id = ?', array($this->params['answer_id'] ) );
		$this->db->close();
	}
	function answerExists( $param_names )
	{
		$db->select('id', 'answers', 'id = ?', false, false, array( $this->params['answer_id'] ) );
 	
	 	if( !$db->found_rows ) return false;
	 	return true;
	}
	function isProfessorOfAnswer( $param_names )
	{
		$this->db->query(	'SELECT * FROM `user_devices` AS ud, ' .
							'`questions` AS qu, ' .
							'`quizzes` AS q, ' .
							'`professors` AS p, ' .
							'`courses` AS c, ' .
							'`professor_courses` AS pc, ' .
							'`lectures` AS l, ' .
							'`lecture_courses` AS lc ' .
							'`answers` AS a' . 
							'WHERE ud.device_id = ? ' .
							'AND p.user_id = ud.user_id ' .
							'AND p.user_id = pc.professor_id ' .
							'AND pc.course_id = c.id  ' .
							'AND c.id = lc.course_id ' .
							'AND lc.lecture_id = l.id ' .
							'AND l.id = q.lecture_id ' .
							'AND a.id = ? ' .
							'AND qu.id = a.question_id ' .
							'AND q.id = qu.quiz_id', 
							array('device_id' => $this->params[$param_names[0]], 'answer_id' => $this->params[$param_names[1]] ));
							
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	function showView()
	{
	 	$this->response->addData( $this->params );
	 	
		$this->response->send();
	}
	
}
 
 /* Main */

$controller = new DeleteAnswer();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'answer_id', 'isParamSet', 'no_answer_id_supplied', true );
$controller->addValidation( array( 'device_id', 'answer_id' ), 'isProfessorOfAnswer', 'user_not_professor_of_answer', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
 
  
?>
