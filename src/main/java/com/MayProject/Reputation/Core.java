/* Copyright 2018 MayProject */
package com.MayProject.Reputation;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Core extends Application {
	private static final String ICON_PNG         = "";
	private static final String APPLICATION_NAME = "Reputation";
	private static final String VERSION          = "V 1.0.0 EN";
	private static final String COPYRIGHT        = "© 2018 May project";
    
	public static Stage primaryStage;
	private static SimpleObjectProperty<Parent> mainView;

	public static void main(String[] args) {	
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		initPrimaryStage(primaryStage);			
		initSignInView();		
		Core.primaryStage.show();		
		/* initMainView(); */		
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

	private void initSignInView() {
		try {
			Parent mainView = FXMLLoader.load(ClassLoader.getSystemResource("com/MayProject/Reputation/View/SignIn.fxml"));
			Scene scene = new Scene(mainView);
			scene.getStylesheets().add("com/MayProject/Reputation/Style/SignIn.css");	
			Core.primaryStage.setMaximized(true);			
			Core.primaryStage.setScene(scene);			
		} catch (IOException e) {
			
		}
	
	}

	public static void initMainView() {
		mainView = new SimpleObjectProperty<Parent>();
		new MainLoader(); 
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

	private static class MainLoader extends Thread {
		MainLoader(){
			this.start();
		}
		
		public void run(){
			try {
				mainView.set(FXMLLoader.load(ClassLoader.getSystemResource("com/MayProject/Reputation/View/sendform.fxml")));
	            Platform.runLater(new Runnable() {
	                @Override public void run() {
	                	Scene scene=new Scene(mainView.getValue(),
					                			Core.primaryStage.getScene().getWidth(),
					                			Core.primaryStage.getScene().getHeight());
	                	scene.getStylesheets().add("com/MayProject/Reputation/Style/sendform.css");	 
	                	FadeTransition fade_out = new FadeTransition(Duration.seconds(1), Core.primaryStage.getScene().getRoot());
	                	fade_out.setFromValue(1);
	                	fade_out.setToValue(0);
                		FadeTransition fade_in = new FadeTransition(Duration.seconds(1), scene.getRoot());
                		fade_in.setFromValue(0);
                		fade_in.setToValue(1);                		
	                	fade_out.setOnFinished(e -> {
	                		scene.getRoot().setOpacity(0);
	                		Core.primaryStage.setScene(scene);
		                	fade_in.play();	                		
	                	});
	                	fade_out.play();
	                }	                
	            });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
