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
        $token = wp_generate_auth_cookie( $user->ID, time() + DAY_IN_SECONDS*24, 'auth' );

        $return['auth'] = true;
        $return['token'] = $token;
    }

    return $return;    
}

function reputation_checkAuth( $args ) {
    global $wp_xmlrpc_server;
    $wp_xmlrpc_server->escape( $args );

    $token = $args;
    
    if ( wp_validate_auth_cookie($token, 'auth') ){        
        $return['auth'] = true;        
    } else {
        $return['auth'] = false;
    }

    return $return;    
}

function reputation_xmlrpc_methods( $methods ) {
    $methods['reputation.getAuth'] = 'reputation_getAuth';
    $methods['reputation.checkAuth'] = 'reputation_checkAuth';

    return $methods;   
}
add_filter( 'xmlrpc_methods', 'reputation_xmlrpc_methods');

?>
