package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class UserInsert extends JFrame implements Base{

	JPanel p1 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(10, 10, 5, 10)));
	JPanel p2 = get(new JPanel(new GridLayout(0, 1)));
	JPanel p3 = get(new JPanel(new GridLayout(0, 1, 0, 10)));
	JPanel p4 = get(new JPanel(new GridLayout(2, 1)));
	JPanel p5 = get(new JPanel());
	JPanel p6 = get(new JPanel());
	
	JCheckBox ck1 = get(new JCheckBox("장애인"), setb(Color.white));
	JButton btn1=  new JButton("회원가입");
	
	JLabel lab1= get(new JLabel("이름:"), set(15));
	JLabel lab2= get(new JLabel("ID:"), set(15));
	JLabel lab3= get(new JLabel("PW:"), set(15));
	JLabel lab4= get(new JLabel("키:"), set(15));
	JLabel lab5= get(new JLabel("생년월일:"), set(15));
	
	JTextField txt1 = new JTextField();
	JTextField txt2 = new JTextField();
	JTextField txt3 = new JTextField();
	JTextField txt4 = new JTextField();
	JTextField txt5 = new JTextField();
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public UserInsert() {
		
		SetFrame(this, "회원가입", DISPOSE_ON_CLOSE, 350, 400);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Main();
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1);
		
		p1.add(p2, "West");
		p1.add(p3);
		p1.add(p4, "South");
		
		p4.add(p5);
		p4.add(p6);
		
		p2.add(lab1);
		p2.add(lab2);
		p2.add(lab3);
		p2.add(lab4);
		p2.add(lab5);
		
		p3.add(txt1);
		p3.add(txt2);
		p3.add(txt3);
		p3.add(txt4);
		p3.add(txt5);
		
		p5.add(ck1);
		p6.add(btn1);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			for (Component c : p3.getComponents()) {
				if (c instanceof JTextField && ((JTextField) c).getText().isBlank()) {
					err("빈칸없이 입력하세요.");
					return;
				}
			}
			
			Query("select * from user where u_id = ?;", list, txt2.getText());
			
			if (!list.isEmpty()) {
				err("이미 사용중인 아이디입니다.");
			}else if (Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$]).{4,}$", txt3.getText()) == false) {
				err("비밀번호를 확인해주세요.");
			}else if (!isnum(txt4.getText())) {
				err("문자는 입력이 불가합니다.");
			}else if (!isdate(txt5.getText())) {
				err("생년월일을 확인해주세요.");
			}else {
				
				jop("회원가입이 완료되었습니다.");
				
				int age = LocalDate.now().getYear() - LocalDate.parse(txt5.getText()).getYear();
				String uage = age >= 65 ? "4" : age >=20 ? "1" : age >=13 && age <= 19 ? "2" : "3";
				
				Update("insert into user values(null, ?,?,?,?,?,?,?);", txt1.getText(), txt2.getText(), txt3.getText(), txt4.getText(), txt5.getText(), uage, ck1.isSelected() ? "1" : "0");
				
				dispose();
				new Main();
				
			}
			
			
		});
		
	}

}
