package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Base.Base;

public class Ride extends JFrame implements Base{
	
	JLabel maker[][] = new JLabel[5][];
	String ride[][] = new String[5][];
	
	ImageIcon img;
	
	JPanel p1 = get(new JPanel(), set(1000, 650));
	JPanel p2 = get(new JPanel(), set(250, 0));
	
	String tab[] = "1F, 2F, 3F, 4F, 외부".split(", ");
	JComboBox com1 =get(new JComboBox<>(tab), set(200, 30));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Ride() {
		
		SetFrame(this, "놀이기구 등록/수정", DISPOSE_ON_CLOSE, 1300, 650);
		design();
		action();
		setVisible(true);
		
		com1.setSelectedIndex(0);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Main();
			}
		});
		
	}

	@Override
	public void design() {
		
		for (int i = 0; i < tab.length; i++) {
			
			Query("select * from ride where r_floor = ?;", list, tab[i]);
			
			maker[i] = new JLabel[list.size()];
			ride[i] = new String[list.size()];
			
			for (int j = 0; j < list.size(); j++) {
				ride[i][j] = list.get(j).get(1);
			}
			
		}
		
		map(0);
		
		for (int i = 0; i < tab.length; i++) {
			for (int j = 0; j < Map.row[i].length; j+=2) {
				
				int n = j/2;
				p1.add(maker[i][n] = getimg("아이콘.png", 30, 30));
				maker[i][n].setBounds(Map.row[i][j], Map.row[i][j + 1], 30, 30);
				maker[i][n].setVisible(false);
				
			}
		}
		
		add(p1);
		add(p2, "East");
		
		p2.add(com1);
		
	}
	
	public void map(int i) {
		
		img = new ImageIcon(file("지도/" + tab[i] + ".jpg"));
		
		p1 = get(new JPanel(null) {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor(new Color(255, 255, 255, 120));
				
				g.drawImage(img.getImage(), 0, 0, 1000, 600, null);
				g.fillRect(0, 0, 1000, 600);
				
			}
		}, set(1000, 600));
		
		p1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				new RIdeInsert(tab[i], e.getX(), e.getY(), i);
			}
		});
		
	}
	
	@Override
	public void action() {
		
		com1.addActionListener(e->{
			
			int r = com1.getSelectedIndex();
			map(r);

			for (int i = 0; i < maker.length; i++) {
				for (int j = 0; j < maker[i].length; j++) {
					maker[i][j].setVisible(false);
				}
			}
			
			for (int i = 0; i < maker[r].length; i++) {
				maker[r][i].setVisible(true);
			}
			
			revalidate();
			repaint();
			
		});
		
		for (int i = 0; i < maker.length; i++) {
			for (int j = 0; j < maker[i].length; j++) {
				
				int r = i;
				int c = j;
				
				maker[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						dispose();
						new RideUpdate(ride[r][c]);
					}
				});
				
			}
		}
		
	}

}
