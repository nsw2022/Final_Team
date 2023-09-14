package aaa.controll;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import aaa.model.AdminDTO;
import aaa.model.AskDTO;
import aaa.model.MCompanyDTO;
import aaa.model.SoloDTO;
import aaa.service.AskMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;

@Controller
@RequestMapping("/ask") // QnA게시판
public class AskController {
	
	@Resource
	AskMapper mapper;
	
	
	@RequestMapping("list/{page}") 
	String list(Model mm, AskDTO dto,HttpSession session) {
		String sid = (String)session.getAttribute("sid");
		String cid = (String)session.getAttribute("cid");
		AdminDTO adto = (AdminDTO)session.getAttribute("adminSession");
		String zuserid;
		if(sid==null) {
			zuserid = cid;
		}else {
			zuserid = sid;
		}
		dto.calc(mapper.listCnt());
		dto.setPname(zuserid);
		System.out.println("askdto" + dto);
		List<AskDTO>admindata = mapper.askList(dto);
		List<AskDTO>data = mapper.useraskList(dto);
		
		if(adto!=null) {
			mm.addAttribute("mainData", admindata);
		}else{
			mm.addAttribute("mainData", data);
		}
		
		 
		 
		/* 관리자일때
		 * List<AskDTO>data = mapper.askList();
		 */
		
		
		return "ask/ask_list";
	}
	
	@RequestMapping("detail/{page}/{id}")
	String detail(Model mm, @PathVariable int page, @PathVariable int id) {
		mapper.addCount(id);// 조회수증가
		AskDTO dto = mapper.detail(id);
		dto.setPage(page);
		mm.addAttribute("askdto", dto);
		return "ask/ask_detail";
	}
	
	//글을써보자
	@GetMapping("insert/{page}")
	String insert(Model mm,AskDTO dto, HttpSession session,SoloDTO sdto, MCompanyDTO cdto) {
		String sid = (String)session.getAttribute("sid");
		String cid = (String)session.getAttribute("cid");
	
		System.out.println(sid+cid);
		if(sid==null) {
			mm.addAttribute("uid", cid);
		}else {
			mm.addAttribute("uid", sid);
		}
	
		
		return "ask/ask_insertForm";
	}
	
	@PostMapping("insert/{page}")
	String insertReg(AskDTO dto,HttpServletRequest request ) {
		
		dto.setId(mapper.maxId()+1);
		fileSave(dto,request);
		dto.setPname(request.getParameter("uid"));
		System.out.println(dto);
		mapper.askInsert(dto);
		dto.setMsg("게시글 등록되었습니다.");
		dto.setGoUrl("/ask/list/1");
		
		
		return "ask/ask_alert";
	}
	
	
	//사진첨부
	
	void fileSave(AskDTO dto,HttpServletRequest request) {
		
		//파일 업로드 확인
		if(dto.getMmff().isEmpty()) {
			return;
		}
		
		 String path = request.getServletContext().getRealPath("askup");
		 path = "C:\\Users\\콩쥐\\Desktop\\final\\0912 Merge\\Spring_Team\\Spring_Team\\owang\\src\\main\\webapp\\askup";
		 System.out.println(path);
		 
		 int dot = dto.getMmff().getOriginalFilename().lastIndexOf("."); //. 위치 찾음
		 String fDomain = dto.getMmff().getOriginalFilename().substring(0, dot); //확장자 제외한 부분 추출
		 String ext = dto.getMmff().getOriginalFilename().substring(dot); //확장자 추출
		 
		 
		 dto.setUpfile(fDomain+ext); //새로운 이름 설정
		 File ff = new File(path+"\\"+dto.getUpfile()); //전체파일 경로 설정
		 int cnt = 1; //파일이름 중복을 피하기위해
		 while(ff.exists()) {
			 dto.setUpfile(fDomain+"_"+cnt+ext);
			 ff = new File(path+"\\"+dto.getUpfile());
			 cnt++;
		 } // abc.jpg (중복)-> abc_2.jpg로 바꾼다
		 
		 try {
				FileOutputStream fos = new FileOutputStream(ff);
				
				fos.write(dto.getMmff().getBytes() ); //dto.getMmff() - 파일데이터가져옴
													 //dto.getMmff() - 문자열을 바이트 배열로 변환
													//fos.write - 바이트 배열을 파일에 씁니다. 이 부분은 실제로 파일에 데이터를 쓰는 부분으로, 파일을 생성하고 파일에 데이터를 기록
				fos.close(); // 닫아줌!
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 
	}
		
	
	//글을지워보자꾸나 
	@RequestMapping("delete/{page}/{id}")
	String delete(AskDTO dto, HttpServletRequest request ,@PathVariable int id) {
		
//		AskDTO adto = mapper.detail(id);
//		System.out.println("ask 삭제 dto"+adto);
		dto.setMsg("삭제에 실패했습니다");
		dto.setGoUrl("/ask/detail"+dto.getPage()+"/"+dto.getId());
		
		 AskDTO delDTO = mapper.detail(dto.getId());
		 
		 //int cnt = mapper.askDelete(delDTO);
		 int cnt = mapper.askDelete(dto);
		 
		 System.out.println("deleteReg:"+cnt);
		 
		 if(cnt>0) {
			 
			 fileDeleteModule(delDTO,request);
			 
			 dto.setMsg("삭제완료되었습니다.");
			 dto.setGoUrl("/ask/list/1");
		 }
		
		return "ask/ask_alert";
	}
	
	
	//수정페이지
	@GetMapping("modify/{page}/{id}")
	String modify(Model mm, @PathVariable int page, @PathVariable int id) {
		
		AskDTO dto = mapper.detail(id);
		dto.setPage(page);
		mm.addAttribute("dto", dto);
		
		return "ask/ask_modifyform";
	}
	
	
	@PostMapping("modify/{page}/{id}")
	String modifyReg(AskDTO dto, HttpServletRequest request ) {
		
		dto.setMsg("QnA게시글 수정 실패입니다.");
	    dto.setGoUrl("/ask/modify" + dto.getPage() + "/" +dto.getId());

	    // 수정된 데이터를 데이터베이스에 저장
	     int cnt = mapper.askModify(dto);
	    
	    System.out.println("1:1의 cnt"+cnt);
	    
	    if (cnt > 0) {
	    		
	    		if(dto.getUpfile()==null) {
	    			fileSave(dto, request);
	    		}
	    	
	    		mapper.askModify(dto);
	        dto.setMsg("1:1 -수정되었습니다.");
	        dto.setGoUrl("/ask/detail/" + dto.getPage() + "/" + dto.getId());
	    }
		
		
		return "ask/ask_alert"; 
	} 
	
	//답변을만들어보자!
	@GetMapping("reply/{page}/{id}")
	String reply(Model mm,@PathVariable int page, @PathVariable int id ) {
		
		AskDTO dto = new AskDTO();
		dto.setPage(page);
		dto.setId(id);
		mm.addAttribute("askdto",dto);
		
		return "ask/ask_replyForm";
	}
	
//	@PostMapping("reply/{page}/{id}")
//	String replyReg(AskDTO dto, HttpServletRequest request, @PathVariable int page, @PathVariable int id) {
//	    dto.setPage(page);
//	    dto.setGid(id); // 부모 글의 id를 gid로 설정하여 답글 그룹을 형성
//	    dto.setLev(1);  // 답글의 계층을 1로 설정
//	    dto.setSeq(1);  // 답글의 순서를 1로 설정 (첫 번째 답글)
//
//	    // Mapper를 사용하여 답글 데이터를 데이터베이스에 저장
//	    int result = mapper.insertReply(dto);
//	    
//
//	    if (result > 0) {
//	        dto.setMsg("답글 작성이 완료되었습니다.");
//	        dto.setGoUrl("/ask/detail/" + page + "/" + id);
//	    } else {
//	        dto.setMsg("답글 작성에 실패했습니다.");
//	        dto.setGoUrl("/ask/reply/" + page + "/" + id);
//	    }
//	    
//	    return "ask/ask_alert";
//	}
	
	@PostMapping("reply/{page}/{id}")
	String replyReg(AskDTO dto, HttpServletRequest request, @PathVariable("page") int page, @PathVariable("id") int id) {
	    dto.setPage(page);
	    dto.setGid(id); // 부모 글의 id를 gid로 설정하여 답글 그룹을 형성
	    dto.setLev(1);  // 답글의 계층을 1로 설정
	    dto.setSeq(1);  // 답글의 순서를 1로 설정 (첫 번째 답글)

	    // Mapper를 사용하여 답글 데이터를 데이터베이스에 저장
	    int result = mapper.insertReply(dto);
	    

	    if (result > 0) {
	        dto.setMsg("답글 작성이 완료되었습니다.");
	        
	        // 답변이 있는 글의 detail 페이지로 이동
	        //dto.setGoUrl("/ask/detail/" + page + "/" + id);
	        //답변작성 시 목록으로 가게 이동
	        dto.setGoUrl("/ask/list/1");
	        
	        
	    } else {
	        dto.setMsg("답글 작성에 실패했습니다.");
	        dto.setGoUrl("/ask/reply/" + page + "/" + id);
	    }
	    
	    return "ask/ask_alert";
	}
	
	
	@PostMapping("fileDelete")
	String fileDelete(AskDTO dto, HttpServletRequest request) {
		
		AskDTO delDTO = mapper.detail(dto.getId());
		dto.setMsg("파일삭제에 실패했습니다");
		dto.setGoUrl("/ask/modify/"+dto.getPage()+"/"+dto.getId());
		
		int cnt = mapper.fileDelete(dto);
		System.out.println("modifyReg"+cnt);
		if(cnt>0) {
			fileDeleteModule(delDTO,request);
			dto.setMsg("업로드하신 파일 삭제되었습니다");
		}
		
		
		return "ask/ask_alert";
	}
	
	
	
	
	//파일 삭제
	void fileDeleteModule(AskDTO delDTO, HttpServletRequest request) {
		if(delDTO.getUpfile()!=null) {//파일이 있다면
			//시스템 경로를 문자열로 저장
			String path = request.getServletContext().getRealPath("askup");
			 path = "C:\\Users\\콩쥐\\Desktop\\final\\0912 Merge\\Spring_Team\\Spring_Team\\owang\\src\\main\\webapp\\askup";
			
			 new File(path+"\\"+delDTO.getUpfile()).delete();
			
			// new File - 새로운 파일 객체 생성
			// path -  파일이 저장된 디렉토리의 실제 파일 시스템 경로
			//delDTO.getUpfile(): delDTO 객체에서 파일 이름을 가져옴 - 실제로 삭제할 파일의 이름
			//.delete() -  파일 객체가 나타내는 파일을 삭제
		}
	
	}
	
}