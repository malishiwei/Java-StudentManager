package frontend.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Stage: 应用程序窗口
 * Scene: 包含页面的组件
 *
 */
public class LoginView extends Application{
	//1. 属性区
	private Stage stage;
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	

	//2. 构造方法区
	public LoginView() {
		this.stage = new Stage();
	}
	
	public LoginView(Stage stage) {
		this.stage = stage;
	}
	
	//3. 方法区
	@Override
	public void start(Stage arg0) throws Exception {
		// Read file ui and draw interface.
        Parent root = FXMLLoader.load(getClass()
                .getResource("/frontend/ui/fxml/Login.fxml"));
        stage.setTitle("Student Manager");
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false); 
	}
	
	public void show() throws Exception {
		start(stage);
	}
	
	public void close() {
		stage.close();
	}
}