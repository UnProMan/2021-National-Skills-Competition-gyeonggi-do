package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class Ticketing extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)), set(0, 40), set(new EmptyBorder(10, 0, 0, 0)));
	JPanel p3 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)), set(new EmptyBorder(5, 0, 0, 0)));
	
	JButton btn1 = get(new JButton("예매"));
	JTextField txt1 = get(new JTextField(13), set(false));
	
	Vector v1;
	Vector v2 = new Vector<>(Arrays.asList(" , 번호, 기구명, 층수, 탑승인원, 키 제한, 나이 제한, 금액, 기구설명".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			if (column == 0) {
				return true;
			}else {
				return false;
			}
		};
		public java.lang.Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	int count = 0, pay = 0;
	
	public Ticketing() {
		
		SetFrame(this, "예매", DISPOSE_ON_CLOSE, 700, 600);
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
		
		add(p1);
		
		p1.add(p2, "North");
		p1.add(scl);
		p1.add(p3, "South");
		
		p2.add(btn1);
		
		p3.add(new JLabel("총금액 : "));
		p3.add(txt1);
		
		String dis = member.get(0).get(7).contentEquals("1") ? " and r_disable = 1" : "";
		int age = LocalDate.now().getYear() - LocalDate.parse(member.get(0).get(5)).getYear();
		
		Query("select * from ride where replace(r_height, 'cm이상', '') <= cast(? as unsigned) and replace(r_old, '세이상', '') <= cast(? as unsigned)" + dis, list, member.get(0).get(4), age + "");
		
		double h = member.get(0).get(6).contentEquals("1") ? 1 : member.get(0).get(6).contentEquals("2") ? 0.9 : 0.8; 
		h += member.get(0).get(7).contentEquals("1") ? 0.5 : 0;
		
		for (int i = 0; i < list.size(); i++) {
			model.addRow(new Object[] {false, list.get(i).get(0), list.get(i).get(1), list.get(i).get(2), list.get(i).get(3), list.get(i).get(4),list.get(i).get(5), df.format(intnum(list.get(i).get(6)) * h), list.get(i).get(8)});
		}
		
		
		tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				this.setHorizontalAlignment(JLabel.CENTER);
				
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});
		
//		table();
		
		tbl.setSelectionBackground(Color.yellow);
		tbl.setSelectionMode(0);
		txt1.setHorizontalAlignment(JLabel.RIGHT);
		pay();
		
	}
	
	public void table() {
		
		String dis = member.get(0).get(7).contentEquals("1") ? " and r_disable = 1" : "";
		int age = LocalDate.now().getYear() - LocalDate.parse(member.get(0).get(5)).getYear();
		
		Query("select * from ride where replace(r_height, 'cm이상', '') <= cast(? as unsigned) and replace(r_old, '세이상', '') <= cast(? as unsigned)" + dis, list, member.get(0).get(4), age + "");
		
		double h = member.get(0).get(6).contentEquals("1") ? 1 : member.get(0).get(6).contentEquals("2") ? 0.9 : 0.8; 
		h += member.get(0).get(7).contentEquals("1") ? 0.5 : 0;
		
		for (int i = 0; i < list.size(); i++) {
			model.addRow(new Object[] {false,list.get(i).get(0), list.get(i).get(1), list.get(i).get(2), list.get(i).get(3), list.get(i).get(4), list.get(i).get(5), df.format( intnum(list.get(i).get(6)) * h) , list.get(i).get(8)});
		}
		
		tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				this.setHorizontalAlignment(JLabel.CENTER);
				
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});
		
	}
	
	public void pay() {
		
		pay =0;
		count = 0;
		for (int i = 0; i < tbl.getRowCount(); i++) {
			if (tbl.getValueAt(i, 0).equals(true)) {
				pay += intnum(list.get(i).get(6).replace(",", ""));
				count++;
			}
		}
		
		txt1.setText(pay == 0 ? "" : df.format(pay));
		
	}
	
	@Override
	public void action() {
		
		tbl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				pay();
			}
		});
		
		btn1.addActionListener(e->{
			
			pay();
			
			if (count == 0) {
				err("예매할 놀이기구를 선택해주세요.");
			}else {
				
				for (int i = 0; i < tbl.getRowCount(); i++) {
					if (tbl.getValueAt(i, 0).equals(true)) {
						Query("select count(*) from ticket where r_no = ? and t_date = ?;", temp, tbl.getValueAt(i, 1).toString(), LocalDate.now().toString());
						
						if (intnum(temp.get(0).get(0)) >= intnum(tbl.getValueAt(i, 4).toString())) {
							err(tbl.getValueAt(i, 2) + "은(는) 만석입니다.");
							return;
						}
						
					}
				}
				
				if (count >= 5) {
					jop("매직패스권이 발급되었습니다.");
					Main.magic++;
				}
				
				for (int i = 0; i < tbl.getRowCount(); i++) {
					if (tbl.getValueAt(i, 0).equals(true)) {
						Update("insert into ticket values(null, ?,?,?,?);", member.get(0).get(0), LocalDate.now().toString(), tbl.getValueAt(i, 1).toString(),"0");
					}
				}
				
				jop("예매가 완료되었습니다.");
				
				dispose();
				new Main();
				
			}
			
		});
		
	}

}
