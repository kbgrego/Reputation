package com.MayProject.Reputation.Controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.MayProject.Reputation.Core;
import com.MayProject.Reputation.Connection.Site;
import com.MayProject.Reputation.Resourses.Resources;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class SendForm extends BorderPane implements Initializable {
	private static enum AlertType { WARNING, INFORMING, SUCCESS }
	
	@FXML public FlowPane FlowPaneSendForm;
	@FXML public GridPane GridPaneForm;
	@FXML public TextField TextFieldLogin;
	@FXML public PasswordField PasswordFieldPassword;
	@FXML public PasswordField PasswordFieldConfirmPassword;
	@FXML public TextField TextFieldFirstName;
	@FXML public Label LabelErrorMessage;
	
	@FXML public Label LabelCopyright;
	@FXML public Label LabelVersion;
	
	private List<Field> Fields;
	private String login;
	private String password;
	private String FormToSend;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		LabelCopyright.setText(Core.getCopyright());
		LabelVersion.setText(Core.getVersion());
				
		setAsRequired();
	
		loadFields();
		addfields();		
	}

	private void setAsRequired() {
		TextFieldLogin.setUserData(true);		
		PasswordFieldPassword.setUserData(true);
		PasswordFieldConfirmPassword.setUserData(true);
		TextFieldFirstName.setUserData(true);
	}
	
	private void loadFields() {
		String s;
		Fields = new ArrayList<>();
		InputStream fieldsSream = ClassLoader.getSystemResourceAsStream(Resources.FieldsCSV);
		try(Scanner file = new Scanner(fieldsSream)) {
			while(file.hasNext()&&(s=file.nextLine())!=null)
				if(s.split(";").length==2)
					Fields.add(new Field(s.split(";")[0].equals("y"),s.split(";")[1]));
			
		}
	}

	private void addfields() {
		int i = 4;
		for(Field field : Fields)	{
			TextField txField = new TextField();		
			txField.setPromptText(field.Name);
			txField.setUserData(field.isRequired);
			GridPaneForm.addRow(i++,txField);
			GridPane.setMargin(txField, new Insets(2, 0, 2, 0));
		}
		
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
	
	@FXML public void onSend(Event event) {
		if( isRun != null ) return ; 	
						
		setAlertText("", AlertType.INFORMING);
		if (!validateData()) { setAlertText("No all required fileds are filled.", AlertType.WARNING); return ; }
		if (!validatePass()) { setAlertText("Passwords are not equal.", AlertType.WARNING);           return ; }
						
		login = TextFieldLogin.getText();
		password = PasswordFieldPassword.getText();
		FormToSend = prepairText();
		
		new runSendForm();
		
	}
	
	@FXML public void backToSignIn (Event event) {
		Core.initSignInView();
	}

	private boolean validateData() {
		boolean res = true;
		for(Node node : GridPaneForm.getChildren()) {
			if(node instanceof TextField) {
				if( (Boolean)node.getUserData() && ((TextField)node).getText().isEmpty() ) {
					node.getStyleClass().add("err");
					res = false;
				} 
				
				if(!((TextField)node).getText().isEmpty())
					node.getStyleClass().remove("err");
			}
		}
		return res;
	}

	private boolean validatePass() {
		boolean res = PasswordFieldPassword.getText()
			  .equals(PasswordFieldConfirmPassword.getText());
		
		if(!res) {
			PasswordFieldPassword.getStyleClass().add("err");
			PasswordFieldConfirmPassword.getStyleClass().add("err");
		} else {
			PasswordFieldPassword.getStyleClass().remove("err");
			PasswordFieldConfirmPassword.getStyleClass().remove("err");
		}
		
		return res;   
	}

	private String prepairText() {
		StringBuffer buffer = new StringBuffer();
		for(Node node : GridPaneForm.getChildren()) {
			if(node instanceof TextField) {
				TextField field = (TextField) node;
				
				if(field.getPromptText().contains("password")) 
					continue;
				
				buffer.append(field.getPromptText())
				      .append(" : ")
				      .append(field.getText())
				      .append('\n');
			}
		}
		
		return buffer.toString();
	}
	
	private static runSendForm isRun;
	private class runSendForm implements Runnable {
		
		runSendForm(){
			
			if(isRun==null)
				new Thread(isRun=this).start();

			setAlertText("connecting ...", AlertType.INFORMING);
			Site.setUser(login);
			Site.setPassword(password);
			
		}

		@Override
		public void run() {
			try {
				String result = Site.RegistrateAuth(FormToSend);
				boolean confirm = result.equals("Confirmed");
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						setAlertText(confirm ? "Confirmed" : 
							                    result, 
								     confirm ? AlertType.SUCCESS : 
								    	    AlertType.WARNING);					
					}					
				});		
				
				if(confirm) {
					
					Timeline timeline = new Timeline();
					timeline.getKeyFrames().add(new KeyFrame(
							Duration.millis(3000), 
							ae -> fadeoutAnimation(FlowPaneSendForm, 1)));
					timeline.getKeyFrames().add(new KeyFrame(
					        Duration.millis(4000),
					        ae -> Core.initSignInView()));
					timeline.play();
					
					;
				}
				
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

	private static class Field {
		String Name;
		boolean isRequired;
		public Field(boolean required, String name) {
			isRequired = required;
			Name = name;
		}
	}
}	
