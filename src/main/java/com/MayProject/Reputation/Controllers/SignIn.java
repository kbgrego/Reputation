package com.MayProject.Reputation.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.MayProject.Reputation.Core;
import com.MayProject.Reputation.Connection.Site;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class SignIn extends BorderPane implements Initializable {
	
	@FXML public FlowPane FlowPaneSignInForm;
	@FXML public Label LabelErrorMessage;
	@FXML public TextField TextFieldLogin;
	@FXML public PasswordField PasswordFieldPassowrd;
	@FXML public ProgressBar ProgressBarWaiting;
	
	@FXML public Label LabelCopyright;
	@FXML public Label LabelVersion;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		LabelCopyright.setText(Core.getCopyright());
		LabelVersion.setText(Core.getVersion());
		
		LabelErrorMessage.setText("");
		
		ProgressBarWaiting.setOpacity(0);
		
		fadeinAnimation(FlowPaneSignInForm, 3);	
		
	}

	private void fadeinAnimation(Node node, int time) {
		FadeTransition fade_in = new FadeTransition(Duration.seconds(time), node);
		fade_in.setFromValue(0);
		fade_in.setToValue(1);   
		fade_in.setCycleCount(1);
		fade_in.play();
	}
	
	private void fadeoutAnimation(Node node, int time) {
		FadeTransition fade_in = new FadeTransition(Duration.seconds(time), node);
		fade_in.setFromValue(1);
		fade_in.setToValue(0);   
		fade_in.setCycleCount(1);
		fade_in.play();
	}
	
	@FXML public void signIn(Event event) {	
		new runSignIn();
	}
	
	private static runSignIn isRun;
	private class runSignIn implements Runnable {
		
		runSignIn(){
			
			if(isRun==null)
				new Thread(isRun=this).start();

			LabelErrorMessage.setTextFill(Paint.valueOf("#555555"));
			LabelErrorMessage.setText("trying to connect");
			Site.setUser(TextFieldLogin.getText());
			Site.setPassword(PasswordFieldPassowrd.getText());
			
			fadeinAnimation(ProgressBarWaiting, 1);
		}

		@Override
		public void run() {
			try {
				
				boolean auth = Site.Auth();
				
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						LabelErrorMessage.setTextFill(Paint.valueOf(auth ? "#39d339" : "#d33939"));
						LabelErrorMessage.setText(auth ? "success" : "failuer");
					}					
				});											
			} catch (Exception e) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						LabelErrorMessage.setTextFill(Paint.valueOf("#d33939"));	
						LabelErrorMessage.setText(e.getMessage());
					}					
				});
			}
			
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					fadeoutAnimation(ProgressBarWaiting, 1);
					isRun = null;
				}					
			});	
		}
		
	}
	
}
