<?php
/*
Plugin Name: Reputation RPC manager
Plugin URI: http://may-project.pp.ua/
Description: Plagin to manage RPC requests
Author: Kabanov Gregory
Author URI: http://may-project.pp.ua/
Version: 0.1
*/

//return;

if ( ! defined( 'ABSPATH' ) ) { // prevent full path disclosure
	exit;
}

function reputation_getAuth( $args ) {
    global $wp_xmlrpc_server;
    $wp_xmlrpc_server->escape( $args );

    $username = $args[0];
    $password = $args[1];

    if ( ! $user = $wp_xmlrpc_server->login( $username, $password ) ){        
        $return['auth'] = false;
    } else {
        $guid = wp_generate_auth_cookie($user->ID, 120);

        $return['auth'] = true;
        $return['token'] = $guid;
    }

    return $return;    
}

function reputation_checkAuth( $args ) {
    global $wp_xmlrpc_server;
    $wp_xmlrpc_server->escape( $args );

    $cookie = $args[0];

    
    if ( wp_validate_auth_cookie($cookie) ){        
        $return['auth'] = false;
    } else {
        $return['auth'] = true;
    }

    return $return;    
}



function reputation_xmlrpc_methods( $methods ) {
    $methods['reputation.getAuth'] = 'reputation_getAuth';
    $methods['reputation.checkAuth'] = 'reputation_checkAuth';

    return $methods;   
}
add_filter( 'xmlrpc_methods', 'reputation_xmlrpc_methods');

function guidv4()
{
    if (function_exists('com_create_guid') === true)
        return trim(com_create_guid(), '{}');

    $data = openssl_random_pseudo_bytes(16);
    $data[6] = chr(ord($data[6]) & 0x0f | 0x40); // set version to 0100
    $data[8] = chr(ord($data[8]) & 0x3f | 0x80); // set bits 6-7 to 10
    return vsprintf('%s%s-%s-%s-%s-%s%s%s', str_split(bin2hex($data), 4));
}

?>
