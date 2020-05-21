import java.lang.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Home_page extends JFrame {

	// needed to connect to mysql through jdbc:	
	 private static final String dbClassName = "com.mysql.cj.jdbc.Driver";
	
	// Declare JFrame , JButton and all other stuff here(Globally) to be accessed by all methods:
	  JFrame f = new JFrame();
	  
	  JFrame f1 = new JFrame();
	 
	  JFrame f2 = new JFrame();
	 
	  JFrame f3 = new JFrame();
	  
	  JFrame f6 = new JFrame();
	 
	 JButton sup = new JButton("SuperAdmin  ");
	 
	 JButton register = new JButton("Register  ");
	 
	 JButton reg = new JButton("Register");
	 
	 JButton get = new JButton("See Database");
	
	JPasswordField pass = new JPasswordField(10);

	// these are sample events:
	String event[]={"Code Crunch","Quizomaniaa","Conference on PowerSystems","Workshop on SuperConductors","Techy Circuits","ElectroQuiz","Boat War","IEEE WorkShop","CS:GO","Tug OF War","RoboWar"};
	
	 JComboBox cb1 = new JComboBox(event);
	 
	 JComboBox cb2 = new JComboBox(event);
	 
	 JTextField Name = new JTextField(10);
	 
	 JTextField Dept = new JTextField(10);
	 
	 JTextField Sem = new JTextField(10);
	 
	 JTextField Contact = new JTextField(10);
	 
	 JTextField Gmail = new JTextField(10);
	 
	// object of action listner class:
	 ListenForButton listen = new ListenForButton();
	 
	// Constructor:
	 Home_page() {
		Home();
	}
	 
	//Code for Home Page: 
	  public void Home() {
		  f1.setLocationRelativeTo(null);
			// set icon for window
			Icon logo_b = new ImageIcon("logo_b.png");
			// this.setIconImage(logo_b.getImage());

			// set logo for window
			Icon logo_a = new ImageIcon(getClass().getResource("logo_a.png"));
			JPanel pan = new JPanel();
			JLabel back;
			back = new JLabel(logo_a);
			pan.add(back);
			pan.setBackground(new Color(0, 255, 255, 50));
			f1.add(pan, BorderLayout.NORTH);

			// icons for buttons:
			Icon students_s = new ImageIcon(getClass().getResource("students_s.png"));
			Icon event_s = new ImageIcon(getClass().getResource("event_s.png"));
			Icon admin_s = new ImageIcon(getClass().getResource("admin_s.png"));
			

			JPanel pan1 = new JPanel();
			
			// Superadmin Button:
			sup.setBorderPainted(false);
			sup.setContentAreaFilled(false);
			sup.setIcon(admin_s);
			sup.setToolTipText("Click here to login as Superadmin and to see DataBase");
			sup.addActionListener(listen);
			
			// register Button:
			register.setBorderPainted(false);
			register.setContentAreaFilled(false);
			register.setIcon(students_s);
			register.setToolTipText("Click here to register as Participant and see details");
			register.addActionListener(listen);
						
			pan1.add(sup, BorderLayout.WEST);
			pan1.add(register, BorderLayout.EAST);
			pan1.setBackground(new Color(0, 255, 255, 50));
			f1.add(pan1, BorderLayout.CENTER);

			f1.setTitle("EVENT MANAGEMENT SYSTEM");
			f1.setResizable(false);
			f1.setVisible(true);
			f1.setSize(700, 360);
			f1.setDefaultLookAndFeelDecorated(true);
			f1.setDefaultCloseOperation(EXIT_ON_CLOSE);
	  }

	// window to prompt password and select event to get database: 
	  public void Pass() {
		    f6.setSize(430,150);
			f6.setLocationRelativeTo(null);
			f6.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

			ImageIcon logo_b = new ImageIcon("logo_b.png");
			f6.setIconImage(logo_b.getImage());

			JPanel thePanel1 = new JPanel();
			thePanel1.setLayout(new GridLayout(2,3,20,20));		

			JLabel lb1 = new JLabel("enter password:");
			thePanel1.add(lb1);
			thePanel1.add(pass);

	        JLabel lb6 = new JLabel("Select Event*:");			
			thePanel1.add(lb6);
			thePanel1.add(cb1);

			f6.add(thePanel1,BorderLayout.NORTH);

			JPanel thePanel2 = new JPanel();			
			get.addActionListener(listen);
			thePanel2.add(get);
			f6.add(thePanel2,BorderLayout.SOUTH);
			
			f6.setResizable(false);
			f6.setVisible(true);
	  }
	  	// window to show database in a table formate:
		public void Details(ResultSet rs) {
			f2.setSize(500,430);
			f2.setLocationRelativeTo(null);
			f2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

				JPanel Panel2 = new JPanel();		
				JTable tb = new JTable();
				JScrollPane src = new JScrollPane(Panel2);
				DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Department", "Semester", "Contact_No", "Gmail"}, 0);
				// set column names:
				model.addRow(new Object[]{"Name >>","Department >>","Semester >>","Contact >>","Gmail >>"});
				try {
					// for repeatedly showing the rows: 
					while(rs.next())
					{
					    String a = rs.getString("Name");
					    String b = rs.getString("Department");
					    String c = rs.getString("Semester");
					    String d = rs.getString("Contact_no");
					    String y = rs.getString("Gmail");
					    model.addRow(new Object[]{a,b,c,d,y});
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				tb.setModel(model);
				Panel2.add(tb);
				//Panel2.add(src);
				f2.add(Panel2,BorderLayout.CENTER);

			
			f2.setVisible(true);
			f2.setResizable(false);
		}

	  // window to register for an event:
	  public void Register() {
		    f3.setSize(430,340);
			f3.setLocationRelativeTo(null);
			f3.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

			ImageIcon logo_b = new ImageIcon("logo_b.png");
			f3.setIconImage(logo_b.getImage());

			JPanel thePanel1 = new JPanel();
			thePanel1.setLayout(new GridLayout(7,3,20,20));		

			JLabel lb1 = new JLabel("Name*:");
			thePanel1.add(lb1);
			thePanel1.add(Name);

			JLabel lb2 = new JLabel("Dept.*:");
			thePanel1.add(lb2);
			thePanel1.add(Dept);
		
			JLabel lb3 = new JLabel("Semester*:");
			thePanel1.add(lb3);
			thePanel1.add(Sem);

			JLabel lb4 = new JLabel("Contact No.*:");
			thePanel1.add(lb4);
			thePanel1.add(Contact);

			JLabel lb5 = new JLabel("Gmail*:");
			thePanel1.add(lb5);
			thePanel1.add(Gmail);

	        JLabel lb6 = new JLabel("Select Event*:");
			
			thePanel1.add(lb6);
			thePanel1.add(cb2);

			f3.add(thePanel1,BorderLayout.NORTH);

			JPanel thePanel2 = new JPanel();			
			reg.addActionListener(listen);
			thePanel2.add(reg);
			f3.add(thePanel2,BorderLayout.SOUTH);
			
			f3.setResizable(false);
			f3.setVisible(true);
	  }
	 // class for ActionListener for Buttons:
	 private class ListenForButton implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// if sup button is pressed:
			if(e.getSource() == sup) {
				Pass();
			}
			// if register button is pressed:
			else if(e.getSource() == register) {
				Register();
			}
			// if reg button is pressed:
			else if(e.getSource() == reg) {
				String name = Name.getText();
				String dept = Dept.getText();
				String sem = Sem.getText();
				String contact = Contact.getText();
				String gmail = Gmail.getText();
				String Department = null;
				String Event = null;
				// variable to store the value of selected event:
				String item = (String)cb2.getItemAt(cb2.getSelectedIndex());
				// variables for event details:
				String Date = null,Time = null,Timing = "Two Hours",venue = "Training and placement cell",prizes = "prizes will be distributed at 04:30PM(after events) in Training and Placement Cell",extra = "> please reach 15min before Event.";

				if(item == "Code Crunch") {
					//This is database
					Department = "CSIT";
					//This is a table inside databse above.
					Event = "Code_Crunch";
					Date = "22-03-2019";
					Time = "10:00 AM";
					venue = "C.S.I.T. Dept.";
					extra += "\n" + "> please bring your own systems";
				}
				
				else if(item == "Quizomaniaa") {
					Department = "CSIT";
					Event = "Quizomaniaa";
					Date = "22-03-2019";
					Time = "02:00 PM";
					venue = "C.S.I.T. Dept.";
					prizes = "certificates are awarded to winners.";
				}

				else if(item == "Conference on PowerSystems") {
					Department = "CHEM";
					Event = "Conference_on_PowerSystems";
					Date = "24-03-2019";
					Time = "10:00 AM";
					venue = "Chemistry Dept.";
					prizes = "certificate of participation is awarded.";
				}

				else if(item == "Workshop on SuperConductors") {
					Department = "CHEM";
					Event = "SuperConductor_WorkShop";
					Date = "24-03-2019";
					Time = "02:00 PM";
					venue = "C.S.I.T. Dept.";
					prizes = "certificate of participation is awarded.";
				}

				else if(item == "Techy Circuits") {
					Department = "EC";
					Event = "Techy_Circuits";
					Date = "25-03-2019";
					Time = "10:00 AM";
					venue = "E.C. Dept.";
					extra = "\n" + "> please bring your own components.";
				}

				else if(item == "ElectroQuiz") {
					Department = "EC";
					Event = "ElectroQuiz";
					Date = "24-03-2019";
					Time = "02:00 PM";
					venue = "C.S.I.T. Dept.";
					prizes = "certificates are awarded to winners.";
				}
				
				else if(item == "Boat War") {
					Department = "MECH";
					Event = "Boat_War";
					Date = "25-03-2019";
					Time = "10:00 AM";
					venue = "Mechanical Dept.";
					
				}
				
				else if(item == "IEEE WorkShop") {
					Department = "MECH";
					Event = "IEEE_Workshop";
					Date = "24-03-2019";
					Time = "02:00 PM";
					venue = "Mechanical Dept.";
					prizes = "Certificate of Participation is awarded..";
				}
				
				else if(item == "CS:GO") {
					Department = "OTHERS";
					Event = "CS_GO";
					Date = "25-03-2019";
					Time = "02:00 PM";
					venue = "C.S.I.T. Dept.";
				}
				
				else if(item == "Tug OF War") {
					Department = "OTHERS";
					Event = "Tug_OF_War";
					Date = "25-03-2019";
					Time = "10:00 AM";
					Timing = "45 Minutes";
					venue = "Nehru kendra";
				}
				
				else if(item == "RoboWar") {
					Department = "OTHERS";
					Event = "RoboWar";
					Date = "25-03-2019";
					Time = "11:00 AM";
					Timing = "One Hour";
					venue = "Nehru Kendra";
				}
				
				// make an SQL to insert values into tables:	
				String sqlUpdate =  "INSERT INTO " + Event + "(Name, Department, Semester, Contact_No, Gmail) " + 
						"VALUES(?,?,?,?,?)";
				// to connect to the specific database(Department):
				String CONNECTION = "jdbc:mysql://127.0.0.1/" + Department;
				  
				 Connection conn = null;
				 
				  try{
				    // Class.forName() loads the jdbc classes
				    Class.forName(dbClassName);

				    // enter your user and password:
				    Properties p = new Properties();
				    p.put("user","superadmin");
				    p.put("password","Super@1999");

				    // trying to get connection:
				    conn = DriverManager.getConnection(CONNECTION,p); 
				   }
				  catch (Exception e1) {
					// window to show error:
					  JOptionPane.showMessageDialog(f,"Something is going wrong.","Error",JOptionPane.WARNING_MESSAGE);     
				  }

		 
		        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
		 
		            // prepare data for update
		            pstmt.setString(1, name);
		            pstmt.setString(2, dept);
		            pstmt.setString(3, sem);
		            pstmt.setString(4, contact);
		            pstmt.setString(5, gmail);
		 		// shows number of rows effected:
		            int rowAffected = pstmt.executeUpdate();
		            System.out.println(String.format("Row affected %d", rowAffected));
		            
		            String status = "You have been registered in " + item + "\n";
		            
		            status += ">>> Event Details:" + "\n";
		            
		           status += ">Date : " + Date + "\n";
		           
		           status += ">Time : " + Time + "\n";
		           
		           status += ">Timing : " + Timing + "\n";
		           
		           status += ">Venue : " + venue + "\n";
		           
		           status += ">Prizes : " + prizes + "\n";
		           
		           status += extra;
		            
		            JOptionPane.showMessageDialog(f,status);
		            f3.dispose();
		 
		 
		        } catch (SQLException ex) {
		        	JOptionPane.showMessageDialog(f,"Something is going wrong.","Error",JOptionPane.WARNING_MESSAGE);
		        }	
			}
			
			
			else if(e.getSource() == get) {
				// to check if entered password is wrong:
				if (!Arrays.equals(pass.getPassword(), new char[]{'S','u','p','e','r','@','1','9','9','9'}))
					JOptionPane.showMessageDialog(f,"Wrong Password.","Error",JOptionPane.WARNING_MESSAGE); 
				else {
						String Department = null,Event = null,item = (String)cb1.getItemAt(cb1.getSelectedIndex());
						if(item == "Code Crunch") {
							Department = "CSIT";
							Event = "Code_Crunch";
						}
						
						else if(item == "Quizomaniaa") {
							Department = "CSIT";
							Event = "Quizomaniaa";
						}

						else if(item == "Conference on PowerSystems") {
							Department = "CHEM";
							Event = "Conference_on_PowerSystems";
						}

						else if(item == "Workshop on SuperConductors") {
							Department = "CHEM";
							Event = "SuperConductor_WorkShop";
						}

						else if(item == "Techy Circuits") {
							Department = "EC";
							Event = "Techy_Circuits";
						}

						else if(item == "ElectroQuiz") {
							Department = "EC";
							Event = "ElectroQuiz";
						}
						
						else if(item == "Boat War") {
							Department = "MECH";
							Event = "Boat_War";
						}
						
						else if(item == "IEEE WorkShop") {
							Department = "MECH";
							Event = "IEEE_Workshop";
						}
						
						else if(item == "CS:GO") {
							Department = "OTHERS";
							Event = "CS_GO";
						}
						
						else if(item == "Tug OF War") {
							Department = "OTHERS";
							Event = "Tug_OF_War";
						}
						
						else if(item == "RoboWar") {
							Department = "OTHERS";
							Event = "RoboWar";
						}
						
						String dbClassName = "com.mysql.cj.jdbc.Driver";
						
						String CONNECTION = "jdbc:mysql://127.0.0.1/" + Department;
						  
						 Connection conn = null;
						 
						 java.sql.Statement st = null;
						 
						  try{
						    Class.forName(dbClassName);

						    Properties p = new Properties();
						    p.put("user","superadmin");
						    p.put("password","Super@1999");

						    conn = DriverManager.getConnection(CONNECTION,p);

						    st = conn.createStatement();
						   }
						  catch (Exception e1) {
							  JOptionPane.showMessageDialog(f,"error in connection.","Error",JOptionPane.WARNING_MESSAGE);     
						  }

						  PreparedStatement s = null;
						try {
							s = conn.prepareStatement("select * from " + Event);
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(f,"error in Preparedstatement.","Error",JOptionPane.WARNING_MESSAGE);
						}
						  // rs stores items of data base as objects:
						  ResultSet rs = null;
						try {
							rs = s.executeQuery();
							Details(rs);
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(f,"error in statement.","Error",JOptionPane.WARNING_MESSAGE);
						}
			}
			
		}
		 
	 }


}
		public static void main(String[] arg) {
			new Home_page();
		}

}


