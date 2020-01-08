package JaBae.Seller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
 * 입력받은 텍필의 값과 DB내부의 ID 비교후 그 비교값에 맞는 정액권만을 호출.
 * 호출한 정액권을 라벨로 붙여 넣기.
 */
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Seller_FreeTicketDAO implements ActionListener {

	private static Connection conn;      // 접속
	private static Statement st; 		 // 실행
	private static PreparedStatement ps; // 실행
	private static ResultSet rs;         // 결과
	
	private JTextField txt_Ft_Id_1;
	private JTable ta_Ft_read;
	
	public Seller_FreeTicketDAO() {
			
		}
	
	public Seller_FreeTicketDAO(JTable ta_Ft_read, JTextField txt_Ft_Id_1) {
		this.ta_Ft_read = ta_Ft_read;
		this.txt_Ft_Id_1 = txt_Ft_Id_1;
	}

//	---------------------------------------------------------------------------------------
//	--------------------------------- DB 연결  ----------------------------------------------

		void connectDB() throws SQLException {
			String url = "jdbc:oracle:thin:@localhost:1521/xe"; //2.url설정
			String id = "sample";
			String pwd = "sample";
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");//1.드라이버 로딩
				
				conn = DriverManager.getConnection(url, id, pwd); //3.db연결 접속 확인
				
			} catch (ClassNotFoundException e) {
				System.out.println("jdbc 연결 실패");
				e.printStackTrace();
			} catch(SQLException e) {
				System.out.println("orcale연결 실패");
			}
			
			st = conn.createStatement();
		}
		
//		---------------------------------------------------------------------------------------
//		--------------------------------- DB 해제 --------------------------------------------
		
		void closeDB() throws SQLException {
			if(rs != null) rs.close();
			if(st != null) st.close();
			if(ps != null) ps.close();
			if(conn!= null) conn.close();
		}
		
//		---------------------------------------------------------------------------------------
//		---------------------------------------------------------------------------------------
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("남은 정액권 확인")) {
					
						try {
							read();
						} catch (SQLException e1) {
							System.out.println("남은 정액권 읽어들이기 실패");
							e1.printStackTrace();
						}
					
			}
		}	
		
//  -----------------------------------------------------------------------------------------------------
//		------------------------------------------- 정액권 잔여 확인 메소드 -------------------------------------
		
		private void read() throws SQLException  { // (정상작동)
		
			connectDB();
			
			try { // 128
			String sellid = txt_Ft_Id_1.getText().toString().trim(); // id 입력받은 텍필 문자열 변수 selid로 정의

			if(sellid.equals("")) {
				JOptionPane.showMessageDialog(null, "사용자 ID를 입력해주시길 바랍니다.");

			} else { // 126
			
			String arr[] = new String[3]; // id, name, ft								  
	        DefaultTableModel model = (DefaultTableModel) ta_Ft_read.getModel();	  
	         int rowNum = model.getRowCount(); // 열 갯수 카운트								  
        																			  
	         for(int i=0; i<rowNum; i++) {	//열 갯수만큼 반복.								  
	        	 model.removeRow(0);//													  
	         }
			
			String sql = "select sell_id, name, freeticketcnt from seller where sell_id = ?";
//																						  ▲
				ps = conn.prepareStatement(sql);//										  |
				ps.setString(1, sellid); // SELL_ID ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ┘
				rs = ps.executeQuery();
				
				while(rs.next()) { 			
					 arr[0] = rs.getString("sell_id");
					 arr[1] = rs.getString("name");
					 arr[2] = rs.getString("freeticketcnt");
					 model.addRow(arr);
				} // while end
				
			} // if end
			
			} catch (NullPointerException e) {
			
			} // catch
			
				closeDB();
		}
		
//  -----------------------------------------------------------------------------------------------------
//	------------------------------------------- 정액권 구매 메소드 --------------------------------------------

		public void insert(int fa, JTextField txt_Ft_Id_1) throws SQLException { // (정상작동)
			
			connectDB();
			
			String sellid = txt_Ft_Id_1.getText().toString().trim(); // 입력 받은 id 문자열 selid에 정의
			String sql = "update seller set freeticketcnt = freeticketcnt + ? where sell_id = ?";

			if(sellid.equals("")) {
				JOptionPane.showMessageDialog(null, "사용자 ID를 입력해주시길 바랍니다.");
			} else if (fa == 0) {
				JOptionPane.showMessageDialog(null, "담겨 있는 정액권이 없습니다.");
			} else { // 162
			
			ps = conn.prepareStatement(sql);//                              |                 |
			ps.setInt(1, fa); // 		freeticketcnt ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ┘                 |
			ps.setString(2, sellid);//	sell_id ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ ㅡ   ┘ 
			
			rs = ps.executeQuery();
			
			JOptionPane.showMessageDialog(null, sellid + "님, " + fa + "개의 정액권 구매 완료했습니다.");
			
			read();
			
			} // if end
			
			closeDB();
			
		}

		public static void main(String[] args) {
	} // main method
} // main class