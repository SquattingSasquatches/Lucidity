<?php
/*
 * Created on Oct 26, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id
 * 
 */
 
	include('init.php');
	
	global $db;
	
	extract( $_REQUEST );
 
 	
 	/*
 	 * 		Parameter checks.
 	 */
 	
 	if( !isset( $device_id ) )
 	{
 		$response->add('no_device_id_supplied', true);
 	}
 	
 	
 	$db->query('SELECT * FROM `user_devices` AS ud, `student_courses` AS sc, `courses` AS c WHERE ud.device_id = "' . $device_id . '" AND ud.user_id = sc.student_id AND c.id = sc.course_id');
 	
 	$records = $db->fetch_assoc_all();

	echo json_encode( $records );
	
	$db->close();
	
?>