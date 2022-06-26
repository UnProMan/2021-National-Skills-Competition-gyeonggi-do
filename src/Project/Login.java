package Project;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Login extends JFrame implements Base{
	
	JLabel img = getimg("캐릭터/로티1.jpg", 100, 170);
	
	JPanel p1 = get(new JPanel(new BorderLayout(5, 5)), set(new EmptyBorder(30, 20, 30, 20)));
	JPanel p2 = get(new JPanel(new GridLayout(2, 1)), set(60, 0));
	JPanel p3 = get(new JPanel(new GridLayout(2, 1, 0, 10)));
	
	JTextField txt1 = new JTextField();
	JPasswordField txt2 = new JPasswordField();
	
	JButton btn1= get(new JButton("로그인"));
	
	public Login() {
		
		SetFrame(this, "로그인", DISPOSE_ON_CLOSE, 400, 200);
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
			
		add(img, "West");
		add(p1);
		
		p1.add(p2, "West");
		p1.add(p3);
		p1.add(btn1,"South");
		
		p2.add(new JLabel("ID:"));
		p2.add(new JLabel("PW:"));
		for (Component c : p2.getComponents()) {
			c.setFont(new Font("", 1, 14));
		}
		
		p3.add(txt1);
		p3.add(txt2);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank()) {
				err("ID 또는 PW를 입력해주세요.");
			}else if (txt1.getText().contentEquals("admin") && txt2.getText().contentEquals("1234")) {
				jop("관리자님 환영합니다.");
				Main.type = 2;
				dispose();
				new Main();
			}else {
				
				Query("select * from user where u_id = ? and u_pw = ?;", member, txt1.getText(), txt2.getText());
				
				if (member.isEmpty()) {
					err("회원정보를 다시 확인해주세요.");
				}else {
					
					String a= member.get(0).get(6).contentEquals("1") ? "성인" : member.get(0).get(6).contentEquals("2") ? "청소년" : member.get(0).get(6).contentEquals("3") ? "어린이" : "노인";
					a += member.get(0).get(7).contentEquals("0") ? "" : ",장애인";
					
					jop(member.get(0).get(1) + "고객님 환영합니다.(" + a + ")");
					Main.type = 1;
					dispose();
					new Main();
					
				}
				
			}
			
		});
		
	}

}
