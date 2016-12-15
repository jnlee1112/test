import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

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
		int userNo = 0; // login시 저장되는 userno
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
						String sqlF = "select mno, id from member1 where id = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlF);
							ps.setString(1, md.getID());

							ResultSet rs = ps.executeQuery();
							if (rs.next()) {
								sendResponse(new MemberData(MemberData.ID_FOUND, rs.getInt("mno"), rs.getString("id")));
							} else {
								sendResponse(new MemberData(MemberData.ID_NOTFOUND, -1, null));
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

				} else if (data instanceof ScheduleData) { // ScheduleData
					ScheduleData sd = (ScheduleData) data;
					switch (sd.getState()) {
					case ScheduleData.CREATE_NEW_GROUP: // 1명 이하면 나가리. 2<= <전체:
														// 최적의 날짜와 참여멤버. 75%
														// 찬성해야 약속 성사.
						System.out.println("뉴그룹등록");
						ArrayList<Integer> memberL = new ArrayList<>();
						int possibleDates[][] = new int[15][32]; // 각 개인이 가능한
						memberL = sd.getMemberNoList();// 날짜의 집합
						memberL.add(userNo);
						for (int i : memberL) {
							System.out.println(i);
						}
						System.out.println();
						for (int memberNO : memberL) {
							try {
								String sqlPossible = "select to_char(pdate, 'mm'), to_char(pdate,'dd') from possible1 where mno = ? and pdate > sysdate";
								PreparedStatement ps = con.prepareStatement(sqlPossible);
								ps.setInt(1, memberNO);
								ResultSet rs = ps.executeQuery(); // 가능한 날짜의 모음
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

						int optimum = 0; // possibleDates 중 가장 큰 수 저장
						int optimumMonth = 0; // 적합한 날의 월
						int optimumDate = 0; // 적합한 날의 일
						Calendar today = Calendar.getInstance();
						int month = today.get(Calendar.MONTH) + 1;
						int date = today.get(Calendar.DATE);
						for (int i = month; i < month + 2; i++) {
							for (int j = date; j < 32; j++) {
								if (possibleDates[i][j] > optimum) {
									optimum = possibleDates[i][j];
									optimumMonth = i;
									optimumDate = j;
								}
							}
						}

						if (memberL.size() == 0 || ((double) optimum / memberL.size()) < 0.75) {
							sendResponse(new ScheduleData(ScheduleData.CREATE_FAIL, null, null));
						} else {
							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.MONTH, optimumMonth - 1);
							cal.set(Calendar.DATE, optimumDate);
							long d = cal.getTimeInMillis();
							java.sql.Date optimumD = new java.sql.Date(d);

							String sqlG = "insert into group1 (grno,gname,gdate) values(seq_grno.nextval,?,?)";
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
						sd.setState(ScheduleData.GET_PERSONAL_SCHEDULE);
						sendResponse(sd);
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
						ArrayList<ScheduleData> personalS = new ArrayList<>();
						String sqlPS = "select sdate,title,splace from schedule1 where mno = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlPS);
							ps.setInt(1, userNo);
							ResultSet rs = ps.executeQuery();
							while (rs.next()) {
								System.out.println(rs.getString(1) + "<-sdate" + rs.getString(2) + "<-title"
										+ rs.getString(3) + "<-splace");
								personalS.add(new ScheduleData(0, rs.getString("title"), rs.getString("splace"), rs.getDate("sdate")));
							}
							sendResponse(new ScheduleData(ScheduleData.GET_PERSONAL_SCHEDULE,personalS));
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case ScheduleData.GET_GROUP_SCHEDULE:
						ArrayList<ScheduleData> groupS = new ArrayList<>();
						ArrayList<String> mnameList = new ArrayList<>();
						String sqlGS = "select * from meeting where mno = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlGS);
							ps.setInt(1, userNo);
							ResultSet rs = ps.executeQuery();
							while (rs.next()) {
								double count = 0; // agree count
								double total = 0;
								int gNo = rs.getInt("grno");
								System.out.println("groupNumber:" + gNo);
								String sql1 = "select * from meeting, member1 where meeting.mno = member1.mno and meeting.grno = ?";
								PreparedStatement ps1 = con.prepareStatement(sql1);
								ps1.setInt(1, gNo);
								ResultSet rs1 = ps1.executeQuery();
								while (rs1.next()) {
									total++;
									if (rs1.getString("agree") != null && rs1.getString("agree").equals("YES")) {
										count++;
										mnameList.add(rs1.getString("mname"));
									}
								}
								rs1.close();
								ps1.close();
								System.out.println("count:" + count);
								System.out.println("total:" + total);
								if (count == total) {
									String sql2 = "select * from group1 where grno = ?";
									PreparedStatement ps2 = con.prepareStatement(sql2);
									ps2.setInt(1, gNo);
									ResultSet rs2 = ps2.executeQuery();
									while (rs2.next()) {
										System.out.println("date: " + rs2.getDate("gdate"));
										groupS.add(new ScheduleData(0, rs2.getString("gname"), rs2.getString("gplace"), mnameList, rs2.getDate("gdate")));
									}
									rs2.close();
									ps2.close();
								}

							}
							sendResponse(new ScheduleData(ScheduleData.GET_GROUP_SCHEDULE, groupS));
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case ScheduleData.GROUP_MANAGE:
						ArrayList<ScheduleData> notFixedL = new ArrayList<>();
						ArrayList<String> memberList = new ArrayList<>();
						String sqlGM = "select * from meeting where mno = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlGM);
							ps.setInt(1, userNo);
							ResultSet rs = ps.executeQuery();
							while (rs.next()) {
								String sqlFindMN = "select mno from meeting where grno = ?";
								PreparedStatement psFindMN = con.prepareStatement(sqlFindMN);
								psFindMN.setInt(1, rs.getInt("grno"));
								ResultSet rsFindMN = psFindMN.executeQuery();
								while (rsFindMN.next()) {
									String sqlF = "select mname from member1 where mno = ?";
									PreparedStatement psF = con.prepareStatement(sqlF);
									psF.setInt(1, rsFindMN.getInt("mno"));
									ResultSet rsF = psF.executeQuery();
									while (rsF.next()) {
										memberList.add(rsF.getString("mname"));
									}
									rsF.close();
									psF.close();
								}
								String sql1 = "select * from group1 where grno = ?";
								PreparedStatement ps1 = con.prepareStatement(sql1);
								ps1.setInt(1, rs.getInt("grno"));
								ResultSet rs1 = ps1.executeQuery();
								while (rs1.next()) {
									notFixedL.add(new ScheduleData(0, rs1.getString("gname"), rs1.getString("gplace"), memberList, rs1.getDate("gdate")));
								}
								sendResponse(new ScheduleData(ScheduleData.GROUP_MANAGE,notFixedL));
								rs1.close();
								ps1.close();
								rsFindMN.close();
								psFindMN.close();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case ScheduleData.AGREE:
						String subject = null;
						String message = null;
						String members = null;
						String sql1 = "select * from meeting, member1, group1 where group1.grno = meeting.grno and meeting.mno = member1.mno and meeting.grno = ? ";
						if (sd.isAgree()) {
							String sql = "update meeting set agree = ? where mno = ? and grno = ?";
							PreparedStatement ps;
							try {
								ps = con.prepareStatement(sql);
								ps.setString(1, "YES");
								ps.setInt(2, userNo);
								ps.setInt(3, sd.getGrno());
								ps.executeUpdate();
								ps.close();
								
								PreparedStatement ps1 = con.prepareStatement(sql1);
								ps1.setInt(1, sd.getGrno());
								ResultSet rs = ps1.executeQuery();
								while (rs.next()) {
									members += rs.getString("mname") + " ";
									subject = String.format("[ㅇㅇㅇ] 그룹 %s에 대한 결과입니다.", rs.getString("gname"));
									message = String.format("일시: %s%n장소: %s%n인원: %s%n일정 성사되었습니다.", rs.getDate("gdate"),
											rs.getString("gplace"), members);
									if (rs.getString("agree") == null) {
										return;
									} 
								}
								rs.close();
								ps1.close();
								
								PreparedStatement ps2 = con.prepareStatement(sql1);
								ResultSet rs2 = ps2.executeQuery();
								while(rs2.next()){
									MailManager.sendMail(rs.getString("email"), subject, message);
								}
								rs2.close();
								ps2.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
						}else{
							try {
								PreparedStatement ps1 = con.prepareStatement(sql1);
								ps1.setInt(1, sd.getGrno());
								ResultSet rs = ps1.executeQuery();
								while (rs.next()) {
									members += rs.getString("mname") + " ";
									subject = String.format("[ㅇㅇㅇ] 그룹 %s에 대한 결과입니다.", rs.getString("gname"));
									message = String.format("참석 불가능한 인원이 있어 일정이 취소되었습니다.%n일시: %s%n장소: %s%n인원: %s%n", rs.getDate("gdate"),
											rs.getString("gplace"), members);
								}
								rs.close();
								ps1.close();
								
								String sqlDM = "delete group1 where grno = ?";
								PreparedStatement psDM = con.prepareStatement(sqlDM);
								psDM.setInt(1, sd.getGrno());
								psDM.executeUpdate();
								psDM.close();
								
								PreparedStatement ps2 = con.prepareStatement(sql1);
								ResultSet rs2 = ps2.executeQuery();
								while(rs2.next()){
									MailManager.sendMail(rs.getString("email"), subject, message);
								}
								rs2.close();
								ps2.close();
							
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						break;
					case ScheduleData.GET_POSSIBLE_DATE:
						ArrayList<ScheduleData> possibleDL = new ArrayList<>();
						String sqlD = "select * from possible1 where mno = ?";
						try {
							PreparedStatement ps = con.prepareStatement(sqlD);
							ps.setInt(1, userNo);
							ResultSet rs = ps.executeQuery();
							while (rs.next()) {
								possibleDL.add(new ScheduleData(0, null, null, rs.getDate("pdate")));
							}
							sendResponse(new ScheduleData(ScheduleData.GET_POSSIBLE_DATE, possibleDL));
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case ScheduleData.ADD_POSSIBLE_DATE:
						String sqlDp = "delete possible1 where mno = ?";
						try {
							PreparedStatement psDp = con.prepareStatement(sqlDp);
							psDp.setInt(1, userNo);
							psDp.executeUpdate();
							psDp.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						for (ScheduleData s : sd.getScheduleList()) {
							Date pd = s.getDate();
							String sqlP = "insert into possible1 values(?,?)";
							try {
								PreparedStatement ps = con.prepareStatement(sqlP);
								ps.setInt(1, userNo);
								ps.setDate(2, pd);
								ps.executeUpdate();
								ps.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
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
