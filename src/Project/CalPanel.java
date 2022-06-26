package Project;

import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class CalPanel extends JPanel implements Base{
	
	JLabel lab1;
	JLabel lab2 = get(new JLabel(""), setf(Color.green), set(12));
	JLabel lab3 = get(new JLabel(""), setf(Color.red), set(12));
	
	LocalDate date;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public CalPanel(LocalDate date) {

		this.date = date;
		
		setLayout(new GridLayout(0, 1));
		setBackground(Color.white);
		setBorder(new LineBorder(Color.black));
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		int c= date.getDayOfWeek().getValue();
		
		Query("select count(*), format(sum(r.r_money), 0) from ticket t, ride r where r.r_no = t.r_no and t_date = ?;", list, date.toString());
		
		if (list.get(0).get(0).contentEquals("0")) {
			lab2.setText("");
			lab3.setText("");
		}else {
			lab2.setText(list.get(0).get(0) + "명");
			lab3.setText(list.get(0).get(1) + "원");
		}
		
		add(lab1 = get(new JLabel(date.getDayOfMonth() + "", JLabel.RIGHT), setf(c == 7 ? Color.red : c == 6 ? Color.blue : Color.black), set(12)), set(new EmptyBorder(5, 0, 0, 10)));
		add(lab2);
		add(lab3);
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
