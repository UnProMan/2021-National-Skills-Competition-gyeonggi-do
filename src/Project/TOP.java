 package Project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Base.Base;

public class TOP extends JFrame implements Base{
	
	JPanel p1;
	
	JLabel lab1 = get(new JLabel("놀이기구 인기순위 TOP5", 0), set(20), setb(Color.white));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	Color color[] = {Color.black, Color.blue, Color.red, Color.green, Color.yellow};
	
	public TOP() {
		
		SetFrame(this, "놀이기구 인기순위 TOP5", DISPOSE_ON_CLOSE, 700, 400);
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
		
		add(lab1, "North");
		lab1.setOpaque(true);
		
		Query("select r.r_name, count(*) from ticket t, ride r where t.r_no = r.r_no group by t.r_no order by count(*) desc, 1 desc limit 5;", list);
		
		add(p1 = get(new JPanel() {
			public void paint(Graphics g) {
				super.paint(g);
				
				int max = intnum(list.get(0).get(1));
				
				for (int i = 0; i < list.size(); i++) {
					
					float a = (float) (Double.parseDouble(list.get(i).get(1)) / max);
					int h = (int) (200 * a);
					
					g.setColor(color[i]);
					g.fillRect(45 + (i * 100), 250 - h, 40, h);
					g.fillRect(520, 140 + (i * 20), 10, 10);
					
					g.setColor(Color.black);
					g.drawRect(45 + (i * 100), 250 - h, 40, h);
					
					g.drawString(list.get(i).get(0), 35 + (i * 100), 270);
					g.drawString(list.get(i).get(0) + " : " + list.get(i).get(1) + "개", 550, 150 + (i * 20));
					
				}
				
			}
		}));
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
