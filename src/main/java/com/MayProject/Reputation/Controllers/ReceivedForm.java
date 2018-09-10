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
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class ReceivedForm extends BorderPane implements Initializable {
	private static enum AlertType { WARNING, INFORMING, SUCCESS }
	
	@FXML public TextArea TextAreaReceived;
	@FXML public Label LabelErrorMessage;
	
	@FXML public Label LabelCopyright;
	@FXML public Label LabelVersion;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		LabelCopyright.setText(Core.getCopyright());
		LabelVersion.setText(Core.getVersion());
					
		TextAreaReceived.setText("");
		new runReceiveMessage();
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
	
	@FXML public void onRefresh(Event event) {
		new runReceiveMessage();
	}
	
	@FXML public void onExit(Event event) {
		Site.clear(); 
		Core.initSignInView();
	}
	
	private static runReceiveMessage isRun;
	private class runReceiveMessage implements Runnable {
		
		runReceiveMessage(){
			
			if(isRun==null)
				new Thread(isRun=this).start();

			setAlertText("connecting ...", AlertType.INFORMING);
			
		}

		@Override
		public void run() {
			try {
				String message = Site.ReceiveMessage();
				boolean confirm = !message.isEmpty();
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						setAlertText(confirm ? "received" : 
							                   "not received", 
								     confirm ? AlertType.SUCCESS : 
								    	    AlertType.WARNING);
						TextAreaReceived.setText(message);
					}					
				});	
										
			} catch (Exception e) {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						System.out.println(e.getMessage());
						setAlertText("Fail to connect", AlertType.WARNING);
					}					
				});
			}
			
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					isRun = null;
				}					
			});	
		}
		
	}

}	
