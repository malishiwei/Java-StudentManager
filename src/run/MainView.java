package run;


import frontend.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;


/**
 * ��Ϊ��Stage�������������ʵ��ҳ����л�
 *
 */
public class MainView extends Application{
	//1. ������

	@Override
	public void start(Stage primaryStage) throws Exception {
		new LoginView().show();		
	}
	
	//���������
	public static void main(String[] args) {
		launch();
	}
}
