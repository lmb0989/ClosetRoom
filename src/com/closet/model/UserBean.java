package com.closet.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.closet.dbdao.DatabaseDao;
import com.closet.dbdao.ObjectMapper;
import com.closet.dbdao.PersistentObject;
import java.util.ArrayList;

public class UserBean implements PersistentObject, ObjectMapper{
	
    private static final String JSON_KEY_USERNAME = "username";
    private static final String JSON_KEY_PASSWORD = "password";
    private static final String JSON_KEY_SEX = "sex";
    private static final String JSON_KEY_AGE = "age";
    private static final String JSON_KEY_PHONE = "phone";
    private static final String JSON_KEY_EMAIL = "email";
    private static final String JSON_KEY_JOB = "job";
    private static final String JSON_KEY_BUST = "bust";
    private static final String JSON_KEY_WAIST = "waist";
    private static final String JSON_KEY_HIPS = "hips";
    private static final String JSON_KEY_HEIGHT = "height";
    private static final String JSON_KEY_WEIGHT = "weight";
	
	public String userName = null;
	public String passWord = "";
	public String email = "";
	public String phone = "";
	public String sex = "";
	public int age;
	public String job = "";
	public String height = "";		//����
	public String weight = "";		//����
	public String bust = "";		//��Χ
	public String waist = "";		//��Χ
	public String hip = "";			//��Χ
	
	private static DatabaseDao db= new DatabaseDao();
	
	public UserBean(){ };
	public UserBean(String userName, String passWord){
		this.userName = userName;
		this.passWord = passWord;
	}
	
	public UserBean(String userName, String passWord, String email, 
			String phone, String sex, int age,  String job, 
			String height, String weight, String bust, 
			String waist, String hip){
            this.userName = userName;
            this.passWord = passWord;
            this.sex = sex;
            this.age = age;
            this.phone = phone;
            this.email = email;
            this.job = job;
            this.bust = bust;
            this.waist = waist;
            this.hip = hip;
            this.height = height;
            this.weight = weight;
	}
	
	public int create() {
		if(isUserNameExist()) return REG_MESSAGE_USEREXIST;
		if(isEmailExist()) return REG_MESSAGE_EMAILEXIST;
		if(isPhoneExist()) return REG_MESSAGE_PHONEEXIST;
		StringBuilder sb = new StringBuilder();
		sb.append("insert into user values (");
		sb.append("'").append(userName).append("'");
		sb.append(",'").append(passWord).append("'");
		sb.append(",'").append(email).append("'");
		sb.append(",'").append(phone).append("'");
		sb.append(",'").append(sex).append("'");
		sb.append(",").append(age);
		sb.append(",'").append(job).append("'");
		sb.append(",'").append(height).append("'");
		sb.append(",'").append(weight).append("'");
		sb.append(",'").append(bust).append("'");
		sb.append(",'").append(waist).append("'");
		sb.append(",'").append(hip).append("'");
		sb.append(")");
		String sql = sb.toString();
		System.out.println("sql::"+sql);
		int result = db.update(sql);
		return result>0 ? REG_MESSAGE_REGSUCCESS : REG_MESSAGE_FAILED;
	}
	
	public int delete() {
		String sql = "delete from user where username='"+userName+"'";
		return db.update(sql);
	}
	
	public void update(String key, String value) {
		String sql = "update user set "+ key +"='"+ value +"' where username='"+ userName +"'";
		db.update(sql);
	}
	
	public void update() {
		StringBuilder sb = new StringBuilder();
		sb.append("update user set ");
//		sb.append("username='").append(userName).append("'");
		sb.append("password='").append(passWord).append("'");
		sb.append(",").append("email='").append(email).append("'");
		sb.append(",").append("phone='").append(phone).append("'");
		sb.append(",").append("sex='").append(sex).append("'");
		sb.append(",").append("age=").append(age);
		sb.append(",").append("job='").append(job).append("'");
		sb.append(",").append("height='").append(height).append("'");
		sb.append(",").append("weight='").append(weight).append("'");
		sb.append(",").append("bust='").append(bust).append("'");
		sb.append(",").append("waist='").append(waist).append("'");
		sb.append(",").append("hip='").append(hip).append("'");
		sb.append(" where username='").append(userName).append("'");
		String sql = sb.toString();
		System.out.println("sql::"+sql);
		db.update(sql);
	}
	
	public UserBean query(){
		if(this.userName == null) return null;
		String sql = "select * from user where username='"+userName+"'";
		return (UserBean)db.query(sql, new UserBean());
	}
	
//	@SuppressWarnings("unchecked")
	public static List<UserBean> getAllUser(){
		String sql = "select * from user";
		List<UserBean> allUser = (List<UserBean>)db.queryList(sql, new UserBean());
		for(UserBean user : allUser){
			System.out.println("userName:"+user.userName);
		}
		return allUser;
	}
	
	public boolean isUserNameExist(){
		return query() != null;
	}

	public boolean isEmailExist(){
		List<UserBean> allUser = getAllUser();
		for(UserBean user : allUser){
			if(user.email.equals(this.email)) return true;
		}
		return false;
	}
	
	public boolean isPhoneExist(){
		List<UserBean> allUser = getAllUser();
		for(UserBean user : allUser){
			if(user.phone.equals(this.phone)) return true;
		}
		return false;
	}
	
	public int login(){
		UserBean user = query();
		if(user == null) return LOGIN_MESSAGE_USERNOTEXIST;
		System.out.println("sql user.password"+user.passWord);
		System.out.println("user.password"+this.passWord);
		if(!user.passWord.equals(this.passWord)) return LOGIN_MESSAGE_PASSWRONG;
		return LOGIN_MESSAGE_LOGINSUCCESS;
	}
	
	public static String getUserName(String key, String value){
		String sql = "select * from user where "+key+"='"+value+"'";
		DatabaseDao db = new DatabaseDao();
		UserBean user = (UserBean)db.query(sql, new UserBean());
		return user.userName;
	}
	
	public JSONObject createMessage(int message){
		try{
			JSONObject jobj = new JSONObject();
			jobj.put("username", this.userName);
			jobj.put("message", message);
			return jobj;
		}catch(JSONException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public JSONObject getUserInfo(){
		UserBean user = query();
		try{
			JSONObject jobj = new JSONObject();
			jobj.put(JSON_KEY_USERNAME, user.userName);
			jobj.put(JSON_KEY_PASSWORD, user.passWord);
			jobj.put(JSON_KEY_SEX, user.sex);
			jobj.put(JSON_KEY_AGE, user.age);
			jobj.put(JSON_KEY_PHONE, user.phone);
			jobj.put(JSON_KEY_EMAIL, user.email);
			jobj.put(JSON_KEY_JOB, user.job);
			jobj.put(JSON_KEY_BUST, user.bust);
			jobj.put(JSON_KEY_WAIST, user.waist);
			jobj.put(JSON_KEY_HIPS, user.hip);
			jobj.put(JSON_KEY_HEIGHT, user.height);
			jobj.put(JSON_KEY_WEIGHT, user.weight);
			return jobj;
		}catch(JSONException ex){
			ex.printStackTrace();
			return null;
		}
	}
        
        public ArrayList<ImageBean> getUserAllImage(){
            String sql = "select * from images where username='" + this.userName +"'";
            return (ArrayList<ImageBean>)db.queryList(sql, new ImageBean());
        }
	
	public UserBean mapping(ResultSet rs) {
        try {
        	this.userName = rs.getString("username");
        	this.passWord = rs.getString("password");
        	this.sex = rs.getString("sex");
        	this.age = rs.getInt("age");
        	this.phone = rs.getString("phone");
        	this.email = rs.getString("email");
        	this.job = rs.getString("job");
			this.bust = rs.getString("bust");
			this.waist = rs.getString("waist");
			this.hip = rs.getString("hip");
			this.height = rs.getString("height");
			this.weight = rs.getString("weight");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public UserBean clone(){
		UserBean user = new UserBean();
		user.userName = this.userName;
		user.passWord = this.passWord;
		user.sex = this.sex;
		user.age = this.age;
		user.phone = this.phone;
		user.email = this.email;
		user.job = this.job;
		user.bust = this.bust;
		user.waist = this.waist;
		user.hip = this.hip;
		user.height = this.height;
		user.weight = this.weight;
		return user;
	}
}
