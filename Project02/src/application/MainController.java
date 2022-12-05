package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import domain.JDBCUtil;

public class MainController {
	public static String dataId = "";
	
	@FXML
	private Button btnLogin;

	@FXML
	private Button eads;

	@FXML
	private Button bf;

	@FXML
	private Button btnRegister;

	@FXML
	private CheckBox yes;

	@FXML
	private CheckBox no;

	@FXML
	private CheckBox sick;

	@FXML
	private ProgressBar yesBar;

	@FXML
	private ProgressBar noBar;

	@FXML
	private ProgressBar sickBar;

	@FXML
	private TextArea note;

	@FXML
	private TextField id;

	@FXML
	private TextField pw;

	@FXML
	private TextField age;

	@FXML
	private TextField name;

	@FXML
	private TextField ne;

	@FXML
	private TextField sc;
	
	static String loginId = "s";
	public JDBCUtil db;
	static String stageL = "";

	@FXML
	public void initialize() {
		db = new JDBCUtil();
		if (stageL.equals("main")) {
			Connection con = db.getConnection();
			if (con == null) {
				System.out.println("DB연결 오류 발생");
				return;
			}
			
			PreparedStatement pstmt = null;// n = 회원가입 관련된 데이터 베이스 이름
			ResultSet rs = null;
			String sql = "SELECT `memo`,`yes`,`no`,`sick` FROM `ui` WHERE `ID`= ?";
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, loginId);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					String memo = rs.getString("memo");
					int y = rs.getInt("yes");
					int n = rs.getInt("no");
					int s = rs.getInt("sick");
					note.setText(memo);
					yesBar.setProgress(y);
					noBar.setProgress(n);
					sickBar.setProgress(s);
					if (y == 1) {
						yes.setSelected(true);
					}
					if (n == 1) {
						no.setSelected(true);
					}
					if (s == 1) {
						sick.setSelected(true);
					}
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			
		}
		

	}
	public void register() {
		// alert("회원가입 성공", null);
		JDBCUtil db = new JDBCUtil();
		Connection con = db.getConnection();
		if (con == null) {
			System.out.println("DB연결 오류 발생");
			return;
		}

		PreparedStatement pstmt = null;// n = 회원가입 관련된 데이터 베이스 이름
		String sql = "insert into users(`name`, `age`, `id`, `password`) values(?,?,?,?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name.getText());
			pstmt.setInt(2, Integer.parseInt(age.getText()));
			pstmt.setString(3, id.getText());
			pstmt.setInt(4, Integer.parseInt(pw.getText()));
			insMemo(id.getText());
			int cnt = pstmt.executeUpdate();
			if (cnt == 0) {
				System.out.println("데이터 삽입 실패");
				return;
			}
			
			System.out.println("삽입 성공!!");

		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			alert("회원가입 실패", null);
			System.out.println("삽입 실패!");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("삽입 실패!");
			return;
		}

		try {
			Parent login = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(login);
			Stage primaryStage = (Stage) btnRegister.getScene().getWindow(); // 현재 윈도우 가져오기
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String j;

	public void login() {
		String NE = ne.getText();
		String SC = sc.getText();
		JDBCUtil db = new JDBCUtil();
		Connection con = db.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * from users where id = '" + NE + "' AND password = '" + SC + "'";

		try {

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);

			while (rs.next()) {
				dataId = rs.getString("id");
				String datapass = rs.getString("password");

				System.out.println(dataId + "," + datapass);
				j = datapass;

				System.out.println("로그인 성공!!");
				loginId = dataId;
				stageL = "main";
				try {
					// 값이 같냐
					Parent login = FXMLLoader.load(getClass().getResource("UI.fxml"));
					Scene scene = new Scene(login);
					Stage primaryStage = (Stage) btnLogin.getScene().getWindow(); // 현재 윈도우 가져오기
					primaryStage.setScene(scene);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("로그인 실패!");
			return;
		}
	}

	public void rsedg() {

		try {
			Parent login = FXMLLoader.load(getClass().getResource("Register.fxml"));
			Scene scene = new Scene(login);
			Stage primaryStage = (Stage) eads.getScene().getWindow(); // 현재 윈도우 가져오기
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ytg() {
		alert("로그아웃", null);
		try {
			Parent login = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(login);
			Stage primaryStage = (Stage) bf.getScene().getWindow(); // 현재 윈도우 가져오기
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void insMemo(String id) {
		db = new JDBCUtil();
		Connection con = db.getConnection();

		PreparedStatement pstmt = null;
		String sql = "INSERT INTO `ui`(`yes`, `no`, `sick`, `memo`, `ID`) VALUES ('0', '0', '0', '', ?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			
			System.out.println("삽입 성공!!");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("실패");
		}
	}
	public void updateMemo(int y, int n, int s, String memo) {
		Connection con = db.getConnection();

		
		PreparedStatement pstmt = null;
		String sql = "UPDATE `ui` SET `yes`=?,`no`=?,`sick`=?,`memo`=? WHERE `ID`=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, y);
			pstmt.setInt(2, n);
			pstmt.setInt(3, s);
			pstmt.setString(4, memo);
			pstmt.setString(5, loginId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void toCheck1() {
		System.out.println(no.isSelected());

		noBar.setProgress(1);
	}

	public void kmjh() {
		alert("회원가입 성공", null);
		try {
			Parent login = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(login);
			Stage primaryStage = (Stage) btnRegister.getScene().getWindow(); // 현재 윈도우 가져오기
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void toCheck() {
		
		System.out.println(yes.isSelected());
		int y = 0;
		int n = 0;
		int s = 0;
		if (yes.isSelected()) {
			System.out.println("출석");

			yesBar.setProgress(1);
			y = 1;
		} else {
			yesBar.setProgress(0);
		}

		if (no.isSelected()) {
			System.out.println("미출석");
			noBar.setProgress(1);
			n = 1;
		} else {
			noBar.setProgress(0);
		}

		if (sick.isSelected()) {
			sickBar.setProgress(1);
			s = 1;
		} else {
			sickBar.setProgress(0);
		}
		
		toGetText();
		String memo = note.getText();
		updateMemo(y, n, s, memo);
	}

	public void toCheck2() {
		System.out.println(no.isSelected());

		if (no.isSelected()) {
			System.out.println("미출석");
			noBar.setProgress(1);
		}
	}

	public void toCheck3() {
		System.out.println(yes.isSelected());
		if (yes.isSelected()) {
			System.out.println("출석취소");

			yesBar.setProgress(0);
		}

		if (no.isSelected()) {
			System.out.println("미출석취소");
			noBar.setProgress(0);
		}

		if (sick.isSelected()) {
			sickBar.setProgress(0);
		}

	}

	public void toGetText() {
		String tmp = note.getText();
		System.out.println(tmp);
		
		
		
	}

	public static void alert(String msg, String header) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("알람");
		alert.setHeaderText(header);
		alert.setContentText(msg);
		alert.show();
	}
}
