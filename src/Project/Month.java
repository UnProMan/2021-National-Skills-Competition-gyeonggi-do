package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Base.Base;

public class Month extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(0, 60));
	JPanel p2 = get(new JPanel());
	JPanel p3 = get(new JPanel(new GridLayout(2, 1)), set(200, 0));
	JPanel p4 = get(new JPanel(new GridLayout(0, 7)));
	
	JButton btn1 = get(new JButton("◀"));
	JButton btn2 = get(new JButton("▶"));
	
	LocalDate now = LocalDate.now();
	
	JLabel lab;
	JLabel lab1 = get(new JLabel("", 0), set(20), set(250, 30));
	JLabel lab2 = get(new JLabel(""), set(17), setf(Color.green));
	JLabel lab3 = get(new JLabel(""), set(17), setf(Color.red));
	
	String week[] = "일, 월, 화, 수, 목, 금, 토".split(", ");
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Month() {
		
		SetFrame(this, "월별 분석", DISPOSE_ON_CLOSE, 800, 800);
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
		
		add(p1, "North");
		add(p4);
		
		p1.add(p2);
		p1.add(p3, "East");
		
		p2.add(btn1);
		p2.add(lab1);
		p2.add(btn2);
		
		p3.add(lab2);
		p3.add(lab3);
		
		cal();
		
	}
	
	public void cal() {
		
		p4.removeAll();
		
		Query("select count(*),format(sum(r.r_money), 0) from ticket t, ride r where r.r_no = t.r_no and month(t_date) = ? group by month(t_date);", list, now.getMonthValue() + "");
		
		lab1.setText(now.format(DateTimeFormatter.ofPattern("yyyy년 MM월")));
		
		if (list.isEmpty()) {
			lab2.setText("");
			lab3.setText("");
		}else {
			lab2.setText(list.get(0).get(0) + "명");
			lab3.setText(list.get(0).get(1) + "원");
		}
		
		for (int i = 0; i < week.length; i++) {
			p4.add(lab = get(new JLabel(week[i] , 0), setf(i == 0 ? Color.red : i == 6 ? Color.blue : Color.black)));
		}
		
		int val = now.withDayOfMonth(1).getDayOfWeek().getValue();
		int start =val == 7 ? 0 : val;
		int fin = now.lengthOfMonth();
		
		for (int i = 1 - start; i < 42 - start; i++) {
			
			if (i < 1 || i > fin) {
				p4.add(new JLabel(""));
			}else {
				p4.add(new CalPanel(LocalDate.of(now.getYear(), now.getMonthValue(), i)));
			}
			
		}
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			now = now.plusMonths(-1);
			cal();
		});
		
		btn2.addActionListener(e->{
			
			if (now.getMonthValue() == LocalDate.now().getMonthValue()) {
				err("미래는 매출실적을 확인 할 수 없습니다.");
			}else {
				now = now.plusMonths(1);
				cal();
			}
			
		});
		
	}

}
