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
		int userNo = 0; //login시 저장되는 userno
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
								sendResponse(new MemberData(MemberData.ID_FOUND, rs.getInt("mno"),rs.getString("mname")));
							}else{
								sendResponse(new MemberData(MemberData.ID_NOTFOUND, -1,null));
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

				} else if (data instanceof ScheduleData) { //ScheduleData
					ScheduleData sd = (ScheduleData) data;
					switch (sd.getState()) {
					case ScheduleData.CREATE_NEW_GROUP: //1명 이하면 나가리. 2<= <전체: 최적의 날짜와 참여멤버. 75% 찬성해야 약속 성사.
						ArrayList<Integer> memberL = new ArrayList<>();
						memberL = sd.getMemberNoList();
						
						int possibleDates[][] = new int[13][32]; //각 개인이 가능한 날짜의 집합
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
						int optimumMonth = 0; //적합한 날의 월
						int optimumDate = 0; //적합한 날의 일
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
							sendResponse(new ScheduleData(ScheduleData.CREATE_FAIL, null, null));
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
							for (int m : memberL) {
								String sqlM = "insert into meeting (grno, mno) values(seq_grno.currval,?)";
								try {
									PreparedStatement ps = con.prepareStatement(sqlM);
									ps.setInt(1, m);
									ps.executeUpdate();
									ps.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							sendResponse(new ScheduleData(ScheduleData.CREATE_NEW_GROUP, null, null));
						}
						break;
					case ScheduleData.PERSNAL_SCHEDULE_ADD:
						String sqlAdd = "insert into schedule1 values(?,?,?,?)";
						try {
							PreparedStatement ps = con.prepareStatement(sqlAdd);
							ps.setInt(1, userNo);
							ps.setString(2, sd.getTitle());
							ps.setString(3, sd.getPlace());
							ps.setDate(4, sd.getDate());
							ps.executeUpdate();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case ScheduleData.PERSNAL_SCHEDULE_UPDATE:
						String sqlUpdate = "update schedule1 set title = ?, splace = ? where mno = ? and sdate = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlUpdate);
							ps.setString(1, sd.getTitle());
							ps.setString(2, sd.getPlace());
							ps.setInt(3, userNo);
							ps.setDate(4, sd.getDate());
							ps.executeUpdate();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case ScheduleData.PERSNAL_SCHEDULE_DELETE:
						String sqlDelete = "delete schedule1 where mno = ? and sdate = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlDelete);
							ps.setInt(1, userNo);
							ps.setDate(2, sd.getDate());
							ps.executeUpdate();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case ScheduleData.GET_PERSONAL_SCHEDULE:
						String sqlPS = "select sdate,title,splace from schedule1 where mno = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlPS);
							ps.setInt(1, userNo);
							ResultSet rs = ps.executeQuery();
							while(rs.next()){
								sendResponse(new ScheduleData(ScheduleData.GET_PERSONAL_SCHEDULE, rs.getString("title"), rs.getString("splace"), rs.getDate("sdate")));
							}
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case ScheduleData.GET_GROUP_SCHEDULE:
						double count = 0; //agree count
						double percent = 0; 
						String sqlGS = "select * from meeting where mno = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlGS);
							ps.setInt(1, userNo);
							ResultSet rs = ps.executeQuery();
							while(rs.next()){
								int gNo = rs.getInt("grno");
								String sql1 = "select * from meeting where grno = ?";
								PreparedStatement ps1 = con.prepareStatement(sql1);
								ps1.setInt(1, gNo);
								ResultSet rs1 = ps1.executeQuery();
								while(rs1.next()){
									percent++;
									if (rs1.getString("agree").equals("YES")) {
										count++;
									}
								}
								rs1.close();
								ps1.close();
								if ((count/percent) >= 0.75) {
									String sql2 = "select * from group1 where grno = ?";
									PreparedStatement ps2 = con.prepareStatement(sql2);
									ResultSet rs2 = ps2.executeQuery();
									while(rs2.next()){
										sendResponse(new ScheduleData(ScheduleData.GET_GROUP_SCHEDULE, rs2.getString("gname"), rs2.getString("gplace"),rs2.getDate("gdate"), null));
									}
									rs2.close();
									ps2.close();
								}
								
							}
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case ScheduleData.GROUP_MANAGE:
						ArrayList<String> memberList = new ArrayList<>();
						String sqlGM = "select * from meeting where mno = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlGM);
							ps.setInt(1, userNo);
							ResultSet rs = ps.executeQuery();
							while(rs.next()){
								String sqlFindMN = "select mno from meeting where grno = ?"; 
								PreparedStatement psFindMN = con.prepareStatement(sqlFindMN);
								psFindMN.setInt(1, rs.getInt("grno"));
								ResultSet rsFindMN = psFindMN.executeQuery();
								while(rsFindMN.next()){
									String sqlF = "select mname from member1 where mno = ?";
									PreparedStatement psF = con.prepareStatement(sqlF);
									psF.setInt(1, rsFindMN.getInt("mno"));
									ResultSet rsF = psF.executeQuery();
									while(rsF.next()){
										memberList.add(rsFindMN.getString("mname"));
									}
								}
								String sql1 = "select * from group1 where grno = ?";
								PreparedStatement ps1 = con.prepareStatement(sql1);
								ps1.setInt(1, rs.getInt("grno"));
								ResultSet rs1 = ps1.executeQuery();
								while(rs1.next()){
									sendResponse(new ScheduleData(ScheduleData.GROUP_MANAGE, rs1.getString("gname"), rs1.getString("gplace"), memberList, rs1.getDate("gdate")));
								}
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
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
