<?php
/*
 * Created on Oct 26, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: user_id
 * 
 */
 
	include('init.php');
	
	global $db;
	
	extract( $_REQUEST );
 
 	
 	/*
 	 * 		Parameter checks.
 	 */
 	
 	if( !isset( $user_id ) )
 	{
 		$response->add('no_user_id_supplied', true);
 	}
 	
 	
 	//$db->query('SELECT * FROM `user_devices` AS ud, `student_courses` AS sc, `courses` AS c WHERE ud.device_id = "' . $device_id . '" AND ud.user_id = sc.student_id AND c.id = sc.course_id');
 	$db->query('SELECT * FROM `courses` AS C inner join `student_courses` AS SC on SC.course_id = C.id WHERE SC.student_id = ?', array($user_id));
 	
 	$records = $db->fetch_assoc_all();

	echo json_encode( $records );
	
	$db->close();
	
?>