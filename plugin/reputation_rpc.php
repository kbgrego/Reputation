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


add_action('admin_menu', 'reputation_plugin_setup_menu');
function reputation_plugin_setup_menu(){
    add_menu_page( 'Reputation console', 'Reputation', 'manage_options', 'reputation-plugin', 'main_view_init' );
}
 
function main_view_init(){
    echo "<h1>Reputation console</h1>";

    ?>
    <h2>Registered users</h2>
    <table>
        <tr>
            <th>User ID</th>            
            <th>Username</th>
            <th>Name</th>
            <th>Registered</th>
        </tr>

    <?
        $args['role'] = 'RepUser';
        foreach(get_users($args) as $v_user){
            echo '<tr>';
            $user_info = get_userdata($v_user->ID);
            echo '<td>' .  get_user_meta($v_user->ID, 'rep_id')[0] . '</td>';
            echo '<td>' .  $user_info->user_login . '</td>';            
            echo '<td>' .  $user_info->user_firstname . ' ' . $user_info->user_lastname . '</td>';
            echo '<td>' .  $user_info->user_registered . '</td>';
            echo '<td>' .  '<a>view</a>' . '</td>';
            echo '<td>' .  '<a>delete</a>' . '</td>';
            echo '</tr>';
        }
    ?>
    </table>
    <?
}

?>
