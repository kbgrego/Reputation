package com.MayProject.Reputation.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.MayProject.Reputation.Core;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

public class SignIn extends BorderPane implements Initializable {
	
	@FXML public FlowPane FlowPaneSignInForm;
	@FXML public TextField TextFieldLogin;
	@FXML public PasswordField PasswordFieldPassowrd;
	@FXML public Label LabelCopyright;
	@FXML public Label LabelVersion;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		LabelCopyright.setText(Core.getCopyright());
		LabelVersion.setText(Core.getVersion());
		
		FadeTransition fade_in = new FadeTransition(Duration.seconds(3), FlowPaneSignInForm);
		fade_in.setFromValue(0);
		fade_in.setToValue(1);   
		fade_in.setCycleCount(1);
		fade_in.play();	
		
	}
}
