package aaa.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("recruitDTO")
public class RecruitDTO {
	int recruitId, start, limit = 3, pageLimit = 4, page, pageStart, pageEnd, pageTotal, cnt;
	String recruitTitle, recruitName, recruitUpfile, recruitContent;
	Date regDate = new Date();
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd (E) HH:mm");

	public String getRegDate() {
		return sdf.format(regDate);
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getRecruitUpfile() {
		if(recruitUpfile == null || recruitUpfile.trim().equals("") ||  recruitUpfile.trim().equals("null") ) {
			recruitUpfile = null;
		}
		return recruitUpfile;
	}

	public boolean isImg() {
		if(getRecruitUpfile()==null) {
			return false;
		}
		return Pattern.matches(".{1,}[.](bmp|png|gif|jpg|jpeg)", recruitUpfile.toLowerCase());
	}
	
	public void calc(int total) {

		start = (page -1) * limit;
		
		pageStart = (page -1)/pageLimit*pageLimit +1;
		pageEnd = pageStart + pageLimit -1;
		
		
		pageTotal = total / limit;
		if(total % limit != 0) {
			pageTotal++;
		}
		
		if(pageEnd > pageTotal) {
			pageEnd = pageTotal;
		}
		
	}
	
}
/*
    # 답글을 안써도 될꺼같아 seq lev gid를 제거했습니다.
	# 임시로 만듬 채용 테이블 recruit
	# recruit_id 무결성을 위한 id
	# recruit_title 채용 게시판 이름 
	# recruit_name 체용 게시판에 작성한 기업 이름 !!! 기업 이름과 교체 !!!
	# recruit_upfile 체용 게시판에 업로드한 이미지 파일
	# recruit_content 체용 게시판에 작성한 내용
	# reg_date 체용 게시판 작성일
	# cnt 체용 게시판 조회수
	CREATE TABLE recruit (
	    recruit_id INT AUTO_INCREMENT PRIMARY KEY,
	    recruit_title VARCHAR(100),
	    recruit_name VARCHAR(100),
	    recruit_upfile VARCHAR(100),
	    recruit_content VARCHAR(9999),
	    reg_date TIMESTAMP,
	    cnt INT
	);
  */
