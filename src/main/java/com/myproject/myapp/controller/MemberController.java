package com.myproject.myapp.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myaws.myapp.service.MemberService;
import com.myproject.myapp.domain.MemberVo;

import netscape.javascript.JSObject;

@Controller
@RequestMapping(value = "/member/")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private MemberService memberService;

	@Autowired(required = false)
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = "memberJoin.aws", method = RequestMethod.GET)
	public String memberJoin() {
		logger.info("memberJoin들어옴");
		logger.info("bCryptPasswordEncoder==>" + bCryptPasswordEncoder);
		// logger.info("tt값은"+tt.test());

		return "WEB-INF/member/memberJoin";
	}

	@RequestMapping(value = "memberJoinAction.aws", method = RequestMethod.POST)
	public String memberJoinAction(MemberVo mv) {
		logger.info("memberJoinAction들어옴");
		logger.info("bCryptPasswordEncoder==>" + bCryptPasswordEncoder);

		String memberpwd_enc = bCryptPasswordEncoder.encode(mv.getMemberpwd());
		mv.setMemberpwd(memberpwd_enc);

		int value = memberService.memberInsert(mv);
		logger.info("value:" + value);

		String path = "";
		if (value == 1) {
			path = "redirect:/";
		} else if (value == 0) {
			path = "redirect:/member/memberJoin.aws";
		}
		return path;
	}

	@RequestMapping(value = "memberLogin.aws", method = RequestMethod.GET)
	public String memberLogin() {
		// System.out.println("로그인에 들어왔니");
		return "WEB-INF/member/memberLogin";
	}

	@RequestMapping(value = "memberLoginAction.aws", method = RequestMethod.POST)
	public String memberLoginAction(

			@RequestParam("memberid") String memberid, @RequestParam("memberpwd") String memberpwd,
			RedirectAttributes rttr, HttpSession session) {
//		System.out.println("아이디" + memberid);
//		System.out.println("비번" + memberpwd);

		MemberVo mv = memberService.memberLoginCheck(memberid);
		// 저장된 비밀번호를 가져온다

		String path = "";
		if (mv != null) { // 객체값이 있으면
			String reservedPwd = mv.getMemberpwd();
			if (bCryptPasswordEncoder.matches(memberpwd, reservedPwd)) {
				// System.out.println("비밀번호 일치");
				rttr.addAttribute("midx", mv.getMidx());
				rttr.addAttribute("memberId", mv.getMemberid());
				rttr.addAttribute("memberName", mv.getMembername());

				logger.info("saveUrl" + session.getAttribute("saveUrl"));

				if (session.getAttribute("saveUrl") != null) {
					path = "redirect:" + session.getAttribute("saveUrl").toString();
				} else
					path = "redirect:/";

			} else {
//				 rttr.addAttribute("midx","");
//				 rttr.addAttribute("memberId", "");
//				 rttr.addAttribute("memberName", "");	
				rttr.addFlashAttribute("msg", "아이디/비밀번호를 확인해주세요");
				path = "redirect:/member/memberLogin.aws";
			}
		} else {
//			 rttr.addAttribute("midx","");
//			 rttr.addAttribute("memberId", "");
//			 rttr.addAttribute("memberName", "");	
			rttr.addFlashAttribute("msg", "해당하는 아이디가 없습니다.");
			path = "redirect:/member/memberLogin.aws";
		}
		// 회원정보를 세션에 담는다
		return path;
	}

	@ResponseBody
	@RequestMapping(value = "memberIdCheck.aws", method = RequestMethod.POST)
	public JSONObject memberIdCheck(@RequestParam("memberId") String memberId) {

		int cnt = memberService.memberIdCheck(memberId);

		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);

		return obj;
	}

	@RequestMapping(value = "memberList.aws", method = RequestMethod.GET)
	public String memberList(Model model) {

		ArrayList<MemberVo> alist = memberService.memberSelectAll();

		model.addAttribute("alist", alist);

		return "WEB-INF/member/memberList";

	}
	
	@RequestMapping(value = "memberLogout.aws", method = RequestMethod.GET)
	public String memberLogout(HttpSession session) {

		session.removeAttribute("midx");
		session.removeAttribute("memberName");
		session.removeAttribute("memberId");
		session.invalidate(); // 세션값 초기화

		return "redirect:/";
	}

}
