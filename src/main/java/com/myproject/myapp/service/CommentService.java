package com.myproject.myapp.service;

import java.util.ArrayList;

import com.myproject.myapp.domain.CommentVo;


public interface CommentService {

	public ArrayList<CommentVo> commentSelectAll(int bidx, int block);
	public int commentInsert(CommentVo cv);
	public int commentDelete(CommentVo cv);
	public int commentTotalCnt(int bidx); // µ¡±Û ÃÑ°¹¼ö
}
