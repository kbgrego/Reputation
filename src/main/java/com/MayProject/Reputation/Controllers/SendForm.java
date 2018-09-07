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
	
	@FXML public FlowPane FlowPaneSendFormForm;
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
						
		LabelErrorMessage.setTextFill(Paint.valueOf("#d33939"));
		if (!validateData()) { LabelErrorMessage.setText("No all required fileds are filled."); return ; }
		if (!validatePass()) { LabelErrorMessage.setText("Passwords are not equal.");           return ; }
						
		login = TextFieldLogin.getText();
		password = PasswordFieldPassword.getText();
		FormToSend = prepairText();
		
		new runSendForm();
		
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

			LabelErrorMessage.setTextFill(Paint.valueOf("#555555"));
			LabelErrorMessage.setText("connecting ...");
			Site.setUser(login);
			Site.setPassword(password);
			
		}

		@Override
		public void run() {
			try {
				String result = Site.RegistrateAuth(FormToSend);
				boolean auth = result.equals("Confirm");
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						LabelErrorMessage.setTextFill(Paint.valueOf(auth ? "#39d339" : "#d33939"));
						LabelErrorMessage.setText(auth ? "success" : result);						
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
