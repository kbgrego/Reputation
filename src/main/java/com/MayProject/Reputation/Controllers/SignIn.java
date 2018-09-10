package com.MayProject.Reputation.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.MayProject.Reputation.Core;
import com.MayProject.Reputation.Connection.Site;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
	private static enum AlertType { WARNING, INFORMING, SUCCESS }	
	
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
	
	private void setAlertText(String text,  AlertType type) {
		Paint color=Paint.valueOf("#000000");
		LabelErrorMessage.setText(text);
		
		switch(type) {
			case WARNING:   color = Paint.valueOf("#D33939"); break;
			case INFORMING: color = Paint.valueOf("#555555"); break;
			case SUCCESS:   color = Paint.valueOf("#559977"); break;
		}
		
		LabelErrorMessage.setTextFill(color);
	}
	
	@FXML public void signIn(Event event) {	
		new runSignIn();
	}
	
	@FXML public void onShowSendForm(Event event) {
		Core.initSendFormView();
	}
	
	private static runSignIn isRun;
	private class runSignIn implements Runnable {
		
		runSignIn(){
			
			if(isRun==null)
				new Thread(isRun=this).start();

			setAlertText("connecting ...", AlertType.INFORMING);
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
						setAlertText(auth ? "" : "invalid login or password", 
								     auth ? AlertType.SUCCESS : AlertType.WARNING);
					}					
				});		
				
				if(auth) {
					
					Timeline timeline = new Timeline();
					timeline.getKeyFrames().add(new KeyFrame(
							Duration.millis(0), 
							ae -> fadeoutAnimation(FlowPaneSignInForm, 1)));
					timeline.getKeyFrames().add(new KeyFrame(
					        Duration.millis(2000),
					        ae -> Core.initReceivedFormView()));
					timeline.play();
					
					;
				}
			} catch (Exception e) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						System.out.println(e.getMessage());
						setAlertText("Connection fail.", AlertType.WARNING);
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
