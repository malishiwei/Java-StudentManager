package frontend.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Stage: Ӧ�ó��򴰿�
 * Scene: ����ҳ������
 *
 */
public class StudentView extends Application{
	
	//1. ������
	private Stage stage;

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	//2. ���췽����
	public StudentView() {
		this.stage = new Stage();
	}
	
	public StudentView(Stage stage) {
		this.stage = stage;
	}

	//3. ������
	@Override
	public void start(Stage primaryStage) throws Exception {
		 // Read file ui and draw interface.
        Parent root = FXMLLoader.load(getClass()
                .getResource("/frontend/ui/fxml/Student.fxml"));
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