package aaa.controll;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import aaa.model.RecruitDTO;
import aaa.service.RecruitMapper;

import jakarta.annotation.Resource;

@Controller
@RequestMapping("/recruit")
public class RecruitController {
	
	@Resource
	RecruitMapper recruitMapper;
	
	@RequestMapping("calendar")
	String calendar() {
		
		
		return "recruit/recruit_calendar";
	}
	@RequestMapping("list/{page}")
	String list(Model mm, RecruitDTO dto) {
		System.out.println(dto);
		System.out.println(recruitMapper.recruitList(dto));
		dto.calc(recruitMapper.recruitListCnt());
		
		List<RecruitDTO> data = recruitMapper.recruitList(dto);
		mm.addAttribute("mainData",data);
		return "recruit/recruit_list";
	}
	
}