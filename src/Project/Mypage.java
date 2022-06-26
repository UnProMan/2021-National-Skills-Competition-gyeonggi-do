package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import Base.Base;

public class Mypage extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(5, 0)), set(new EmptyBorder(5, 5, 5, 5)));
	
	Vector v1;
	Vector v2 = new Vector<>(Arrays.asList("예매번호, 기구이름, 예약날짜, 기구설명".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl;
	JScrollPane scl;
	
	JLabel img = get(new JLabel(""), setb(Color.white), set(new LineBorder(Color.black)), set(200, 0));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	JPopupMenu pop =new JPopupMenu();
	JMenuItem item1 = new JMenuItem("삭제");
	
	public Mypage() {
		
		SetFrame(this, "Mypage", DISPOSE_ON_CLOSE, 800, 300);
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
		
		pop.add(item1);
		
		tbl = new JTable(model) {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component comp = super.prepareRenderer(renderer, row, column);
				
				if (row%2 == 0) {
					comp.setBackground(Color.blue);
					comp.setForeground(Color.white);
				}else {
					comp.setBackground(Color.white);
					comp.setForeground(Color.black);
				}
				
				return comp;
			}
		};
		scl = new JScrollPane(tbl);
		
		add(p1);
		
		p1.add(scl);
		p1.add(img, "East");
		
		tabel();
		
	}
	
	public void tabel() {
		
		SetData("select t_no, r.r_name, t.t_date, r.r_explation, t.r_no from ticket t, ride r where r.r_no = t.r_no and t.u_no = ?", list, model, 0, 4, member.get(0).get(0));
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		tbl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				img.setIcon(new ImageIcon(getimg(list.get(tbl.getSelectedRow()).get(4)).getImage().getScaledInstance(300, 300, 4)));
				
				if (e.getButton() == 3) {
					tbl.setRowSelectionInterval(tbl.rowAtPoint(e.getPoint()), tbl.rowAtPoint(e.getPoint()));
					pop.show(tbl, e.getX(), e.getY());
				}
				
				repaint();
				
			}
		});
		
		item1.addActionListener(e->{
			
			jop("삭제가 완료되었습니다.");
			
			Update("delete from ticket where t_no = ?;", list.get(tbl.getSelectedRow()).get(0));
			img.setIcon(new ImageIcon(""));
			
			tabel();
			
		});
				
	}

}
