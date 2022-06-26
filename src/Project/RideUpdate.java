package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class RideUpdate extends JFrame implements Base{
	
	JPanel pn = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(5, 5, 5, 5)), setb(null));
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(150, 0), setb(null));
	JPanel p2 = get(new JPanel(new BorderLayout()), setb(null));
	JPanel p3 = get(new JPanel(), setb(null));
	JPanel p4 = get(new JPanel(new FlowLayout(0, 10, 10)), setb(null));
	JPanel p5 = get(new JPanel(), set(0, 70), setb(null));
	
	JLabel lab1 = get(new JLabel("기구명 :"), set(60, 25));
	JLabel lab2 = get(new JLabel("층 :"), set(60, 25));
	JLabel lab3 = get(new JLabel("탑승인원 :"), set(60, 25));
	JLabel lab4 = get(new JLabel("키 제한 :"), set(60, 25));
	JLabel lab5 = get(new JLabel("나이 제한 :"), set(60, 25));
	JLabel lab6 = get(new JLabel("금액 :"), set(60, 25));
	JLabel img;
	
	JTextField txt1 = get(new JTextField(), set(170, 25));
	JTextField txt2 = get(new JTextField(), set(170, 25), set(false));
	JTextField txt3 = get(new JTextField(), set(170, 25));
	JTextField txt4 = get(new JTextField(), set(170, 25));
	JTextArea txt5 = get(new JTextArea(), set(320, 60), set(new LineBorder(Color.black)));
	
	JComboBox com1 = get(new JComboBox<>(), set(170, 25));
	JComboBox com2 = get(new JComboBox<>(), set(170, 25));
	
	JCheckBox ck1 = get(new JCheckBox("장애인"));
	
	JButton btn1 = new JButton("사진 등록");
	JButton btn2 = new JButton("수정");
	
	String name;
	
	JFileChooser fc;
	File file = new File("");
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public RideUpdate(String name) {
		
		this.name = name;
		
		SetFrame(this, "놀이기구 수정", DISPOSE_ON_CLOSE, 700, 300);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Ride();
			}
		});
		
	}

	@Override
	public void design() {
		
		Query("select * from ride where r_name = ?;", list, name);
		
		com1.addItem("");
		com2.addItem("");
		
		for (int i = 90; i <= 140; i+=10) {
			com1.addItem(i + "cm이상");
		}
		
		for (int i = 6; i <= 20; i++) {
			com2.addItem(i + "세이상");
		}
		
		add(pn);
		
		pn.add(p1, "West");
		pn.add(p2);
		pn.add(p3, "South");
		
		p1.add(img = Dbimg(list.get(0).get(0), 150, 175, set(new LineBorder(Color.black))));
		p1.add(btn1, "South");
		
		p3.add(btn2);
		
		p2.add(p4);
		p2.add(p5, "South");
		
		p4.add(lab1);
		p4.add(txt1);
		
		p4.add(lab2);
		p4.add(txt2);
		
		p4.add(lab3);
		p4.add(txt3);
		
		p4.add(lab4);
		p4.add(com1);
		
		p4.add(lab5);
		p4.add(com2);
		
		p4.add(lab6);
		p4.add(txt4);
		
		p4.add(ck1);
		
		p5.add(new JLabel("기구설명 : "));
		p5.add(txt5);
		
		txt5.setLineWrap(true);
		
		txt1.setText(list.get(0).get(1));
		txt2.setText(list.get(0).get(2));
		txt3.setText(list.get(0).get(3));
		txt4.setText(list.get(0).get(6));
		txt5.setText(list.get(0).get(8));
		
		ck1.setSelected(list.get(0).get(7).contentEquals("1"));
		com1.setSelectedItem(list.get(0).get(4));
		com2.setSelectedItem(list.get(0).get(5));
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			fc = new JFileChooser();
			
			int a = fc.showOpenDialog(this);
			
			if (a == 0) {
				
				file = fc.getSelectedFile();
				img.setIcon(new ImageIcon(new ImageIcon(file.toString()).getImage().getScaledInstance(150, 175, 4)));
				
				repaint();
				
			}
			
		});
		
		btn2.addActionListener(e->{
			
			if (txt1.getText().isBlank()) {
				error("기구명");
			}else if (txt3.getText().isBlank()) {
				error("탑승인원");
			}else if (txt4.getText().isBlank()) {
				error("금액");
			}else if (txt5.getText().isBlank()) {
				error("기구설명");
			}else {
				
				Query("select * from ride where replace(r_name, ' ', '') = ? and not r_no = ?", temp, txt1.getText().replace(" ", ""), list.get(0).get(0));
				
				if (!temp.isEmpty()) {
					err("기구명이 중복되었습니다.");
				}else if (!isnum(txt4.getText())) {
					err("금액은 숫자로 입력하세요.");
				}else if (!isnum(txt3.getText())) {
					err("탑승인원은 숫자로 입력하세요.");
				}else if (txt1.getText().contentEquals(list.get(0).get(1)) && txt3.getText().contentEquals(list.get(0).get(3)) &&  txt4.getText().contentEquals(list.get(0).get(6)) && txt5.getText().contentEquals(list.get(0).get(8)) && com1.getSelectedItem().toString().contentEquals(list.get(0).get(4)) && com2.getSelectedItem().toString().contentEquals(list.get(0).get(5)) && file.toString().isBlank()) {
					err("수정사항이 없습니다.");
				}else {
					
					jop("수정이 완료되었습니다.");
					
					Update("update ride set r_name = ?, r_max = ?, r_height = ?, r_old = ?, r_money = ?, r_disable = ?, r_explation = ? where r_no = ?;", txt1.getText(), txt3.getText(), com1.getSelectedItem().toString(), com2.getSelectedItem().toString(), txt4.getText(), ck1.isSelected() ? "1" : "0", txt5.getText(), list.get(0).get(0));
					
					if (!file.toString().isBlank()) {
						Saveimg(file.toString(), list.get(0).get(0));
					}
					
					dispose();
					new Ride();
					
				}
				
			}
			
		});
		
	}
	
	void error(String txt) {
		err(txt + "을(를) 입력해주세요.");
	}
	
}
