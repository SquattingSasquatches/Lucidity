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



class UserCheckin extends Controller
{
	
	function withinRadius( $param_names )
	{
		$this->db->query(
							'SELECT * ' .
							'FROM `sections` ' .
							'WHERE id = ?',
							array($this->params['section_id'])
							);
 	
 	
		$records = $this->db->fetch_assoc_all();
		
	 	return $this->calculateDistance( 	array('latitude' => $records[0]['checkin_lat'], 'longitude' => $records[0]['checkin_long']), 
	 											array('latitude' => $this->params['gps_lat'], 'longitude' => $this->params['gps_long']),
	 											'm' 
	 											) < $records[0]['checkin_radius'];
		
	}
	function calculateDistance( $point1, $point2, $uom = 'km' ) {
		//	Use Haversine formula to calculate the great circle distance
		//		between two points identified by longitude and latitude
		switch (strtolower($uom)) {
			case 'km'	:
				$earthMeanRadius = 6371.009; // km
				break;
			case 'm'	:
				$earthMeanRadius = 6371.009 * 1000; // km
				break;
			case 'miles'	:
				$earthMeanRadius = 3958.761; // miles
				break;
			case 'yards'	:
			case 'yds'	:
				$earthMeanRadius = 3958.761 * 1760; // miles
				break;
			case 'feet'	:
			case 'ft'	:
				$earthMeanRadius = 3958.761 * 1760 * 3; // miles
				break;
			case 'nm'	:
				$earthMeanRadius = 3440.069; // miles
				break;
		}
		$deltaLatitude = deg2rad($point2['latitude'] - $point1['latitude']);
		$deltaLongitude = deg2rad($point2['longitude'] - $point1['longitude']);
		$a = sin($deltaLatitude / 2) * sin($deltaLatitude / 2) +
			 cos(deg2rad($point1['latitude'])) * cos(deg2rad($point2['latitude'])) *
			 sin($deltaLongitude / 2) * sin($deltaLongitude / 2);
		$c = 2 * atan2(sqrt($a), sqrt(1-$a));
		$distance = $earthMeanRadius * $c;
		//echo $distance;
		return $distance;
	}
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params['device_id'] ));
	 	
	 	return $this->db->found_rows;
	}
	function sectionExists( $param_names )
	{
		$this->db->query('SELECT * FROM `sections` WHERE section_id = ?', array('id' => $this->params['section_id'] ));
	 	
	 	return $this->db->found_rows;
	}
	protected function onShowForm()
	{
	
		
	}
	
 	protected function onValid(){
 		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params['device_id'] ));
 		
 		$records = $this->db->fetch_assoc_all();
 		
 		$this->db->update('users', array('in_class' => 1), 'id = ?', array($records[0]['user_id']));
		
 	}
 	protected function onInvalid(){
 		
 	}
}


/* Main */

$controller = new UserCheckin();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'section_id', 'isParamSet', 'no_section_id_supplied', true );
//$controller->addValidation( 'device_id', 'userExists', 'no_user_id_found', true );
//$controller->addValidation( 'section_id', 'sectionExists', 'no_section_id_found', true );
$controller->addValidation( 'gps_lat', 'isParamSet', 'no_gps_lat_supplied', true );
$controller->addValidation( 'gps_long', 'isParamSet', 'no_gps_long_supplied', true );
$controller->addValidation( array('gps_lat', 'gps_long'), 'withinRadius', 'user_not_within_radius', true );

$controller->execute();

 	
 	
 	
 	
 	
 	
	
?>
