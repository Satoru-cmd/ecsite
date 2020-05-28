package jp.co.internous.ecsite.model.form;

import java.io.Serializable; //インスタンスの情報を永続化できる

//画面からjavaプログラムに送るデータを管理するクラス
public class Loginform  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
