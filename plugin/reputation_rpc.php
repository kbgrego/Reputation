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

function reputation_getUserID( $args ) {
    global $wp_xmlrpc_server;
    $wp_xmlrpc_server->escape( $args );

    $username = $args[0];
    $password = $args[1];

    if ( ! $user = $wp_xmlrpc_server->login( $username, $password ) )
        return $wp_xmlrpc_server->error;

    return $user->ID;    
}

function reputation_getAuth( $args ) {
    global $wp_xmlrpc_server;
    $wp_xmlrpc_server->escape( $args );

    $username = $args[0];
    $password = $args[1];
    $guid = $args[2];

    if ( ! $user = $wp_xmlrpc_server->login( $username, $password ) ){        
        $return['auth'] = false;
    } else {
        $new_key_pair = openssl_pkey_new(array(
            "private_key_bits" => 2048,
            "private_key_type" => OPENSSL_KEYTYPE_RSA,
        ));

        openssl_pkey_export($new_key_pair, $private_key_pem);

        openssl_sign($guid, $signature, $private_key_pem, OPENSSL_ALGO_SHA256);

        $return['auth'] = true;
        $return['signature'] = base64_encode($signature);
    }



    return $return;    
}

function reputation_xmlrpc_methods( $methods ) {
    $methods['reputation.getUserID'] = 'reputation_getUserID';
    $methods['reputation.getAuth'] = 'reputation_getAuth';
    return $methods;   
}
add_filter( 'xmlrpc_methods', 'reputation_xmlrpc_methods');

?>
