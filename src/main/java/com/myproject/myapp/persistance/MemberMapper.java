package com.myproject.myapp.persistance;

import java.util.ArrayList;

import com.myproject.myapp.domain.MemberVo;

//mybatis�� �޼ҵ�
public interface MemberMapper {	
	public int memberInsert(MemberVo mv);
	public int memberIdCheck(String memberId);
	public MemberVo memberLoginCheck(String memberId); 
	public ArrayList<MemberVo> memberSelectAll();
}
