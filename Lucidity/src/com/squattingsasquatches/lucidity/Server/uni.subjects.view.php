<?php
/*
 * Created on Nov 24, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: uni_id
 * 
 */
 include('class.controller.php');
 
class UniCoursesView extends Controller
{
 	function execute()
 	{
 		$this->db->query('select S.id as subject_id, S.short_name as subject_name from `subjects` as S on S.id in (select subject_id from `uni_subjects` where uni_id = "' . $this->params['uni_id'] . '")');
 	
	 	$records = $db->fetch_assoc_all();
	
		$this->addData( $records );
		
		$this->db->close();
 	}
 	
}
 

 
$controller = new UniCoursesView();

$controller->addValidation( 'uni_id', 'isParamSet', 'no_uni_id_supplied', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

	
?>