package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Detail extends JFrame implements Base{

	JPanel p1 = get(new JPanel(new BorderLayout(20, 10)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new BorderLayout()));
	JPanel p3 = get(new JPanel(new BorderLayout()));
	JPanel p4 = get(new JPanel(new GridLayout(2, 2)));
	JPanel p5 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
	JPanel p6 = get(new JPanel());
	
	JCheckBox ck1 = get(new JCheckBox("장애인"), set(false), setb(Color.white));
	
	JLabel img;
	JLabel lab1 = get(new JLabel("", 0));
	
	JTextArea txt1 = get(new JTextArea(), set(new LineBorder(Color.black)), set(false));
	
	JButton btn1 = new JButton("예매");
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> count = new ArrayList<>();
	String name;
	
	public Detail(String name) {
		
		this.name = name;
		
		SetFrame(this, "놀이기구 상세정보", DISPOSE_ON_CLOSE, 650, 300);
		design();
		action();
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Map();
			}
		});
		
	}

	@Override
	public void design() {
		
		Query("select * from ride where r_name = ?;", list, name);
		
		img = Dbimg(list.get(0).get(0), 200, 200, set(new LineBorder(Color.black)));
		lab1.setText(list.get(0).get(1));
		lab1.setFont(new Font("맑은 고딕", 1, 30));
		
		add(p1);
		
		p1.add(img, "West");
		p1.add(p2);
		p1.add(p5, "South");
		
		p2.add(lab1, "North");
		p2.add(p3);
		p2.add(p4, "South");
		
		p5.add(btn1);
		
		p3.add(new JLabel("설명"), "North");
		p3.add(txt1);
		
		String a = list.get(0).get(4) == null ? "" : list.get(0).get(4);
		a += list.get(0).get(5) == null ? "" : (a.isBlank() ? "" : ",") + list.get(0).get(5);
		
		p4.add(new JLabel("탑승인원 : " + list.get(0).get(3)));
		p4.add(new JLabel("탑승제한 : " + a));
		p4.add(new JLabel("위치 : " + list.get(0).get(2)));
		p4.add(p6);
		
		p6.add(ck1);
		
		txt1.setText(list.get(0).get(8));
		txt1.setLineWrap(true);
		ck1.setSelected(list.get(0).get(7).contentEquals("1") ? true : false);
		
	}

	@Override
	public void action() {

		btn1.addActionListener(e->{
			
			Query("select count(*) from ticket where r_no = ? and t_date = ?;", count, list.get(0).get(0), LocalDate.now().toString());
			
			if (intnum(count.get(0).get(0)) >= intnum(list.get(0).get(3))) {
				err(list.get(0).get(1) + "은(는) 만석입니다.");
			}else {
				
				jop("에매가 완료되었습니다.");
				
				Update("insert into ticket values(null, ?,?,?,?);", member.get(0).get(0),LocalDate.now().toString(), list.get(0).get(0), "0");
				
				dispose();
				new Main();
				
			}
			
		});
		
	}

}
