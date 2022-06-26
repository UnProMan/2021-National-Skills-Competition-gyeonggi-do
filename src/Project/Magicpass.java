package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Magicpass extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(0, 10)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new GridLayout(0, 4, 5, 5)));
	JPanel p3= get(new JPanel());
	
	JPanel p[] = new JPanel[16];
	JPanel back[] = new JPanel[16];
	JLabel img;
	
	JLabel lab1= get(new JLabel("환상의 매직패스", 0),set(20));
	
	String name[] = new String[16];
	
	JButton btn1 = get(new JButton("Stop"), set(180, 25));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp =new ArrayList<>();
	
	boolean start = true;
	int index;
	
	public Magicpass() {
		
		SetFrame(this, "매직패스", DISPOSE_ON_CLOSE, 400, 500);
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

		String dis = member.get(0).get(7).contentEquals("1") ? " and r_disable = 1" : "";
		int age = LocalDate.now().getYear() - LocalDate.parse(member.get(0).get(5)).getYear();
		
		Query("select * from ride where replace(r_height, 'cm이상', '') <= cast(? as unsigned) and replace(r_old, '세이상', '') <= cast(? as unsigned)" + dis, list, member.get(0).get(4), age + "");
		
		add(p1);
		
		p1.add(lab1, "North");
		p1.add(p2);
		p1.add(p3, "South");
		
		p3.add(btn1);
		
		for (int i = 0; i < p.length; i++) {
			p2.add(p[i]= new JPanel(null));
		}
		
		setting();
		random();
		
	}

	public void setting() {
		
		int a = ThreadLocalRandom.current().nextInt(1, 6);
		
		for (int i = 0; i < a; i++) {
			name[i] = "꽝";
		}
		
		int index = 0;
		for (int i = 0; i < name.length; i++) {
			if (name[i] == null) {
				name[i] = list.get(index).get(0);
				index++;
			}
		}
		
		Collections.shuffle(Arrays.asList(name));
		
		for (int i = 0; i < back.length; i++) {
			
			if (name[i].contentEquals("꽝")) {
				img = getimg("이미지/꽝.jpg", 100, 100);
			}else {
				img = Dbimg(name[i], 100, 100);
			}
			p[i].add(back[i] = get(new JPanel(), set(100, 100), setb(new Color(255, 255, 255, 120))));
			p[i].add(img);
			
			back[i].setBounds(0, 0, 100, 100);
			img.setBounds(0, 0, 100, 100);
			
		}
		
	}
	
	public void random() {
		
		new Thread(() ->{
			try {
				
				while (start) {
					
					for (int i = 0; i < back.length; i++) {
						
						back[i].setVisible(false);
						Thread.sleep(50);
						back[i].setVisible(true);
						Thread.sleep(50);
						
						index = i;
						
						if (start == false) {
							break;
						}
						
					}
					
				}
				
			} catch (Exception e) {
			}
		}).start();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			start = false;
			int row = index;
			
			back[row].setVisible(false);
			
			if (name[row].contentEquals("꽝")) {
				jop("아쉽네요~ 다음 기회에 다시 도전해주세요.");
				Main.magic--;
				dispose();
				new Main();
			}else {
				
				Query("select count(*) from ticket where r_no = ? and t_date = ?;", temp, name[row], LocalDate.now().toString());
				Query("select * from ride where r_no = ?;", list, name[row]);
				
				if (intnum(temp.get(0).get(0)) >= intnum(list.get(0).get(3))) {
					err("해당 " + list.get(0).get(1) + "은(는) 만석입니다. 다시 한번 도전해주세요.");
					start = true;
					random();
				}else {
					
					jop("'" + list.get(0).get(1) + "' 매직패스에 당첨되셨습니다.");
					Update("insert into ticket values(null, ?,?,?,?);", member.get(0).get(0), LocalDate.now().toString(), list.get(0).get(0), "1");
					
					Main.magic--;
					dispose();
					new Main();
					
				}
				
			}
			
		});
		
	}

}
