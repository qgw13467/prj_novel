package io.team.domain;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	private int memId;
	private String memUserId;
	private String memPassword;
	private String memChangepwd;
	private String memEmail;
	private String memNick;
	private int memPoint;
	private int state;
	private String memRegistDatetime;
	private String memLastloginDatetime;
	private int memFollowingNvnum;
	private int memFollowingWrnum;
	private int memFollowed;
	private String memIcon;
	private int memIsAdmin;
	private String token;
}
