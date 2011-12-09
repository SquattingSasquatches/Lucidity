<?php
include_once( dirname( __FILE__ ) . '/config.php');

// Load database class
include_once( dirname( __FILE__ ) . '/db/class.database.php');  

	error_reporting( E_ALL);
	ini_set("display_errors", 1);

class c2dmMessage
{
	
	public static function sendToSection( $id, $data )
	{
		global $config;
	    
		// 	Connect to database and set timeout.
		$db = new database();
		$db->connect($config['DB_HOST'], $config['DB_USER'], $config['DB_PASS'], $config['DB_NAME']);
		$db->maxQueryTime = 10;
	    $auth_code = self::getAuthCode();
		
		$db->query('SELECT c2dm_id FROM student_courses AS sc, users as u WHERE sc.section_id = ? AND sc.student_id = u.id', array($id));
		
		$records = $db->fetch_assoc_all();
		
		if( !$db->found_rows ) return;
		
		foreach( $records as $student )
		{
			self::send( $student['c2dm_id'], $data, $auth_code, "section" );
		}
		
		$db->close();
	}
	public static function sendToStudent( $id, $data )
	{
		global $config;
		// 	Connect to database and set timeout.
		$db = new database();
		$db->connect($config['DB_HOST'], $config['DB_USER'], $config['DB_PASS'], $config['DB_NAME']);
		$db->maxQueryTime = 10;
	    $auth_code = self::getAuthCode();
		
		$db->query('SELECT c2dm_id FROM `users` WHERE id = ?', array($id));
		
		$records = $db->fetch_assoc_all();
		
		
		echo $db->found_rows;
		
		if( !$db->found_rows ) return;
		
		$db->close();
		
		
		return self::send( $records[0]['c2dm_id'], $data, $auth_code, "student" );
		
		
	}
	public static function getNewAuthCode()
	{
	    global $config;
	    
		// 	Connect to database and set timeout.
		$db = new database();
		$db->connect($config['DB_HOST'], $config['DB_USER'], $config['DB_PASS'], $config['DB_NAME']);
		$db->maxQueryTime = 10;
	    
	    // get an authorization token
	    $ch = curl_init();
	    
	    if(!$ch){
	        return false;
	    }
	
	    curl_setopt($ch, CURLOPT_URL, "https://www.google.com/accounts/ClientLogin");
	    $post_fields = "accountType=" . urlencode('HOSTED_OR_GOOGLE')
	        . "&Email=" . urlencode($config['C2DM_EMAIL'])
	        . "&Passwd=" . urlencode($config['C2DM_PASS'])
	        . "&source=" . urlencode($config['C2DM_SOURCE'])
	        . "&service=" . urlencode($config['C2DM_SERVICE']);
	    curl_setopt($ch, CURLOPT_HEADER, true);
	    curl_setopt($ch, CURLOPT_POST, true);
	    curl_setopt($ch, CURLOPT_POSTFIELDS, $post_fields);
	    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	    curl_setopt($ch, CURLOPT_FRESH_CONNECT, true);    
	    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_ANY);
	    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	
	    // for debugging the request
	    curl_setopt($ch, CURLINFO_HEADER_OUT, true); // for debugging the request
	
	    $response = curl_exec($ch);
	
	    //var_dump(curl_getinfo($ch)); //for debugging the request
	    //var_dump($response);
	
	    curl_close($ch);
	
	    if (strpos($response, '200 OK') === false) {
	        return false;
	    }
	
	    // find the auth code
	    preg_match("/(Auth=)([\w|-]+)/", $response, $matches);
	
	    if (!$matches[2]) {
	        return false;
	    }
	    
		
		$db->update('c2dm_auth', array('auth_code' => $matches[2]), 'email = ?', array($config['C2DM_EMAIL']));
		$db->close();
		
		return $matches[2];
	}
	public static function getAuthCode()
	{
	    global $config;
	    
		// 	Connect to database and set timeout.
		$db = new database();
		$db->connect($config['DB_HOST'], $config['DB_USER'], $config['DB_PASS'], $config['DB_NAME']);
		$db->maxQueryTime = 10;
	    
		$db->select('c2dm_auth', 'auth_code', 'email = ?', array($config['C2DM_EMAIL']));
		$records = $db->fetch_assoc_all();
		$db->close();
		return $records[0];
	}
	public static function send( $registration_id, $inc_data, $auth_token, $collapse_key = "" ) {
		global $config;
	    
		// 	Connect to database and set timeout.
		$db = new database();
		$db->connect($config['DB_HOST'], $config['DB_USER'], $config['DB_PASS'], $config['DB_NAME']);
		$db->maxQueryTime = 10;
	    
		
		$resend = true;
		
		while( $resend )
		{
	        $headers = array('Authorization: GoogleLogin auth=' . $auth_token );
	        $data = array(
	            'registration_id' => $registration_id,
	            'collapse_key' => $collapse_key           
	        );
			foreach( $inc_data as $k => $d )
			{
				$data['data.' . $k] = $d; 
			}
			
	        $ch = curl_init();
	
	        curl_setopt($ch, CURLOPT_URL, "https://android.apis.google.com/c2dm/send");
	        if ($headers)
	            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
	        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	        curl_setopt($ch, CURLOPT_POST, true);
	        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	        
	        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
	
	
	        $response = curl_exec($ch);
	
	        curl_close($ch);
			
			if( strpos($response, "Error 401") !== false )
			{
				$auth_token = self::getNewAuthCode();
			}
			else
				$resend = false;
					
		}
		$db->close();
        return $response;
    }
	
}

?>