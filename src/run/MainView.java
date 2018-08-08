package run;


import frontend.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;


/**
 * 此为主Stage，在这个基础上实现页面的切换
 *
 */
public class MainView extends Application{
	//1. 属性区

	@Override
	public void start(Stage primaryStage) throws Exception {
		new LoginView().show();		
	}
	
	//主程序入口
	public static void main(String[] args) {
		launch();
	}
}
