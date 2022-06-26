package Setting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import Base.Base;

public class Setting implements Base{

	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Setting() {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true" , "root", "1234");
			Statement s = c.createStatement();
			
			s.executeUpdate("drop database if exists adventure");
			s.executeUpdate("create database if not exists adventure");
			s.executeUpdate("use adventure");
			s.executeUpdate("create table user(u_no int primary key auto_increment, u_name varchar(10), u_id varchar(15), u_pw varchar(15), u_height int, u_date date, u_age int, u_disable int)");
			s.executeUpdate("create table ride(r_no int primary key auto_increment, r_name varchar(15), r_floor varchar(2), r_max int, r_height varchar(15), r_old varchar(15), r_money int, r_disable int, r_explation varchar(150), r_img longblob)");
			s.executeUpdate("create table ticket(t_no int primary key auto_increment, u_no int, t_date varchar(10), r_no int, t_magicpass int, foreign key(u_no) references user(u_no), foreign key(r_no) references ride(r_no))");
			s.executeUpdate("drop user if exists user@'localhost'");
			s.executeUpdate("create user if not exists user@'localhost' identified by '1234'");
			s.executeUpdate("grant select, insert, delete, update on adventure.* to user@'localhost'");
			s.executeUpdate("set global local_infile = 1");
			
			String st[] = "user, ride, ticket".split(", ");
			for (int i = 0; i < st.length; i++) {
				s.executeUpdate("load data local infile 'datafiles/" + st[i] + ".txt' into table " + st[i] + " lines terminated by '\r\n' ignore 1 lines;");
			}
			
			Query("SELECT * FROM adventure.ride;", list);
			
			for (int i = 0; i < list.size(); i++) {
				Saveimg(file("이미지/" + list.get(i).get(1) + ".jpg"), list.get(i).get(0));
			}
			
			jop("세팅완료");
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	public static void main(String[] args) {
		new Setting();
	}

	@Override
	public void design() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
