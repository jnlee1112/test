import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Server implements Runnable {

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public static void main(String[] args) {
		new Server();
	}

	public Server() {
		try {
			ServerSocket serverSocket = new ServerSocket(8888);
			while (true) {
				Socket socket = serverSocket.accept();
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
				new Thread(this).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		int userNo;
		String userID;
		boolean isConnect = true;
		Connection con = ConnectionManager.getConnectrion();
		while (isConnect) {
			try {
				Object data = (Object) ois.readObject();
				if (data instanceof MemberData) { // MemberData
					MemberData md = (MemberData) data;

					switch (md.getState()) {
					case MemberData.FIRST_CONNECT:
						System.out.println("FIRST_CONNECT");
						int loginState = 0;
						String sql = "select * from member1 where ID = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sql);
							ps.setString(1, md.getID());
							ResultSet rs = ps.executeQuery();
							if (rs.next()) {
								if (rs.getString("PW").equals(md.getPW())) {
									loginState = MemberData.LOGIN_SUCCESS;
									userNo = rs.getInt("MNO");
									userID = rs.getString("ID");
								} else {
									loginState = MemberData.PW_MISSMATCH;
								}
							} else {
								loginState = MemberData.ID_NOTFOUND;
							}
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						System.out.println(loginState);
						sendResponse(new MemberData(loginState, md.getID(), md.getPW()));
						break;
					case MemberData.REGISTER:
						System.out.println("REGISTER");
						String sqlR = "insert into member1 values(seq_mno.nextval,?,?,?,?)";
						try {
							PreparedStatement ps = con.prepareStatement(sqlR);
							ps.setString(1, md.getName());
							ps.setString(2, md.getID());
							ps.setString(3, md.getPW());
							ps.setString(4, md.getEmail());
							ps.executeUpdate();
							ps.close();
						} catch (SQLException e) {
							sendResponse(new MemberData(MemberData.REGISTER_FAIL, null, null));
							e.printStackTrace();
						}
						sendResponse(new MemberData(MemberData.REGISTER_SUCCESS, null, null));
						break;
					case MemberData.FIND_ID:
						String sqlF = "select mno from member1 where mno = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlF);
							ps.setInt(1, md.getMemberNo());
							ResultSet rs = ps.executeQuery();
							if (rs.next()) {
								sendResponse(new MemberData(MemberData.ID_FOUND, rs.getInt("mno")));
							}else{
								sendResponse(new MemberData(MemberData.ID_NOTFOUND, -1));
							}
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case MemberData.DISCONNECT:
						System.out.println("DISCONNECT");
						ConnectionManager.close(con);
						ois.close();
						oos.close();
						isConnect = false;
						System.out.println("client out");
						break;
					}// switch

				} else if (data instanceof ScheduleData) {
					ScheduleData sd = (ScheduleData) data;
					switch (sd.getState()) {
					case ScheduleData.CREATE_NEW_GROUP: //1명 이하면 나가리. 2<= <전체: 최적의 날짜와 참여멤버. 75% 찬성해야 약속 성사.
						ArrayList<Integer> memberL = new ArrayList<>();
						memberL = sd.getMemberList();
						ArrayList<Integer> possibleMember = new ArrayList<>();
						int possibleDates[][] = new int[13][32];
						for (int memberNO : memberL) {
							try {
								String sqlPossible = "select to_char(pdate, 'mm'), to_char(pdate,'dd') from possible1 where mno = ? and pdate > sysdate";
								PreparedStatement ps = con.prepareStatement(sqlPossible);
								ps.setInt(1, memberNO);
								ResultSet rs = ps.executeQuery(); //가능한 날짜의 모음
								while (rs.next()) {
									possibleDates[Integer.parseInt(rs.getString(1))][Integer
											.parseInt(rs.getString(2))]++;
								}
								rs.close();
								ps.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						
						int optimum = 0; //possibleDates 중 가장 큰 수 저장
						int optimumMonth = 0;
						int optimumDate = 0;
						Calendar today = Calendar.getInstance();
						int month = today.get(Calendar.MONTH)+1;
						int date = today.get(Calendar.DATE);
						for (int i = month; i < month+2; i++) {
							for (int j = date; j < 32; j++) {
								if(possibleDates[i][j] > optimum){
									optimum = possibleDates[i][j];
									optimumMonth = i;
									optimumDate = j;
								}
							}
						}
							
						if (optimum < 2) { //가능한 날이 아무도 안 겹칠때 fail
							sendResponse(new ScheduleData(ScheduleData.CREATE_FAIL, null, null, null));
						}else{
							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.MONTH, optimumMonth-1);
							cal.set(Calendar.DATE, optimumDate);
							long d = cal.getTimeInMillis();
							java.sql.Date optimumD = new java.sql.Date(d);

							String sqlG = "insert into group1 (grno,grname,gdate) values(seq_grno.nextval,?,?)";
							try {
								PreparedStatement ps = con.prepareStatement(sqlG);
								ps.setString(1, sd.getGrName());
								ps.setDate(2, optimumD);
								ps.executeUpdate();
								ps.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						break;

					default:
						break;
					}
					
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendResponse(Object data) {
		try {
			oos.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
