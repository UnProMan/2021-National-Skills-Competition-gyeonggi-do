package Project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Main extends JFrame implements Base{

	JPanel p1 = get(new JPanel(new GridLayout(0, 1, 0, 10)), set(new EmptyBorder(10, 10, 10, 10)), set(180, 0));
	JPanel p2 = new JPanel(null);
	
	JLabel lab1 = get(new JLabel("로그인", 0));
	JLabel lab[] = new JLabel[9];
	String st[] = "회원가입, 위치정보, 예매, 매직패스(0), Mypage, 놀이기구 인기순위 TOP5, 놀이기구 등록/수정, 월별 분석, 종료".split(", ");
	
	ImageIcon img1, img2;
	
	public static int type = 0;
	public static int magic =  0;
	int log[][] = { {0,5,8}, {1,2,3,4,5,8}, {5,6,7,8}};
	
	public Main() {
		
		SetFrame(this, "메인", EXIT_ON_CLOSE, 800, 400);
		design();
		action();
		setVisible(true);
		
	}

	public static void main(String[] args) {
		new Main();
	}

	@Override
	public void design() {
		
		add(p1, "West");
		add(p2 = new JPanel() {
			public void paint(Graphics g) {
				super.paint(g);
				
				this.setBackground(Color.white);
				
				int w = this.getWidth() / 2 - 50;
				
				g.drawImage(img1.getImage(), 0, 0, w, this.getHeight() - 10, null);
				g.drawImage(img2.getImage(), w + 50, 0, w, this.getHeight() - 10, null);
				
			}
		});
		
		p1.add(lab1);
		for (int i = 0; i < lab.length; i++) {
			p1.add(lab[i] = get(new JLabel(st[i], 0), set(false)));
		}
		
		log();
		
		new Thread(()->{
			try {
				
				while (true) {
					
					for (int i = 1; i <= 3; i++) {
						
						img1 = new ImageIcon(file("캐릭터/로티" + i + ".jpg"));
						img2 = new ImageIcon(file("캐릭터/로리" + i + ".jpg"));
						
						repaint();
						revalidate();
						
						Thread.sleep(200);
						
					}
					
				}
				
			} catch (Exception e) {
			}
		}).start();
		
	}

	public void log() {
		
		for (int i = 0; i < lab.length; i++) {
			lab[i].setEnabled(false);
		}
		
		for (int i = 0; i < log[type].length; i++) {
			lab[log[type][i]].setEnabled(true);
		}
		
		if (type == 0) {
			lab1.setText("로그인");
		}else {
			lab1.setText("로그아웃");
		}
		
		lab[3].setText("매직패스(" + magic + ")");
		if (magic == 0) {
			lab[3].setEnabled(false);
		}else {
			lab[3].setEnabled(true);
		}
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		lab1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (type == 0) {
					dispose();
					new Login();
				}else {
					type = 0;
					member.clear();
					log();
				}
				
			}
		});
		
		for (int i = 0; i < lab.length; i++) {
			int j = i;
			lab[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					if (!lab[j].isEnabled()) {
						return;
					}
					
					if (j == 0) {
						dispose();
						new UserInsert();
					}else if (j == 1) {
						dispose();
						new Map();
					}else if (j == 2) {
						dispose();
						new Ticketing();
					}else if (j == 3) {
						dispose();
						new Magicpass();
					}else if (j == 4) {
						dispose();
						new Mypage();
					}else if (j == 5) {
						dispose();
						new TOP();
					}else if (j == 6) {
						dispose();
						new Ride();
					}else if (j == 7) {
						dispose();
						new Month();
					}else {
						System.exit(0);
					}
					
				}
			});
		}
		
	}

}
