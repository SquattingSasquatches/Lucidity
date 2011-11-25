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
 
	include('init.php');
	
	global $db;
	
	extract( $_REQUEST );
 
 	
 	/*
 	 * 		Parameter checks.
 	 */
 	
 	if( !isset( $uni_id ) )
 	{
 		$response->add('no_uni_id_supplied', true);
 	}
 	
 	
 	$db->query('select S.id as subject_id, S.short_name as subject_name, C.course_number, C.id as course_id from `courses` as C inner join `subjects` as S on S.id in (select subject_id from `uni_subjects` where uni_id = "' . $uni_id . '")');
 	
 	$records = $db->fetch_assoc_all();

	echo json_encode( $records );
	
	$db->close();
	
?>