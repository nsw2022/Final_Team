package aaa.controll;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import aaa.model.PageData;
import aaa.model.ResumeDTO;
import aaa.service.ResumeMapper;
import jakarta.annotation.Resource;

@Controller
@RequestMapping("solo_resume")
public class SoloResumeController {
	
	@Resource
	ResumeMapper rsmapper;
	
	// 이력서 리스트
	@RequestMapping("home") // 리스트 임
	String solo_resume(Model mm, ResumeDTO rdto) {
		// 이력서 총 개수
		int cnt = rsmapper.resumecnt();
		List<ResumeDTO> data = rsmapper.resumelist();
		System.out.println(rdto.getCnt());
		mm.addAttribute("mainData", data);
		System.out.println(data);
		System.out.println(cnt);
		mm.addAttribute("cnt", cnt);
		//mm.addAttribute("cnt", data);
		return "solo_resume/home";
	}
	
	// 이력서 디테일
	@RequestMapping("detail/{rsid}") 
	String solo_resume_detail(Model mm, @PathVariable int rsid) {
		
		mm.addAttribute("rdto", rsmapper.resumedetail(rsid));
		return "solo_resume/detail";
	}
	

	// 이력서 등록
	@GetMapping("write")
	String solo_resume_write(ResumeDTO rdto) {
		
		return "solo_resume/write";
	}
	
	// 이력서 제출
	@PostMapping("write")
	String solo_resume_writeReg(ResumeDTO rdto, PageData pd) {
		rsmapper.resumeinsert(rdto);
		pd.setMsg("이력서가 등록되었습니다.");
		pd.setGoUrl("home");
		return "solo_resume/alert";
	}

	// 이력서 수정
	@GetMapping("modify/{rsid}")
	String modify(ResumeDTO rdto, Model mm, @PathVariable int rsid) { 
		// 일단 디테일 정보 가져와 줌

		mm.addAttribute("rdto", rsmapper.resumedetail(rsid));
		return "solo_resume/modifyForm";
	}
	
	
	@DeleteMapping("/delete/{rsid}")
	public ResponseEntity<String> solo_resume_delete(@PathVariable int rsid) {
	    try {
	        // 이력서 삭제 로직 구현
	        int deletedRows = rsmapper.resumedelete(rsid);

	        if (deletedRows > 0) {
	            return ResponseEntity.ok("이력서가 삭제되었습니다.");
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이력서 삭제 실패");
	        }
	    } catch (Exception e) {
	        // 예외 처리: 삭제 과정 중에 예외가 발생한 경우
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이력서 삭제 중 오류 발생");
	    }
	}

    @DeleteMapping("/deleteReg/{rsid}")
    public ResponseEntity<String> solo_resume_deleteReg(@PathVariable int rsid) {
        ResumeDTO delDTO = rsmapper.resumedetail(rsid);
        if (delDTO != null) {
            int cnt = rsmapper.resumedelete(rsid);
            if (cnt > 0) {
                // 삭제 성공 시
                return ResponseEntity.ok("삭제되었습니다.");
            } else {
                // 삭제 실패 시
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
            }
        } else {
            // 해당 이력서를 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 이력서를 찾을 수 없습니다.");
        }
    }
}