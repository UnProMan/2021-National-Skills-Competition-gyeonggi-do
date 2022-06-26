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
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Base.Base;

public class Map extends JFrame implements Base{
	
	public static int row[][] = { {116,408,234,330,286,531,350,492,599,390,643,252,701,352,731,282,785,349,832,273}, {739,214,839,352},{267,359,678,295}, {382,318,664,306,801,348},{206,459,248,354,293,324,320,287,315,437,400,241,447,281,432,437,465,487,505,443,542,180,690,230,785,292} };
	
	JPanel p[] = new JPanel[5];
	ImageIcon img[] = new ImageIcon[5];
	JLabel maker[][] = new JLabel[5][];
	String ride[][] = new String[5][];
	String ok[][] = new String[5][];
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(250, 0));
	JPanel p2 = get(new JPanel());
	JPanel p3 = get(new JPanel(new BorderLayout()), set(0, 220));
	
	BufferedImage icon = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
	ColorConvertOp op1 = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
	
	JComboBox com1 =get(new JComboBox<>(), set(200, 30));
	String tab[] = "1F, 2F, 3F, 4F, 외부".split(", ");
	JLabel am = new JLabel();
	
	JTabbedPane tp = new JTabbedPane(JTabbedPane.LEFT);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	int check1 = 0, check2 = 0;
	
	public Map() {
		
		SetFrame(this, "위치정보", DISPOSE_ON_CLOSE, 1300, 650);
		design();
		action();
		setVisible(true);
		
		tp.setSelectedIndex(1);
		tp.setSelectedIndex(0);
		
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
			ok[i] = new String[list.size()];
			
			for (int j = 0; j < list.size(); j++) {
				ride[i][j] = list.get(j).get(1);
			}
			
		}
		
		add(tp);
		add(p1, "East");
		
		p1.add(p2);
		p1.add(p3, "South");
		
		p2.add(com1);
		
		try {
			
			icon = ImageIO.read(new File(file("아이콘.png")));
			
			for (int i = 0; i < tab.length; i++) {
				
				map(i);
				
				for (int j = 0; j < row[i].length; j+=2) {
					
					int n = j/2;
					p[i].add(maker[i][n] = new JLabel());
					op1.filter(icon, icon);
					maker[i][n].setIcon(new ImageIcon(new ImageIcon(icon).getImage().getScaledInstance(30, 30, 4)));
					maker[i][n].setBounds(row[i][j], row[i][j + 1], 30, 30);
					
				}
				
				tp.add(p[i], tab[i]);
				
			}
			
		} catch (Exception e) {
		}
		
	}

	public void map(int i) {
		
		img[i] = new ImageIcon(file("지도/" + tab[i] + ".jpg"));
		
		p[i] = get(new JPanel(null) {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor(new Color(255, 255, 255, 120));
				
				g.drawImage(img[i].getImage(), 0, 0, 1000, 600, null);
				g.fillRect(0, 0, 1000, 600);
				
			}
		}, set(1000, 600));
		
		
	}
	
	@Override
	public void action() {
		
		tp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				if (check1 == 1) {
					check1 = 0;
					am.setVisible(true);
				}
				
				p3.removeAll();
				
				String st[] = "로리1,로리2,로리3,로티1,로티2,로티3".split(",");
				JLabel image = new JLabel(new ImageIcon(new ImageIcon(file("캐릭터/" + st[ThreadLocalRandom.current().nextInt(0, st.length)] + ".jpg")).getImage().getScaledInstance(200, 220, 4)));
				p3.add(image);
				
				com1.removeAllItems();
				int r = tp.getSelectedIndex();
				
				String dis = member.get(0).get(7).contentEquals("1") ? " and r_disable = 1" : "";
				int age = LocalDate.now().getYear() - LocalDate.parse(member.get(0).get(5)).getYear();
				
				try {
					
					com1.addItem("");
					Query("select * from ride where r_floor = ? and replace(r_height, 'cm이상', '') <= cast(? as unsigned) and replace(r_old, '세이상', '') <= cast(? as unsigned)" + dis, list,tab[r], member.get(0).get(4), age + "");
					
					for (int i = 0; i < list.size(); i++) {
						
						icon = ImageIO.read(new File(file("아이콘.png")));
						com1.addItem(list.get(i).get(1));
						
						for (int j = 0; j < ride[r].length; j++) {
							if (list.get(i).get(1).contentEquals(ride[r][j])) {
								maker[r][j].setIcon(new ImageIcon(new ImageIcon(icon).getImage().getScaledInstance(30, 30, 4)));
								ok[r][j] = ride[r][j];
							}
						}
						
					}
					
				} catch (Exception e2) {
				}
				
				com1.setSelectedIndex(0);
				
				revalidate();
				repaint();
				
			}
		});
		
		com1.addActionListener(e->{
			
			if (check1 == 1) {
				check1 = 0;
				am.setVisible(true);
			}
			
			try {
				
				for (int i = 0; i < ride.length; i++) {
					for (int j = 0; j < ride[i].length; j++) {
						
						if (ok[i][j] != null && ok[i][j].contentEquals(ride[i][j])) {
							if (com1.getSelectedItem().toString().contentEquals(ride[i][j])) {
								
								am = maker[i][j];
								
								check1 = 1;
								if (check2 == 0) {
									run();
								}
								
							}
						}
						
					}
				}
				
			} catch (Exception e2) {
			}
			
		});
		
		for (int i = 0; i < maker.length; i++) {
			for (int j = 0; j < maker[i].length; j++) {
				
				int r = i;
				int c=  j;
				
				maker[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						
						if (ok[r][c] == null) {
							err("회원님이 이용할 수 없는 놀이기구입니다.");
						}else {
							dispose();
							new Detail(ride[r][c]);
						}
						
					}
				});
				
			}
		}
		
	}
	
	public void run() {
		
		new Thread(()->{
			try {
				
				while (check1 == 1) {
					
					check2 = 1;
					
					am.setVisible(false);
					Thread.sleep(500);
					am.setVisible(true);
					Thread.sleep(500);
					
				}
				
				check2 = 0;
				
			} catch (Exception e) {
			}
		}).start();
		
	}
	
}
