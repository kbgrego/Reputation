/* Copyright 2018 MayProject */
package com.MayProject.Reputation;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class Core extends Application {
	private static final String ICON_PNG         = "";
	private static final String APPLICATION_NAME = "Reputation";
	private static final String VERSION          = "V 1.0.0 EN";
	private static final String COPYRIGHT        = "© 2018 May project";
    
	public static Stage primaryStage;

	public static void main(String[] args) {	
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		initPrimaryStage(primaryStage);			
		initSignInView();		
		Core.primaryStage.show();		
	}

	public void initPrimaryStage(Stage primaryStage) {
		Core.primaryStage = primaryStage;
		/* Core.primaryStage.initStyle(StageStyle.UNIFIED); */ 
		/* Core.primaryStage.getIcons().add(new Image(ICON_PNG)); */
		Core.primaryStage.setTitle(APPLICATION_NAME);
		
		Core.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {			
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);				
			}
		});
	}

	public static void initSignInView() {
		try {
			Parent mainView = FXMLLoader.load(ClassLoader.getSystemResource("com/MayProject/Reputation/View/SignIn.fxml"));
			Scene scene = new Scene(mainView);
			scene.getStylesheets().add("com/MayProject/Reputation/Style/SignIn.css");					
			Core.primaryStage.setScene(scene);			
		} catch (IOException e) {
			
		}
	
	}
	
	public static void initSendFormView() {
		try {
			Parent mainView = FXMLLoader.load(ClassLoader.getSystemResource("com/MayProject/Reputation/View/sendform.fxml"));
			Scene scene = new Scene(mainView);
			scene.getStylesheets().add("com/MayProject/Reputation/Style/sendForm.css");					
			Core.primaryStage.setScene(scene);			
		} catch (IOException e) {
			
		}
	
	}
	
	public static void initReceivedFormView() {
		try {
			Parent mainView = FXMLLoader.load(ClassLoader.getSystemResource("com/MayProject/Reputation/View/receivedform.fxml"));
			Scene scene = new Scene(mainView);
			scene.getStylesheets().add("com/MayProject/Reputation/Style/receivedform.css");					
			Core.primaryStage.setScene(scene);			
		} catch (IOException e) {
			
		}
	
	}

	public static String getVersion(){
		return VERSION;
	}
	
	public static String getCopyright(){
		return COPYRIGHT;
	}

	public Window getPrimaryStage() {
		return primaryStage;
	}

    protected static void showVersion(Object source){
        try {
        	Parent root = FXMLLoader.load(source.getClass().getResource("View/AboutView.fxml"));
            Stage stage = new Stage();
            stage.getIcons().add(new Image(ICON_PNG));
            stage.setResizable(false);            
            stage.setTitle("About Reputation");
            stage.setScene(new Scene(root, 490, 234));
            stage.show(); 	   
        } catch (IOException e) {

        }		
	}

	protected static void showLicense(Object source) {
        try {
        	Parent root = FXMLLoader.load(source.getClass().getResource("View/LicenseView.fxml"));
            Stage stage = new Stage();
            stage.getIcons().add(new Image(ICON_PNG));
            stage.setResizable(false);            
            stage.setTitle("License");
            stage.setScene(new Scene(root, 700, 400));
            stage.show(); 	   
        } catch (IOException e) {

        }		
	}
}
