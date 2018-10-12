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

function reputation_registrateAuth( $args ) {
    global $wp_xmlrpc_server;
    $wp_xmlrpc_server->escape( $args );

    $username = $args[0];
    $password = $args[1];
    $form = $args[2];

    $return['auth'] = false;
        
    $user_id = username_exists( $username );
    if ( !$user_id and email_exists($user_email) == false ) {
        $user_id = wp_create_user( $username, $password, '');
        $user_id = wp_update_user( array( 'ID' => $user_id, 'role' => 'RepUser' ) );    
        add_user_meta( $user_id, 'form', $form);
        add_user_meta( $user_id, 'send', '');
		add_user_meta( $user_id, 'received', 'no'); 

        $return['auth'] = true;
        
    } else {        
        $return['ErrorMessage'] = __('User already exists.');
    }

    return $return;    
}

function reputation_receiveMessage( $args ) {
    global $wp_xmlrpc_server;
    $wp_xmlrpc_server->escape( $args );

    $token = $args;

    $user_id = wp_validate_auth_cookie($token, 'auth');
    
    if ( $user_id ){        
        $return['auth'] = true;    
        $return['message'] = get_user_meta($user_id, 'send')[0];
        if(empty(get_user_meta($user_id, 'received')))
            update_user_meta($user_id, 'received', 'yes');         
    } else {
        $return['auth'] = false;
    }

    return $return;    
}

add_filter( 'xmlrpc_methods', 'reputation_xmlrpc_methods');
function reputation_xmlrpc_methods( $methods ) {
    $methods['reputation.getAuth'] = 'reputation_getAuth';
    $methods['reputation.checkAuth'] = 'reputation_checkAuth';
    $methods['reputation.registrateAuth'] = 'reputation_registrateAuth';
    $methods['reputation.receiveMessage'] = 'reputation_receiveMessage';

    return $methods;   
}



add_action('admin_menu', 'reputation_plugin_setup_menu');
function reputation_plugin_setup_menu(){
    add_menu_page( 'Reputation console', 'Reputation', 'manage_options', 'reputation-plugin', 'main_view_init' );
}


add_action('admin_enqueue_scripts', 'admin_style');
function admin_style() {
  wp_enqueue_style('admin-styles', plugin_dir_url( __FILE__ ) .'/admin.css');
}


 
function main_view_init(){
    echo "<h1 class=\"reputation\" >Reputation console</h1>";

    ?>
    <h2>Registered users</h2>
    <table class="reputation">
        <tr>
            <th>User ID</th>            
            <th>login</th>
            <th>Registered</th>
            <th>Received</th>
            <th>Sending</th>
        </tr>

    <?
        $args['role'] = 'RepUser';
        foreach(get_users($args) as $v_user){
            $received = get_user_meta($v_user->ID, 'received')[0];
            echo '<form action="'.esc_url( admin_url('admin-post.php') ).'" method="post">';
            echo '<tr class="reputation">';
            $user_info = get_userdata($v_user->ID);
            echo '<td>' .  $v_user->ID . '</td>';
            echo '<td>' .  $user_info->user_login . '</td>';            
            echo '<td>' .  $user_info->user_registered . '</td>';
            echo '<td><textarea readonly>' .  get_user_meta($v_user->ID, 'form')[0] . '</textarea></td>';
            echo '<td><textarea name="send_text">' .  get_user_meta($v_user->ID, 'send')[0] . '</textarea></td>';
            echo '<td>' .  '<input type="submit" value="Save sending" />' . '</td>';
            if ($received=='yes') echo '<td>received</td>'; else echo '<td>not yet</td>';
            echo '<td>' .  '<a href="'. get_edit_user_link( $v_user->ID ) .'">'. 'view profile' .'</a>' . '</td>';
            echo '<input type="hidden" name="ID" value="'.$v_user->ID.'">';
            echo '<input type="hidden" name="action" value="send_text_form">';
            echo '</tr>';
            echo '</form>';
        }
    ?>
    </table>
    <?
}

add_action( 'admin_post_send_text_form', 'reputation_save_send_text' );
function reputation_save_send_text($args) {
  $user = get_userdata( $_POST['ID'] );
  if ( $user === false ) {
      //user id does not exist
  } else {
      update_user_meta( $_POST['ID'], 'send', $_POST['send_text']);
	  update_user_meta( $_POST['ID'], 'received', 'no');   
  }

  wp_redirect(admin_url('admin.php?page=reputation-plugin')); 
}


?>
                                    