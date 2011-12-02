<?php

/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 class EditLecture extends Controller
{
	function execute()
	{
		$db->update('	lectures',
		 
						array( 	'course_id' 	=> 	$this->params['course_id'], 
								'lecture_name' 	=> 	$this->params['lecture_name'] ), 
						
						'lecture_id = ?',
						 
						array( $this->params['lecture_id'] ) );
						
						
		$this->db->close();
	}
	function lectureExists( $param_names )
	{
		$db->select('course_id', 'lectures', 'id = ?', false, false, array( $this->params['lecture_id'] ) );
 	
	 	if( !$db->found_rows ) return false;
	 	return true;
	}
	function isProfessorOfLecture( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p, `courses` AS c, `professor_courses` AS pc, `lectures` as l, `lecture_courses` as lc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = lc.course_id AND lc.lecture_id = ?', array('device_id' => $this->params[$param_names[0]], 'lecture_id' => $this->params[$param_names[1]] ));
 	
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

$controller = new EditLecture();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );
$controller->addValidation( 'lecture_name', 'isParamSet', 'no_lecture_name_supplied', true );
$controller->addValidation( 'lecture_id', 'isParamSet', 'no_lecture_id_supplied', true );
$controller->addValidation( array( 'device_id', 'lecture_id'), 'isProfessorOfLecture', 'user_not_professor_of_lecture', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
 	
?>
